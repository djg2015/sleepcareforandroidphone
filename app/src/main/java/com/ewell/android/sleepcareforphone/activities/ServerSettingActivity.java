package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.ServerSettingBinder;
import com.ewell.android.sleepcareforphone.viewmodels.ServerSettingViewModel;

/**
 * Created by lillix on 10/28/16.
 */
public class ServerSettingActivity extends Activity {
    private EditText mServerJID;
    private EditText mServerIP;
    private EditText mServerPort;
    private EditText mServerPwd;
    private EditText mServerUser;

    private SharedPreferences mSP;
    private ServerSettingViewModel serversetting = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定model
        serversetting = new ServerSettingViewModel();
        serversetting.SetParentActivity(this);
        serversetting.InitData();
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        serversetting.setSharedPreferences(mSP);


        ServerSettingBinder binding = DataBindingUtil.setContentView(this, R.layout.activity_serversetting);
        binding.setServersetting(serversetting);

        mServerJID = (EditText) findViewById(R.id.txt_serverjid);
        mServerJID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serversetting.setServerJID(s.toString());
            }
        });

        mServerIP = (EditText) findViewById(R.id.txt_serverip);
        mServerIP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serversetting.setServerIP(s.toString());
            }
        });

        mServerPort = (EditText) findViewById(R.id.txt_serverport);
        mServerPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serversetting.setServerPort(s.toString());
            }
        });


        mServerUser = (EditText) findViewById(R.id.txt_serveruser);
        mServerUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serversetting.setServerUser(s.toString());
            }
        });


        mServerPwd = (EditText) findViewById(R.id.txt_serverpwd);
        mServerPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                serversetting.setServerPwd(s.toString());
            }
        });


    }
    public void ClickClose(View view) {

        this.finish();
    }
}
