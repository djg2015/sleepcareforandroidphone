package com.ewell.android.sleepcareforphone.common.pushnotification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.activities.MainActivity;
import com.ewell.android.sleepcareforphone.activities.ShowAlarmActivity;
import com.ibm.mqtt.IMqttClient;
import com.ibm.mqtt.MqttClient;
import com.ibm.mqtt.MqttException;
import com.ibm.mqtt.MqttPersistenceException;
import com.ibm.mqtt.MqttSimpleCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/*
 * PushService that does all of the work.
 * Most of the logic is borrowed from KeepAliveService.
 * http://code.google.com/p/android-random/source/browse/trunk/TestKeepAlive/src/org/devtcg/demo/keepalive/KeepAliveService.java?r=219
 */
public class PushService extends Service {
    // this is the log tag
    public static final String TAG = "SleepcarePushService";

    // the IP address, where your MQTT broker is running.
    private static final String MQTT_HOST = "122.224.242.241";
    // the port at which the broker is running.
    private static int MQTT_BROKER_PORT_NUM = 7020;
    // Let's not use the MQTT persistence.
//	private static MqttPersistene	MQTT_PERSISTENCE          = null;
    // We don't need to remember any state between the connections, so we use a clean start.
    private static boolean MQTT_CLEAN_START = true;
    // Let's set the internal keep alive for MQTT to 15 mins. I haven't tested this value much. It could probably be increased.
    private static short MQTT_KEEP_ALIVE = 60 * 15;
    // Set quality of services to 0 (at most once delivery), since we don't want push notifications
    // arrive more than once. However, this means that some messages might get lost (delivery is not guaranteed)
    private static int[] MQTT_QUALITIES_OF_SERVICE = {0};
    private static int MQTT_QUALITY_OF_SERVICE = 0;
    // The broker should not retain any messages.
    private static boolean MQTT_RETAINED_PUBLISH = false;

    // MQTT client ID, which is given the broker. In this example, I also use this for the topic header.
    // You can use this to run push notifications for multiple apps with one MQTT broker.
    public static String MQTT_CLIENT_ID = "sleepcarehospital/android";

    // These are the actions for the service (name are descriptive enough)
    private static final String ACTION_START = MQTT_CLIENT_ID + ".START";
    private static final String ACTION_STOP = MQTT_CLIENT_ID + ".STOP";
    private static final String ACTION_KEEPALIVE = MQTT_CLIENT_ID + ".KEEP_ALIVE";
    private static final String ACTION_RECONNECT = MQTT_CLIENT_ID + ".RECONNECT";

    // Connection log for the push service. Good for debugging.
    private ConnectionLog mLog;

    // Connectivity manager to determining, when the phone loses connection
    private ConnectivityManager mConnMan;
    // Notification manager to displaying arrived push notifications
    private NotificationManager mNotifMan;

    // Whether or not the service has been started.
    private boolean mStarted;

    // This the application level keep-alive interval, that is used by the AlarmManager
    // to keep the connection active, even when the device goes to sleep.
    private static final long KEEP_ALIVE_INTERVAL = 1000 * 60 * 20;

    // Retry intervals, when the connection is lost.
    private static final long INITIAL_RETRY_INTERVAL = 1000 * 10;
    private static final long MAXIMUM_RETRY_INTERVAL = 1000 * 60 * 30;

    // Preferences instance
    private SharedPreferences mPrefs;
    // We store in the preferences, whether or not the service has been started
    public static final String PREF_STARTED = "isStarted";
    // We also store the deviceID (target)
    public static final String PREF_DEVICE_ID = "deviceID";
    // We store the last retry interval
    public static final String PREF_RETRY = "retryInterval";

    // Notification title
    public static String NOTIF_TITLE = "智能床";
    // Notification id
    private static final int NOTIF_CONNECTED = 0;

    // This is the instance of an MQTT connection.
    private MQTTConnection mConnection;
    private long mStartTime;


