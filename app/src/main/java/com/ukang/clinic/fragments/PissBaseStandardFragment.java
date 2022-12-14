package com.ukang.clinic.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * @author ZZD 尿常规
 */
public class PissBaseStandardFragment extends Fragment implements
		AdapterView.OnItemClickListener {

	View root;
	public ProgressDialog dia;

	/**
	 * 尿白细胞结果
	 **/
	@ViewInject(R.id.leu_result)
	private EditText leu_result;
	/**
	 * 尿红细胞结果
	 **/
	@ViewInject(R.id.ery_result)
	private EditText ery_result;
	/**
	 * 尿白蛋白结果
	 **/
	@ViewInject(R.id.pro_result)
	private EditText pro_result;
	/**
	 * 尿白细胞其他单位
	 **/
	@ViewInject(R.id.leu_units)
	private EditText leu_units;
	/**
	 * 尿红细胞其他单位
	 **/
	@ViewInject(R.id.ery_units)
	private EditText ery_units;
	/**
	 * 尿白蛋白其他单位
	 **/
	@ViewInject(R.id.pro_units)
	private EditText pro_units;
	/**
	 * 尿白细胞异常说明
	 **/
	@ViewInject(R.id.leu_exception)
	private EditText leu_exception;
	/**
	 * 尿红细胞异常说明
	 **/
	@ViewInject(R.id.ery_exception)
	private EditText ery_exception;
	/**
	 * 尿白蛋白异常说明
	 **/
	@ViewInject(R.id.pro_exception)
	private EditText pro_exception;
	/**
	 * 尿白细胞异常数字
	 **/
	@ViewInject(R.id.r1)
	private RadioButton r1;
	@ViewInject(R.id.r2)
	private RadioButton r2;
	@ViewInject(R.id.r3)
	private RadioButton r3;
	@ViewInject(R.id.r4)
	private RadioButton r4;
	/**
	 * 尿红细胞异常数字
	 **/
	@ViewInject(R.id.r5)
	private RadioButton r5;
	@ViewInject(R.id.r6)
	private RadioButton r6;
	@ViewInject(R.id.r7)
	private RadioButton r7;
	@ViewInject(R.id.r8)
	private RadioButton r8;
	/**
	 * 尿白蛋白异常数字
	 **/
	@ViewInject(R.id.r9)
	private RadioButton r9;
	@ViewInject(R.id.r10)
	private RadioButton r10;
	@ViewInject(R.id.r11)
	private RadioButton r11;
	@ViewInject(R.id.r12)
	private RadioButton r12;

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

	private XThread rThread;
	MWDApplication ma;
	String photo_path = ""; // 拍照动态生成图片地址

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		this.root = paramLayoutInflater.inflate(
				R.layout.layout_urinate_routine, paramViewGroup, false);
		ViewUtils.inject(this, this.root);
		ma = (MWDApplication) getActivity().getApplication();
		initview();
		addClickListener();
		setBroadcastRecevicer();
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setDataByPost(2);
			}
		});
		((MainActivity) getActivity()).setSubmitVisibily(submit);
		setDataByPost(1);
		bitmapUtils = new BitmapUtils(getActivity());
		return this.root;
	}

	/**
	 * 数据请求 1请求，2编辑
	 */
	private void setDataByPost(int i) {
		dia = new ProgressDialog(getActivity());
		dia.setMessage("请稍候...");
		dia.setCanceledOnTouchOutside(false);
		dia.show();
		RequestParams params = new RequestParams();
		params.addHeader("Cookie", Constant.sessionId);
		params.addBodyParameter("token", Constant.token);
		params.addBodyParameter("nums", ma.nums);
		if (i == 1) {
			params.addBodyParameter("id", ma.pid);
			this.rThread = new XThread(getActivity(), 0, params,
					Constant.URINALYSIS_URL, mHandler);
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
					Constant.URINALYSIS_URL, mEidHandler);
		}
		this.rThread.start();
	}

	Handler mEidHandler = new Handler() {
		public void handleMessage(Message msg) {
			dia.dismiss();
			String result = msg.obj.toString();
			try {
				JSONObject json = new JSONObject(result);
				int status = json.getInt("status");
				if (status == 1) {
					Toast.makeText(ma, "提交成功", Toast.LENGTH_SHORT).show();
					photoList.clear();
					list.clear();
					del_picid_list.clear();
					setDataByPost(1);
				} else {
					Toast.makeText(ma, "提交失败", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		;
	};

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			dia.dismiss();
			String str = msg.obj.toString();
			try {
				JSONObject json = new JSONObject(str);
				JSONObject js = json.getJSONObject("info");
				JSONObject onamer = js.getJSONObject("lab");
				JSONObject abn = js.getJSONObject("oname");
				JSONObject lab = js.getJSONObject("abn");
				JSONObject abns = js.getJSONObject("abns");
				String LEUreuslt = onamer.has("1") ? onamer.getString("1") : "";
				leu_result.setText(LEUreuslt);
				String ERYreuslt = onamer.has("2") ? onamer.getString("2") : "";
				ery_result.setText(ERYreuslt);
				String PROreuslt = onamer.has("3") ? onamer.getString("3") : "";
				pro_result.setText(PROreuslt);

				String LEUunits = abn.has("1") ? abn.getString("1") : "";
				leu_units.setText(LEUunits);
				String ERYunits = abn.has("2") ? abn.getString("2") : "";
				ery_units.setText(ERYunits);
				String PROunits = abn.has("3") ? abn.getString("3") : "";
				pro_units.setText(PROunits);

				int LEU = lab.has("1") ? lab.getInt("1") : 0;
				switch (Integer.valueOf(LEU)) {
				case 1:
					r1.setChecked(true);
					break;
				case 2:
					r2.setChecked(true);
					break;
				case 3:
					r3.setChecked(true);
					break;
				case 4:
					r4.setChecked(true);
					break;
				}
				int ERY = lab.has("2") ? lab.getInt("2") : 0;
				switch (ERY) {
				case 1:
					r5.setChecked(true);
					break;
				case 2:
					r6.setChecked(true);
					break;
				case 3:
					r7.setChecked(true);
					break;
				case 4:
					r8.setChecked(true);
					break;
				}
				int PRO = lab.has("3") ? lab.getInt("3") : 0;
				switch (PRO) {
				case 1:
					r9.setChecked(true);
					break;
				case 2:
					r10.setChecked(true);
					break;
				case 3:
					r11.setChecked(true);
					break;
				case 4:
					r12.setChecked(true);
					break;
				}

				String LEUexception = abns.has("1") ? abns.getString("1") : "";
				leu_exception.setText(LEUexception);
				String ERYexception = abns.has("2") ? abns.getString("2") : "";
				ery_exception.setText(ERYexception);
				String PROexception = abns.has("3") ? abns.getString("3") : "";
				pro_exception.setText(PROexception);

				String str_photo = js.getString("photo");
				String[] photo_num = str_photo.split(",");
				if (photo_num != null && photo_num.length > 0) {
					String str_photos = js.getString("imgs");
					String[] photos_list = str_photos.split(",");
					list.clear();
					ImgInfo imginfo;
					for (int p = 0; p < photos_list.length; p++) {
						imginfo = new ImgInfo();
						imginfo.setImgPath(photos_list[p]);
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
			}
		}

		;
	};

	private void initview() {
		camera_gridview.setOnItemClickListener(this);
		list = new ArrayList<ImgInfo>();
		adapter = new TuPianAdapter(getActivity(), list);
		camera_gridview.setAdapter(adapter);
		phoChangeList = new ArrayList<Bitmap>();
		mtime.setText("");
		DialogUtil.toast(r3, getActivity());
		DialogUtil.toast(r3, getActivity());
		DialogUtil.toast(r11, getActivity());
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

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(PicbCast);
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
					break;
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

	private JSONObject getJSON() {
		JSONObject Json = null;
		try {
			Json = new JSONObject();
			JSONObject json1 = new JSONObject();
			json1.put("1", leu_result.getText().toString());
			json1.put("2", ery_result.getText().toString());
			json1.put("3", pro_result.getText().toString());
			Json.put("lab", json1);
			JSONObject json2 = new JSONObject();
			json2.put("1", leu_units.getText().toString());
			json2.put("2", ery_units.getText().toString());
			json2.put("3", pro_units.getText().toString());
			Json.put("oname", json2);
			JSONObject json3 = new JSONObject();
			json3.put("1", getRadio(r1, r2, r3, r4) + "");
			json3.put("2", getRadio(r5, r6, r7, r8) + "");
			json3.put("3", getRadio(r9, r10, r11, r12) + "");
			Json.put("abn", json3);
			JSONObject json4 = new JSONObject();
			json4.put("1", leu_exception.getText().toString());
			json4.put("2", ery_exception.getText().toString());
			json4.put("3", pro_exception.getText().toString());
			Json.put("abns", json4);
			return Json;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Json;
	}

	/** 获取RadioButton选项 */
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
