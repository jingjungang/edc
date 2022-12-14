package com.ukang.clinic.fragments;

/**
 * 用药依从性
 * jjg
 * 2016年6月13日 13:24:30
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.clinic.R;
import com.ukang.clinic.application.MWDApplication;
import com.ukang.clinic.common.Constant;
import com.ukang.clinic.main.MainActivity;
import com.ukang.clinic.thread.RequestThread;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ObeyMedicine_Frgment extends Fragment implements OnClickListener {

	private RequestThread rThread;
	public ProgressDialog dia;
	// 真实用药
	@ViewInject(R.id.real_drugname)
	private EditText et_real_drugname;
	// 实际用药
	@ViewInject(R.id.should_take_drugname)
	private EditText et_should_take_drugname;
	// 依从性
	@ViewInject(R.id.take_drug_probability)
	private EditText et_take_drug_probability;

	@ViewInject(R.id.submit)
	private Button submit;
	View root;
	MWDApplication ma;

	String fetch_id = "";

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		this.root = paramLayoutInflater.inflate(
				R.layout.fragement_obeymedicine, paramViewGroup, false);
		ViewUtils.inject(this, this.root);
		ma = (MWDApplication) getActivity().getApplication();
		submit.setOnClickListener(this);
		((MainActivity)getActivity()).setSubmitVisibily(submit);
		setDataByPost(1);
		return this.root;
	}

	/**
	 * 数据请求 1fetch 2edit
	 */
	private void setDataByPost(int i) {
		dia = new ProgressDialog(getActivity());
		dia.setMessage("请稍候...");
		dia.setCanceledOnTouchOutside(false);
		dia.show();
		ArrayList<NameValuePair> localArrayList = new ArrayList<NameValuePair>();
		localArrayList.add(new BasicNameValuePair("token", Constant.token));
		localArrayList.add(new BasicNameValuePair("nums", ma.nums));
		localArrayList.add(new BasicNameValuePair("pid", ma.pid));
		if (i == 1) {
			this.rThread = new RequestThread(localArrayList, "http", "post",
					Constant.OBEY_MEDICINE_URL, fetchHandler);
		} else if (i == 2) {
			localArrayList.add(new BasicNameValuePair("drugsname",
					et_real_drugname.getText().toString()));
			localArrayList.add(new BasicNameValuePair("drugstitle",
					et_should_take_drugname.getText().toString()));
			localArrayList.add(new BasicNameValuePair("drugspercent",
					et_take_drug_probability.getText().toString()));
			this.rThread = new RequestThread(localArrayList, "http", "post",
					Constant.OBEY_MEDICINE_EDIT_URL, mEidHandler);
		}
		this.rThread.start();
	}

	Handler mEidHandler = new Handler() {
		public void handleMessage(Message msg) {
			dia.dismiss();
			String result = msg.obj.toString();
			if (null != result) {
				JSONObject json;
				try {
					json = new JSONObject(result);
					int status = json.getInt("status");
					switch (status) {
					case 0:
						Toast.makeText(getActivity(), "保存失败",
								Toast.LENGTH_SHORT).show();
						break;
					case 1:
						Toast.makeText(getActivity(), "保存成功",
								Toast.LENGTH_SHORT).show();
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	};

	Handler fetchHandler = new Handler() {
		public void handleMessage(Message msg) {
			dia.dismiss();
			try {
				if (null != msg.obj) {
					String reuslt = msg.obj.toString();
					JSONObject json = new JSONObject(reuslt);
					String status = json.getString("status");
					if (status.equals("-1")) {
					} else if (status.equals("1")) {
						String temp = json.getString("info");
						json = new JSONObject(temp);
						String r_name, d_name, possible;
						r_name = json.getString("drugsname")
								.replace("null", "");
						d_name = json.getString("drugstitle").replace("null",
								"");
						possible = json.getString("drugspercent").replace(
								"null", "");
						et_real_drugname.setText(r_name);
						et_should_take_drugname.setText(d_name);
						et_take_drug_probability.setText(possible);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		};
	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		setDataByPost(2);
	}
}
