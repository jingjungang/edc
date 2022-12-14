package com.ukang.clinic.common;

import android.annotation.SuppressLint;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("UseSparseArrays")
public class Constant {

	public static final String VERSION_CODE = "1.2.1";
	public static final int CODE = 121;
	public static boolean ISDEBUG = true;
	public static String UTF = "utf-8";
	public static final String USER_AGENT = "Android;Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.2.2000 Chrome/30.0.1599.101 Safari/537.36";
	public static final String AUTH_ADDR = "https://passport-test.wiwide.com/auth/mobile";

	public static final String APKurl = "http://yd.baiyu.cn/api.php/version/index"; // 更新请求
	public static String APK_URL = "http://yd.baiyu.cn/index.php/apkdown/report/1"; // apk地址
	/** host */
	public static final String HOST_ADDR = "http://yd.baiyu.cn/api.php";
	// public static final String HOST_ADDR =
	// "http://192.168.88.121/baiyu/api.php";
	/** 注册 */
	public static String REGISTER_URL = HOST_ADDR + "/user/register";
	/** 登陆 */
	public static String LOGIN_URL = HOST_ADDR + "/user/newlogin";
	public static final String LOGOUT_URL = HOST_ADDR + "/main/logout";
	public static final String DOCTOR_NEWS_URL = HOST_ADDR + "/pubmed/";
	public static final String DOCTOR_NEWS_DETAIL_URL = HOST_ADDR
			+ "/pubmed/newsshow";
	public static final String UPLOAD_IMG_URL = HOST_ADDR + "/user/avatar";
	public static String sessionId = "";
	public static String token = null;
	public static String GETUSER_URL = HOST_ADDR + "/report/lcpatient/index";
	/** 新建病例 */
	public static String NEWBL_URL = HOST_ADDR + "/pad/lcpatient/add";
	/** 编辑病例 */
	public static String EDIT_BL_URL = HOST_ADDR + "/pad/lcpatient/edit";
	/** 查看基本特征 */
	public static String BASE_FEATURE_URL = HOST_ADDR + "/pad/informed/feature";
	/** 基本特征-编辑 */
	public static String BASE_FEATURE_EDIT_URL = HOST_ADDR
			+ "/pad/informed/featureedit";
	/** 访视首页查看 */
	public static String VISIT_FIRSTPAGE_URL = HOST_ADDR + "/pad/fstype/index";
	/** 访视首页编辑 */
	public static String VISIT_FIRSTPAGE_URL_EDIT = HOST_ADDR
			+ "/pad/fstype/edit";

