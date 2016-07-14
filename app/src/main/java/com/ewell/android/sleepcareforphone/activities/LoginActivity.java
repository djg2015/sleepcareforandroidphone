package com.ewell.android.sleepcareforphone.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.ewell.android.sleepcareforphone.LoginBinder;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.viewmodels.LoginViewModel;


public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private LoginViewModel user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mvvm绑定
        user = new LoginViewModel();
        LoginBinder binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setUser(user);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.userName);
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
