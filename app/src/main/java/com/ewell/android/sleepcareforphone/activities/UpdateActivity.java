package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.common.CheckUpdate;

/**
 * Created by lillix on 8/9/16.
 */
public class UpdateActivity extends Activity {

    private TextView currentversionTxt;
    private TextView lastestversionTxt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        currentversionTxt = (TextView)findViewById(R.id.currentversion);
        lastestversionTxt = (TextView)findViewById(R.id.latestversion);

        CheckUpdate ck = new CheckUpdate(this,false,null);
        ck.CheckVersionTask();

        currentversionTxt.setText("当前版本: "+ck.getLocalversion());
        lastestversionTxt.setText("最新版本: "+ck.getLatestversion());

    }

    public void ClickClose(View view) {
        this.finish();

    }
}
