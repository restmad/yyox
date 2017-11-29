package com.yyox.mvp.contract;

import android.graphics.Bitmap;

import com.google.gson.JsonObject;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.mvp.BaseView;
import com.jess.arms.mvp.IModel;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.OrderPackage;
import com.yyox.mvp.model.entity.PackageItem;
import com.yyox.mvp.model.entity.Warehouse;
import com.yyox.mvp.ui.adapter.PackageExpandableAdapter;

import java.util.List;

import rx.Observable;

/**
 * Created by dadaniu on 2017-02-27.
 */

public interface PackageContract {

    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        void setAdapter(DefaultAdapter adapter);

        void setPackageAdapter(PackageExpandableAdapter adapter);

        void startLoadMore();

        void endLoadMore();

        //申请权限
        RxPermissions getRxPermissions();

        void CheckGoodsDeclare(List<OrderPackage> orderPackages);

        void ListWarehouses(List<Warehouse> warehouses);

        void setUIResult(int type);

        void dataFew();

        void packageImage(int status);//订单截图上传成功

        void setImage(int i,String img,int size,String url,int status);
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        Observable<BaseJson<List<PackageItem>>> getOrderWaitings(String pageNo, int warehouseId, int pageNumber, String statusCodeC);

        Observable<BaseJson<List<OrderPackage>>> getPackageDetail(List<Integer> list);

        Observable<BaseJson<String>> getOrderWaitingEdit(PackageItem packageItem);

        Observable<BaseJson<String>> deleteOrder(int id);

        Observable<BaseJson<String>> updatePackageName(int id, String nickname);

        Observable<BaseJson<String>> updateGoods(int id, String nickname, String brandcny, double amount, int unitsNumber);

        Observable<BaseJson<String>> deleteGoods(int goodsId, int inventoryId);

        Observable<BaseJson<List<Warehouse>>> getWarehouses();

        Observable<BaseJson<JsonObject>> commitForecast(String carrierNo, int warehouseId, String nickname, int inventoryBasicId);

        Observable<BaseJson<JsonObject>> putPackageImage(String packageImage1, String packageImage2, String packageImage3);

        Observable<BaseJson<JsonObject>> uploadPackageImage(Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3);

        Observable<BaseJson<String>> extractPicture(String urls);
    }
}
