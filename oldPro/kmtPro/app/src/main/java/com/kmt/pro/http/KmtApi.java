package com.kmt.pro.http;

import com.kmt.pro.bean.AccountIncomeBean;
import com.kmt.pro.bean.AppVersionBean;
import com.kmt.pro.bean.CountryBean;
import com.kmt.pro.bean.DeviceManagerBean;
import com.kmt.pro.bean.HomeBannerBean;
import com.kmt.pro.bean.KeFu;
import com.kmt.pro.bean.OneKeyBuyBean;
import com.kmt.pro.bean.ProductBean;
import com.kmt.pro.bean.SplashBean;
import com.kmt.pro.bean.detail.CommentBean;
import com.kmt.pro.bean.detail.DetailBean;
import com.kmt.pro.bean.mine.AvatorBean;
import com.kmt.pro.bean.mine.LockUpBean;
import com.kmt.pro.bean.mine.MineBean;
import com.kmt.pro.bean.mine.MineItemBean;
import com.kmt.pro.bean.order.ExchangeOrderBean;
import com.kmt.pro.bean.order.OrderDetailbean;
import com.kmt.pro.bean.redpacket.OpenRedBean;
import com.kmt.pro.bean.redpacket.RedDetailListBean;
import com.kmt.pro.bean.redpacket.RedPacketRecordBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-06-29 17:41
 **/
public interface KmtApi {

    //app启动图
    @POST("queryBootBackground.do")
    Observable<CommonResp<SplashBean>> getSplash();

    //首页banner
    @POST("queryBannerList.do")
    Observable<CommonResp<HomeBannerBean>> queryBannerList();

    //首页列表
    @POST("queryStockMarket.do")
    Observable<CommonListResp<ProductBean>> queryStock();

    //查询股票详情接口 时间股票代码
    @POST("publishInformation.do")
    Observable<CommonResp<DetailBean>> queryStorkInformation(@Query("pcode") String code);

    //获取评论列表
    @POST("queryInvestorsEva.do")
    Observable<CommonListResp<CommentBean>> commentList(@Query("investorsCode") String code, @Query("currentPage") String current, @Query("pageSize") String size);

    //获取国家接口
    @POST("queryCountryCode.do")
    Observable<CommonListResp<CountryBean>> getConturyList();

    //查询我的页面的显示的
    @POST("queryTotalWorth.do")
    Observable<CommonResp<MineBean>> requestPerson();

    //锁仓
    @POST("lockup/lockup.do")
    Observable<CommonResp<LockUpBean>> lockUp();

    //我的 条目
    @POST("controlAppComponent.do")
    Observable<CommonListResp<MineItemBean>> getMineItem();

    @POST("rollNotices.do")
    Observable<CommonListResp<String>> rollNotices();

    //修改个人信息 0改变用户名称,22修改用户头像
    @POST("userModifyMessage.do")
    Observable<CommonResp> modifyPersonMessage(@Query("type") String type, @Query("content") String content);

    //图片上传,type=0 代表上传头像,
    @Multipart
    @POST("fileupload.do")
    Observable<CommonResp<AvatorBean>> headImgRequest(@Query("type") String type, @Part MultipartBody.Part file);

    //退出登陆
    @POST("userLogout.do")
    Observable<CommonResp> userLogout();

    //绑定好友
    @POST("bindMaster.do")
    Observable<CommonResp<OneKeyBuyBean>> bindFriend(@Query("higherLevel") String code);

    //修改支付密码 type = 4
    @POST("security/userPayPassword.do")
    Observable<CommonResp> verifyPayPwd(@Query("oldPayPassword") String old, @Query("type") String type);

    //修改支付密码 type = 2
    @POST("security/userPayPassword.do")
    Observable<CommonResp> updatePayPwd(@Query("oldPayPassword") String old, @Query("newPayPassword") String newpwd, @Query("type") String type);

