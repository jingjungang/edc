package com.uakng.clinic.announcement;
/**
 * Created by zzd on 2016/7/5
 * 项目公告
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ukang.clinic.R;
import com.ukang.clinic.adapter.Announcement_Adapter;
import com.ukang.clinic.common.Constant;
import com.ukang.clinic.entity.Notice;
import com.ukang.clinic.systembartint.SystemBarTintManager;
import com.ukang.clinic.thread.XThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementActivity extends Activity {

    private PullToRefreshListView mPullToRefreshListView;
    private SystemBarTintManager tintManager;
    private Announcement_Adapter adapter;
    private List<Notice> list;
    int page = 1;
    int spare_page;
    boolean isLoad = true;

    @ViewInject(R.id.img_back)
    private ImageView img_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.announcement_layout);
        ViewUtils.inject(this);
        
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.action_bar);
        
        addBtnLis();
        
        list = new ArrayList<Notice>();
        onLoad();
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
        //设置同时开启上拉加载，下拉刷新
        mPullToRefreshListView.setMode(Mode.BOTH);
        //更改上拉加载的文字内容（默认使用下拉刷新的文字内容）
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pullup_to_load));
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));
        //设置PullToRefreshListView的上拉、下拉监听
        mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                refreshLoad();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                onLoad();
            }
        });
        //得到ListView
        ListView mListView = mPullToRefreshListView.getRefreshableView();
        adapter = new Announcement_Adapter(AnnouncementActivity.this, list);
        mListView.setAdapter(adapter);

    }

    private void addBtnLis() {
        img_back.setOnClickListener(btnLis);
    }

    //刷新数据请求
    private void refreshLoad() {
        isLoad = false;
        list.clear();
        for (int i = 1; i < spare_page; i++) {
            page = i;
            onLoad();
        }
        isLoad = true;
    }

    private void onLoad() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("token", Constant.token);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("pagesize", "10");
        XThread thread = new XThread(AnnouncementActivity.this, 0, params, Constant.ANNOUNCEMENT_URL, mhandler);
        if(isLoad){
            thread.setShowDia(true);
        }
        thread.start();
    }

    Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            String result = msg.obj.toString();
            try {
                JSONObject json = new JSONObject(result);
                switch (json.getInt("status")) {
                    case 1:
                        setJson(json);
                        page++;
                        spare_page = page;
                        //告诉控件，加载完成
                        mPullToRefreshListView.onRefreshComplete();
                        break;
                    case -1:
                        page = spare_page;
                        //告诉控件，加载完成
                        Toast.makeText(AnnouncementActivity.this, "没有了...", Toast.LENGTH_SHORT).show();
                        mPullToRefreshListView.onRefreshComplete();
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };

    private void setJson(JSONObject json) {
        try {
            JSONArray array = json.getJSONArray("info");
            if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    Notice notice = new Notice();
                    JSONObject item = array.getJSONObject(i);
                    notice.setId(item.has("id") ? item.getString("id") : "");
                    notice.setAdd_time(item.has("add_time") ? item.getString("add_time") : "");
                    notice.setUpdate_time(item.has("update_time") ? item.getString("update_time") : "");
                    notice.setType(item.has("type") ? item.getInt("type") : 1);
                    notice.setTitle(item.has("title") ? item.getString("title") : "");
                    notice.setState(item.has("state") ? item.getInt("state") : 1);
                    notice.setThumb(item.has("thumb") ? item.getString("thumb") : "");
                    notice.setUsername(item.has("username") ? item.getString("username") : "");
                    notice.setNickname(item.has("nickname") ? item.getString("nickname") : "");
                    if (notice.getState() == 3) {
                        list.add(0, notice);
                    } else {
                        list.add(notice);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener btnLis = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (img_back == view) {
                finish();
            }
        }
    };
}
