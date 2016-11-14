package com.ewell.android.sleepcareforphone.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ewell.android.common.GetRealtimeDataDelegate;
import com.ewell.android.common.RealTimeHelper;
import com.ewell.android.model.EMRealTimeReport;
import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.common.ProgressView;
import com.ewell.android.sleepcareforphone.common.fancychart.ChartData;
import com.ewell.android.sleepcareforphone.common.fancychart.FancyChart;
import com.ewell.android.sleepcareforphone.common.fancychart.FancyChartPointListener;
import com.ewell.android.sleepcareforphone.common.fancychart.Point;
import com.ewell.android.sleepcareforphone.viewmodels.RRViewModel;

import java.util.ArrayList;
import java.util.Map;

public class FragmentRr extends Fragment implements View.OnClickListener, GetRealtimeDataDelegate {
		//, XmppMsgDelegate<EMRealTimeReport> {

	private String BedUserCode = "";
	private String BedUserName = "";

	private TextView patientname;
	private Button back;
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

	private String onbedstatus="";
	private ImageView onbedstatusImg;

	private ImageView alarmImg;


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


		rrViewModel = new RRViewModel();
		rrViewModel.RefreshRRData();

		//Grobal.getXmppManager().SetXmppMsgDelegate(this);
		RealTimeHelper.GetInstance().SetDelegate("realtimedelegate",this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_rr, container, false);


		patientname = (TextView) v.findViewById(R.id.texttitle);
		patientname.setOnClickListener(this);
		patientname.setText(BedUserName);


		onbedstatusImg = (ImageView) v.findViewById(R.id.onbedstatusimg);

		alarmImg = (ImageView) v.findViewById(R.id.alarmimg);
		alarmImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ShowAlarmActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("currentusercode", BedUserCode);
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
			}
		});


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


		back=(Button)v.findViewById(R.id.btnClose);
		back.setOnClickListener(this);
		return v;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnClose:

				getActivity().finish();
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


//	@Override
//	public void ReciveMessage(EMRealTimeReport emRealTimeReport) {
//		if (!BedUserCode.equals("") && BedUserCode.equals(emRealTimeReport.getBedUserCode())) {
//
//			currentRR = emRealTimeReport.getRR();
//			avgRR = emRealTimeReport.getLastedAvgRR();
//			onbedstatus = emRealTimeReport.getOnBedStatus();
//
//
//			mProgressView.post(new Runnable() {
//
//				@Override
//				public void run() {
//					if(!avgRR.equals("")) {
//						mProgressView.setAvgCount(Float.parseFloat(avgRR));
//					}
//
//
//					if(!currentRR.equals("")) {
//						int score = Integer.parseInt(currentRR);
//						mProgressView.setCurrentCount(score);
//						mProgressView.setScore(score);
//						if (onbedstatus.equals("在床")) {
//							if (score > 40 || score < 15) {
//								alarmImg.setBackgroundResource(R.drawable.img_alarm);
//							} else {
//								alarmImg.setBackgroundResource(R.drawable.img_noalarm);
//							}
//						}
//					}
//
//					switch (onbedstatus) {
//						case "在床":
//							onbedstatusImg.setBackgroundResource(R.drawable.icon_onbed);
//							break;
//						case "离床":
//							onbedstatusImg.setBackgroundResource(R.drawable.icon_offbed);
//							break;
//
//						case "请假":
//							onbedstatusImg.setBackgroundResource(R.drawable.icon_offduty);
//							break;
//
//						case "异常":
//							onbedstatusImg.setBackgroundResource(R.drawable.icon_innormal);
//							break;
//
//						default:
//							onbedstatusImg.setBackgroundResource(R.drawable.icon_offline);
//							break;
//					}
//
//
//
//				}
//
//			});
//		}
//	}

	@Override
	public void GetRealtimeData(Map<String, EMRealTimeReport> realtimeData) {
		for (EMRealTimeReport emRealTimeReport : realtimeData.values()) {

			if (BedUserCode.equals(emRealTimeReport.getBedUserCode())) {
				currentRR = emRealTimeReport.getRR();
			avgRR = emRealTimeReport.getLastedAvgRR();
			onbedstatus = emRealTimeReport.getOnBedStatus();


			mProgressView.post(new Runnable() {

				@Override
				public void run() {
					if(!avgRR.equals("")) {
						mProgressView.setAvgCount(Float.parseFloat(avgRR));
					}


					if(!currentRR.equals("")) {
						int score = Integer.parseInt(currentRR);
						mProgressView.setCurrentCount(score);
						mProgressView.setScore(score);
//						if (onbedstatus.equals("在床")) {
//							if (score > 40 || score < 15) {
//								alarmImg.setBackgroundResource(R.drawable.img_alarm);
//							} else {
//								alarmImg.setBackgroundResource(R.drawable.img_noalarm);
//							}
//						}
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

				System.out.print(BedUserCode + "实时数据===============\n");
				break;
			}
		}
	}

}