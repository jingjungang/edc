package com.ukang.clinic.adapter;

/**
 * 合并用药Adapter 
 */

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ukang.clinic.R;
import com.ukang.clinic.activity.add.AddCombinationDrug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/***
 * @author Administrator
 */
public class CombinationDrugListAdapter extends BaseAdapter {

	private Context contextNative;
	private JSONArray InfoArray;
	private LayoutInflater inflater;
	String nums = "";
	int clicked = 0;

	/**
	 * 
	 * @param context
	 * @param nursingAdviceRecordInfoArra
	 * @param nums
	 */
	public CombinationDrugListAdapter(Context context,
			JSONArray nursingAdviceRecordInfoArra, String nums) {
		this.contextNative = context;
		this.InfoArray = nursingAdviceRecordInfoArra;
		this.nums = nums;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (InfoArray == null) {
			return 0;
		} else {
			return InfoArray.length();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Object res = null;
		try {
			res = InfoArray.get(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		try {
			viewHolder = new ViewHolder();
			if (nums.equals("2") || nums.equals("3") || nums.equals("4")) {
				convertView = (View) inflater.inflate(
						R.layout.adapter_combination_drug_234, parent, false);
				if (nums.equals("4")) {
					viewHolder.tx_4 = (TextView) convertView
							.findViewById(R.id.tv4);
					viewHolder.tx_4.setVisibility(View.VISIBLE);
				} else {

				}
				viewHolder.tv_3_1 = (TextView) convertView
						.findViewById(R.id.tv_3_1);
			} else {
				convertView = (View) inflater.inflate(
						R.layout.adapter_combination_drug, parent, false);
				viewHolder.tx_4 = (TextView) convertView.findViewById(R.id.tv4);
			}
			viewHolder.tx_1 = (TextView) convertView.findViewById(R.id.tv1);
			viewHolder.tx_2 = (TextView) convertView.findViewById(R.id.tv2);
			viewHolder.tx_3 = (TextView) convertView.findViewById(R.id.tv3);

			viewHolder.date1 = (TextView) convertView.findViewById(R.id.date1);
			viewHolder.date2 = (TextView) convertView.findViewById(R.id.date2);

			JSONObject jo = InfoArray.getJSONObject(position);
			String columns1 = "", columns2 = "", columns3 = "", columns4 = "", columns5 = "", columns6 = "", columns7 = "";

			columns1 = jo.getString("diseasename");
			columns2 = jo.getString("drugtherapy");
			columns3 = jo.getString("dosage");
			if (!TextUtils.isEmpty(jo.getString("startdate"))) {
				columns4 = jo.getString("startdate"); // .substring(0,jo.getString("startdate").indexOf(" "))
				viewHolder.date1.setText(columns4);
			}
			if (!TextUtils.isEmpty(jo.getString("enddate"))) {
				columns5 = jo.getString("enddate");// .substring(0,jo.getString("enddate").indexOf(" "))
				viewHolder.date2.setText(columns5);
			}
			columns6 = jo.getString("stillinuse").equals("on") ? "是" : "否";

			if (nums.equals("2") || nums.equals("3") || nums.equals("4")) {
				columns7 = jo.getString("usereason");
				viewHolder.tv_3_1.setText(columns7);
			}

			viewHolder.tx_1.setText(columns1);
			viewHolder.tx_2.setText(columns2);
			viewHolder.tx_3.setText(columns3);
			if (null != viewHolder.tx_4)
				viewHolder.tx_4.setText(columns6);

			if (position % 2 == 0) {
				convertView.setBackgroundResource(R.color.transparent);
			} else {
				convertView.setBackgroundResource(R.color.gray_1);
			}
			convertView.setOnClickListener(new ItemOnClicked(position));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	class ItemOnClicked implements View.OnClickListener {

		private int pos;

		public ItemOnClicked(int position) {
			this.pos = position;
		}

		public void onClick(View v) {
			clicked = pos;
			// notifyDataSetChanged();
			JSONObject jo = null;
			try {
				jo = InfoArray.getJSONObject(clicked);
				Intent i = new Intent(contextNative, AddCombinationDrug.class);
				i.putExtra("id", jo.getString("id"));
				i.putExtra("diseasename", jo.getString("diseasename"));
				i.putExtra("drugtherapy", jo.getString("drugtherapy"));
				i.putExtra("dosage", jo.getString("dosage"));
				i.putExtra("usereason",
						jo.has("usereason") ? jo.getString("usereason") : "");
				i.putExtra(
						"startdate",
						jo.getString("startdate").substring(0,
								jo.getString("startdate").indexOf(" ")));
				i.putExtra(
						"date11",
						jo.getString("startdate").substring(
								jo.getString("startdate").indexOf(" ") + 1));
				if (!TextUtils.isEmpty(jo.getString("enddate"))) {
					i.putExtra(
							"enddate",
							jo.getString("enddate").substring(0,
									jo.getString("enddate").indexOf(" ")));
					i.putExtra(
							"date22",
							jo.getString("enddate").substring(
									jo.getString("enddate").indexOf(" ") + 1));
				}
				i.putExtra("stillinuse",
						jo.has("stillinuse") ? jo.getString("stillinuse") : "");
				contextNative.startActivity(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	static class ViewHolder {
		LinearLayout ll;
		TextView tx_1;
		TextView tx_2;
		TextView tx_3;
		TextView tv_3_1;
		TextView tx_4;
		TextView date1;
		TextView date2;

	}

}
