package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.common.addpatientAdapter.MainAdapter;
import com.ewell.android.sleepcareforphone.common.addpatientAdapter.MoreAdapter;
import com.ewell.android.sleepcareforphone.viewmodels.AddPatientViewModel;

import java.util.List;
import java.util.Map;

/**
 * Created by lillix on 10/28/16.
 */
public class AddPatientActivity extends Activity{
    private AddPatientViewModel viewmodel;
    private MoreAdapter moreAdapter;
    private ListView morelist;
    private List<AddPatientViewModel.PartandPatientEntity> mainList;
    private ListView mainlist;
    private MainAdapter mainAdapter;
    private Map<String,String> addCodeNamelist;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_addpatient);
        initModle();// 添加数据

        mainlist = (ListView) findViewById(R.id.classify_mainlist);
        morelist = (ListView) findViewById(R.id.classify_morelist);

        mainAdapter = new MainAdapter(this, mainList);
        mainAdapter.setSelectItem(0);
        mainlist.setAdapter(mainAdapter);

        mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                List<AddPatientViewModel.patientEntity> lists = mainList.get(position).getPatient();

                initAdapter(lists);
                mainAdapter.setSelectItem(position);
                mainAdapter.notifyDataSetChanged();
            }
        });
        mainlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // 一定要设置这个属性，否则ListView不会刷新
        initAdapter(mainList.get(0).getPatient());

        morelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                moreAdapter.setSelectItem(position, addCodeNamelist);
                moreAdapter.notifyDataSetChanged();
            }
        });
    }

    public void AddPatient(View v){

        System.out.print(addCodeNamelist+"============\n");

        viewmodel.AddPatient(addCodeNamelist);
    }

    private void initAdapter(List<AddPatientViewModel.patientEntity> lists) {
        moreAdapter = new MoreAdapter(this, lists);
        morelist.setAdapter(moreAdapter);
        moreAdapter.notifyDataSetChanged();
    }


    private void initModle() {
        viewmodel = new AddPatientViewModel();
        viewmodel.InitData();
        viewmodel.setParentactivity(this);
        mainList = viewmodel.getPartandPatientDic();
        addCodeNamelist = viewmodel.getAddUserCodeList();

    }

    public void ClickClose(View view) {

        this.finish();
    }

}