package com.ukang.clinic.fragments;

/**
 * 病例报告
 * 景俊钢
 * 2016年4月23日 17:03:02
 */

import android.app.ProgressDialog;
import android.content.Context;
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
import com.ukang.clinic.common.MWDUtils;
import com.ukang.clinic.main.MainActivity;
import com.ukang.clinic.thread.RequestThread;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Frg_18 extends Fragment implements OnClickListener {

	View root;
	/** 药物编号 **/
	@ViewInject(R.id.frg_18_1)
	private EditText frg_18_1;
	/** name **/
	@ViewInject(R.id.frg_18_2)
	private EditText frg_18_2;
	/** 中心编号 **/
	@ViewInject(R.id.frg_18_3)
	private EditText frg_18_3;
	/** 临床研究单位 **/
	@ViewInject(R.id.frg_18_4)
	private EditText frg_18_4;
	/** 研究者姓名 **/
	@ViewInject(R.id.frg_18_5)
	private EditText frg_18_5;
	/** 提交 **/
	@ViewInject(R.id.submit)
	private Button submit;
	// **********************分割线**************************
	Context mContent;
	private RequestThread rThread;
	MWDApplication application;
	public ProgressDialog dia;

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		this.root = paramLayoutInflater.inflate(R.layout.layout_case_report,
				paramViewGroup, false);
		ViewUtils.inject(this, this.root);
		return this.root;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		dia = new ProgressDialog(getActivity());
		dia.setMessage("请稍候...");
		dia.setCanceledOnTouchOutside(false);
		mContent = getActivity();
		application = ((MWDApplication) mContent.getApplicationContext());
		fetchDataFromWebservice(0);
		((MainActivity)getActivity()).setSubmitVisibily(submit);
		submit.setOnClickListener(this);
	}

	/**
	 * 获取历史记录
	 */
	private void fetchDataFromWebservice(int index) {
		if (MWDUtils.isNetworkConnected(mContent)) {
			dia.show();
			ArrayList<NameValuePair> localArrayList = new ArrayList<NameValuePair>();
			localArrayList.add(new BasicNameValuePair("id", application.pid));
			localArrayList
					.add(new BasicNameValuePair("nums", application.nums));
			localArrayList.add(new BasicNameValuePair("token", Constant.token));
			localArrayList.add(new BasicNameValuePair("page", "1"));
			if (index == 0) {
				this.rThread = new RequestThread(localArrayList, "http",
						"post", Constant.BL_REPORT_ADD_URL, mHandler);
				this.rThread.start();
			} else {
				this.rThread = new RequestThread(localArrayList, "http",
						"post", Constant.BL_REPORT_ADD_URL, emHandler);
				this.rThread.start();
			}

		}
	}

	Handler mHandler = new Handler() {

		public void handleMessage(Message paramAnonymousMessage) {
			try {
				dia.dismiss();
				if (paramAnonymousMessage.what != -1) {
					String str = paramAnonymousMessage.obj.toString();
					JSONObject localJSONObject = new JSONObject(str);
					if (localJSONObject.getString("status").toString()
							.equals("1")) {
						str = localJSONObject.getString("info");
					}
				} else {

				}

			} catch (JSONException localJSONException) {
				localJSONException.printStackTrace();
			}
		}
	};
	Handler emHandler = new Handler() {

		public void handleMessage(Message paramAnonymousMessage) {
			try {
				dia.dismiss();
				if (paramAnonymousMessage.what != -1) {
					String str = paramAnonymousMessage.obj.toString();
					JSONObject localJSONObject = new JSONObject(str);
					if (localJSONObject.getString("status").toString()
							.equals("1")) {
						Toast.makeText(mContent, "保存成功", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(mContent, "保存失败", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(mContent, "保存失败", Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException localJSONException) {
				localJSONException.printStackTrace();
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit:
			fetchDataFromWebservice(1);
			break;
		}
	}
}
