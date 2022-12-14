package com.ukang.clinic.patient;

/**
 * MenuActivity 首页底层菜单
 * 景俊钢
 * 2016年6月30日 14:08:20
 */

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.uakng.clinic.announcement.AnnouncementActivity;
import com.ukang.clinic.R;
import com.ukang.clinic.activity.add.AddCaseActivity;
import com.ukang.clinic.application.MWDApplication;
import com.ukang.clinic.common.Constant;
import com.ukang.clinic.login.LoginActivity;
import com.ukang.clinic.systembartint.SystemBarTintManager;
import com.ukang.clinic.thread.XThread;
import com.ukang.clinic.utils.UpdateManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends FragmentActivity implements
		View.OnClickListener {

	protected static final String TAG = "PatientaListAcitivity";
	@ViewInject(R.id.add_bl)
	private Button btn_add_bl;

	@ViewInject(R.id.person_center)
	private LinearLayout _person_center;

	@ViewInject(R.id.pages)
	private TextView mpages;
	// 系统退出
	@ViewInject(R.id.mianui_system_exit)
	LinearLayout mianui_system_exit;

	@ViewInject(R.id.refresh)
	LinearLayout l_refresh;

	MWDApplication application;
	JSONArray mjson_array = null;
	private static SystemBarTintManager tintManager;
	public ProgressDialog dia;
	// 数据填充*****
	ViewPager viewPager;
	JSONArray listInfo; // 总数据
	int page;// viewpager的頁數
	public static int PER_SIZE = 8; // 每页个数
	public static int PER_Columns = 4; // 设置的列值
	Context context = MenuActivity.this;

	@ViewInject(R.id.options)
	ImageView img_options;

	@ViewInject(R.id.tv_p_center)
	TextView tv_p_center;

	@ViewInject(R.id.drawer_l)
	DrawerLayout drawer_l;
	/**
	 * 个人中心
	 */
	@ViewInject(R.id.l1)
	LinearLayout l1;
	/**
	 * 电子病例
	 */
	@ViewInject(R.id.l2)
	RelativeLayout l2;
	/**
	 * 信息公告
	 */
	@ViewInject(R.id.l3)
	RelativeLayout l3;
	/**
	 * 病例搜索
	 */
	@ViewInject(R.id.rl_search)
	RelativeLayout rl_search;

	@ViewInject(R.id.search)
	TextView tv_search;

	@ViewInject(R.id.add_bl1)
	EditText et_addbl;

	@ViewInject(R.id.three_visit)
	TextView tv_three_visit;
	/**
	 * 医生姓名
	 */
	@ViewInject(R.id.doc_name)
	TextView tv_doc_name;

	// 当前页*****
	public int currentpage = 1;
	int Current_index = 0; // 当前选择
	PatientListFragment pl_frg;
	FragmentManager fragmentManager;
	public String str_selection = ""; // 搜索字符（名字、患者ID）

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_patient_list);// layout_patient_list);
		ViewUtils.inject(this);
		init();
		selectItem(0, false);
		apkRequest();
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

	/**
	 * 初始化
	 */
	private void init() {
		tv_doc_name.setText(Constant.nickname.equals("") ? "医生您好"
				: Constant.nickname);
		dia = new ProgressDialog(MenuActivity.this);
		// dia.setMessage("请稍候...");
		dia.setCanceledOnTouchOutside(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.login_top_color);
		this._person_center.setOnClickListener(this);
		this.btn_add_bl.setOnClickListener(this);
		mianui_system_exit.setOnClickListener(this);
		l1.setOnClickListener(this);
		l2.setOnClickListener(this);
		l3.setOnClickListener(this);
		l_refresh.setOnClickListener(this);
		application = (MWDApplication) this.getApplicationContext();
		img_options.setOnClickListener(this);
		l_refresh.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// tv_refresh.setTextColor(Color.GREEN);
					break;
				case MotionEvent.ACTION_UP:
					// tv_refresh.setTextColor(Color.WHITE);
					break;
				default:
					break;
				}
				return false;
			}
		});
		mianui_system_exit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// tv_exit.setTextColor(Color.GREEN);
					break;
				case MotionEvent.ACTION_UP:
					// tv_exit.setTextColor(Color.WHITE);
					break;
				default:
					break;
				}
				return false;
			}
		});
		_person_center.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					tv_p_center.setTextColor(Color.GREEN);
					break;
				case MotionEvent.ACTION_UP:
					tv_p_center.setTextColor(Color.WHITE);
					break;
				default:
					break;
				}
				return false;
			}
		});
		tv_search.setOnClickListener(this);
	}

	public void onClick(View paramView) {
		switch (paramView.getId()) {
		default:
			break;
		case R.id.search:
			str_selection = et_addbl.getText().toString().trim();
			selectItem(0, true);
			break;
		case R.id.add_bl:
			application.is_add_patient = "1";
			startActivity(new Intent(this, AddCaseActivity.class));
			break;
		case R.id.mianui_system_exit:
			startActivity(new Intent(this, LoginActivity.class));
			this.finish();
			break;
		case R.id.refresh:
			selectItem(2, false);
			break;
		case R.id.options:
			if (drawer_l.isDrawerOpen(Gravity.LEFT)) {
				drawer_l.closeDrawer(Gravity.LEFT);
			} else {
				drawer_l.openDrawer(Gravity.LEFT);
			}
			break;
		// 个人中心
		case R.id.l1:
			// startActivity(new Intent(this, PersonCenterActivity.class));
			// drawer_l.closeDrawer(Gravity.LEFT);
			break;
		// 电子病例
		case R.id.l2:
			selectItem(0, false);
			break;
		// 信息公告
		case R.id.l3:
			startActivity(new Intent(this, AnnouncementActivity.class));
			drawer_l.closeDrawer(Gravity.LEFT);
			break;
		}
	}

	/**
	 * 切换主视图区域的Fragment
	 * 
	 * @param position
	 */
	private void selectItem(int position, boolean isrefresh) {
		// TODO Auto-generated method stub
		// Bundle args = new Bundle();
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragments(transaction);
		switch (position) {
		case 0: // 电子病例
			rl_search.setVisibility(View.VISIBLE); // 搜索框显示
			Current_index = 0;
			tv_three_visit.setText("电子病历");
			if (pl_frg != null) {
				if (isrefresh) {
					pl_frg = new PatientListFragment();
					transaction.add(R.id.content, pl_frg);
				} else {
					transaction.show(pl_frg);
				}

			} else {
				pl_frg = new PatientListFragment();
				transaction.add(R.id.content, pl_frg);
			}
			break;
		case 1: // 信息公告
			Toast.makeText(MenuActivity.this, "敬请期待", Toast.LENGTH_SHORT)
					.show();
			rl_search.setVisibility(View.INVISIBLE);
			Current_index = 1;
			tv_three_visit.setText("电子病例");
			if (pl_frg != null) {
				transaction.show(pl_frg);
			} else {
				pl_frg = new PatientListFragment();
				transaction.add(R.id.content, pl_frg);
			}
			break;
		case 2: // 刷新
			if (Current_index == 0) {
				pl_frg = new PatientListFragment();
				transaction.replace(R.id.content, pl_frg);
			} else {
				pl_frg = new PatientListFragment();
				transaction.replace(R.id.content, pl_frg);
			}
			break;
		default:
			break;
		}
		// currentfragment.setArguments(args);
		// FragmentActivity将点击的菜单列表标题传递给Fragment
		transaction.commit();
		drawer_l.closeDrawer(Gravity.LEFT);// 更新选择后的item和title，然后关闭菜单
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (pl_frg != null) {
			transaction.hide(pl_frg);
		}
	}

	/**
	 * 开启线程访问网络得到数据更新UI
	 */
	private void apkRequest() {
		// 请求网络
		RequestParams params = new RequestParams();
		params.addBodyParameter("type", "3");
		params.addBodyParameter("test", "1");
		XThread thread = new XThread(0, params, Constant.APKurl, handler);
		// thread.setShowDia(true);
		thread.start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String result = msg.obj.toString();
			if (result != null && !result.equals("")) {
				try {
					JSONObject json = new JSONObject(result);
					if (json.getInt("vnum") > Constant.CODE) {
						String content = json.has("content") ? json
								.getString("content") : "";
						int size = json.has("size") ? json.getInt("size") : 0;
						new UpdateManager(MenuActivity.this, Constant.APK_URL,
								content, size).checkUpdateInfo();
					}
				} catch (JSONException e) {
				}
			}
		}
	};
}
