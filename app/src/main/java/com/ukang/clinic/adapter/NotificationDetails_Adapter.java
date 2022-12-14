package com.ukang.clinic.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ukang.clinic.R;
import com.ukang.clinic.utils.AsyncLoadingImg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JJG on 2016/7/5.
 * 
 * 项目公告评论列表
 */
public class NotificationDetails_Adapter extends BaseAdapter {

	Context content;
	JSONArray jarray;

	public NotificationDetails_Adapter(Context content, JSONArray jarray) {
		this.content = content;
		this.jarray = jarray;
	}

	public JSONArray getJarray() {
		return jarray;
	}

	public void setJarray(JSONArray jarray) {
		this.jarray = jarray;
	}

	@Override
	public int getCount() {
		if (jarray != null) {
			return jarray.length();
		}
		return 0;
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(final int i, View view, ViewGroup viewGroup) {
		ViewHodler hodler;
		hodler = new ViewHodler();
		view = LayoutInflater.from(content).inflate(
				R.layout.announcement_details_item, null);

		hodler.pic = (ImageView) view.findViewById(R.id.thumb);

		hodler.tv_name = (TextView) view.findViewById(R.id.name);
		hodler.tv_time = (TextView) view.findViewById(R.id.time);
		hodler.tv_content = (TextView) view.findViewById(R.id.content);

		JSONObject jo;
		try {
			jo = jarray.getJSONObject(i);
			hodler.tv_name.setText(jo.getString("username"));
			hodler.tv_time.setText(jo.getString("add_time"));
			hodler.tv_content.setText(jo.getString("content"));
			if (TextUtils.isEmpty(jo.getString("avatar"))) {
//				hodler.pic.setVisibility(View.GONE);
			} else {
				hodler.pic.setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(jo.getString("avatar"))) {
					ImageLoader imageLoader = AsyncLoadingImg
							.getImageLoader(content);
					imageLoader.displayImage(jo.getString("avatar"),
							hodler.pic, AsyncLoadingImg.getDefaultOptions());
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	class ViewHodler {
		ImageView pic;
		TextView tv_name, tv_time, tv_content;
	}
}
