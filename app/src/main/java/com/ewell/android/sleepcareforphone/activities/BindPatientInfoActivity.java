package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ewell.android.sleepcareforphone.PatientInfoBinder;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.viewmodels.BindPatientInfoViewModel;

/**
 * Created by lillix on 7/19/16.
 */


public class BindPatientInfoActivity extends Activity {
    private BindPatientInfoViewModel bindPatientInfo = null;

    private EditText mTelephone;
    private EditText mAddress;
    private Button mFemaleBtn;
    private Button mMaleBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindPatientInfo = new BindPatientInfoViewModel();
        bindPatientInfo.setParentactivity(this);
        bindPatientInfo.InitData();

        Bundle bundle = this.getIntent().getExtras();
        String tempequipmentid = bundle.getString("equipmentid");
        bindPatientInfo.setEquipmentid(tempequipmentid);


        PatientInfoBinder binding = DataBindingUtil.setContentView(this,R.layout.activity_bindpatientinfo);
        binding.setBindPatientInfo(bindPatientInfo);

        //


        //其他属性都可编辑
        mTelephone = (EditText) findViewById(R.id.textpatienttelephone);
        mTelephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bindPatientInfo.setPatientTelephone(s.toString());
            }
        });
        mAddress = (EditText) findViewById(R.id.textpatientaddress);
        mAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                bindPatientInfo.setPatientAddress(s.toString());
            }
        });

        mFemaleBtn = (Button) findViewById(R.id.btnfemale);
        mMaleBtn = (Button) findViewById(R.id.btnmale);
        mFemaleBtn.setSelected(bindPatientInfo.getIsFemale());
        mMaleBtn.setSelected(bindPatientInfo.getIsMale());

    }

    public void ClickFemale(View view) {

if (!bindPatientInfo.getIsFemale()){
    bindPatientInfo.setIsFemale(true);
    bindPatientInfo.setIsMale(false);
    mFemaleBtn.setSelected(true);
    mMaleBtn.setSelected(false);
        }

    }

    public void ClickMale(View view) {
        if (!bindPatientInfo.getIsMale()){
            bindPatientInfo.setIsMale(true);
            bindPatientInfo.setIsFemale(false);
            mFemaleBtn.setSelected(false);
            mMaleBtn.setSelected(true);
        }

    }

    public void ClickConfirm(View view) {
        Boolean confirmFlag = false;
        confirmFlag = bindPatientInfo.ConfirmPatientInfoCommand();
        onResultHandler(confirmFlag);
    }

    /**
     * 跳转到上一个页面
     *
     * @param resultBoolean
     */
    private void onResultHandler(Boolean resultBoolean) {

        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("result", resultBoolean);
        bundle.putString("bedusername",bindPatientInfo.getPatientName());
        bundle.putString("bedusercode",bindPatientInfo.getPatientCode());
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        this.finish();

    }


}
