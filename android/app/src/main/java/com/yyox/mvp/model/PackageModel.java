package com.yyox.mvp.model;

import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.JsonObject;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BaseModel;
import com.yyox.Utils.PictureUtil;
import com.yyox.mvp.contract.PackageContract;
import com.yyox.mvp.model.api.cache.CacheManager;
import com.yyox.mvp.model.api.service.ServiceManager;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.OrderPackage;
import com.yyox.mvp.model.entity.PackageItem;
import com.yyox.mvp.model.entity.Warehouse;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by dadaniu on 2017-02-27.
 */
@ActivityScope
public class PackageModel extends BaseModel<ServiceManager, CacheManager> implements PackageContract.Model {

    @Inject
    public PackageModel(ServiceManager serviceManager, CacheManager cacheManager) {
        super(serviceManager, cacheManager);
    }

    @Override
    public Observable<BaseJson<List<OrderPackage>>> getPackageDetail(List<Integer> list) {

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("inventorys", list);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        return mServiceManager.getPackageService().getPackageDetail(body);
    }

    @Override
    public Observable<BaseJson<String>> getOrderWaitingEdit(PackageItem packageItem) {

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("id", packageItem.getId());
        jsonParams.put("warehouseId", packageItem.getWarehouseId());
        jsonParams.put("inventoryStatusCode", packageItem.getInventoryStatus().getCode());
        jsonParams.put("carrierNo", packageItem.getCarrierNo());
        jsonParams.put("nickname", packageItem.getNickname());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        return mServiceManager.getPackageService().savePackage(body);
    }

    @Override
    public Observable<BaseJson<String>> deleteOrder(int id) {
        return mServiceManager.getPackageService().deletePackage(id);
    }

    @Override
    public Observable<BaseJson<String>> updatePackageName(int id, String nickname) {
        return mServiceManager.getPackageService().updatePackageName(id, nickname);
    }

    @Override
    public Observable<BaseJson<String>> updateGoods(int id, String nickname, String brandcny, double amount, int unitsNumber) {
        return mServiceManager.getPackageService().updateGoods(id, nickname, brandcny, amount, unitsNumber);
    }

    @Override
    public Observable<BaseJson<String>> deleteGoods(int goodsId, int inventoryId) {
        return mServiceManager.getPackageService().deleteGoods(goodsId, inventoryId);
    }

    @Override
    public Observable<BaseJson<List<PackageItem>>> getOrderWaitings(String pageNo, int warehouseId, int pageNumber, String statusCodeC) {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("pageNo", pageNo);
        jsonParams.put("pageNumber", pageNumber);
        jsonParams.put("warehouseId", warehouseId);
        jsonParams.put("statusCodeC", statusCodeC);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        return mServiceManager.getPackageService().getPackages(body);
    }

    @Override
    public Observable<BaseJson<List<Warehouse>>> getWarehouses() {
        return mServiceManager.getCommonService().getWarehouses();
    }

    @Override
    public Observable<BaseJson<JsonObject>> commitForecast(String carrierNo, int warehouseId, String nickname, int inventoryBasicId) {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("carrierNo", carrierNo);
        jsonParams.put("warehouseId", warehouseId);
        jsonParams.put("nickname", nickname);
        if (inventoryBasicId == 0) {
        } else {
            jsonParams.put("inventoryBasicId", inventoryBasicId);
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        return mServiceManager.getPackageService().commitForecast(body);
    }

    @Override
    public Observable<BaseJson<JsonObject>> putPackageImage(String packageImage1, String packageImage2, String packageImage3) {
        File filea = new File(packageImage1);//idcarda 图片地址
        File fileb = new File(packageImage2);//idcarda 图片地址
        File filec = new File(packageImage3);//idcarda 图片地址
        Log.i("filea22+2", packageImage2);
        String token = "ASDDSKKK19990SDDDSS";//用户token
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("TOKEN", token);//ParamKey.TOKEN 自定义参数key常量类，即参数名

        String prefixA = filea.getName().substring(filea.getName().lastIndexOf(".") + 1);
        String prefixB = fileb.getName().substring(fileb.getName().lastIndexOf(".") + 1);
        String prefixC = filec.getName().substring(filec.getName().lastIndexOf(".") + 1);

        if (!packageImage1.equals("")) {
            RequestBody imageBodya = RequestBody.create(MediaType.parse("image/" + prefixA), filea);
            builder.addFormDataPart("inventoryPic1", filea.getName(), imageBodya);//"cardFront" 后台接收图片流的参数名
        }
        if (!packageImage2.equals("")) {
            RequestBody imageBodyb = RequestBody.create(MediaType.parse("image/" + prefixB), fileb);
            builder.addFormDataPart("inventoryPic2", filea.getName(), imageBodyb);
        }
        if (!packageImage3.equals("")) {
            RequestBody imageBodyc = RequestBody.create(MediaType.parse("image/" + prefixC), filec);
            builder.addFormDataPart("inventoryPic3", filea.getName(), imageBodyc);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        return mServiceManager.getPackageService().putPackageImage(parts);
    }

    @Override
    public Observable<BaseJson<JsonObject>> uploadPackageImage(Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3) {
        String token = "ASDDSKKK19990SDDDSS";//用户token
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("TOKEN", token);//ParamKey.TOKEN 自定义参数key常量类，即参数名

        if (bitmap1 != null) {
            //Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap1,120,120,true);
            Bitmap smallBitmap = PictureUtil.getCompressBitmap(bitmap1);
            if (smallBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                smallBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                RequestBody imageBodya = RequestBody.create(MediaType.parse("image/png"), stream.toByteArray());
                builder.addFormDataPart("inventoryPic1", UUID.randomUUID()+".png", imageBodya);
            }
        }

        if (bitmap2 != null) {
            //Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap2,120,120,true);
            Bitmap smallBitmap = PictureUtil.getCompressBitmap(bitmap2);
            if (smallBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                smallBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                RequestBody imageBodya = RequestBody.create(MediaType.parse("image/png"), stream.toByteArray());
                builder.addFormDataPart("inventoryPic2", UUID.randomUUID()+".png", imageBodya);
            }
        }

        if (bitmap3 != null) {
            //Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap3,120,120,true);
            Bitmap smallBitmap = PictureUtil.getCompressBitmap(bitmap3);
            if (smallBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                smallBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                RequestBody imageBodya = RequestBody.create(MediaType.parse("image/png"), stream.toByteArray());
                builder.addFormDataPart("inventoryPic3", UUID.randomUUID()+".png", imageBodya);
            }
        }

        List<MultipartBody.Part> parts = builder.build().parts();
        return mServiceManager.getPackageService().putPackageImage(parts);
    }

    @Override
    public Observable<BaseJson<String>> extractPicture(String urls) {
        return mServiceManager.getCommonService().extractPicture(urls);
    }

}
