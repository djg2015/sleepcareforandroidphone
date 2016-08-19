package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.RegistAccountBinder;
import com.ewell.android.sleepcareforphone.viewmodels.RegistAccountViewModel;

/**
 * Created by lillix on 7/18/16.
 */
public class RegistAccountActivity extends Activity {

    private RegistAccountViewModel registAccount = null;

    private EditText userName;
    private EditText vcCode;
    private EditText password;
    private String QR;

    private Button mBtnPwdVisible;
    private Boolean mPwdVisible = false;
    private Button mBtnSendVerify;

    private boolean isrun = false;
    private int seconds = 60;
    private Thread countdown;
    private Handler handler;
    private Runnable runnable;


    private final static int SCANNIN_GREQUEST_CODE = 1;
    private final static int CONFIRM_PATIENT_CODE = 2;

    /**
     * 显示扫描结果
     */
    private TextView mTextView;
    private Button mScanButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if (seconds > 0) {
                    mBtnSendVerify.setText(Integer.toString(seconds) + "s后重发");
                } else {
                    isrun = false;
                    seconds = 60;
                    mBtnSendVerify.setTextColor(Color.argb(255, 77, 157, 253));
                    mBtnSendVerify.setBackgroundResource(R.drawable.icon_sendverify);
                    mBtnSendVerify.setText("获取校验码");


                }

            }
        };

        countdown = new Thread() {
            @Override
            public void run() {

                while (isrun && seconds >= 0) {

                    handler.post(runnable);
                    seconds--;
                    try {
                        sleep(1000); //直接调用

                    } catch (InterruptedException e) {
                        return;
                    }
                }//while
            }
        };

        //绑定model
        registAccount = new RegistAccountViewModel();
        registAccount.SetParentActivity(this);
        RegistAccountBinder binding = DataBindingUtil.setContentView(this, R.layout.activity_registaccount);
        binding.setRegistAccount(registAccount);

        mTextView = (TextView) findViewById(R.id.textscancode);
        mScanButton = (Button) findViewById(R.id.btnScan);

        userName = (EditText) findViewById(R.id.telephone);
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registAccount.setTelephone(s.toString());
            }
        });

        vcCode = (EditText) findViewById(R.id.verifynum);
        vcCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registAccount.setVerifynum(s.toString());
            }
        });
        password = (EditText) findViewById(R.id.textnewpassword);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registAccount.setNewpassword(s.toString());
            }
        });

        mBtnPwdVisible = (Button) findViewById(R.id.btnpwdvisible);
        mBtnSendVerify = (Button) findViewById(R.id.btnsendverify);
    }


    public void ClickClose(View view) {

        this.finish();
    }

    //密码可见
    public void ClickPwdVisible(View view) {

        mPwdVisible = !mPwdVisible;
        //设置为明文显示
        if (mPwdVisible) {
            this.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            this.mBtnPwdVisible.setBackgroundResource(R.drawable.icon_eyeopen);
        } else {
            this.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.mBtnPwdVisible.setBackgroundResource(R.drawable.icon_eyeclose);
        }
    }

    //获取验证码
    public void ClickSendVerify(View view) {
        if(registAccount.getTelephone().equals("")){
            Toast.makeText(this, "请先输入手机号!", Toast.LENGTH_SHORT).show();
        }
        else{
        if (!isrun) {
            //异步,服务器方法请求发送验证码
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    registAccount.SendVerifyCode();

                }
            });
            thread.start();

            //开启一分钟的线程,进行倒计时,结束后恢复button可选
            mBtnSendVerify.setBackgroundResource(R.drawable.icon_60s);
            mBtnSendVerify.setTextColor(Color.argb(255, 146, 146, 146));
            isrun = true;
            countdown.start();
        }
        }
    }

    //扫一扫
    public void ClickScanCode(View view) {

        Intent intent = new Intent();
        intent.setClass(this, MipcaActivityCapture.class);
     //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    //   勾选框选中,跳转阅读协议页面
    public void ClickReadProtocol(View view) {

        if (((CheckBox) view).isChecked()) {
            registAccount.setIsCheckedProtocal(true);
            Intent intent = new Intent(this, ReadProtocolActivity.class);
            this.startActivity(intent);
        }
        else{
            registAccount.setIsCheckedProtocal(false);
        }
    }

    // 点击注册
    public  void ClickRegist(View view){

       Boolean registFlag = registAccount.RegistCommand();
        //注册成功,继续2确认老人信息;失败,弹窗提示
        if(registFlag){
            Toast.makeText(this, "注册成功!请继续完成老人信息确认", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            Bundle bundle=new Bundle();
            bundle.putString("equipmentid",registAccount.getVcCode() );
            intent.putExtras(bundle);
            intent.setClass(this, BindPatientInfoActivity.class);
            startActivityForResult(intent, CONFIRM_PATIENT_CODE);

        }

    }


    //从上一页返回本页,根据code区分activity,获取传回的参数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //设备编号认证,成功则显示编号隐藏扫一扫按钮;失败提示错误
                    String tempEquipmentid = bundle.getString("result");
                    Boolean checkFlag = registAccount.CheckEquipmentIDAfterScan(tempEquipmentid);

                    //成功:显示扫描到的内容
                    if (checkFlag) {
                        Toast.makeText(getApplicationContext(), "扫描设备成功!", Toast.LENGTH_LONG).show();
                        String equipmentcode = "设备编号:" + tempEquipmentid;
                        mTextView.setText(equipmentcode);
                        mTextView.setTextColor(Color.parseColor("#56a3fd"));
                        mScanButton.setVisibility(View.INVISIBLE);

                        registAccount.setVcCode(equipmentcode);
                    }
                }
                break;
            case CONFIRM_PATIENT_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //老人信息确认成功
                    Boolean confirmFlag = bundle.getBoolean("result");
                    if (confirmFlag) {
                        Toast.makeText(getApplicationContext(), "注册成功!请继续登录", Toast.LENGTH_LONG).show();
                    }
                    else{

                        Toast.makeText(getApplicationContext(), "老人信息确认失败!您可在登录后修改", Toast.LENGTH_LONG).show();
                    }
                    this.finish();
                }
                break;
        }
    }

}
