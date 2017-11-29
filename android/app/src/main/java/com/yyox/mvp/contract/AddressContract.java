package com.yyox.mvp.contract;

import android.graphics.Bitmap;

import com.google.gson.JsonObject;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.mvp.BaseView;
import com.jess.arms.mvp.IModel;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.mvp.model.entity.Address;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.Region;

import java.util.List;

import rx.Observable;

/**
 * Created by dadaniu on 2017-01-12.
 */

public interface AddressContract {

    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        void setAdapter(DefaultAdapter adapter);
        void startLoadMore();
        void endLoadMore();
        //申请权限
        RxPermissions getRxPermissions();
        void setRegions(int type, List<Region> regionList);
        void deleteTempPicture();
        void refreshAddress();
        void setAddressDetail(Address address);

        void setImage(int i, String data);
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        Observable<BaseJson<List<Address>>> getAddress();
        Observable<BaseJson<List<Region>>> getRegions(int parentId);
        Observable<BaseJson<String>> saveAddress(Address address);
        Observable<BaseJson<String>> deleteAddress(int id);
        Observable<BaseJson<String>> setDefaultAddress(int id);
        Observable<BaseJson<JsonObject>> uploadIdCard(Bitmap idcarda, Bitmap idcardb);
        Observable<BaseJson<Address>> getAddressDetail(int id);
        Observable<BaseJson<String>> getAddressImage(String url);
    }

}
