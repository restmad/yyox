package com.yyox.mvp.contract;

import com.google.gson.JsonObject;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.mvp.BaseView;
import com.jess.arms.mvp.IModel;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.CouponItem;
import com.yyox.mvp.model.entity.Record;
import com.yyox.mvp.model.entity.User;
import com.yyox.mvp.model.entity.WarehousJson;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by dadaniu on 2017-02-08.
 */

public interface UserContract {

    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        void setAdapter(DefaultAdapter adapter);

        void startLoadMore();

        void endLoadMore();

        //申请权限
        RxPermissions getRxPermissions();

        void UserResult(JsonObject jsonObject);

        void setUserVip(String member, String expiryDate, String balanceCny);

        void aliPayReturn(String data);

        void getCouponItem(CouponItem couponItem);//优惠卷列表

        void datadFew();

        void SetUserInfo(User user);

        void alterPassword();

    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        Observable<BaseJson<String>> authCode(String mobile);

        Observable<BaseJson<JsonObject>> register(String name, String email, String pwd, String code, String invite);

        Observable<BaseJson<JsonObject>> login(String name, String pwd);

        Observable<BaseJson<String>> logout();

        Observable<BaseJson<String>> forget(String phone, String pwd, String code);

        Observable<BaseJson<List<CouponItem>>> getCoupons(String monye, int warehouseRange, int ordertypeRange);

        Observable<BaseJson<JsonObject>> getUserVip();

        Observable<BaseJson<String>> getPayRecharge(String pay_type_comments, double total_fee);

        Observable<BaseJson<JsonObject>> verifyResult(Map<String, Object> partMap);

        Observable<BaseJson<List<Record>>> getRecords(int pageNo, int pageNumber);

        Observable<BaseJson<JsonObject>> alterpassword(String originalPassword, String password, String confirmedPassword);

        Observable<BaseJson<User>> getUserInfo();

        Observable<BaseJson<String>> altercustomer(User user);

        Observable<WarehousJson> getwarehouse();
    }
}