    //设置支付密码 type = 1
    @POST("security/userPayPassword.do")
    Observable<CommonResp> setPayPwd(@Query("userPayPassword") String old, @Query("type") String type);

    //找回支付密码 type = 3
    @POST("security/userPayPassword.do")
    Observable<CommonResp> foundPayPwd(@Query("userPayPassword") String old, @Query("veryCode") String code, @Query("type") String type);

    //找回支付密码的第一步验证
    @POST("checkVerificationCode.do")
    Observable<CommonResp> checkSmsCode(@Query("veryCode") String code);

    //安全中心-设备列表接口
    @POST("queryUserDeviceInfo.do")
    Observable<CommonListResp<DeviceManagerBean>> queryDeviceList();

    //安全中心-设备删除接口
    @POST("delUserDeviceInfo.do")
    Observable<CommonResp> deleteDevice(@Query("infoId") String id);

    //安全中心-设备编辑接口
    @POST("editUserDeviceInfo.do")
    Observable<CommonResp> remarkDevice(@Query("infoId") String id, @Query("deviceName") String note);

    //版本检查
    @POST("userCheckVersion.do")
    Observable<CommonResp<AppVersionBean>> checkVerison(@Query("clientVersion") String verison, @Query("osType") String type, @Query("uuid") String uuid, @Query("channel") String channel);

    //新订单交易记录接口 默认为type=0  查全部，1查行权记录，2.查买卖交易记录，3.充值提现记录
    @POST("orderRecord.do")
    Observable<CommonListResp<AccountIncomeBean>> newOrderRecord(@Query("currentPage") String currentPager, @Query("pageSize") String pageSize, @Query("type") String type);

    //取消提现
    @POST("userWithdrawCancel.do")
    Observable<CommonResp> cancelWithdraw(@Query("withdrawId") String id);

    //点红包查询接口
    @POST("getRedEnvelop.do")
    Observable<CommonResp<OpenRedBean>> openRedRequest(@Query("envelopId") String id);

    //抢红包列表
    @POST("getRedEnvelopWiners.do")
    Observable<CommonListResp<RedDetailListBean>> grapRedListRequest(@Query("envelopId") String id, @Query("pageNo") String no, @Query("pageSize") String size);

    //红包总记录 1,发出的/2，收到的
    @POST("getRedEnvelopAnalyze.do")
    Observable<CommonResp<RedPacketRecordBean>> redRecordRequest(@Query("type") String id);

    //我抢到的红包
    @POST("queryRedEnvelopSantchHis.do")
    Observable<CommonListResp<RedDetailListBean>> myRedGrayRequest(@Query("pageNo") String no, @Query("pageSize") String size);

    //我发出的红包
    @POST("queryRedEnvelopShowHis.do")
    Observable<CommonListResp<RedDetailListBean>> myRedSendRequest(@Query("pageNo") String no, @Query("pageSize") String size);

    //预约入住订单列表
    @POST("queryBookings.do")
    Observable<CommonListResp<ExchangeOrderBean>> queryOrderList(@Query("currentPage") String currentPage, @Query("pageSize") String size);

    //完成订单接口(确认完成)
    @POST("completeBooking.do")
    Observable<CommonResp> completeBooking(@Query("bookingId") String bookingId);

    //0 取消预约入住订单 1删除预约入住订单
    @POST("cancelBooking.do")
    Observable<CommonResp> cancleBook(@Query("bookingId") String id, @Query("type") String type);

    //订单评价
    @FormUrlEncoded
    @POST("investorsEvaluate.do")
    Observable<CommonResp> commitBooking(@Field("bookingId") String id, @Field("investorsCode") String code, @Field("content") String content, @Field("images") String image);

    //客服
    @POST("customerService.do")
    Observable<CommonResp<KeFu>> customerService();

    //获取预约详情
    @POST("queryBookingDetails.do")
    Observable<CommonResp<OrderDetailbean>> queryBookingDetails(@Query("bookingId") String id);
}
