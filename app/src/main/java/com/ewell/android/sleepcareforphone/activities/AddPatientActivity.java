package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ewell.android.sleepcareforphone.R;

/**
 * Created by lillix on 10/28/16.
 */
public class AddPatientActivity extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_addpatient);

    }

    public void ClickClose(View view) {

        this.finish();
    }

}