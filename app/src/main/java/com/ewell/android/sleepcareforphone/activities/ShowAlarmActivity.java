package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.content.Context;
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

import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenu;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuCreator;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuItem;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuLayout;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuListView;
import com.ewell.android.sleepcareforphone.common.widgets.SwipeMenuView;
import com.ewell.android.sleepcareforphone.viewmodels.ShowAlarmViewModel;

import java.util.List;
import java.util.Map;

/**
 * Created by lillix on 7/25/16.
 */
public class ShowAlarmActivity extends Activity {

    private List<Map<String, Object>> mAppList;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    private Context mContext = this;
    private SwipeMenuCreator creator ;
    private final static String TAG = MyDevicesActivity.class.getSimpleName();

    private ShowAlarmViewModel alarms;


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

        alarms = new ShowAlarmViewModel();
        alarms.setParentactivity(this);
        alarms.InitData();
        setContentView(R.layout.activity_showalarm);

        mAppList = alarms.getList();

        mListView = (SwipeMenuListView) findViewById(R.id.alarmlistView);
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
                Boolean flag =  alarms.DeleteAlarm(position);
                //本地list中删除
                if (flag) {
                    mAppList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
                else{


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
            AlarmViewHolder holder = null;
            SwipeMenuLayout layout = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_list_alarm, null);
                holder = new AlarmViewHolder(convertView);
                SwipeMenu menu = new SwipeMenu(mContext);

                menu.setViewType(1);
                createMenu(menu);
                SwipeMenuView menuView = new SwipeMenuView(menu,(SwipeMenuListView) parent);
                menuView.setOnSwipeItemClickListener(this);
                SwipeMenuListView listView = (SwipeMenuListView) parent;
                layout = new SwipeMenuLayout(convertView, menuView, listView.getCloseInterpolator(), listView.getOpenInterpolator());
                layout.setPosition(position);
                layout.setTag(holder);
            } else {
                layout = (SwipeMenuLayout) convertView;
                layout.closeMenu();
                layout.setPosition(position);
                holder = (AlarmViewHolder)layout.getTag();
            }


            //获取数据源
            Map<String, Object> item = getItem(position);

            String sex = item.get("sex").toString();
            if (sex.equals("1")) {
                holder.sex.setSelected(false);
            } else if (sex.equals("2")) {
                holder.sex.setSelected(true);
            }
            holder.type.setText(item.get("alarmtype").toString());
            holder.timeanddate.setText(item.get("timeanddate").toString());
            holder.bednumber.setText(item.get("bednumber").toString()+"号床");
            holder.alarmcontent.setText(item.get("alarmcontent").toString());
            holder.equipmentid.setText("设备编号: "+ item.get("equipmentid").toString());
            holder.username.setText(item.get("username").toString());

            return layout;
        }

        class AlarmViewHolder {
            Button sex;
            TextView type;
            TextView timeanddate;
            TextView bednumber;
            TextView username;
            TextView alarmcontent;
            TextView equipmentid;

            public AlarmViewHolder(View view) {
                sex = (Button)view.findViewById(R.id.sex);
                type = (TextView) view.findViewById(R.id.type);
                timeanddate = (TextView)view.findViewById(R.id.timeanddate);
                bednumber = (TextView)view.findViewById(R.id.bednumber);
                username = (TextView)view.findViewById(R.id.name);
                alarmcontent = (TextView)view.findViewById(R.id.alarmcontent);
                equipmentid = (TextView)view.findViewById(R.id.equipmentid);
            }
        }



        public void createMenu(SwipeMenu menu) {
            if(creator != null) {
                creator.create(menu);
            }
        }

        @Override
        public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
            if (mListView != null && mListView.getOnMenuItemClickListener() != null) {
                mListView.getOnMenuItemClickListener().onMenuItemClick(view.getPosition(), menu, index);
            }

            if(mListView != null && mListView.getTouchView() != null) {
                mListView.getTouchView().smoothCloseMenu();
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  dp, getResources().getDisplayMetrics());
    }

    public void ClickClose(View view){
        this.finish();

    }


}