    // Static method to start the service
    public static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, PushService.class);
        i.setAction(ACTION_START);
        ctx.startService(i);
    }

    // Static method to stop the service
    public static void actionStop(Context ctx) {
        Intent i = new Intent(ctx, PushService.class);
        i.setAction(ACTION_STOP);
        ctx.startService(i);
    }

    // Static method to send a keep alive message
    public static void actionPing(Context ctx) {
        Intent i = new Intent(ctx, PushService.class);
        i.setAction(ACTION_KEEPALIVE);
        ctx.startService(i);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        log("Creating service============\n");
        mStartTime = System.currentTimeMillis();

        try {
            mLog = new ConnectionLog();
            Log.i(TAG, "Opened log at " + mLog.getPath());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open log", e);
        }

        // Get instances of preferences, connectivity manager and notification manager
        mPrefs = getSharedPreferences(TAG, MODE_PRIVATE);
        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mNotifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		/* If our process was reaped by the system for any reason we need
         * to restore our state with merely a call to onCreate.  We record
		 * the last "started" value and restore it here if necessary. */
        handleCrashedService();


    }

    // This method does any necessary clean-up need in case the server has been destroyed by the system
    // and then restarted
    private void handleCrashedService() {
        if (wasStarted() == true) {
            log("Handling crashed service...");
            // stop the keep alives
            stopKeepAlives();

            // Do a clean start
            start();
        }
    }

    @Override
    public void onDestroy() {
        log("Service destroyed (started=" + mStarted + ")========\n");

        // Stop the services, if it has been started
        if (mStarted) {
            stop();
        }
        try {
            if (mLog != null)
                mLog.close();
        } catch (IOException e) {
        }

        if (getSharedPreferences("config", MODE_PRIVATE).getBoolean("notificationflag", true) && getSharedPreferences("config", MODE_PRIVATE).getBoolean("isLogin", false)) {
            //若通知在"开启"状态,则发广播,重新启动service
            Intent intent = new Intent("com.example.lillix.pushnotification.destroy");
            sendBroadcast(intent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        log("========" +
                "Service started with intent=" + intent);

        // Do an appropriate action based on the intent.
        if (intent.getAction().equals(ACTION_STOP) == true) {
            stop();
            stopSelf();
        } else if (intent.getAction().equals(ACTION_START) == true) {
            start();
        } else if (intent.getAction().equals(ACTION_KEEPALIVE) == true) {
            keepAlive();
        } else if (intent.getAction().equals(ACTION_RECONNECT) == true) {
            if (isNetworkAvailable()) {
                reconnectIfNecessary();
            }
        }
        //当service因内存不足被kill，当内存又有的时候，service又被重新创建
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // log helper function
    private void log(String message) {
        log(message, null);
    }

    private void log(String message, Throwable e) {
        if (e != null) {
            Log.e(TAG, message, e);

        } else {
            Log.i(TAG, message);
        }

        if (mLog != null) {
            try {
                mLog.println(message);
            } catch (IOException ex) {
            }
        }
    }

    // Reads whether or not the service has been started from the preferences
    private boolean wasStarted() {
        return mPrefs.getBoolean(PREF_STARTED, false);
    }

    // Sets whether or not the services has been started in the preferences.
    private void setStarted(boolean started) {
        mPrefs.edit().putBoolean(PREF_STARTED, started).commit();
        mStarted = started;
    }

    private synchronized void start() {
        log("Starting service...");

        // Do nothing, if the service is already running.
        if (mStarted ) {
            Log.w(TAG, "Attempt to start connection that is already active");
            return;
        }

        // Establish an MQTT connection
        connect();

        // Register a connectivity listener
        registerReceiver(mConnectivityChanged, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


    }

    private synchronized void stop() {
        // Do nothing, if the service is not running.
        if (!mStarted ) {
            Log.w(TAG, "Attempt to stop connection not active.");
            return;
        }

        // Save stopped state in the preferences
        setStarted(false);

        // Remove the connectivity receiver
        unregisterReceiver(mConnectivityChanged);


        // Any existing reconnect timers should be removed, since we explicitly stopping the service.
        cancelReconnect();

        // Destroy the MQTT connection if there is one
        if (mConnection != null) {
            mConnection.disconnect();
            mConnection = null;
        }
    }

    //
    private synchronized void connect() {
        log("Connecting...");
        // fetch the device ID from the preferences.
           String deviceID = mPrefs.getString(PREF_DEVICE_ID,"");
        // Create a new connection only if the device id is not NULL
        if (deviceID == "") {
            log("Device ID not found.");
        } else {

            new Thread(networkTask).start();

//                mConnection = new MQTTConnection(MQTT_HOST, deviceID);


        }
    }

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            try {
                 String deviceID = mPrefs.getString(PREF_DEVICE_ID, "");

                mConnection = new MQTTConnection(MQTT_HOST, deviceID);
                setStarted(true);
            } catch (MqttException e) {
                // Schedule a reconnect, if we failed to connect
                log("MqttException: " + (e.getMessage() != null ? e.getMessage() : "NULL"));
                if (isNetworkAvailable()) {
                    scheduleReconnect(mStartTime);
                }
            }
        }
    };

    private synchronized void keepAlive() {
        try {
            // Send a keep alive, if there is a connection.
            if (mStarted && mConnection != null) {
                mConnection.sendKeepAlive();
            }
        } catch (MqttException e) {
            log("MqttException: " + (e.getMessage() != null ? e.getMessage() : "NULL"), e);

            mConnection.disconnect();
            mConnection = null;
            cancelReconnect();
        }
    }

    // Schedule application level keep-alives using the AlarmManager
    private void startKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + KEEP_ALIVE_INTERVAL,
                KEEP_ALIVE_INTERVAL, pi);
    }

    // Remove all scheduled keep alives
    private void stopKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.cancel(pi);
    }

    // We schedule a reconnect based on the starttime of the service
    public void scheduleReconnect(long startTime) {
        // the last keep-alive interval
        long interval = mPrefs.getLong(PREF_RETRY, INITIAL_RETRY_INTERVAL);

        // Calculate the elapsed time since the start
        long now = System.currentTimeMillis();
        long elapsed = now - startTime;


        // Set an appropriate interval based on the elapsed time since start
        if (elapsed < interval) {
            interval = Math.min(interval * 4, MAXIMUM_RETRY_INTERVAL);
        } else {
            interval = INITIAL_RETRY_INTERVAL;
        }

        log("Rescheduling connection in " + interval + "ms.");

        // Save the new internval
        mPrefs.edit().putLong(PREF_RETRY, interval).commit();

        // Schedule a reconnect using the alarm manager.
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(ACTION_RECONNECT);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, now + interval, pi);
    }

    // Remove the scheduled reconnect
    public void cancelReconnect() {
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(ACTION_RECONNECT);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.cancel(pi);
    }

    private synchronized void reconnectIfNecessary() {
        if (mStarted  && mConnection == null) {
            log("Reconnecting...");
            connect();
        }
    }

    // This receiver listeners for network changes and updates the MQTT connection accordingly
    private BroadcastReceiver mConnectivityChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get network info
            NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

            // Is there connectivity?
            boolean hasConnectivity = (info != null && info.isConnected()) ? true : false;

            log("Connectivity changed: connected=" + hasConnectivity);

            if (hasConnectivity) {
                reconnectIfNecessary();
            } else if (mConnection != null) {
                // if there no connectivity, make sure MQTT connection is destroyed
                mConnection.disconnect();
                cancelReconnect();
                mConnection = null;
            }
        }
    };

    // Display the topbar notification
    private void showNotification(String text) {
//如果通知开关打开,则进行对应的消息提醒
       if( getSharedPreferences("config", MODE_PRIVATE).getBoolean("notificationflag", true)) {
           // Simply open the parent activity
           //在执行了点击通知之后要跳转到指定的Activity的时候，可以设置以下方法来相应点击事件
           PendingIntent pi;
           if (getSharedPreferences("config", MODE_PRIVATE).getBoolean("isLogin", false)) {
               pi = PendingIntent.getActivity(this, 0,
                       new Intent(this, ShowAlarmActivity.class), 0);
           } else {
               pi = PendingIntent.getActivity(this, 0,
                       new Intent(this, MainActivity.class), 0);
           }

           NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
           String title = "";
           String content = "";

           try {
               JSONObject jsonObject = new JSONObject(text);
               title = jsonObject.getString("Title");
               content = jsonObject.getString("Content");
           } catch (JSONException e) {
               e.printStackTrace();
           }

           mBuilder.setContentTitle(title)//设置通知栏标题
                   .setContentText(content)
                   .setContentIntent(pi) //设置通知栏点击意图
                   // .setNumber(10) //设置通知集合的数量
                   .setTicker("智能床通知提醒") //通知首次出现在通知栏，带上升动画效果的
                   .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                   .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                   .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                   .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                   //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                   .setSmallIcon(R.drawable.ic_notification);//设置通知小ICON

           mNotifMan.notify(NOTIF_CONNECTED, mBuilder.build());
       }
    }



    // Check if we are online
    private boolean isNetworkAvailable() {
        NetworkInfo info = mConnMan.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isConnected();
    }

    // This inner class is a wrapper on top of MQTT client.
    private class MQTTConnection implements MqttSimpleCallback {
        IMqttClient mqttClient = null;

        // Creates a new connection given the broker address and initial topic
        public MQTTConnection(String brokerHostName, String initTopic) throws MqttException {
            // Create connection spec
            String mqttConnSpec = "tcp://" + brokerHostName + "@" + MQTT_BROKER_PORT_NUM;
            // Create the client and connect
            mqttClient = MqttClient.createMqttClient(mqttConnSpec, null);

          //  String clientID = MQTT_CLIENT_ID + "/" + mPrefs.getString(PREF_DEVICE_ID, "");
            String clientID = MQTT_CLIENT_ID;
            mqttClient.connect(clientID, MQTT_CLEAN_START, MQTT_KEEP_ALIVE);

            // register this client app has being able to receive messages
            mqttClient.registerSimpleHandler(this);

            // Subscribe to an initial topic, which is combination of client ID and device ID.
            initTopic = MQTT_CLIENT_ID + "/" + initTopic;
            subscribeToTopic(initTopic);

            log("Connection established to " + brokerHostName + " on topic " + initTopic);

            // Save start time
            mStartTime = System.currentTimeMillis();
            // Star the keep-alives
            startKeepAlives();
        }

        // Disconnect
        public void disconnect() {
            try {
                stopKeepAlives();
                mqttClient.disconnect();
            } catch (MqttPersistenceException e) {
                log("MqttException" + (e.getMessage() != null ? e.getMessage() : " NULL"), e);
            }
        }

        /*
         * Send a request to the message broker to be sent messages published with
         *  the specified topic name. Wildcards are allowed.
         */
        private void subscribeToTopic(String topicName) throws MqttException {

            if ((mqttClient == null) || (mqttClient.isConnected() == false)) {
                // quick sanity check - don't try and subscribe if we don't have
                //  a connection
                log("Connection error" + "No connection");
            } else {
                String[] topics = {topicName};
                mqttClient.subscribe(topics, MQTT_QUALITIES_OF_SERVICE);
            }
        }

        /*
         * Sends a message to the message broker, requesting that it be published
         *  to the specified topic.
         */
        private void publishToTopic(String topicName, String message) throws MqttException {
            if ((mqttClient == null) || (mqttClient.isConnected() == false)) {
                // quick sanity check - don't try and publish if we don't have
                //  a connection
                log("No connection to public to");
            } else {
                mqttClient.publish(topicName,
                        message.getBytes(),
                        MQTT_QUALITY_OF_SERVICE,
                        MQTT_RETAINED_PUBLISH);
            }
        }

        /*
         * Called if the application loses it's connection to the message broker.
         */
        public void connectionLost() throws Exception {
            log("Loss of connection" + "connection downed");
            stopKeepAlives();
            // null itself
            mConnection = null;
            if (isNetworkAvailable() == true) {
                reconnectIfNecessary();
            }
        }

        /*
         * Called when we receive a message from the message broker.
         */
        public void publishArrived(String topicName, byte[] payload, int qos, boolean retained) {
            // Show a notification
            try {
                String s = new String(payload, "UTF-8");
                log("Got message: " + s);
                showNotification(s);
            }catch (IOException e)
            {e.printStackTrace();}
        }

        public void sendKeepAlive() throws MqttException {
            log("Sending keep alive");
            // publish to a keep-alive topic
            publishToTopic(MQTT_CLIENT_ID + "/keepalive", mPrefs.getString(PREF_DEVICE_ID, ""));
        }

    }


}