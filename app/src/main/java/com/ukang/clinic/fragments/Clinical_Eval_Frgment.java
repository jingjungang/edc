package com.ukang.clinic.fragments;

/**
 * 临床综合疗效评价
 * jjg
 * 2016年7月12日 13:10:19
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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

public class Clinical_Eval_Frgment extends Fragment implements OnClickListener {

	private RequestThread rThread;
	public ProgressDialog dia;

	@ViewInject(R.id.radioGroup)
	private RadioGroup radioGroup;

	@ViewInject(R.id.r1)
	RadioButton rb_1;

	@ViewInject(R.id.r2)
	RadioButton rb_2;

	@ViewInject(R.id.r3)
	RadioButton rb_3;

	@ViewInject(R.id.r4)
	RadioButton rb_4;

	@ViewInject(R.id.r5)
	RadioButton rb_5;

	@ViewInject(R.id.submit)
	private Button submit;

	View root;
	MWDApplication ma;

	int choosed_id; // 选中者

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		this.root = paramLayoutInflater.inflate(R.layout.fragment_clinic_eval,
				paramViewGroup, false);
		ViewUtils.inject(this, this.root);
		ma = (MWDApplication) getActivity().getApplication();
		submit.setOnClickListener(this);
		((MainActivity) getActivity()).setSubmitVisibily(submit);
		setDataByPost(1);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {
				// TODO Auto-generated method stub
				switch (id) {
				case R.id.r1:
					choosed_id = 1;
					break;
				case R.id.r2:
					choosed_id = 2;
					break;
				case R.id.r3:
					choosed_id = 3;
					break;
				case R.id.r4:
					choosed_id = 4;
					break;
				case R.id.r5:
					choosed_id = 5;
					break;
				}
			}
		});
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
		if (i == 1) {
			localArrayList.add(new BasicNameValuePair("id", ma.pid));
			this.rThread = new RequestThread(localArrayList, "http", "post",
					Constant.CLINIC_EVAL_URL, fetchHandler);
		} else if (i == 2) {
			localArrayList.add(new BasicNameValuePair("eid", ma.pid));
			localArrayList.add(new BasicNameValuePair("no", choosed_id + ""));
			this.rThread = new RequestThread(localArrayList, "http", "post",
					Constant.CLINIC_EVAL_URL, mEidHandler);
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
					if (status == 1) {
						Toast.makeText(getActivity(), "保存成功",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getActivity(), "保存失败",
								Toast.LENGTH_SHORT).show();
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
			String reuslt = msg.obj.toString();
			try {
				JSONObject json = new JSONObject(reuslt);
				String status = json.getString("status");
				if (status.equals("1")) {
					String temp = json.getString("info");
					json = new JSONObject(temp);
					String no = json.getString("no");
					if (no.equals("1")) {
						rb_1.setChecked(true);
					} else if (no.equals("2")) {
						rb_2.setChecked(true);
					} else if (no.equals("3")) {
						rb_3.setChecked(true);
					} else if (no.equals("4")) {
						rb_4.setChecked(true);
					} else if (no.equals("5")) {
						rb_5.setChecked(true);
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
