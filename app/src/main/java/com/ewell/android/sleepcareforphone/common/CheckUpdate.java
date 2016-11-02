package com.ewell.android.sleepcareforphone.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.ewell.android.bll.DataFactory;
import com.ewell.android.common.Grobal;
import com.ewell.android.common.exception.EwellException;
import com.ewell.android.ibll.SleepcareforPhoneManage;
import com.ewell.android.model.VersionInfo;
import com.ewell.android.model.VersionList;
import com.ewell.android.sleepcareforphone.activities.LoginActivity;
import com.ewell.android.sleepcareforphone.activities.MainActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lillix on 8/8/16.
 */
public class CheckUpdate {

    private Context context;
private boolean firstlaunch;
    private MainActivity parentactivity;

    private String localversion = "";
    public String getLocalversion() {
        return this.localversion;
    }

    private String serverversion = "";
    public String getLatestversion() {
        return this.serverversion;
    }

    private String downloadpath = "";


    //初始化
    public CheckUpdate(Context context, boolean firstlaunchflag, MainActivity mainactivity) {
        this.context = context;
        this.firstlaunch = firstlaunchflag;
        this.parentactivity = mainactivity;
    }


    /*
* 获取当前程序的版本号
*/
    private String getLocalVersion() {
        //获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        String tempname = "";
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            tempname = packInfo.versionName;
        } catch (Exception e) {

        }

        return tempname;
    }

    //从服务器获取当前最高版本号
    private String getServerversion() {
        SleepcareforPhoneManage sleepcareforPhoneManage = DataFactory.GetSleepcareforPhoneManage();
        String highestversion = "";
       Grobal.getInitConfigModel().setXmppUserName("pad");
        try {
            if (Grobal.getXmppManager().Connect()) {
                VersionList versionlist = sleepcareforPhoneManage.GetVersionForPhone("0", "3");
                ArrayList<VersionInfo> versioninfolist = versionlist.getVersionInfoList();
                //找到最大的版本号
                for (int i = 0; i < versioninfolist.size(); i++) {
                    String tempversion = versioninfolist.get(i).getVersionNum();
                    if (tempversion != null && tempversion.compareTo(highestversion) > 0) {
                        highestversion = tempversion;
                        downloadpath = versioninfolist.get(i).getDownloadPath();
                    }

                }
            }
        } catch (EwellException ex) {
            //做消息弹窗提醒
            if(firstlaunch) {
                LoginAction();
            }
        } catch (Exception e) {
            //做消息弹窗提醒
            if(firstlaunch){
                //Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_SHORT).show();
                LoginAction();
            }
        }

        return highestversion;
    }

    //从服务器下载apk
    public File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);

           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);

            //获取到文件的大小
            int length = conn.getContentLength();
            pd.setMax(length);
            InputStream is = conn.getInputStream();


            File file = new File(Environment.getExternalStorageDirectory(), "sleepcareupdate.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    /*
     * 服务器上app版本号比对本地版本号
     */
    public void CheckVersionTask() {


        localversion = getLocalVersion();
        serverversion = getServerversion();


        if (!serverversion.equals("")) {
            if (localversion.compareTo(serverversion)>=0) {
             //   Toast.makeText(context, "当前为最新版本!不需要更新", Toast.LENGTH_SHORT).show();
                if(firstlaunch) {
                    LoginAction();
                }
            } else {
                //对话框通知用户升级程序
                showUpdataDialog();
            }
        }
        else{
            if(firstlaunch) {
                LoginAction();
            }
        }

    }

    /*
 *
 * 弹出对话框通知用户更新程序
 *  1.创建alertDialog的builder.
 *  2.要给builder设置属性, 对话框的内容,样式,按钮
 *  3.通过builder 创建一个对话框
 *  4.对话框show()出来
 */
    protected void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(context);
        builer.setTitle("版本升级");
        builer.setMessage("检测到最新版本，请及时更新！");
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //   Log.i(TAG,"下载apk,更新");
                downLoadApk();
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if(firstlaunch) {
                    LoginAction();
                }
            }
        });
        AlertDialog dialog = builer.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

    }

    /*
 * 从服务器中下载APK
 */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    if (downloadpath != null) {
                        File file = getFileFromServer(downloadpath, pd);
                        sleep(3000);
                        installApk(file);
                        pd.dismiss(); //结束掉进度条对话框
                    }

                } catch (Exception e) {
                    Toast.makeText(context, "下载新版本失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    if(firstlaunch) {
                        LoginAction();
                    }
                }
            }
        }.start();
    }


    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);

    }

    private void LoginAction() {
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
        //结束掉当前的activity
      //  if(parentactivity!=null) {
         //   parentactivity.finish();
      //  }
    }

}
