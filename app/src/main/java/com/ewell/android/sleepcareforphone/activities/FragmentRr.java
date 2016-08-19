package com.ewell.android.sleepcareforphone.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewell.android.common.Grobal;
import com.ewell.android.common.xmpp.XmppMsgDelegate;
import com.ewell.android.model.EMRealTimeReport;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.common.ProgressView;
import com.ewell.android.sleepcareforphone.common.fancychart.ChartData;
import com.ewell.android.sleepcareforphone.common.fancychart.FancyChart;
import com.ewell.android.sleepcareforphone.common.fancychart.FancyChartPointListener;
import com.ewell.android.sleepcareforphone.common.fancychart.Point;
import com.ewell.android.sleepcareforphone.viewmodels.RRViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class FragmentRr extends Fragment implements View.OnClickListener, XmppMsgDelegate<EMRealTimeReport> {

	private String BedUserCode = "";
	private String BedUserName = "";

	private TextView patientname;
	private Button b1;

	private ArrayList<String> tempusercodelist = new ArrayList<String>();
	private ArrayList<String> tempusernamelist = new ArrayList<String>();
	//private String[] areas = new String[]{"张奶奶", "王奶奶", "王爷爷", "李爷爷", "123"};
	private RadioOnClick radioOnClick = new RadioOnClick(0);
	private ListView areaRadioListView;

	private ProgressView mProgressView;


	private FancyChart chart;

	private TextView button1;
	private TextView button2;
	private TextView button3;
	private int button_select = Color.parseColor("#56a3fd");
	private int button_default = Color.parseColor("#929292");

	private SharedPreferences sp;
	// 获取到一个参数文件的编辑器。
	private SharedPreferences.Editor editor;

	private RRViewModel rrViewModel;
	private int SelectedIndex=1;

	private String currentRR;
	private String avgRR;
	private TextView currentRR_text;
	private TextView avgRR_text;

	private String onbedstatus="";
	private ImageView onbedstatusImg;

	private ImageView alarmImg;

	private RelativeLayout view1;
	private LinearLayout view2;
	private RelativeLayout view3;
	private RelativeLayout view4;


	//从父activty传参
	public static FragmentRr newInstance(String bedusercode,String bedusername) {
		FragmentRr fragmentRr = new FragmentRr();
		Bundle args = new Bundle();
		args.putString("bedusercode", bedusercode);
		args.putString("bedusername", bedusername);
		fragmentRr.setArguments(args);
		return fragmentRr;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		BedUserCode = getArguments().getString("bedusercode", "");
		//初始化数据
		BedUserName = getArguments().getString("bedusername", "");
		// 在我的设备里删除当前关注的老人,返回此页面,需要将bedusercode/name置空
		if(Grobal.getInitConfigModel().getCurUserCode().equals("")){
			BedUserCode="";
			BedUserName ="";
		}

		sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
		editor = sp.edit();

		rrViewModel = new RRViewModel();
		rrViewModel.RefreshRRData();

		Grobal.getXmppManager().SetXmppMsgDelegate(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_rr, container, false);

		//change patient button
		b1 = (Button) v.findViewById(R.id.btn1);
		b1.setOnClickListener(this);
		patientname = (TextView) v.findViewById(R.id.texttitle);
		patientname.setOnClickListener(this);
		if(!BedUserName.equals("")) {
			patientname.setText(BedUserName);
		}

		onbedstatusImg = (ImageView) v.findViewById(R.id.onbedstatusimg);
		alarmImg = (ImageView) v.findViewById(R.id.alarmimg);

		currentRR_text = (TextView) v.findViewById(R.id.currentRR);
		avgRR_text = (TextView) v.findViewById(R.id.avgRR);

		//hr circle
		mProgressView = (ProgressView) v.findViewById(R.id.my_progress);
		mProgressView.setMaxCount(60.0f);
		mProgressView.setProgressviewType("呼吸");


		//hr chart
		chart = (FancyChart) v.findViewById(R.id.my_rrchart);
		chart.setOnPointClickListener(new FancyChartPointListener() {

			@Override
			public void onClick(Point point) {
				Toast.makeText(getActivity(), "平均呼吸" + point.y + "次/分", Toast.LENGTH_SHORT).show();

			}
		});
		ShowHourChart();


		//select chart button
		button1 = (TextView) v.findViewById(R.id.button1);
		button1.setOnClickListener(this);
		button2 = (TextView) v.findViewById(R.id.button2);
		button2.setOnClickListener(this);
		button3 = (TextView) v.findViewById(R.id.button3);
		button3.setOnClickListener(this);

		//当前账户下没有设备,则隐藏子view,只显示背景图片
		view1 = (RelativeLayout) v.findViewById(R.id.topview1);
		view2 = (LinearLayout)v.findViewById(R.id.circleview);
		view3 = (RelativeLayout)v.findViewById(R.id.centerview);
		view4=(RelativeLayout)v.findViewById(R.id.chartview);
		if(Grobal.getInitConfigModel().getEquipmentcodeList().size()==0){
			view1.setVisibility(View.INVISIBLE);
			view2.setVisibility(View.INVISIBLE);
			view3.setVisibility(View.INVISIBLE);
			view4.setVisibility(View.INVISIBLE);
		}


		return v;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn1:
				ClickChangeBtn();
				break;
			case R.id.texttitle:
				ClickChangeBtn();
				break;

			case R.id.button1:
				button1.setTextColor(button_select);
				button2.setTextColor(button_default);
				button3.setTextColor(button_default);
				ShowHourChart();
				break;
			case R.id.button2:
				button2.setTextColor(button_select);
				button1.setTextColor(button_default);
				button3.setTextColor(button_default);
				ShowWeekChart();
				break;
			case R.id.button3:
				button3.setTextColor(button_select);
				button2.setTextColor(button_default);
				button1.setTextColor(button_default);
				ShowMonthChart();
				break;

			default:
				System.out.print("============================none");
				break;
		}

	}
	//------------------------------------ 切换老人---------------------------
	public void ClickChangeBtn() {
		Map<String, String> tempmap = Grobal.getInitConfigModel().getUserCodeNameMap();
		tempusernamelist.clear();
		tempusercodelist.clear();

		// entrySet使用iterator遍历key和value
		Iterator<Map.Entry<String, String>> it = tempmap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			tempusercodelist.add(entry.getKey());
			tempusernamelist.add(entry.getValue());
			//   System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
		}

		int count = tempusercodelist.size();

		if (count > 0) {
			int i = 0;
			String[] usernamelist = new String[count];
			while (i < count) {
				usernamelist[i] = tempusernamelist.get(i);
				i++;
			}
			//弹窗
			AlertDialog ad = new AlertDialog.Builder(getActivity())
					.setTitle("选择老人")
					.setSingleChoiceItems(usernamelist, radioOnClick.getIndex(), radioOnClick)
					.create();


			areaRadioListView = ad.getListView();
			ad.show();

			ad.getWindow().setLayout(500, 500);
		} else {
			Toast.makeText(getActivity(), "当前账户下没有关注老人!请先添加设备", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 点击单选框事件
	 *
	 * @author xmz
	 */
	class RadioOnClick implements DialogInterface.OnClickListener {
		private int index;

		public RadioOnClick(int index) {
			this.index = index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public void onClick(DialogInterface dialog, int whichButton) {
			setIndex(whichButton);
			patientname.setText(tempusernamelist.get(index));
			BedUserCode = tempusercodelist.get(index);
			Grobal.getInitConfigModel().setCurUserCode(tempusercodelist.get(index));
			Grobal.getInitConfigModel().setCurUserName(tempusernamelist.get(index));

			editor.putString("curusercode", tempusercodelist.get(index));
			editor.putString("curusername", tempusernamelist.get(index));
			editor.commit();
			//   Toast.makeText(getActivity(), "您已经选择了： " + index + ":" + areas[index], Toast.LENGTH_SHORT).show();
			//更新rr数据
			rrViewModel.RefreshRRData();
			switch (SelectedIndex) {
				case 1:
					ShowHourChart();
					break;
				case 2:
					ShowWeekChart();
					break;
				case 3:
					ShowMonthChart();
					break;
				default:
					break;
			}

			dialog.dismiss();
		}
	}

	//------------------------点击按钮选择查看chart类型
	public void ShowHourChart() {


		chart.clearValues();
		ChartData data = new ChartData(ChartData.LINE_COLOR_GREEN);
	//	int[] yValues = new int[]{14, 13, 19, 15, 35, 30, 33, 32, 46, 33, 26, 42};
		ArrayList<String> tempxlist = rrViewModel.getRrDay_xValues();
		ArrayList<String> tempylist = rrViewModel.getRrDay_yValues();

		if (tempxlist!=null && tempxlist.size() > 0) {
			for (int i = 0; i < tempxlist.size(); i++) {
				data.addPoint(i,  Integer.parseInt(tempylist.get(i)));
				data.addXValue(i, tempxlist.get(i));
			}
			chart.addData(data);
		}
	}

	public void ShowWeekChart() {


		chart.clearValues();
		ChartData data = new ChartData(ChartData.LINE_COLOR_GREEN);
	//	int[] yValues = new int[]{41, 32, 19, 15, 25, 33, 23, 32, 36, 43, 56, 42};

		ArrayList<String> tempxlist = rrViewModel.getRrWeek_xValues();
		ArrayList<String> tempylist = rrViewModel.getRrWeek_yValues();
		if (tempxlist!=null && tempxlist.size() > 0) {

			for (int i = 0; i < tempxlist.size(); i++) {
				data.addPoint(i, Integer.parseInt(tempylist.get(i)));
				data.addXValue(i, tempxlist.get(i));
			}
			chart.addData(data);
		}
	}

	public void ShowMonthChart() {

		chart.clearValues();
		ChartData data = new ChartData(ChartData.LINE_COLOR_GREEN);
		//int[] yValues = new int[]{21, 22, 29, 25, 25, 43, 33, 32, 36, 43, 46, 42};
		ArrayList<String> tempxlist = rrViewModel.getRrMonth_xValues();
		ArrayList<String> tempylist = rrViewModel.getRrMonth_yValues();

		if (tempxlist!=null && tempxlist.size() > 0) {
			if (tempxlist.size() > 20) {
				for (int i = 0; i < tempxlist.size(); i += 2) {
					data.addPoint(i, Integer.parseInt(tempylist.get(i)));
					data.addXValue(i, tempxlist.get(i));
				}
			} else {
				for (int i = 0; i < tempxlist.size(); i++) {
					data.addPoint(i, Integer.parseInt(tempylist.get(i)));
					data.addXValue(i, tempxlist.get(i));
				}
			}
			chart.addData(data);
		}
	}


	@Override
	public void ReciveMessage(EMRealTimeReport emRealTimeReport) {
		if (!BedUserCode.equals("") && BedUserCode.equals(emRealTimeReport.getBedUserCode())) {

			currentRR = emRealTimeReport.getRR();
			avgRR = emRealTimeReport.getLastedAvgRR();
			onbedstatus = emRealTimeReport.getOnBedStatus();


			mProgressView.post(new Runnable() {

				@Override
				public void run() {
					if(!avgRR.equals("")) {
						mProgressView.setAvgCount(Float.parseFloat(avgRR));
					}
					avgRR_text.setText(avgRR);
					currentRR_text.setText(currentRR);

					if(!currentRR.equals("")) {
						int score = Integer.parseInt(currentRR);
						mProgressView.setCurrentCount(score);
						mProgressView.setScore(score);
						if (onbedstatus.equals("在床")) {
							if (score > 40 || score < 15) {
								alarmImg.setBackgroundResource(R.drawable.img_alarm);
							} else {
								alarmImg.setBackgroundResource(R.drawable.img_noalarm);
							}
						}
					}

					switch (onbedstatus) {
						case "在床":
							onbedstatusImg.setBackgroundResource(R.drawable.icon_onbed);
							break;
						case "离床":
							onbedstatusImg.setBackgroundResource(R.drawable.icon_offbed);
							break;

						case "请假":
							onbedstatusImg.setBackgroundResource(R.drawable.icon_offduty);
							break;

						case "异常":
							onbedstatusImg.setBackgroundResource(R.drawable.icon_innormal);
							break;

						default:
							onbedstatusImg.setBackgroundResource(R.drawable.icon_offline);
							break;
					}



				}

			});
		}
	}

}