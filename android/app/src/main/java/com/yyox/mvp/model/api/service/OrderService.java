package com.yyox.mvp.model.api.service;

import com.google.gson.JsonObject;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.Channel;
import com.yyox.mvp.model.entity.Fee;
import com.yyox.mvp.model.entity.Order;
import com.yyox.mvp.model.entity.OrderCount;
import com.yyox.mvp.model.entity.OrderDetail;
import com.yyox.mvp.model.entity.OrderHistoryItem;
import com.yyox.mvp.model.entity.OrderPending;
import com.yyox.mvp.model.entity.PriceQuery;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-14.
 */

public interface OrderService {

    String HEADER_API_VERSION = "Accept-Encoding: application/json";

    /**
     * 获取各状态订单数量
     *
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("customer/initCustomer")
    Observable<BaseJson<OrderCount>> getOrderCount();

    /**
     * 待处理列表数据获取
     *
     * @param pageNo
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("api/inventory/waitForDispose")
    Observable<BaseJson<List<OrderPending>>> getOrderPendings(@Query("pageNo") int pageNo, @Query("pageNumber") int pageNumber);

    /**
     * 待出库、已出库、清关中、国内配送、已完成列表数据获取
     *
     * @param body
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @POST("customer/orderSearch")
    Observable<BaseJson<List<Order>>> getOrders(@Body RequestBody body);

    /**
     * 获取订单历史数据(追踪信息)
     *
     * @param orderNo
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("track/searchhistory")
    Observable<BaseJson<OrderHistoryItem>> getOrderHistory(@Query("searchArray") String orderNo);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("order/{order}")
    Observable<BaseJson<OrderDetail>> getOrderDetail(@Path("order") String orderNo);

    /**
     * 获取运输服务列表
     *
     * @param
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("order/initChannel")
    Observable<BaseJson<List<Channel>>> getChannels(@Query("warehouseId") int warehouseId, @Query("weight") double weight);

    /**
     * 运费计算
     *
     * @param body
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @POST("order/calculateOrderMoney")
    Observable<BaseJson<Fee>> calculateFee(@Body RequestBody body);

    /**
     * 提交订单
     *
     * @param inventoryIds
     * @param orderType
     * @param customerAddressId
     * @param money
     * @param weight
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @FormUrlEncoded
    @POST("order/commitOrder")
    Observable<BaseJson<JsonObject>> commitOrder(@Field("inventoryIds") List<Integer> inventoryIds, @Field("orderType") String orderType, @Field("customerAddressId") int customerAddressId,
                                                 @Field("money") double money, @Field("weight") double weight, @Field("list") String list, @Field("couponcode") String couponcode, @Field("orderNo") String orderNo);

    /**
     * 取消合箱
     *
     * @param orderNo
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @DELETE("customer/orderDel")
    Observable<BaseJson<String>> cancelBox(@Query("orderNo") String orderNo);


    /**
     * 请求支付宝订单信息
     */
    @Headers({HEADER_API_VERSION})
    @GET("customer/processData")
    Observable<BaseJson<String>> getPayOder(@Query("orderId") String orderId, @Query("pay_type_comments") String comments, @Query("order_price") double price, @Query("payTypeBackSide") String payTypeBackSide);

    /**
     * 同步回调
     */
    @Headers({HEADER_API_VERSION})
    @FormUrlEncoded
    @POST("payment/payReturn")
    Observable<BaseJson<JsonObject>> payVerifyResult(@Field("orderNo") String orderNo, @QueryMap Map<String, Object> partMap);

    /**
     * 余额支付
     *
     * @param orderNo
     * @param payType
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("customer/goPayAppOrder")
    Observable<BaseJson<String>> payBalance(@Query("orderNo") String orderNo, @Query("payType") String payType);

    /**
     * 检查是否需要上传身份证
     *
     * @param orderNo
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("order/checkcard")
    Observable<BaseJson<JsonObject>> cardCheck(@Query("orderNo") String orderNo);

    /**
     * 价格界面获取仓库和运输服务
     */
    @Headers({HEADER_API_VERSION})
    @GET("order/warehouse")
    Observable<BaseJson<List<PriceQuery>>> PriceQuery();

    /**
     * 服务里面运费计算工具
     */
    @Headers({HEADER_API_VERSION})
    @POST("utility/priceCalculation/appcalculation")
    Observable<BaseJson<JsonObject>> calculateFeeTool(@Body RequestBody body);

}
