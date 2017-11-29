package com.yyox.mvp.model.api.service;

        import com.google.gson.JsonObject;
        import com.yyox.mvp.model.entity.BaseJson;
        import com.yyox.mvp.model.entity.OrderPackage;
        import com.yyox.mvp.model.entity.PackageItem;

        import java.util.List;

        import okhttp3.MultipartBody;
        import okhttp3.RequestBody;
        import retrofit2.http.Body;
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
 * Created by dadaniu on 2017-02-27.
 */

public interface PackageService {

    String HEADER_API_VERSION = "Accept-Encoding: application/json";

    /**
     * 待入库列表数据获取(包裹列表)
     * @param body
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @POST("api/inventory/notInfoInventorys")
    Observable<BaseJson<List<PackageItem>>> getPackages(@Body RequestBody body);

    /**
     * 获取包裹详情
     * @param body
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @POST("api/inventory")
    Observable<BaseJson<List<OrderPackage>>> getPackageDetail(@Body RequestBody body);

    /**
     * 编辑包裹数据
     * @param body
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @PUT("api/")
    Observable<BaseJson<String>> savePackage(@Body RequestBody body);

    /**
     * 删除包裹数据
     * @param id
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @DELETE("api/{id}")
    Observable<BaseJson<String>> deletePackage(@Path("id") int id);


    /**
     * 修改包裹昵称
     * @param
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @FormUrlEncoded
    @POST("api/updateNickname")
    Observable<BaseJson<String>> updatePackageName(@Field("id") int id,@Field("nickName") String nickname);


    /**
     * 修改商品
     * @param
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("inventory/toUpdateMerchandise")
    Observable<BaseJson<String>> updateGoods(@Query("id") int id,@Query("productNameCNY") String nickname,@Query("brandCNY") String brandcny,@Query("amount") double amount,@Query("unitsNumber") int unitsNumber);

    /**
     * 删除商品
     * @param
     * @return
     */
    @Headers({HEADER_API_VERSION})
    @GET("inventory/delInventoryMerchandis")
    Observable<BaseJson<String>> deleteGoods(@Query("goodsId") int goodsId,@Query("inventoryId") int inventoryId);

    /**
     * 提交预报
     */
    @Headers({HEADER_API_VERSION})
    @POST("api/oneKeyInventory")
    Observable<BaseJson<JsonObject>> commitForecast(@Body RequestBody body);


    @Headers({HEADER_API_VERSION})
    @Multipart
    @POST("inventory/receipt/upload")
    Observable<BaseJson<JsonObject>> putPackageImage(@Part List< MultipartBody.Part> partList);
}
