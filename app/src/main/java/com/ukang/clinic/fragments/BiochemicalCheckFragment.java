package com.ukang.clinic.fragments;

/**
 * 生化检查
 * jjg 2017年4月20日 17:28:08
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.clinic.R;
import com.ukang.clinic.adapter.TuPianAdapter;
import com.ukang.clinic.application.MWDApplication;
import com.ukang.clinic.common.Constant;
import com.ukang.clinic.entity.ImgInfo;
import com.ukang.clinic.main.MainActivity;
import com.ukang.clinic.service.PicBroadcastRecevicer;
import com.ukang.clinic.thread.XThread;
import com.ukang.clinic.utils.DialogUtil;
import com.ukang.clinic.utils.FileUtils;
import com.ukang.clinic.utils.PhotoActivity;
import com.ukang.clinicaltrial.view.Mdate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BiochemicalCheckFragment extends Fragment implements
		AdapterView.OnItemClickListener {

	View root;
	private XThread rThread;
	public ProgressDialog dia;
	MWDApplication ma;
	/**
	 * 结果
	 */
	@ViewInject(R.id.frg_7_1a)
	private EditText frg_7_1a;
	@ViewInject(R.id.frg_7_1b)
	private EditText frg_7_1b;
	@ViewInject(R.id.frg_7_1c)
	private EditText frg_7_1c;
	@ViewInject(R.id.frg_7_1d)
	private EditText frg_7_1d;
	@ViewInject(R.id.frg_7_1e)
	private EditText frg_7_1e;
	@ViewInject(R.id.frg_7_1f)
	private EditText frg_7_1f;
	@ViewInject(R.id.frg_7_1g)
	private EditText frg_7_1g;
	@ViewInject(R.id.frg_7_1h)
	private EditText frg_7_1h;
	@ViewInject(R.id.frg_7_1i)
	private EditText frg_7_1i;
	/**
	 * 其他单位
	 */
	@ViewInject(R.id.frg_7_2a)
	private EditText frg_7_2a;
	@ViewInject(R.id.frg_7_2b)
	private EditText frg_7_2b;
	@ViewInject(R.id.frg_7_2c)
	private EditText frg_7_2c;
	@ViewInject(R.id.frg_7_2d)
	private EditText frg_7_2d;
	@ViewInject(R.id.frg_7_2e)
	private EditText frg_7_2e;
	@ViewInject(R.id.frg_7_2f)
	private EditText frg_7_2f;
	@ViewInject(R.id.frg_7_2g)
	private EditText frg_7_2g;
	@ViewInject(R.id.frg_7_2h)
	private EditText frg_7_2h;
	@ViewInject(R.id.frg_7_2i)
	private EditText frg_7_2i;
	/*
	 * 异常值
	 */
	/**
	 * 谷丙转氨酶（ALT）
	 */
	@ViewInject(R.id.radioGroup)
	private RadioGroup radioGroup;
	@ViewInject(R.id.frg_7_3a)
	private RadioButton frg_7_3a;
	@ViewInject(R.id.frg_7_3b)
	private RadioButton frg_7_3b;
	@ViewInject(R.id.frg_7_3c)
	private RadioButton frg_7_3c;
	@ViewInject(R.id.frg_7_3d)
	private RadioButton frg_7_3d;
	/**
	 * 谷草转氨酶（AST）
	 */
	@ViewInject(R.id.radioGroup_1)
	private RadioGroup radioGroup_1;
	@ViewInject(R.id.frg_7_4a)
	private RadioButton frg_7_4a;
	@ViewInject(R.id.frg_7_4b)
	private RadioButton frg_7_4b;
	@ViewInject(R.id.frg_7_4c)
	private RadioButton frg_7_4c;
	@ViewInject(R.id.frg_7_4d)
	private RadioButton frg_7_4d;
	/**
	 * 总胆红素（TBIL）
	 */
	@ViewInject(R.id.radioGroup_2)
	private RadioGroup radioGroup_2;
	@ViewInject(R.id.frg_7_5a)
	private RadioButton frg_7_5a;
	@ViewInject(R.id.frg_7_5b)
	private RadioButton frg_7_5b;
	@ViewInject(R.id.frg_7_5c)
	private RadioButton frg_7_5c;
	@ViewInject(R.id.frg_7_5d)
	private RadioButton frg_7_5d;
	/**
	 * 碱性磷酸酶（ALP）
	 */
	@ViewInject(R.id.radioGroup_3)
	private RadioGroup radioGroup_3;
	@ViewInject(R.id.frg_7_6a)
	private RadioButton frg_7_6a;
	@ViewInject(R.id.frg_7_6b)
	private RadioButton frg_7_6b;
	@ViewInject(R.id.frg_7_6c)
	private RadioButton frg_7_6c;
	@ViewInject(R.id.frg_7_6d)
	private RadioButton frg_7_6d;
	/**
	 * γ-谷氨酰转移酶（γ-GT）
	 */
	@ViewInject(R.id.radioGroup_4)
	private RadioGroup radioGroup_4;
	@ViewInject(R.id.frg_7_7a)
	private RadioButton frg_7_7a;
	@ViewInject(R.id.frg_7_7b)
	private RadioButton frg_7_7b;
	@ViewInject(R.id.frg_7_7c)
	private RadioButton frg_7_7c;
	@ViewInject(R.id.frg_7_7d)
	private RadioButton frg_7_7d;
	/**
	 * 尿素氮（BUN）
	 */
	@ViewInject(R.id.radioGroup_5)
	private RadioGroup radioGroup_5;
	@ViewInject(R.id.frg_7_8a)
	private RadioButton frg_7_8a;
	@ViewInject(R.id.frg_7_8b)
	private RadioButton frg_7_8b;
	@ViewInject(R.id.frg_7_8c)
	private RadioButton frg_7_8c;
	@ViewInject(R.id.frg_7_8d)
	private RadioButton frg_7_8d;
	/**
	 * 肌酐（Cr）
	 */
	@ViewInject(R.id.radioGroup_6)
	private RadioGroup radioGroup_6;
	@ViewInject(R.id.frg_7_9a)
	private RadioButton frg_7_9a;
	@ViewInject(R.id.frg_7_9b)
	private RadioButton frg_7_9b;
	@ViewInject(R.id.frg_7_9c)
	private RadioButton frg_7_9c;
	@ViewInject(R.id.frg_7_9d)
	private RadioButton frg_7_9d;
	/**
	 * 血清胱抑素C（CySC）
	 */
	@ViewInject(R.id.radioGroup_7)
	private RadioGroup radioGroup_7;
	@ViewInject(R.id.frg_7_10a)
	private RadioButton frg_7_10a;
	@ViewInject(R.id.frg_7_10b)
	private RadioButton frg_7_10b;
	@ViewInject(R.id.frg_7_10c)
	private RadioButton frg_7_10c;
	@ViewInject(R.id.frg_7_10d)
	private RadioButton frg_7_10d;
	/**
	 * 血糖（GLU）
	 */
	@ViewInject(R.id.radioGroup_8)
	private RadioGroup radioGroup_8;
	@ViewInject(R.id.frg_7_11a)
	private RadioButton frg_7_11a;
	@ViewInject(R.id.frg_7_11b)
	private RadioButton frg_7_11b;
	@ViewInject(R.id.frg_7_11c)
	private RadioButton frg_7_11c;
	@ViewInject(R.id.frg_7_11d)
	private RadioButton frg_7_11d;
	/**
	 * 异常说明
	 */
	@ViewInject(R.id.frg_7_12a)
	private EditText frg_7_12a;
	@ViewInject(R.id.frg_7_12b)
	private EditText frg_7_12b;
	@ViewInject(R.id.frg_7_12c)
	private EditText frg_7_12c;
	@ViewInject(R.id.frg_7_12d)
	private EditText frg_7_12d;
	@ViewInject(R.id.frg_7_12e)
	private EditText frg_7_12e;
	@ViewInject(R.id.frg_7_12f)
	private EditText frg_7_12f;
	@ViewInject(R.id.frg_7_12g)
	private EditText frg_7_12g;
	@ViewInject(R.id.frg_7_12h)
	private EditText frg_7_12h;
	@ViewInject(R.id.frg_7_12i)
	private EditText frg_7_12i;

	@ViewInject(R.id.ctime)
	private Mdate mtime;
	@ViewInject(R.id.submit)
	private Button submit;
	@ViewInject(R.id.add_img_btn)
	private Button add_img_btn;

	@ViewInject(R.id.camera_gridview)
	private GridView camera_gridview;

	private List<ImgInfo> list;
	private List<Bitmap> phoChangeList;
	private Bitmap crop_bitmap;
	private String phone;
	/**
	 * 点击的图片位置
	 */
	int k = 0;
	int j;
	private List<String> photoList = new ArrayList<String>();
	private TuPianAdapter adapter;
	Intent intent;
	PicBroadcastRecevicer PicbCast; // 广播接受-用于接受图片
	List<String> del_picid_list = new ArrayList<String>();
	BitmapUtils bitmapUtils;
	String photo_path = "";

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		this.root = paramLayoutInflater.inflate(
				R.layout.layout_biochemistry_routine, paramViewGroup, false);
		ViewUtils.inject(this, this.root);
		ma = (MWDApplication) getActivity().getApplication();
		initview();
		addClickListener();
		setBroadcastRecevicer();
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateDataByPost(2);
			}
		});
		((MainActivity) getActivity()).setSubmitVisibily(submit);
		dia = new ProgressDialog(getActivity());
		dia.setMessage("请稍候...");
		dia.setCanceledOnTouchOutside(false);
		updateDataByPost(1);
		bitmapUtils = new BitmapUtils(getActivity());
		return this.root;

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(PicbCast);
	}

	/**
	 * 数据请求 1请求，2编辑
	 */
	private void updateDataByPost(int i) {
		dia.show();
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("nums", ma.nums);
		if (i == 1) {
			params.addBodyParameter("id", ma.pid);
			this.rThread = new XThread(getActivity(), 0, params,
					Constant.BIOCHEMICAL_URL, mHandler);
		} else {
			if (TextUtils.isEmpty(mtime.getText().toString().trim())) {
				Toast.makeText(getActivity(), "请点击输入时间", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			params.addBodyParameter("eid", ma.pid);
			params.addBodyParameter("js", getJSON().toString());
			params.addBodyParameter("ctime", mtime.getText().toString());
			if (photoList.size() > 0) {
				File file = null;
				for (int t = 0; t < photoList.size(); t++) {
					file = new File(photoList.get(t));
					params.addBodyParameter("imgs" + (t + 1), file);
				}
				params.addBodyParameter("filenum", photoList.size() + "");
			} else {
				params.addBodyParameter("filenum", "0");
			}
			if (del_picid_list.size() > 0) {
				String str_ids = del_picid_list.toString().replace("[", "")
						.replace("]", "");
				params.addBodyParameter("delimgs", str_ids);
			}
			this.rThread = new XThread(getActivity(), 0, params,
					Constant.BIOCHEMICAL_URL, mHandler_edit);
		}
		this.rThread.start();
	}

	private void initview() {
		camera_gridview.setOnItemClickListener(this);
		list = new ArrayList<ImgInfo>();
		adapter = new TuPianAdapter(getActivity(), list);
		camera_gridview.setAdapter(adapter);
		phoChangeList = new ArrayList<Bitmap>();
		mtime.setText("");
		DialogUtil.toast(frg_7_3c, getActivity());
		DialogUtil.toast(frg_7_4c, getActivity());
		DialogUtil.toast(frg_7_5c, getActivity());
		DialogUtil.toast(frg_7_6c, getActivity());
		DialogUtil.toast(frg_7_7c, getActivity());
		DialogUtil.toast(frg_7_8c, getActivity());
		DialogUtil.toast(frg_7_9c, getActivity());
		DialogUtil.toast(frg_7_10c, getActivity());
		DialogUtil.toast(frg_7_11c, getActivity());
	}

	/**
	 * 设置广播-接受选择或拍照图片
	 */
	private void setBroadcastRecevicer() {
		// TODO Auto-generated method stub
		PicbCast = new PicBroadcastRecevicer(pcHandler);
		IntentFilter filter = new IntentFilter();
		filter.addAction("pc");
		getActivity().registerReceiver(PicbCast, filter);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		k = arg2;

		ImageView img = new ImageView(getActivity());
		ImgInfo imginfo = list.get(k);
		if (imginfo.isNetImg()) {
			bitmapUtils.display(img, imginfo.getImgPath());
		} else {
			img.setImageBitmap(imginfo.getBmp());
		}
		new AlertDialog.Builder(getActivity()).setView(img)
				.setPositiveButton("退出", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}

				})
				.setNegativeButton("删除", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						ImgInfo imginfo = list.get(k);
						if (imginfo.isNetImg()) {
							del_picid_list.add(imginfo.getId());
						}
						list.remove(k);
						adapter.notifyDataSetChanged();
						dialog.dismiss();
					}

				}).show();
	}

	private void addClickListener() {
		add_img_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (list.size() >= 9) {
					Toast.makeText(getActivity(), "最多可以添加9张照片",
							Toast.LENGTH_SHORT).show();
				} else {
					showChoosePicDia();
				}
			}
		});
	}

	void showChoosePicDia() {
		CharSequence[] items = { "相册", "相机" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("选择");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					intent = new Intent(getActivity(), PhotoActivity.class);// 调用android的图库
					// intent.putExtra("size", (Serializable) list);
					getActivity().startActivityForResult(intent,
							Activity.RESULT_FIRST_USER);
					dialog.dismiss();
					break;
				case 1:
					File file = FileUtils.NewFile();
					photo_path = file.getPath();
					intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
					getActivity().startActivityForResult(intent, 2);
					dialog.dismiss();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 相册选择后，图片获取Handler
	 */
	Handler pcHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// list.clear(); // 初始化先清空从相册中选择的所有图片
				phoChangeList.clear();
				if (crop_bitmap == null) {
					photoList = msg.getData().getStringArrayList("data");
					ImgInfo imginfo;
					for (String path : photoList) {
						crop_bitmap = showLitBitmap(path);// PathChangeBitMap.convertToBitmap(path,200,
						// 200);
						if (list.size() < 9) {
							imginfo = new ImgInfo();
							imginfo.setNetImg(false);
							imginfo.setBmp(crop_bitmap);
							if (!list.contains(imginfo)) {
								list.add(imginfo);
							}
							phoChangeList.add(crop_bitmap);
						}
					}
					camera_gridview.setVisibility(View.VISIBLE);
					adapter.notifyDataSetChanged();
					crop_bitmap = null;
				}
				break;
			case 2:
				ImgInfo imginfo = new ImgInfo();
				imginfo.setNetImg(false);
				imginfo.setImgPath(photo_path);
				imginfo.setBmp(showLitBitmap(photo_path));
				list.add(imginfo);
				photoList.add(photo_path);
				camera_gridview.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};

	/**
	 * 显示小图片
	 * 
	 * @param imgUrl
	 * @return
	 */
	private Bitmap showLitBitmap(String imgUrl) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			// 先设置为TRUE不加载到内存中，但可以得到宽和高
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(imgUrl, options); // 此时返回bm为空
			options.inJustDecodeBounds = false;
			// 计算缩放比
			int outW = options.outWidth > options.outHeight ? options.outWidth
					: options.outHeight;
			int be = (int) (outW / (float) (512));
			if (be <= 0)
				be = 1;
			options.inSampleSize = be;
			// 这样就不会内存溢出了
			bitmap = BitmapFactory.decodeFile(imgUrl, options);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
		return bitmap;
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			dia.dismiss();
			try {
				if (null != msg.obj) {
					String reuslt = msg.obj.toString();
					JSONObject localJSONObject = new JSONObject(reuslt);
					if (localJSONObject.getString("status").toString()
							.equals("1")) {
						JSONObject js = localJSONObject.getJSONObject("info");
						setLocalData(js);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};
	Handler mHandler_edit = new Handler() {
		public void handleMessage(Message msg) {
			dia.dismiss();

			if (null != msg.obj) {
				String reuslt = msg.obj.toString();
				try {
					JSONObject localJSONObject = new JSONObject(reuslt);
					// status -3无数据
					if (localJSONObject.getString("status").toString()
							.equals("1")) {
						Toast.makeText(getActivity(), "保存成功",
								Toast.LENGTH_SHORT).show();
						photoList.clear();
						list.clear();
						del_picid_list.clear();
						updateDataByPost(1);
					} else {
						Toast.makeText(getActivity(), "保存失败",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	/**
	 * 显示历史记录
	 * 
	 * @param localJSONObject
	 */
	protected void setLocalData(JSONObject localJSONObject) {
		JSONObject js = null;
		String ctime = "";
		try {
			js = localJSONObject.getJSONObject("lab");
			frg_7_1a.setText(js.getString("1"));
			frg_7_1b.setText(js.getString("2"));
			frg_7_1c.setText(js.getString("3"));
			frg_7_1d.setText(js.getString("4"));
			frg_7_1e.setText(js.getString("5"));
			frg_7_1f.setText(js.getString("6"));
			frg_7_1g.setText(js.getString("7"));
			frg_7_1h.setText(js.getString("8"));
			frg_7_1i.setText(js.getString("9"));

			js = localJSONObject.getJSONObject("oname");
			frg_7_2a.setText(js.getString("1"));
			frg_7_2b.setText(js.getString("2"));
			frg_7_2c.setText(js.getString("3"));
			frg_7_2d.setText(js.getString("4"));
			frg_7_2e.setText(js.getString("5"));
			frg_7_2f.setText(js.getString("6"));
			frg_7_2g.setText(js.getString("7"));
			frg_7_2h.setText(js.getString("8"));
			frg_7_2i.setText(js.getString("9"));

			js = localJSONObject.getJSONObject("abn");
			String temp = "";
			temp = js.getString("1");
			if (!TextUtils.isEmpty(temp)) {
				SetRadioGroupChecked(temp, radioGroup, frg_7_3a, frg_7_3b,
						frg_7_3c, frg_7_3d);
			}
			temp = js.getString("2");
			if (!TextUtils.isEmpty(temp)) {
				SetRadioGroupChecked(temp, radioGroup_1, frg_7_4a, frg_7_4b,
						frg_7_4c, frg_7_4d);
			}
			temp = js.getString("3");
			if (!TextUtils.isEmpty(temp)) {
				SetRadioGroupChecked(temp, radioGroup_2, frg_7_5a, frg_7_5b,
						frg_7_5c, frg_7_5d);
			}
			temp = js.getString("4");
			if (!TextUtils.isEmpty(temp)) {
				SetRadioGroupChecked(temp, radioGroup_3, frg_7_6a, frg_7_6b,
						frg_7_6c, frg_7_6d);
			}
			temp = js.getString("5");
			if (!TextUtils.isEmpty(temp)) {
				SetRadioGroupChecked(temp, radioGroup_4, frg_7_7a, frg_7_7b,
						frg_7_7c, frg_7_7d);
			}
			temp = js.getString("6");
			if (!TextUtils.isEmpty(temp)) {
				SetRadioGroupChecked(temp, radioGroup_5, frg_7_8a, frg_7_8b,
						frg_7_8c, frg_7_8d);
			}
			temp = js.getString("7");
			if (!TextUtils.isEmpty(temp)) {
				SetRadioGroupChecked(temp, radioGroup_6, frg_7_9a, frg_7_9b,
						frg_7_9c, frg_7_9d);
			}
			temp = js.getString("8");
			if (!TextUtils.isEmpty(temp)) {
				SetRadioGroupChecked(temp, radioGroup_7, frg_7_10a, frg_7_10b,
						frg_7_10c, frg_7_10d);
			}
			temp = js.getString("9");
			if (!TextUtils.isEmpty(temp)) {
				SetRadioGroupChecked(temp, radioGroup_8, frg_7_11a, frg_7_11b,
						frg_7_11c, frg_7_11d);
			}
			js = localJSONObject.getJSONObject("abns");
			frg_7_12a.setText(js.getString("1"));
			frg_7_12b.setText(js.getString("2"));
			frg_7_12c.setText(js.getString("3"));
			frg_7_12d.setText(js.getString("4"));
			frg_7_12e.setText(js.getString("5"));
			frg_7_12f.setText(js.getString("6"));
			frg_7_12g.setText(js.getString("7"));
			frg_7_12h.setText(js.getString("8"));
			frg_7_12i.setText(js.getString("9"));

			ctime = localJSONObject.getString("ctime");
			if (!ctime.equals("")) {
				mtime.setText(ctime);
			}
			String str_photo = localJSONObject.getString("photo");
			String[] photo_num = str_photo.split(",");
			if (photo_num != null && photo_num.length > 0) {
				String str_photos = localJSONObject.getString("imgs");
				String[] photos_list = str_photos.split(",");
				list.clear();
				Bitmap bmp = null;
				ImageView view;
				Drawable da;
				ImgInfo imginfo;
				for (int p = 0; p < photos_list.length; p++) {
					imginfo = new ImgInfo();
					imginfo.setImgPath(photos_list[p].replace(
							"192.168.88.122/baiyu", "yd.baiyu.cn"));
					imginfo.setNetImg(true);
					imginfo.setId(photo_num[p]);
					if (imginfo != null) {
						list.add(imginfo);
					}
				}
				if (list.size() > 0) {
					camera_gridview.setVisibility(View.VISIBLE);
					adapter.notifyDataSetChanged();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置Radiogroup
	 * 
	 * @param index
	 * @param g
	 * @param b1
	 * @param b2
	 * @param b3
	 * @param b4
	 */
	private void SetRadioGroupChecked(String index, RadioGroup g,
			RadioButton b1, RadioButton b2, RadioButton b3, RadioButton b4) {
		if (index.equals("1")) {
			g.check(b1.getId());
		} else if (index.equals("2")) {
			g.check(b2.getId());
		} else if (index.equals("3")) {
			g.check(b3.getId());
		} else {
			g.check(b4.getId());
		}
	}

	/**
	 * 获取json数据
	 */
	private JSONObject getJSON() {
		JSONObject Json = null;
		try {
			Json = new JSONObject();
			JSONObject json1 = new JSONObject();
			json1.put("1", frg_7_1a.getText().toString());
			json1.put("2", frg_7_1b.getText().toString());
			json1.put("3", frg_7_1c.getText().toString());
			json1.put("4", frg_7_1d.getText().toString());
			json1.put("5", frg_7_1e.getText().toString());
			json1.put("6", frg_7_1f.getText().toString());
			json1.put("7", frg_7_1g.getText().toString());
			json1.put("8", frg_7_1h.getText().toString());
			json1.put("9", frg_7_1i.getText().toString());
			Json.put("lab", json1);
			JSONObject json2 = new JSONObject();
			json2.put("1", frg_7_2a.getText().toString());
			json2.put("2", frg_7_2b.getText().toString());
			json2.put("3", frg_7_2c.getText().toString());
			json2.put("4", frg_7_2d.getText().toString());
			json2.put("5", frg_7_2e.getText().toString());
			json2.put("6", frg_7_2f.getText().toString());
			json2.put("7", frg_7_2g.getText().toString());
			json2.put("8", frg_7_2h.getText().toString());
			json2.put("9", frg_7_2i.getText().toString());
			Json.put("oname", json2);
			JSONObject json3 = new JSONObject();
			json3.put("1", getRadio(frg_7_3a, frg_7_3b, frg_7_3c, frg_7_3d)
					+ "");
			json3.put("2", getRadio(frg_7_4a, frg_7_4b, frg_7_4c, frg_7_4d)
					+ "");
			json3.put("3", getRadio(frg_7_5a, frg_7_5b, frg_7_5c, frg_7_5d)
					+ "");
			json3.put("4", getRadio(frg_7_6a, frg_7_6b, frg_7_6c, frg_7_6d)
					+ "");
			json3.put("5", getRadio(frg_7_7a, frg_7_7b, frg_7_7c, frg_7_7d)
					+ "");
			json3.put("6", getRadio(frg_7_8a, frg_7_8b, frg_7_8c, frg_7_8d)
					+ "");
			json3.put("7", getRadio(frg_7_9a, frg_7_9b, frg_7_9c, frg_7_9d)
					+ "");
			json3.put("8", getRadio(frg_7_10a, frg_7_10b, frg_7_10c, frg_7_10d)
					+ "");
			json3.put("9", getRadio(frg_7_11a, frg_7_11b, frg_7_11c, frg_7_11d)
					+ "");
			Json.put("abn", json3);
			JSONObject json4 = new JSONObject();
			json4.put("1", frg_7_12a.getText().toString());
			json4.put("2", frg_7_12b.getText().toString());
			json4.put("3", frg_7_12c.getText().toString());
			json4.put("4", frg_7_12d.getText().toString());
			json4.put("5", frg_7_12e.getText().toString());
			json4.put("6", frg_7_12f.getText().toString());
			json4.put("7", frg_7_12g.getText().toString());
			json4.put("8", frg_7_12h.getText().toString());
			json4.put("9", frg_7_12i.getText().toString());
			Json.put("abns", json4);
			return Json;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Json;
	}

	/**
	 * 获取RadioButton选项
	 */
	private int getRadio(RadioButton r1, RadioButton r2, RadioButton r3,
			RadioButton r4) {
		if (r1.isChecked()) {
			return 1;
		} else if (r2.isChecked()) {
			return 2;
		} else if (r3.isChecked()) {
			return 3;
		} else if (r4.isChecked()) {
			return 4;
		}
		return 1;
	}

}
