package com.yyox.mvp.model.api.service;

import com.google.gson.JsonObject;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.CouponItem;
import com.yyox.mvp.model.entity.Record;
import com.yyox.mvp.model.entity.User;
import com.yyox.mvp.model.entity.UserDetail;
import com.yyox.mvp.model.entity.WarehousJson;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-06.
 */

public interface UserService {

    String HEADER_API_VERSION = "Accept-Encoding: application/json";

    @Headers(HEADER_API_VERSION)
    @POST("register")
    Observable<BaseJson<JsonObject>> register(@Body RequestBody body);

    @Headers({HEADER_API_VERSION})
    @FormUrlEncoded
    @POST("login/send")
    Observable<BaseJson<JsonObject>> login(@Field("username") String name, @Field("password") String pwd);

    @Headers(HEADER_API_VERSION)
    @GET("logout")
    Observable<BaseJson<String>> logout();

    @Headers(HEADER_API_VERSION)
    @POST("setpassword")
    Observable<BaseJson<String>> setpassword(@Body RequestBody body);

    @Headers(HEADER_API_VERSION)
    @GET("customer")
    Observable<BaseJson<UserDetail>> getUserDetail();

    @Headers(HEADER_API_VERSION)
    @GET("customer/coupon")
    Observable<BaseJson<List<CouponItem>>> getCoupons(@Query("money") String money,@Query("warehouseRange")int warehouseRange,@Query("ordertypeRange")int ordertypeRange);

    /**
     * 充值详情
     */
    @Headers(HEADER_API_VERSION)
    @GET("customer/payment")
    Observable<BaseJson<JsonObject>> getUserVip();

    /**
     * 充值访问支付宝
     */
    @Headers(HEADER_API_VERSION)
    @GET("customer/processDataForDeposit")
    Observable<BaseJson<String>> getPayRecharge(@Query("pay_type_comments") String pay_type_comments, @Query("total_fee") double total_fee);

    /**
     * 充值同步回调
     */
    @Headers({HEADER_API_VERSION})
    @FormUrlEncoded
    @POST("payment/verifyResult")
    Observable<BaseJson<JsonObject>> alipayCheck(@Field("_input_charset") String _input_charset, @Field("body") String body,
                                                 @Field("currency") String currency, @Field("forex_biz") String forex_biz, @Field("notify_id") String notify_id, @Field("notify_time")
                                                         String notify_time, @Field("notify_type") String notify_type, @Field("notify_url") String notify_url, @Field("out_trade_no") String out_trade_no,
                                                 @Field("partner") String partner, @Field("payment_type") String payment_type, @Field("return_url") String return_url, @Field("rmb_fee") String rmb_fee,
                                                 @Field("seller_id") String seller_id, @Field("service") String service, @Field("sign") String sign, @Field("sign_type") String sign_type, @Field("subject")
                                                         String subject, @Field("success") String success, @Field("trade_no") String trade_no, @Field("trade_status") String trade_status);

    /**
     * 充值同步回调
     */
    @Headers({HEADER_API_VERSION})
    @FormUrlEncoded
    @POST("payment/verifyResult")
    Observable<BaseJson<JsonObject>> verifyResult(@FieldMap Map<String, Object> partMap);

    /**
     * 获取消费记录
     * @param pageNo
     * @param pageNumber
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @FormUrlEncoded
    @POST("customer/statementSearch")
    Observable<BaseJson<List<Record>>> getRecords(@Field("pageNo") int pageNo, @Field("pageNumber") int pageNumber);

    /**
     * 修改密码
     * @param body
     * @return
     */
    @Headers(HEADER_API_VERSION)
    @POST("customer/modifypassword")
    Observable<BaseJson<JsonObject>> alterpassword(@Body RequestBody body);


    /**
     * 获取个人资料
     * @return
     */
    @Headers(HEADER_API_VERSION)
    @GET("customer/detail")
    Observable<BaseJson<User>> getUserInfo();

    /**
     * 修改个人资料
     * @param body
     * @return
     */
    @Headers(HEADER_API_VERSION)
    @POST("customer/detail")
    Observable<BaseJson<String>> altercustomer(@Body RequestBody body);

    /**
     * 获取仓库地址列表
     */
    @Headers(HEADER_API_VERSION)
    @GET("order/warehouseList")
    Observable<WarehousJson> getwarehouse();

}