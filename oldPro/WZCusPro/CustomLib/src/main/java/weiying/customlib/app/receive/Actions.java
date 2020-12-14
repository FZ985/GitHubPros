package weiying.customlib.app.receive;

/** 广播动作集 */
public interface Actions {
	//签到后更新用户余额
	public static String ACT_SIGN_IN_UPDATE_MONEY = "act_click_sign_in_update_money";
	//更新个人中心邀请码
	public static String ACT_UPDATE_INVITE_CODE = "act_update_invite_code";
	//更新 onTrimMemory
	public static String ACT_ON_TRIMMEMEORY = "act_on_trimmemeory";
}
