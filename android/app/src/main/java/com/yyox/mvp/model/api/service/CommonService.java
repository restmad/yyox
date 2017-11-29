package com.yyox.mvp.model.api.service;

import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.MessageItem;
import com.yyox.mvp.model.entity.Region;
import com.yyox.mvp.model.entity.Warehouse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 存放通用的一些API
 * Created by dadaniu on 2017-01-06.
 */

public interface CommonService {

    String HEADER_API_VERSION = "Accept-Encoding: application/json";

    /**
     * 获取手机验证码
     *
     * @param mobile
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("sms/user/authCode")
    Observable<BaseJson<String>> authCode(@Query("mobile") String mobile);

    /**
     * 获取仓库数据
     *
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("warehouse/list")
    Observable<BaseJson<List<Warehouse>>> getWarehouses();

    /**
     * 获取三级地址
     *
     * @param parentId
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("region/regions")
    Observable<BaseJson<List<Region>>> getRegions(@Query("parentId") int parentId);


    /**
     * 获取消息列表
     *
     * @param date
     * @param pageNumber
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("customer/searchInfoHistory")
    Observable<BaseJson<MessageItem>> getMessages(@Query("date") String date, @Query("pageNumber") int pageNumber);


    @Headers({HEADER_API_VERSION})
    @GET("customer/extractPicture")
    Observable<BaseJson<String>> extractPicture(@Query("url") String urls);

}
