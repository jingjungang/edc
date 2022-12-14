package com.ukang.clinic.activity.add;

/**
 * 新增病例
 * jjg
 * 2016年6月14日 10:35:46
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.clinic.R;
import com.ukang.clinic.application.MWDApplication;
import com.ukang.clinic.common.Constant;
import com.ukang.clinic.common.MWDUtils;
import com.ukang.clinic.db.DBAdapater;
import com.ukang.clinic.systembartint.SystemBarTintManager;
import com.ukang.clinic.thread.RequestThread;
import com.ukang.clinic.utils.AreaSelectedListener;
import com.ukang.clinic.utils.DateUtilities;
import com.ukang.clinic.utils.JsonUtil;
import com.ukang.clinic.utils.Mobile_IDCardUtils;
import com.ukang.clinicaltrial.view.Emial_EditText;
import com.ukang.clinicaltrial.view.Mdate;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//@ViewInject(R.layout.layout_build_case)
public class AddCaseActivity extends Activity implements View.OnClickListener {

	private RequestThread rThread;

	@ViewInject(R.id.img_back)
	private ImageView img_back;
	@ViewInject(R.id.submit)
	private Button btn_submit;
	// ***0 *****************************
	@ViewInject(R.id.card)
	private EditText edt_card;
	@ViewInject(R.id.rname)
	private EditText edt_rname;
	@ViewInject(R.id.sex)
	private RadioGroup rg_sex;
	@ViewInject(R.id.age)
	private EditText edt_age;
	@ViewInject(R.id.birthdate)
	private Mdate m_birthdate;
	@ViewInject(R.id.phone)
	private EditText edt_phone;
	@ViewInject(R.id.email)
	private Emial_EditText edt_email;
	@ViewInject(R.id.weight)
	private EditText edt_weight;
	// ***1 Spinner*****************************
	@ViewInject(R.id.province)
	private Spinner spin_province;
	@ViewInject(R.id.city)
	private Spinner spin_city;
	@ViewInject(R.id.country)
	private Spinner spin_country;
	@ViewInject(R.id.province1)
	private Spinner spin_province1;
	@ViewInject(R.id.city1)
	private Spinner spin_city1;
	@ViewInject(R.id.country1)
	private Spinner spin_country1;
	// ***3 EditText*****************************
	@ViewInject(R.id.street)
	private EditText edt_street;
	@ViewInject(R.id.marriage)
	private RadioGroup rg_marriage;
	@ViewInject(R.id.height)
	private EditText edt_height;
	// 身份证
	@ViewInject(R.id.birthday)
	private EditText edt_birthday;
	// ***4 RadioGroup*****************************
	@ViewInject(R.id.sex)
	private RadioGroup RadioGrp_sex;
	@ViewInject(R.id.r0)
	private RadioButton RadioBtn_mail;
	@ViewInject(R.id.r1)
	private RadioButton RadioBtn_femail;
	@ViewInject(R.id.marriage)
	private RadioGroup RadioGrp_marriage;
	@ViewInject(R.id.r2)
	private RadioButton RadioBtn_marriged;
	@ViewInject(R.id.r3)
	private RadioButton RadioBtn_marriging;
	@ViewInject(R.id.card_id)
	private Spinner spin_card_id;
	// ***5
	String choose_sex = "1"; // 默认1 1男2女
	String choose_marriage = "1"; // 默认1 1已婚2未婚
	HashMap<String, Object> ha;
	String eid = ""; // 编辑的标记 默认为空

	private MWDApplication application;
	static DBAdapater db;
	public ProgressDialog dia;

	// ************************区域********************************
	List<String> list_province = new ArrayList<String>();
	List<String> list_city = new ArrayList<String>();
	List<String> list_country = new ArrayList<String>();
	List<String> list_cardid = new ArrayList<String>();
	AreaSelectedListener listener1, listener2, listener3, listener4, listener5,
			listener6;
	private static SystemBarTintManager tintManager;

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_build_case);
		ViewUtils.inject(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.action_bar);
		application = (MWDApplication) getApplicationContext();
		db = application.db;
		getArea();
		initSpinner();
		init();
		if (null != getIntent().getExtras()) {
			String id = getIntent().getExtras().getString("id").toString();
			getDataByPost(id);
		}
	}

	// 初始化
	private void init() {
		// TODO Auto-generated method stub
		dia = new ProgressDialog(this);
		dia.setMessage("请稍候...");
		dia.setCanceledOnTouchOutside(false);
		img_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		edt_email = (Emial_EditText) findViewById(R.id.email);
		edt_email.initEditText();
		RadioGrp_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int radioButtonId = arg0.getCheckedRadioButtonId(); // 获取变更后的选中项的ID
				RadioButton rb = (RadioButton) findViewById(radioButtonId); // 根据ID获取RadioButton的实例
				choose_sex = rb.getText().equals("男") ? "1" : "2";
			}
		});
		RadioGrp_marriage
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						int radioButtonId = arg0.getCheckedRadioButtonId(); // 获取变更后的选中项的ID
						RadioButton rb = (RadioButton) findViewById(radioButtonId); // 根据ID获取RadioButton的实例
						choose_marriage = rb.getText().equals("已婚") ? "1" : "2";
					}
				});
		list_cardid.add("卡号");
		list_cardid.add("NK");
		list_cardid.add("NA");
		spin_card_id.setOnItemSelectedListener(new CardIDSelectedListener());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				AddCaseActivity.this, R.layout.myspinner, list_cardid);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 设置下拉列表的风格
		spin_card_id.setAdapter(adapter); // 将adapter 添加到spinner中.
		edt_birthday.addTextChangedListener(new TextWatcher() {

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
				// TODO Auto-generated method stub
				if (edt_birthday.length() >= 15) {
					try {
						String temp1 = edt_birthday.getText()
								.subSequence(6, 10).toString()
								+ "-";
						temp1 += edt_birthday.getText().subSequence(10, 12)
								+ "-";
						temp1 += edt_birthday.getText().subSequence(12, 14);
						m_birthdate.setText(temp1);
						edt_age.setText(DateUtilities.GetAge(temp1));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		m_birthdate.dateTime.addTextChangedListener(new TextWatcher() {

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
				// TODO Auto-generated method stub
				edt_age.setText(DateUtilities.GetAge(m_birthdate.getText()
						.toString()));
			}
		});
	}

	class CardIDSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int po,
				long arg3) {
			// TODO Auto-generated method stub
			switch (po) {
			case 0:
				edt_card.setVisibility(View.VISIBLE);
				break;
			case 1:
				edt_card.setVisibility(View.INVISIBLE);
				break;
			case 2:
				edt_card.setVisibility(View.INVISIBLE);
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	// 获取区域
	private void getArea() {
		// TODO Auto-generated method stub
		try {
			String sql1 = "select area_name from by_area WHERE level =1";
			String sql2 = "select area_name from by_area WHERE level =2";
			String sql3 = "select area_name from by_area WHERE level =3";
			db = application.db;
			db.open();
			Cursor cursor = db.rawQuery(sql1, null);
			String name = "";
			while (cursor.moveToNext()) {
				name = cursor.getString(cursor.getColumnIndex("area_name"));
				list_province.add(name);
			}
			cursor.close();
			Cursor cursor1 = db.rawQuery(sql2, null);
			while (cursor1.moveToNext()) {
				name = cursor1.getString(cursor1.getColumnIndex("area_name"));
				list_city.add(name);
			}
			cursor1.close();
			Cursor cursor2 = db.rawQuery(sql3, null);
			while (cursor2.moveToNext()) {
				name = cursor2.getString(cursor2.getColumnIndex("area_name"));
				list_country.add(name);
			}
			cursor2.close();
			db.close();
		} catch (Exception e) {
			Toast.makeText(AddCaseActivity.this, "数据库打开失败，请稍等重试",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	// 初始化区域
	private void initSpinner() {
		// 设置数据
		listener1 = new AreaSelectedListener(AddCaseActivity.this, db,
				spin_province, spin_city, spin_country, 1);
		listener2 = new AreaSelectedListener(AddCaseActivity.this, db,
				spin_city, spin_country, null, 2);
		listener3 = new AreaSelectedListener(AddCaseActivity.this, db,
				spin_country, null, null, 3);
		listener4 = new AreaSelectedListener(AddCaseActivity.this, db,
				spin_province1, spin_city1, spin_country1, 1);
		listener5 = new AreaSelectedListener(AddCaseActivity.this, db,
				spin_city1, spin_country1, null, 2);
		listener6 = new AreaSelectedListener(AddCaseActivity.this, db,
				spin_country1, null, null, 3);

		setSpinnerData(spin_province, list_province, listener1); // 省
		setSpinnerData(spin_city, list_city, listener2); // 市
		setSpinnerData(spin_country, list_country, listener3); // 县
		setSpinnerData(spin_province1, list_province, listener4);
		setSpinnerData(spin_city1, list_city, listener5);
		setSpinnerData(spin_country1, list_country, listener6);
	}

	public void onClick(View paramView) {
		switch (paramView.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.submit:
			ha = new HashMap<String, Object>();
			String temp = "0";
			if (spin_card_id.getSelectedItemPosition() == 1) {
				temp = "NK";
				ha.put("card", "");
			} else if (spin_card_id.getSelectedItemPosition() == 2) {
				temp = "NA";
				ha.put("card", "");
			} else {
				ha.put("card", edt_card.getText().toString());
			}
			ha.put("rname", edt_rname.getText().toString());
			ha.put("sex", choose_sex);
			ha.put("age", edt_age.getText().toString());
			ha.put("birthdate", m_birthdate.getText().toString());
			ha.put("phone", edt_phone.getText().toString());
			ha.put("email", edt_email.getText().toString());
			String ed_street = edt_street.getText().toString();
			ha.put("street", ed_street);
			if (!TextUtils.isEmpty(edt_birthday.getText())) {
				Boolean bl = Mobile_IDCardUtils.isIDCard(edt_birthday.getText()
						.toString());
				if (!bl) {
					Toast.makeText(AddCaseActivity.this, "身份证格式错误",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
			ha.put("birthday", edt_birthday.getText().toString());
			ha.put("marriage", choose_marriage);// rg_marriage
			ha.put("height", edt_height.getText().toString());
			ha.put("weight", edt_weight.getText().toString());
			ha.put("area",
					listener4.getSelectedID() + "," + listener5.getSelectedID()
							+ "," + listener6.getSelectedID());
			if (!eid.equals("")) { // 不是"",则是编辑
				ha.put("id", eid);
			}
			ha.put("address",
					listener1.getSelectedID() + "," + listener2.getSelectedID()
							+ "," + listener3.getSelectedID());
			String js = JsonUtil.map2Json(ha);
			setDataByPost(js, temp);
			break;
		}
	}

	// 写病例请求
	private void setDataByPost(String js, String newcare) {
		if (TextUtils.isEmpty(edt_rname.getText().toString())) {
			Toast.makeText(AddCaseActivity.this, "请填写姓名", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (!Mobile_IDCardUtils.isMobileNO(edt_phone.getText().toString())) { // 手机号验证
			Toast.makeText(AddCaseActivity.this, "手机号格式错误", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		dia.show();
		if (MWDUtils.isNetworkConnected(this)) {
			ArrayList<NameValuePair> localArrayList = new ArrayList<NameValuePair>();
			localArrayList.add(new BasicNameValuePair("newcare", newcare));
			localArrayList.add(new BasicNameValuePair("js", js));
			if (!eid.equals("")) { // 不是"",则是编辑
				localArrayList.add(new BasicNameValuePair("eid", eid));
			}
			localArrayList.add(new BasicNameValuePair("token", Constant.token));
			String url = "";
			if (eid.equals("")) {
				url = Constant.NEWBL_URL;
			} else {
				url = Constant.EDIT_BL_URL;
			}
			this.rThread = new RequestThread(localArrayList, "http", "post",
					url, mHandler_add);
			this.rThread.start();
		}
	}

	// 查病历请求
	private void getDataByPost(String id) {
		eid = id;
		if (MWDUtils.isNetworkConnected(this)) {
			dia.show();
			ArrayList<NameValuePair> localArrayList = new ArrayList<NameValuePair>();
			localArrayList.add(new BasicNameValuePair("id", id));
			localArrayList.add(new BasicNameValuePair("token", Constant.token));
			this.rThread = new RequestThread(localArrayList, "http", "post",
					Constant.EDIT_BL_URL, mHandler_fetch);
			this.rThread.start();
		}
	}

	// Handler处理-新增
	Handler mHandler_add = new Handler() {

		public void handleMessage(Message paramAnonymousMessage) {
			dia.dismiss();
			try {
				String str = paramAnonymousMessage.obj.toString();
				JSONObject localJSONObject = new JSONObject(str);
				String status = localJSONObject.getString("status");
				if (status.equals("1")) {
					Toast.makeText(AddCaseActivity.this, "保存成功",
							Toast.LENGTH_SHORT).show();
					AddCaseActivity.this.finish();
				} else {
					Toast.makeText(AddCaseActivity.this, "保存失败",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException localJSONException) {
				localJSONException.printStackTrace();
				Toast.makeText(AddCaseActivity.this, "保存错误", Toast.LENGTH_SHORT)
						.show();
			}

		}
	};
	// Handler处理-编辑
	Handler mHandler_fetch = new Handler() {

		public void handleMessage(Message paramAnonymousMessage) {
			dia.dismiss();
			try {
				String str = paramAnonymousMessage.obj.toString();
				JSONObject localJSONObject = new JSONObject(str);
				String status = localJSONObject.getString("status");
				if (status.equals("1")) {
					localJSONObject = localJSONObject.getJSONObject("info");
					setData(localJSONObject);
				} else {
					Toast.makeText(AddCaseActivity.this, "查询失败",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException localJSONException) {
				localJSONException.printStackTrace();
				Toast.makeText(AddCaseActivity.this, "查询错误", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	// 页面写获取到的数据
	protected void setData(JSONObject jo) {
		// TODO Auto-generated method stub
		try {
			String temp2 = jo.getString("card");
			if (!temp2.equals("NK") && !temp2.equals("NA")) {
				spin_card_id.setSelection(0);
				temp2 = jo.getString("card");
				edt_card.setText(temp2);
			} else if (temp2.equals("NK")) {
				spin_card_id.setSelection(1);
			} else if (temp2.equals("NA")) {
				spin_card_id.setSelection(2);
			}
			edt_rname.setText(jo.getString("rname").toString());

			edt_age.setText(jo.getString("age").toString().equals("0") ? ""
					: jo.getString("age").toString());
			m_birthdate.setText(jo.getString("birthdate").toString()
					.equals("0") ? "" : DateUtilities.getStrTime((jo
					.getString("birthdate").toString())));
			edt_phone.setText(jo.getString("phone").toString());
			edt_email.setText(jo.getString("email").toString());
			String temp = jo.getString("address").toString();
			final String[] gpos = temp.split(",");
			try {
				setSpinnerItemSelectedByValue(spin_province, gpos[0]);
				setSpinnerItemSelectedByValue(spin_city, gpos[1]);
				setSpinnerItemSelectedByValue(spin_country, gpos[2]);

			} catch (Exception e) {
				e.printStackTrace();
			}
			String street = jo.has("street") ? jo.getString("street") : "";
			edt_street.setText(street.equals("null") ? "" : street);
			edt_height
					.setText(jo.getString("height").toString().equals("0") ? ""
							: jo.getString("height"));
			edt_weight
					.setText(jo.getString("weight").toString().equals("0") ? ""
							: jo.getString("weight"));
			String temp1 = jo.getString("area").toString();
			String[] gpos1 = temp1.split(",");
			try {
				setSpinnerItemSelectedByValue(spin_province1, gpos1[0]);
				setSpinnerItemSelectedByValue(spin_city1, gpos1[1]);
				setSpinnerItemSelectedByValue(spin_country1, gpos1[2]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			edt_birthday.setText(jo.getString("birthday").toString());
			String sex = jo.getString("sex").toString(); // 性别
			String marriage = jo.getString("marriage").toString();// 婚姻
			if (sex.equals("1")) { // 1男2女
				RadioBtn_mail.setChecked(true);
				RadioBtn_femail.setChecked(false);
			} else {
				RadioBtn_mail.setChecked(false);
				RadioBtn_femail.setChecked(true);
			}
			if (marriage.equals("1")) { // 1已婚2未婚
				RadioBtn_marriged.setChecked(true);
				RadioBtn_marriging.setChecked(false);
			} else {
				RadioBtn_marriged.setChecked(false);
				RadioBtn_marriging.setChecked(true);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dia.dismiss();
	}

	/**
	 * 根据string设置spinner
	 */
	public void setSpinnerItemSelectedByValue(Spinner spinner, String getedId) {
		String sql = "select area_name from by_area WHERE area_id =" + getedId;
		String area_name = "";
		db.open();
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToNext();
		area_name = cursor.getString(cursor.getColumnIndex("area_name"));
		cursor.close();
		db.close();
		SpinnerAdapter Adapter = spinner.getAdapter(); // 得到SpinnerAdapter对象
		int k = Adapter.getCount();
		for (int i = 0; i < k; i++) {
			if (area_name.equals(Adapter.getItem(i).toString())) {
				spinner.setSelection(i);// 默认选中项
				break;
			}
		}
	}

	/**
	 * 设置spinner内容
	 * 
	 * @param spin
	 * @param list
	 * @param listener
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 */
	public void setSpinnerData(Spinner spin, List<String> list,
			AreaSelectedListener listener) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				AddCaseActivity.this, R.layout.myspinner, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 设置下拉列表的风格
		spin.setAdapter(adapter); // 将adapter 添加到spinner中
		spin.setOnItemSelectedListener(listener); // 设置监听
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
}
