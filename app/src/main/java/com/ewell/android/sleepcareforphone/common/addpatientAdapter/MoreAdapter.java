package com.ewell.android.sleepcareforphone.common.addpatientAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ewell.android.sleepcareforphone.R;
import com.ewell.android.sleepcareforphone.viewmodels.AddPatientViewModel;

import java.util.List;
import java.util.Map;

public class MoreAdapter extends BaseAdapter {

	private Context context;

	Holder hold;
	private List<AddPatientViewModel.patientEntity> lists;

	public MoreAdapter(Context context, List<AddPatientViewModel.patientEntity> lists) {
		this.context = context;
		this.lists = lists;

	}

	public int getCount() {
		return lists.size();
	}

	public Object getItem(int position) {
		return lists.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int arg0, View view, ViewGroup viewGroup) {

		if (view == null) {
			view = View.inflate(context, R.layout.item_classify_morelist, null);
			hold = new Holder(view);
			view.setTag(hold);
		} else {
			hold = (Holder) view.getTag();
		}
		String name = "姓名:"+lists.get(arg0).getBedusername();
		hold.bedusernametxt.setText(name);
		String bednum = "床号:"+lists.get(arg0).getBednum();
		hold.bednumtxt.setText(bednum);
		String roomnum = "房号:"+lists.get(arg0).getRoomnum();
		hold.roomnumtxt.setText(roomnum);
		String id = "设备编号:"+lists.get(arg0).getEquipmentid();
		hold.equipmentidtxt.setText(id);
		hold.check.setChecked(lists.get(arg0).getIsChoosed());
		//hold.img.setImageResource(Integer.parseInt(list.get(arg0).getCustId()));

		return view;
	}

	public void setSelectItem(int position,Map<String,String> codelist) {
		String tempcode = this.lists.get(position).getBedusercode();
String tempname = this.lists.get(position).getBedusername();

		if(codelist.get(tempcode) != null){
			System.out.print(tempcode+"delete=========\n");
			this.lists.get(position).setIsChoosed(false);
			codelist.remove(tempcode);

		}
		else{
			System.out.print(tempcode+"add=========\n");
			this.lists.get(position).setIsChoosed(true);
			codelist.put(tempcode,tempname);
		}
	}



	private static class Holder {
		TextView bedusernametxt;
		TextView bednumtxt;
		TextView roomnumtxt;
		TextView equipmentidtxt;



		CheckBox check;


		public Holder(View view) {
			bedusernametxt = (TextView) view.findViewById(R.id.bedusername_txt);
			bednumtxt = (TextView) view.findViewById(R.id.bednum_txt);
			roomnumtxt = (TextView) view.findViewById(R.id.roomnum_txt);
			equipmentidtxt = (TextView) view.findViewById(R.id.equipmentid_txt);
			check = (CheckBox) view.findViewById(R.id.item_cb);
		}
	}


}
