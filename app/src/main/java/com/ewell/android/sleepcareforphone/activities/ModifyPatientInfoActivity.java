package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ewell.android.sleepcareforphone.ModifyPatientInfoBinder;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.viewmodels.ModifyPatientInfoViewModel;

/**
 * Created by lillix on 7/21/16.
 */
public class ModifyPatientInfoActivity extends Activity {
    private ModifyPatientInfoViewModel modifyPatientInfo = null;

    private EditText mPatientName;
    private EditText mTelephone;
    private EditText mAddress;
    private Button mFemaleBtn;
    private Button mMaleBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modifyPatientInfo = new ModifyPatientInfoViewModel();
       modifyPatientInfo.SetParentActivity(this);
        modifyPatientInfo.InitData();

        ModifyPatientInfoBinder binding = DataBindingUtil.setContentView(this, R.layout.activity_modifypatientinfo);
        binding.setModifyPatientInfo(modifyPatientInfo);


        mPatientName = (EditText) findViewById(R.id.textpatientname);
        mPatientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                modifyPatientInfo.setPatientName(s.toString());
            }
        });

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
                modifyPatientInfo.setPatientTelephone(s.toString());
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
                modifyPatientInfo.setPatientAddress(s.toString());
            }
        });

        mFemaleBtn = (Button) findViewById(R.id.btnfemale);
        mMaleBtn = (Button) findViewById(R.id.btnmale);
        mFemaleBtn.setSelected(modifyPatientInfo.getIsFemale());
        mMaleBtn.setSelected(modifyPatientInfo.getIsMale());

    }

    public void ClickFemale(View view) {

        if (!modifyPatientInfo.getIsFemale()){
            modifyPatientInfo.setIsFemale(true);
            modifyPatientInfo.setIsMale(false);
            mFemaleBtn.setSelected(true);
            mMaleBtn.setSelected(false);
        }

    }

    public void ClickMale(View view) {
        if (!modifyPatientInfo.getIsMale()){
            modifyPatientInfo.setIsMale(true);
            modifyPatientInfo.setIsFemale(false);
            mFemaleBtn.setSelected(false);
            mMaleBtn.setSelected(true);
        }

    }


    public void ClickClose(View view) {

        this.finish();
    }

}
