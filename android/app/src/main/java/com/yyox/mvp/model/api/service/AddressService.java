package com.yyox.mvp.model.api.service;

import com.google.gson.JsonObject;
import com.yyox.mvp.model.entity.Address;
import com.yyox.mvp.model.entity.BaseJson;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-12.
 */

public interface AddressService {

    String HEADER_API_VERSION = "Accept-Encoding: application/json";

    @Headers({HEADER_API_VERSION})
    @GET("customer/address/all")
    Observable<BaseJson<List<Address>>> getAddress();

    @Headers({HEADER_API_VERSION})
    @FormUrlEncoded
    @POST("customer/address")
    Observable<BaseJson<String>> saveAddress(@Field("name") String name, @Field("country") String country, @Field("province") String province, @Field("city") String city, @Field("district") String district, @Field("detailaddress") String detailaddress
            , @Field("zipcode") String zipcode, @Field("mobile") String mobile, @Field("isdefault") boolean isdefault, @Field("idCardNumber") String idCardNumber, @Field("Id") int Id);

    @Headers({HEADER_API_VERSION})
    @DELETE("customer/address")
    Observable<BaseJson<String>> deleteAddress(@Query("id") int id);

    @Headers({HEADER_API_VERSION})
    @PUT("customer/address/setDefault")
    Observable<BaseJson<String>> setDefaultAddress(@Query("id") int id);

    @Headers({HEADER_API_VERSION})
    @Multipart
    @POST("customer/address/uploadIDCard")
    Observable<BaseJson<JsonObject>> uploadIdCard(@Part List<MultipartBody.Part> partList);

    @Headers({HEADER_API_VERSION})
    @GET("customer/address/{id}")
    Observable<BaseJson<Address>> getAddressDetail(@Path("id") int id);

}
