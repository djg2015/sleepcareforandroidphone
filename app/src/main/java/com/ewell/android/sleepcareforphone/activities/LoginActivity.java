package com.ewell.android.sleepcareforphone.activities;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.ewell.android.common.Crypt;
import com.ewell.android.sleepcareforphone.LoginBinder;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.viewmodels.LoginViewModel;


public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private LoginViewModel user = null;

    private SharedPreferences mSP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mvvm绑定
        user = new LoginViewModel();
        //完成sp的初始化。
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        user.setSharedPreferences(mSP);
       //获取sp里面存储的数据,读取上次登录信息
        user.setLoginName(mSP.getString("loginname", ""));
        String tempPwd = mSP.getString("password", "");

        try {
            user.setPassword(Crypt.decrypt("key",tempPwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setCheckbox(mSP.getBoolean("isSavePassword",false));


        user.SetParentActivity(this);
        LoginBinder binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setUser(user);


        //监听页面输入,赋值给viewmodel
        mEmailView = (EditText)findViewById(R.id.userName);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user.setLoginName(s.toString());
            }
        });

        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user.setPassword(s.toString());
            }
        });
    }



}