	/** 一般特征查看 */
	public static String GENARAL_DATA_URL = HOST_ADDR
			+ "/pad/informed/information";
	/**
	 * 一般特征编辑
	 */
	public static String GENARAL_DATA_EDIT_URL = HOST_ADDR
			+ "/pad/informed/informationedit";
	/** 血常规 */
	public static String BLOOD_ROUTINE_URL = HOST_ADDR
			+ "/pad/laboratory/blood";
	/** 便常规 */
	public static String SHIT_URL = HOST_ADDR + "/pad/laboratory/stool";
	/** 合并用药-查询 */
	public static String COMBINATION_DRUG_URL = HOST_ADDR
			+ "/pad/combineddrugs/index";// "/pad/combineddrugs/edit";
	/** 合并用药-无 */
	public static String COMBINATION_DRUG_NODATA_URL = HOST_ADDR
			+ "/pad/combineddrugs/nosave";
	/** 合并用药-编辑 */
	public static String COMBINATION_DRUG_EDIT_URL = HOST_ADDR
			+ "/pad/combineddrugs/addedit";
	/** 生命体征-查询 */
	public static String VITALSIGNS_URL = HOST_ADDR + "/pad/vitalsigns/index";
	/** 生命体征-编辑 */
	public static String VITALSIGNS_EDIT_URL = HOST_ADDR
			+ "/pad/vitalsigns/addupdate";
	/** 头颅mra查看 */
	public static String MRA_URL = HOST_ADDR + "/report/skullimage/index";
	/** 头颅mra新增 */
	public static String MRA_ADD_URL = HOST_ADDR + "/report/skullimage/add";
	public static final String YZM_URL = HOST_ADDR + "/password";
	public static final String YZM_PWD_URL = HOST_ADDR + "/password/updates";
	/**
	 * nihss评分
	 **/
	public static String NIHSS_URL = HOST_ADDR + "/pad/nihss/edit";
	public static String NIHSS_ADD_EDIT = HOST_ADDR + "/pad/nihss/edit";
	/**
	 * 尿常规
	 **/
	public static String URINALYSIS_URL = HOST_ADDR + "/pad/laboratory/urine";
	/** 生化检查 */
	public static String BIOCHEMICAL_URL = HOST_ADDR
			+ "/pad/laboratory/biochemical";
	/** 凝血实验 */
	public static String BLOOD_COAGULATION_URL = HOST_ADDR
			+ "/pad/laboratory/coagulation";
	/** 尿HCG */
	public static String PISS_HCG_URL = HOST_ADDR + "/pad/laboratory//hcg";
	/** 促凝血因子检查 */
	public static String BLOOD_FACTORS_URL = HOST_ADDR
			+ "/pad/laboratory/hepatitis";
	/** 血小板凝聚率 */
	public static String PLATELET_AGGREGATION_URL = HOST_ADDR
			+ "/pad/laboratory/rate";
	/** 12导心电图 */
	public static String Heart_12_AGGREGATION_URL = HOST_ADDR
			+ "/pad/laboratory/electrocardiogram";
	/** 入组审核查看 */
	public static String REVIEW_URL = HOST_ADDR + "/report/review/index";
	/** 入组审核添加 */
	public static String REVIEW_ADD_URL = HOST_ADDR + "/report/review/add";
	/** 治疗成本查看 */
	public static String COST_URL = HOST_ADDR + "/report/cost/index";
	/** 治疗成本添加 */
	public static String COST_ADD_URL = HOST_ADDR + "/report/cost/add";
	/** 病例报告查看 */
	public static String BL_REPORT_URL = HOST_ADDR + "/report/cost/add";
	/** 病例报告编辑 */
	public static String BL_REPORT_ADD_URL = HOST_ADDR + "/report/cost/add";
	/** 不良事件-查询 */
	public static String ADVERSE_EVENT_URL = HOST_ADDR + "/pad/enevt/index";
	/** 不良事件-编辑 */
	public static String ADVERSE_EVENT_EDIT_URL = HOST_ADDR + "/pad/enevt/add";
	/** 不良事件-删除 */
	public static String ADVERSE_EVENT_DEL_URL = HOST_ADDR
			+ "/pad/enevt/delete";
	/** 不良事件-无 */
	public static String ADVERSE_EVENT_NODATA_URL = HOST_ADDR
			+ "/pad/enevt/nosave";
	/** 不良事件-无 */
	public static String ADVERSE_EVENT_NO_URL = HOST_ADDR + "/pad/enevt/nosave";
	/** 严重不良事件-查询 */
	public static String S_ADVERSE_EVENT_URL = HOST_ADDR + "/pad/enevt/indexs";
	/** 严重不良事件-编辑 */
	public static String S_ADVERSE_EVENT_EDIT_URL = HOST_ADDR
			+ "/pad/enevt/adds";
	/** Barthel指数 */
	public static String BARTHEL_URL = HOST_ADDR + "/pad/barthel/edit";
	/** Barthel指数edit */
	public static String BARTHEL_ADD_EDIT = HOST_ADDR + "/pad/barthel/edit";
	/** Rankin评分 */
	public static String RANKIN_URL = HOST_ADDR + "/report/rankin/rankinscroe";
	/** Rankin评分新增or编辑 */
	public static String RANKIN_ADD_EDIT = HOST_ADDR + "/pad/rankin/edit";
	/** 完成情况-查询 */
	public static String PERFORMANCE_URL = HOST_ADDR + "/pad/complete/index";
	/** 完成情况-编辑 */
	public static String PERFORMANCE_EDIT_URL = HOST_ADDR + "/pad/complete/add";
	/** 院外治疗费用 */
	public static String FEE_OUTSIDE_URL = HOST_ADDR + "/pad/treat/edit";
	/** 院外治疗费用-编辑 */
	public static String FEE_OUTSIDE_EDIT_URL = HOST_ADDR + "/pad/treat/edit";
	/** 复发-查询 */
	public static String RECOVER_URL = HOST_ADDR + "/report/rankin/relapse";
	/** 复发-编辑 */
	public static String RECOVER_EDIT_URL = HOST_ADDR
			+ "/report/rankin/relapseadd";
	/** 死亡-查询 */
	public static String DEAD_URL = HOST_ADDR + "/report/rankin/death";
	/** 死亡-编辑 */
	public static String DEAD_EDIT_URL = HOST_ADDR + "/report/rankin/deathadd";
	/** 治疗措施记录-查询 */
	public static String TREATMENTR_ECORDS_URL = HOST_ADDR
			+ "/pad/miscellaneous/index";
	/** 治疗措施记录-编辑 */
	public static String TREATMENTR_ECORDS_EDIT_URL = HOST_ADDR
			+ "/pad/miscellaneous/insertupdate";
	/** 服用药记录-查询 */
	public static String TAKE_MEDICINE_URL = HOST_ADDR
			+ "/report/rankin/takdrugs";
	/** 服用药记录-编辑 */
	public static String TAKE_MEDICINE_EDIT_URL = HOST_ADDR
			+ "/report/rankin/takdrugsupdate";
	/** 用药依从性-查询 */
	public static String OBEY_MEDICINE_URL = HOST_ADDR + "/report/rankin/obey";
	/** 用药依从性-编辑 */
	public static String OBEY_MEDICINE_EDIT_URL = HOST_ADDR
			+ "/report/rankin/obeyupdate";
	/** 出血事件-查询 */
	public static String BLEED_ECORDS_URL = HOST_ADDR + "/report/rankin/bleed";
	/** 出血事件-编辑 */
	public static String BLEED_ECORDS_EDIT_URL = HOST_ADDR
			+ "/report/rankin/bleedadd";
	/** 项目公告-查询 */
	public static String ANNOUNCEMENT_URL = HOST_ADDR
			+ "/pad/exchange/index";
	/** 项目公告-详情+评论 */
	public static String ANNOUNCEMENT_Details_URL = HOST_ADDR
			+ "/pad/exchange/show";
	/** 项目公告-提交评论 */
	public static String ANNOUNCEMENT_ADD_URL = HOST_ADDR
			+ "/pad/exchange/add";
	/** 临床综合疗效评价-查看-编辑 */
	public static String CLINIC_EVAL_URL = HOST_ADDR
			+ "/pad/treat/clinicaledit";
	
	public static com.ukang.clinic.entity.Users users;
	public static String nickname = "";
	public static Boolean firstLoad = false;
	public static Boolean isNewVersion = false;
	public static Boolean pushFlag;
	public static com.ukang.clinic.entity.UserInfo userinfo = null;

	public static String toJson(String username, String password) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("username", username);
			obj.put("password", password);
			obj.put("versions", Constant.VERSION_CODE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj.toString();
	}

}
