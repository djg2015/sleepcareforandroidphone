package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.GetRealtimeDataDelegate;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.RealTimeHelper;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.model.EMRealTimeReport;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.common.pushnotification.PushService;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenu;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuCreator;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuItem;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuLayout;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuListView;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuView;
import com.ewell.android.sleepcareforphone.viewmodels.MyPatientsViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lillix on 10/28/16.
 */
public class MyPatientsActivity extends Activity implements GetRealtimeDataDelegate {
    private List<Map<String, Object>> mAppList;
    private MyPatientsAdapter mAdapter;
    private SwipeMenuListView mListView;
    private Context mContext = this;
    private SwipeMenuCreator creator ;
private Boolean firstflag= true;

    private MyPatientsViewModel patients;
    private Map<String, Object> mHolderList;

    private Thread mThread = null;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//此方法在ui线程运行
            switch (msg.what) {
                case 1:
                RealTimeHelper.GetInstance().ShowRealTimeData();
                    break;
            }
        }
    };

   private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //run()在新的线程中运行
            while (mThread != null ) {
                   mHandler.obtainMessage(1).sendToTarget();


                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void createMenu2(SwipeMenu menu) {

        SwipeMenuItem item2 = new SwipeMenuItem(
                getApplicationContext());
        item2.setBackground(new ColorDrawable(Color.rgb(0xF9,
                0x3F, 0x25)));
        item2.setWidth(dp2px(90));

        item2.setIcon(R.drawable.ic_delete);
        menu.addMenuItem(item2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.finish();
        mThread = null;

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(!firstflag) {
            patients.InitData();
            mAppList = patients.getList();
            mAdapter.notifyDataSetChanged();
        }

        firstflag = false;
        RealTimeHelper.GetInstance().SetDelegate("realtimedelegate",this);

        if(mThread==null) {
            mThread = new Thread(runnable);
            mThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //开关远程通知
        String deviceid = getSharedPreferences(PushService.TAG, MODE_PRIVATE).getString("deviceID","");
        String loginname = Grobal.getInitConfigModel().getLoginUserName();
        try {
            if (getSharedPreferences("config", MODE_PRIVATE).getBoolean("notificationflag", true)) {
                DataFactory.GetSleepcareforPhoneManage().OpenNotificationForAndroid(deviceid, loginname);
                PushService.actionStart(mContext);
            } else {
                DataFactory.GetSleepcareforPhoneManage().CloseNotificationForAndroid(deviceid, loginname);
                PushService.actionStop(mContext);
            }
        }catch (EwellException ex) {

            Toast.makeText(mContext,ex.get_exceptionMsg(), Toast.LENGTH_SHORT).show();
        }

        //  初始化listview数据
        patients = new MyPatientsViewModel();
        patients.setParentactivity(this);

        setContentView(R.layout.activity_mypatients);
        patients.InitData();
        mAppList = patients.getList();


        mHolderList = new HashMap<String,Object>();
        mListView = (SwipeMenuListView) findViewById(R.id.patientlistView);
        mAdapter = new MyPatientsAdapter();
        mListView.setAdapter(mAdapter);

        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                createMenu2(menu);

            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                String deletecode = mAppList.get(position).get("bedusercode").toString();
                //调用接口
                Boolean flag =  patients.DeletePatient(deletecode);

                if (flag) {
                    //更新bedusercodeList,UserCodeNameMap
                    Map<String,String> tempmap = Grobal.getInitConfigModel().getUserCodeNameMap();
                    ArrayList<String> templist =  Grobal.getInitConfigModel().getBedusercodeList();
                    tempmap.remove(deletecode);
                    templist.remove(deletecode);
                    Grobal.getInitConfigModel().setUserCodeNameMap(tempmap);
                    Grobal.getInitConfigModel().setBedusercodeList(templist);

                    //本地list中删除
                    mHolderList.remove(deletecode);
                    mAppList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String bedusercode = mAppList.get(arg2).get("bedusercode").toString();
                String bedusername = mAppList.get(arg2).get("bedusername").toString();
                Grobal.getInitConfigModel().setCurUserCode(bedusercode);
                Grobal.getInitConfigModel().setCurUserName(bedusername);

                //跳转下一页面
                Intent intent = new Intent(mContext, TabbarActivity.class);
                mContext.startActivity(intent);
            }
        });

    }



    class MyPatientsAdapter extends BaseAdapter implements SwipeMenuView.OnSwipeItemClickListener {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public Map<String, Object> getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //获取数据源
            Map<String, Object> item = getItem(position);

            PatientViewHolder holder = null;
            SwipeMenuLayout layout = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_list_patient, null);
                holder = new PatientViewHolder(convertView);
                SwipeMenu menu = new SwipeMenu(mContext);

                menu.setViewType(1);
                createMenu(menu);
                SwipeMenuView menuView = new SwipeMenuView(menu,(SwipeMenuListView) parent);
                menuView.setOnSwipeItemClickListener(this);
                SwipeMenuListView listView = (SwipeMenuListView) parent;
                layout = new SwipeMenuLayout(convertView, menuView, listView.getCloseInterpolator(), listView.getOpenInterpolator());
                layout.setPosition(position);
                layout.setTag(holder);
            } else {
                layout = (SwipeMenuLayout) convertView;
                layout.closeMenu();
                layout.setPosition(position);
                holder = (PatientViewHolder)layout.getTag();
            }

            mHolderList.put(item.get("bedusercode").toString(),holder);

            holder.bedusercode = item.get("bedusercode").toString();
            String sex = item.get("sex").toString();
            if (sex.equals("0")) {
                holder.sex.setSelected(false);
            } else if (sex.equals("1")) {
                holder.sex.setSelected(true);
            }
            holder.roomnum.setText(item.get("roomnum").toString());
            holder.bednum.setText(item.get("bednum").toString());
            holder.name.setText(item.get("bedusername").toString());

            return layout;
        }

        class PatientViewHolder {

            String bedusercode;
            Button sex;
            TextView roomnum;
            TextView bednum;
            TextView name;
            TextView hr;
            TextView rr;
            TextView status;
          ImageView alarmImg;

            public PatientViewHolder(View view) {
                sex = (Button)view.findViewById(R.id.sex);
                roomnum = (TextView) view.findViewById(R.id.txt_roomnum);
                bednum = (TextView)view.findViewById(R.id.txt_bednum);
                name = (TextView)view.findViewById(R.id.name);
                hr = (TextView)view.findViewById(R.id.txt_hr);
                rr = (TextView)view.findViewById(R.id.txt_rr);
                status = (TextView)view.findViewById(R.id.txt_status);
                alarmImg = (ImageView)view.findViewById(R.id.alarmImg);
            }
        }



        public void createMenu(SwipeMenu menu) {
            if(creator != null) {
                creator.create(menu);
            }
        }

        @Override
        public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
            if (mListView != null && mListView.getOnMenuItemClickListener() != null) {
                mListView.getOnMenuItemClickListener().onMenuItemClick(view.getPosition(), menu, index);
            }

            if(mListView != null && mListView.getTouchView() != null) {
                mListView.getTouchView().smoothCloseMenu();
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  dp, getResources().getDisplayMetrics());
    }


    @Override
    public void GetRealtimeData(Map<String, EMRealTimeReport> realtimeData) {
         for(EMRealTimeReport emRealTimeReport: realtimeData.values()){

             String currentBedusercode =  emRealTimeReport.getBedUserCode();
             if( mHolderList.get(currentBedusercode)!=null) {
             MyPatientsAdapter.PatientViewHolder itemHolder = (MyPatientsAdapter.PatientViewHolder) mHolderList.get(currentBedusercode);

                 itemHolder.hr.setText(emRealTimeReport.getHR());
                 itemHolder.rr.setText(emRealTimeReport.getRR());
                 itemHolder.status.setText(emRealTimeReport.getOnBedStatus());
                // System.out.print(currentBedusercode + "实时数据===============\n");
             }
        }
        // mAdapter.notifyDataSetChanged();
    }

    public void ClickMe(View view) {
        Intent intent = new Intent(mContext, MeActivity.class);
        mContext.startActivity(intent);

    }

    public void ClickAddPatient(View v){

        Intent intent = new Intent(mContext, AddPatientActivity.class);
        mContext.startActivity(intent);
    }
}
