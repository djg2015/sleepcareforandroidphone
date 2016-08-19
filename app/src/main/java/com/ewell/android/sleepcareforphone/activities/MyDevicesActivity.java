package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ewell.android.common.Grobal;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenu;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuCreator;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuItem;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuLayout;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuListView;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuView;
import com.ewell.android.sleepcareforphone.viewmodels.MyDevicesViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lillix on 7/21/16.
 */
public class MyDevicesActivity extends Activity {

    private List<Map<String, Object>> mAppList;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    private Context mContext = this;
    private SwipeMenuCreator creator;
    private final static String TAG = MyDevicesActivity.class.getSimpleName();

    private MyDevicesViewModel mydevices;

    private final static int ADD_DEVICE_CODE = 3;
    private final static int CONFIRM_PATIENT_CODE = 2;

    private void createMenu2(SwipeMenu menu) {

        SwipeMenuItem item2 = new SwipeMenuItem(
                getApplicationContext());
        item2.setBackground(new ColorDrawable(Color.rgb(0xF9,
                0x3F, 0x25)));
        item2.setWidth(dp2px(90));

        item2.setIcon(R.drawable.ic_delete);
        menu.addMenuItem(item2);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mydevices = new MyDevicesViewModel();
        mydevices.setParentacticity(this);
        mydevices.InitData();
        setContentView(R.layout.activity_mydevices);

        mAppList = mydevices.getList();

        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);

        // step 1. create a MenuCreator
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                createMenu2(menu);

            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                //调用接口
                Boolean flag = mydevices.DeleteDevice(position);
                //本地list中删除
                if (flag) {

                    mAppList.remove(position);
                    mAdapter.notifyDataSetChanged();
                } else {


                }


            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

//        // test item long click
//        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }
        });
    }

    class AppAdapter extends BaseAdapter implements SwipeMenuView.OnSwipeItemClickListener {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public Map<String, Object> getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            SwipeMenuLayout layout = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_list_app, null);
                holder = new ViewHolder(convertView);
                SwipeMenu menu = new SwipeMenu(mContext);
                // menu.setViewType(new Random().nextInt(3));//随机选择样式
                menu.setViewType(1);
                createMenu(menu);
                SwipeMenuView menuView = new SwipeMenuView(menu, (SwipeMenuListView) parent);
                menuView.setOnSwipeItemClickListener(this);
                SwipeMenuListView listView = (SwipeMenuListView) parent;
                layout = new SwipeMenuLayout(convertView, menuView, listView.getCloseInterpolator(), listView.getOpenInterpolator());
                layout.setPosition(position);
                layout.setTag(holder);
            } else {
                layout = (SwipeMenuLayout) convertView;
                layout.closeMenu();
                layout.setPosition(position);
                holder = (ViewHolder) layout.getTag();
            }


            //获取数据源
            Map<String, Object> item = getItem(position);

            String sex = item.get("sex").toString();
            if (sex.equals("1")) {
                holder.sex.setSelected(false);
            } else if (sex.equals("2")) {
                holder.sex.setSelected(true);
            }
            holder.name.setText(item.get("name").toString());
            holder.equipmentid.setText("设备编号: " + item.get("equipmentid").toString());
            holder.address.setText(item.get("address").toString());

            return layout;
        }

        class ViewHolder {
            Button sex;
            TextView name;
            TextView equipmentid;
            TextView address;


            public ViewHolder(View view) {
                sex = (Button) view.findViewById(R.id.sex);
                name = (TextView) view.findViewById(R.id.name);
                equipmentid = (TextView) view.findViewById(R.id.equipmentid);
                address = (TextView) view.findViewById(R.id.address);
            }
        }


        public void createMenu(SwipeMenu menu) {
            if (creator != null) {
                creator.create(menu);
            }
        }

        @Override
        public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
            if (mListView != null && mListView.getOnMenuItemClickListener() != null) {
                mListView.getOnMenuItemClickListener().onMenuItemClick(view.getPosition(), menu, index);
            }

            if (mListView != null && mListView.getTouchView() != null) {
                mListView.getTouchView().smoothCloseMenu();
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void ClickClose(View view) {
        this.finish();

    }

    public void ClickAdd(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MipcaActivityCapture.class);
        startActivityForResult(intent, ADD_DEVICE_CODE);

    }

    //从上一页返回本页,根据code区分activity,获取传回的参数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_DEVICE_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String tempEquipmentid = bundle.getString("result");
                    ArrayList<String> templist = Grobal.getInitConfigModel().getEquipmentcodeList();

                    Boolean flag = false;
                    for(String ss:templist){
                        if(ss.equals(tempEquipmentid)){
                            flag = true;
                            break;
                        }
                    }
                    if(flag){
                        Toast.makeText(getApplicationContext(), "已添加过此设备!", Toast.LENGTH_LONG).show();
                    }
                    else{
                    //  绑定设备到当前账户下
                    Boolean bindflag = mydevices.BindDevice(tempEquipmentid);
                    if (bindflag) {
                       //更新session里的equipmentcodelist
                            templist.add(tempEquipmentid);
                            Grobal.getInitConfigModel().setEquipmentcodeList(templist);

                        //绑定设备后,继续确认老人信息
                        Toast.makeText(getApplicationContext(), "添加设备成功!请继续确认老人信息", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        Bundle bundle2=new Bundle();
                        bundle2.putString("equipmentid",tempEquipmentid );
                        intent.putExtras(bundle2);
                        intent.setClass(this, BindPatientInfoActivity.class);
                        startActivityForResult(intent, CONFIRM_PATIENT_CODE);
                    } else {
                    }   Toast.makeText(getApplicationContext(), "绑定设备失败!", Toast.LENGTH_LONG).show();
                    }

                }
                break;
            case CONFIRM_PATIENT_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //老人信息确认成功
                    Boolean confirmFlag = bundle.getBoolean("result");
                    String tempusercode = bundle.getString("bedusercode");
                    String tempusername = bundle.getString("bedusername");

                    if (confirmFlag) {
                        Toast.makeText(getApplicationContext(), "确认老人信息成功!", Toast.LENGTH_LONG).show();
                        //更新session里的UserCodeNameMap,bedusercodelist
                        Map<String, String> tempmap = Grobal.getInitConfigModel().getUserCodeNameMap();
                        tempmap.put(tempusercode,tempusername);
                        Grobal.getInitConfigModel().setUserCodeNameMap(tempmap);

                        ArrayList<String> templist2 = Grobal.getInitConfigModel().getBedusercodeList();
                        templist2.add(tempusercode);
                        Grobal.getInitConfigModel().setBedusercodeList(templist2);

                    } else {

                        Toast.makeText(getApplicationContext(), "老人信息确认失败!可在我的老人里进行修改", Toast.LENGTH_LONG).show();
                    }
                    this.finish();
                }
                break;
        }
    }
}
