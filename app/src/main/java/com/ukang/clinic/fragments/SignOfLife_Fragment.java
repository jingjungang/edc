package com.ukang.clinic.fragments;

/**
 * 生命体征
 * jjg
 * 2016年4月21日 11:46:28
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class SignOfLife_Fragment extends Fragment implements OnClickListener {
	View root;
	/**
	 * 新增
	 */
	@ViewInject(R.id.submit)
	private Button submit;
	// 体温(℃)
	@ViewInject(R.id.tw)
	private EditText edt_tv;
	// 呼吸(次/分)
	@ViewInject(R.id.hx)
	private EditText edt_hx;
	// 心率(次/分)
	@ViewInject(R.id.xl)
	private EditText edt_xl;
	// 血压(mmHg)
	@ViewInject(R.id.xy)
	private EditText edt_xy;
	// 脉搏(次/分)
	@ViewInject(R.id.mb)
	private EditText edt_mb;

	// **********************分割线**************************
	Context mContext;
	private RequestThread rThread;
	MWDApplication application;
	public ProgressDialog dia;

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		this.root = paramLayoutInflater.inflate(R.layout.layout_sign_of_life,
				paramViewGroup, false);
		ViewUtils.inject(this, this.root);
		((MainActivity) getActivity()).setSubmitVisibily(submit);
		setTextChangedListenner(edt_tv, 36, 37);
		setTextChangedListenner(edt_hx, 12, 26);
		setTextChangedListenner(edt_xl, 60, 100);
		setTextChangedListenner(edt_mb, 60, 100);
		return this.root;
	}

	/**
	 * 设置监听
	 */
	private void setTextChangedListenner(final EditText edt, final int min,
			final int max) {
		// TODO Auto-generated method stub
		edt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				try {
					Double temp = Double.valueOf(arg0.toString());
					if (edt.isFocused()) { // 判断聚焦
						if ((temp < min || temp > max)
								&& arg0.toString().replace(".", "").length() > 1) { // 在2位以上开始判断大小
							Toast.makeText(mContext, "输入值超出正常范围!",
									Toast.LENGTH_SHORT).show();
						}
					} else if (temp < min || temp > max) {// 在2位以上开始判断大小
						Toast.makeText(mContext, "输入值超出正常范围!",
								Toast.LENGTH_SHORT).show();
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * 获取历史记录
	 */
	private void fetchDataFromWebservice(int n) {
		if (MWDUtils.isNetworkConnected(mContext)) {
			dia.show();
			ArrayList<NameValuePair> localArrayList = new ArrayList<NameValuePair>();
			localArrayList.add(new BasicNameValuePair("pid", application.pid));
			localArrayList
					.add(new BasicNameValuePair("nums", application.nums));
			localArrayList.add(new BasicNameValuePair("token", Constant.token));

			if (n == 1) {
				this.rThread = new RequestThread(localArrayList, "http",
						"post", Constant.VITALSIGNS_URL, mHandler);
			} else {
				if (TextUtils.isEmpty(edt_tv.getText())
						&& TextUtils.isEmpty(edt_hx.getText())
						&& TextUtils.isEmpty(edt_xl.getText())
						&& TextUtils.isEmpty(edt_xy.getText())
						&& TextUtils.isEmpty(edt_mb.getText())) {
					Toast.makeText(getActivity(), "您还没有填写内容",
							Toast.LENGTH_SHORT).show();
					return;

				} else {
					localArrayList.add(new BasicNameValuePair("temperature",
							edt_tv.getText().toString()));
					localArrayList.add(new BasicNameValuePair("breathing",
							edt_hx.getText().toString()));
					localArrayList.add(new BasicNameValuePair("heartrate",
							edt_xl.getText().toString()));
					localArrayList.add(new BasicNameValuePair("bloodpressure",
							edt_xy.getText().toString()));
					localArrayList.add(new BasicNameValuePair("pulse", edt_mb
							.getText().toString()));
				}
				this.rThread = new RequestThread(localArrayList, "http",
						"post", Constant.VITALSIGNS_EDIT_URL, eHandler);
			}
			this.rThread.start();
		} else {

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
						JSONObject ja = new JSONObject(str);
						edt_tv.setText(ja.has("temperature") ? ja
								.getString("temperature") : "");
						edt_hx.setText(ja.has("breathing") ? ja
								.getString("breathing") : "");
						edt_xl.setText(ja.has("heartrate") ? ja
								.getString("heartrate") : "");
						edt_xy.setText(ja.has("bloodpressure") ? ja
								.getString("bloodpressure") : "");
						edt_mb.setText(ja.has("pulse") ? ja.getString("pulse")
								: "");
						// SignOfLifeListAdapter adapter = new
						// SignOfLifeListAdapter(
						// mContext, ja);
						// lv.setAdapter(adapter);
					}
				} else {

				}

			} catch (JSONException localJSONException) {
				localJSONException.printStackTrace();
			}
		}
	};
	Handler eHandler = new Handler() {
		public void handleMessage(Message msg) {
			dia.dismiss();
			if (null != msg.obj) {
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
				} else {

				}
			} else {

			}
		};
	};

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		dia = new ProgressDialog(getActivity());
		dia.setMessage("请稍候...");
		dia.setCanceledOnTouchOutside(false);
		mContext = getActivity();
		application = ((MWDApplication) mContext.getApplicationContext());
		fetchDataFromWebservice(1);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit:
			fetchDataFromWebservice(2);
			break;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
