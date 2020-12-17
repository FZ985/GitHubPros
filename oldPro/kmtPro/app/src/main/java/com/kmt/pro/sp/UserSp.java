package com.kmt.pro.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.kmt.pro.base.BaseApp;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.utils.SystemUtils;
import com.kmtlibs.app.base.BaseSharedPreferences;

/**
 * Create by JFZ
 * date: 2020-06-30 15:45
 **/
public class UserSp extends BaseSharedPreferences {

    private UserSp() {
    }

    public static synchronized UserSp get() {
        return UserSpHolder.INSTANCE;
    }

    private static class UserSpHolder {
        private static volatile UserSp INSTANCE = new UserSp();
    }

    private final String mobile = "user_mobile";
    private final String password = "user_password";
    private final String codepassword = "user_code_password";
    private final String userId = "user_id";
    private final String headImg = "user_headImg";
    private final String nickName = "user_nickname";
    private final String hxId = "user_hxId";
    private final String brokerNo = "user_brokerNo";
    private final String u2hour = "user_2hour";
    private final String inviteCode = "user_invite_code";

    private String uuid = "Android";//设备ID

    public UserSp setMobile(String val) {
        putString(mobile, val);
        return this;
    }

    public String getMobile() {
        return getString(mobile, "");
    }

    public UserSp setPassword(String val) {
        putString(password, val);
        return this;
    }

    public String getPassword() {
        return getString(password, "");
    }

    public UserSp setCodePassword(String val) {
        putString(codepassword, val);
        return this;
    }

    public String getCodePassword() {
        return getString(codepassword, "");
    }

    public UserSp setUuid(String val) {
        this.uuid = val;
        return this;
    }

    public String getUuid() {
        if (TextUtils.isEmpty(uuid) || (uuid + "").equals("Android")) {
            uuid = SystemUtils.getUUID(BaseApp.getInstance());
        }
        return uuid;
    }

    public String getUserId() {
        return getString(userId, "");
    }

    public UserSp setUserId(String val) {
        putString(userId, val);
        return this;
    }

    public String getHeadImg() {
        return getString(headImg, "");
    }

    public UserSp setHeadImg(String val) {
        putString(headImg, val);
        return this;
    }

    public String getNickName() {
        return getString(nickName, "");
    }

    public UserSp setNickName(String val) {
        putString(nickName, val);
        return this;
    }

    public String getInviteCode() {
        String code = getString(inviteCode, "");
        if (code.trim().equals("kmtadmin"))
            return "";
        else return code;
    }

    public UserSp setInviteCode(String val) {
        if (!val.trim().equals("kmtadmin")) {
            putString(inviteCode, val);
        }
        return this;
    }

    public String getHxId() {
        return getString(hxId, "");
    }

    public UserSp setHxId(String val) {
        putString(hxId, val);
        return this;
    }

    public String getBrokerNo() {
        return getString(brokerNo, "");
    }

    public UserSp setBrokerNo(String val) {
        putString(brokerNo, val);
        return this;
    }

    public int get2hour() {
        return getInt(u2hour, 0);
    }

    public UserSp set2hour(int val) {
        putInt(u2hour, val);
        return this;
    }

    @Override
    public void clear() {
        setPassword("");
        setCodePassword("");
        setMobile("");
        setUserId("");
        setHxId("");
        setBrokerNo("");
        set2hour(0);
        super.clear();
    }

    @Override
    public SharedPreferences getSharedPreference() {
        return BaseApp.getInstance().getSharedPreferences(KConstant.SP.spdata_kmtUser, Context.MODE_PRIVATE);
    }
}
