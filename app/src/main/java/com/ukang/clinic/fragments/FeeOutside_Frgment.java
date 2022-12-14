package com.ukang.clinic.fragments;

/**
 * 院外治疗费用
 * jjg
 * 2016年4月21日 16:59:42
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
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
import com.ukang.clinic.main.MainActivity;
import com.ukang.clinic.thread.RequestThread;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeeOutside_Frgment extends Fragment implements OnClickListener {

	private RequestThread rThread;
	public ProgressDialog dia;
	/** 费用 **/
	@ViewInject(R.id.fee)
	private EditText edt_fee;
	@ViewInject(R.id.submit)
	private Button submit;
	View root;
	MWDApplication ma;

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		this.root = paramLayoutInflater.inflate(R.layout.layout_feeouside,
				paramViewGroup, false);
		ViewUtils.inject(this, this.root);
		ma = (MWDApplication) getActivity().getApplication();
		submit.setOnClickListener(this);
		edt_fee.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						edt_fee.setText(s);
						edt_fee.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					edt_fee.setText(s);
					edt_fee.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						edt_fee.setText(s.subSequence(0, 1));
						edt_fee.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		setDataByPost(1);
		((MainActivity)getActivity()).setSubmitVisibily(submit);
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
					Constant.FEE_OUTSIDE_URL, mHandler);
		} else if (i == 2) {
			localArrayList.add(new BasicNameValuePair("eid", ma.pid));
			localArrayList.add(new BasicNameValuePair("val", edt_fee.getText()
					.toString()));
			this.rThread = new RequestThread(localArrayList, "http", "post",
					Constant.FEE_OUTSIDE_EDIT_URL, mEidHandler);
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

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			dia.dismiss();
			String reuslt = msg.obj.toString();
			try {
				JSONObject json = new JSONObject(reuslt);
				JSONObject js = json.getJSONObject("info");
				edt_fee.setText(js.has("val") ? js.getString("val") : "");
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
