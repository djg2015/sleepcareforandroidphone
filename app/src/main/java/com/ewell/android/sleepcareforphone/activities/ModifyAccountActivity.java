package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ewell.android.sleepcareforphone.ModifyAccountBinder;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.viewmodels.ModifyAccountViewModel;

/**
 * Created by lillix on 7/21/16.
 */
public class ModifyAccountActivity extends Activity {

    private EditText mOldpaswword;
    private EditText mNewpaswword;
    private EditText mConfirmnewpaswword;

    private Button mBtnPwdVisible;
    private Boolean mPwdVisible = false;

    private ModifyAccountViewModel modifyaccount=null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //绑定model
        modifyaccount = new ModifyAccountViewModel();
        modifyaccount.SetParentActivity(this);
        ModifyAccountBinder binding = DataBindingUtil.setContentView(this, R.layout.activity_modifyaccount);
        binding.setModifyaccount(modifyaccount);

        mOldpaswword = (EditText) findViewById(R.id.oldpassword);
        mOldpaswword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                modifyaccount.setOldpassword(s.toString());
            }
        });
        mNewpaswword = (EditText) findViewById(R.id.newpassword);
        mNewpaswword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                modifyaccount.setNewpassword(s.toString());
            }
        });
        mConfirmnewpaswword = (EditText) findViewById(R.id.confirmnewpassword);
       mConfirmnewpaswword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                modifyaccount.setConfirmnewpwd(s.toString());
            }
        });

        mBtnPwdVisible = (Button) findViewById(R.id.btnpwdvisible);
    }

    //密码可见
    public void ClickPwdVisible(View view) {
        mPwdVisible = !mPwdVisible;
        //设置为明文显示
        if (mPwdVisible) {
            this.mOldpaswword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            this.mNewpaswword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            this.mConfirmnewpaswword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            this.mBtnPwdVisible.setBackgroundResource(R.drawable.icon_eyeopen);
        } else {

            this.mOldpaswword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.mNewpaswword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.mConfirmnewpaswword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.mBtnPwdVisible.setBackgroundResource(R.drawable.icon_eyeclose);
        }
    }


    //返回
    public void ClickClose(View view) {

        this.finish();
    }
}
