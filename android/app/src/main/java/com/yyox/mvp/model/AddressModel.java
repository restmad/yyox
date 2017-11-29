package com.yyox.mvp.model;

import android.graphics.Bitmap;

import com.google.gson.JsonObject;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BaseModel;
import com.yyox.Utils.PictureUtil;
import com.yyox.mvp.contract.AddressContract;
import com.yyox.mvp.model.api.cache.CacheManager;
import com.yyox.mvp.model.api.service.ServiceManager;
import com.yyox.mvp.model.entity.Address;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.Region;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-12.
 */
@ActivityScope
public class AddressModel extends BaseModel<ServiceManager, CacheManager> implements AddressContract.Model {

    public static final int ADDRESS_PER_PAGE = 10;

    @Inject
    public AddressModel(ServiceManager serviceManager, CacheManager cacheManager) {
        super(serviceManager, cacheManager);
    }

    @Override
    public Observable<BaseJson<List<Address>>> getAddress() {
        return mServiceManager.getAddressService().getAddress();
    }

    @Override
    public Observable<BaseJson<List<Region>>> getRegions(int parentId) {
        return mServiceManager.getCommonService().getRegions(parentId);
    }

    @Override
    public Observable<BaseJson<String>> saveAddress(Address address) {
        return mServiceManager.getAddressService().saveAddress(address.getName(),address.getCountry(),address.getProvince(),address.getCity(),address.getDistrict(),
                address.getDetailaddress(),address.getZipcode(),address.getMobile(),address.getIsdefault(),address.getIdcard(),address.getId());
    }

    @Override
    public Observable<BaseJson<String>> deleteAddress(int id) {
        return mServiceManager.getAddressService().deleteAddress(id);
    }

    @Override
    public Observable<BaseJson<String>> setDefaultAddress(int id) {
        return mServiceManager.getAddressService().setDefaultAddress(id);
    }

    @Override
    public Observable<BaseJson<JsonObject>> uploadIdCard(Bitmap idcarda, Bitmap idcardb) {
        String token = "ASDDSKKK19990SDDDSS";//用户token
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("TOKEN", token);//ParamKey.TOKEN 自定义参数key常量类，即参数名

        if (idcarda != null) {
            Bitmap smallBitmap = PictureUtil.getCompressBitmap(idcarda);
            if (smallBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                smallBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                RequestBody imageBodya = RequestBody.create(MediaType.parse("image/png"), stream.toByteArray());
                builder.addFormDataPart("cardFront", UUID.randomUUID()+".png", imageBodya);
            }
        }

        if (idcardb != null) {
            Bitmap smallBitmap = PictureUtil.getCompressBitmap(idcardb);
            if (smallBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                smallBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                RequestBody imageBodya = RequestBody.create(MediaType.parse("image/png"), stream.toByteArray());
                builder.addFormDataPart("cardBack", UUID.randomUUID()+".png", imageBodya);
            }
        }

        List<MultipartBody.Part> parts = builder.build().parts();
        return mServiceManager.getAddressService().uploadIdCard(parts);
        /*
        File filea = new File(idcarda);//idcarda 图片地址
        File fileb = new File(idcardb);//idcarda 图片地址
        String token = "ASDDSKKK19990SDDDSS";//用户token
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("TOKEN", token);//ParamKey.TOKEN 自定义参数key常量类，即参数名
        if (!idcarda.isEmpty()){
            String prefixA = filea.getName().substring(filea.getName().lastIndexOf(".")+1);
            RequestBody imageBodya = RequestBody.create(MediaType.parse("image/"+prefixA), filea);
            builder.addFormDataPart("cardFront", filea.getName(), imageBodya);//"cardFront" 后台接收图片流的参数名
        }

        if (!idcardb.isEmpty()){
            String prefixB = fileb.getName().substring(fileb.getName().lastIndexOf(".")+1);
            RequestBody imageBodyb = RequestBody.create(MediaType.parse("image/"+prefixB), fileb);
            builder.addFormDataPart("cardBack", fileb.getName(), imageBodyb);
        }

        List<MultipartBody.Part> parts = builder.build().parts();
        return mServiceManager.getAddressService().uploadIdCard(parts);
        */
    }

    @Override
    public Observable<BaseJson<Address>> getAddressDetail(int id) {
        return mServiceManager.getAddressService().getAddressDetail(id);
    }

    @Override
    public Observable<BaseJson<String>> getAddressImage(String url) {
        return mServiceManager.getCommonService().extractPicture(url);
    }

}
