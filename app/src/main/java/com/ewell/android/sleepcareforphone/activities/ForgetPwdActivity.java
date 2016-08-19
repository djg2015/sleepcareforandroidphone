package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.Toast;

import com.ewell.android.sleepcareforphone.ForgetPwdBinder;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.viewmodels.ForgetPwdViewModel;

/**
 * Created by lillix on 7/17/16.
 */
public class ForgetPwdActivity extends Activity {
    private EditText mTelephone;
    private EditText mVCode;
    private EditText mPwd;

    private Button mBtnPwdVisible;
    private Boolean mPwdVisible = false;
    private Button mBtnSendVerify;

    private ForgetPwdViewModel forgetPwd = null;

    private boolean isrun = false;
    private int seconds = 60;
    private Thread countdown;
    private Handler handler;
    private Runnable runnable ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler= new Handler();
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
        forgetPwd = new ForgetPwdViewModel();
        forgetPwd.SetParentActivity(this);
        ForgetPwdBinder binding = DataBindingUtil.setContentView(this, R.layout.activity_forgetpwd);
        binding.setForgetPwd(forgetPwd);

        mTelephone = (EditText) findViewById(R.id.telephone);
        mTelephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                forgetPwd.setTelephone(s.toString());
            }
        });

        mVCode = (EditText) findViewById(R.id.verifynum);
        mVCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                forgetPwd.setVerifynum(s.toString());
            }
        });
        mPwd = (EditText) findViewById(R.id.textnewpassword);
        mPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                forgetPwd.setNewpassword(s.toString());
            }
        });

        mBtnPwdVisible = (Button) findViewById(R.id.btnpwdvisible);
        mBtnSendVerify = (Button) findViewById(R.id.btnsendverify);

    }

    //密码可见
    public void ClickPwdVisible(View view) {

        mPwdVisible = !mPwdVisible;
        //设置为明文显示
        if (mPwdVisible) {
            this.mPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            this.mBtnPwdVisible.setBackgroundResource(R.drawable.icon_eyeopen);
        } else {

            this.mPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.mBtnPwdVisible.setBackgroundResource(R.drawable.icon_eyeclose);
        }
    }

    //获取验证码
    public void ClickSendVerify(View view) {
        //需要先填写手机号
        if(forgetPwd.getTelephone()==null || forgetPwd.getTelephone().equals("")) {
            Toast.makeText(this, "请先输入手机号!", Toast.LENGTH_LONG).show();
        }
        else{
            if (!isrun) {
                //异步,服务器方法请求发送验证码
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        forgetPwd.SendVerifyCode();

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


    //返回
    public void ClickClose(View view) {

        this.finish();
    }

}