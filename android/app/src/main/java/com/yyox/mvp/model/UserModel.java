package com.yyox.mvp.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.JsonObject;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BaseModel;
import com.yyox.mvp.contract.UserContract;
import com.yyox.mvp.model.api.cache.CacheManager;
import com.yyox.mvp.model.api.service.ServiceManager;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.CouponItem;
import com.yyox.mvp.model.entity.Record;
import com.yyox.mvp.model.entity.User;
import com.yyox.mvp.model.entity.WarehousJson;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by dadaniu on 2017-02-08.
 */
@ActivityScope
public class UserModel extends BaseModel<ServiceManager, CacheManager> implements UserContract.Model {

    @Inject
    public UserModel(ServiceManager serviceManager, CacheManager cacheManager) {
        super(serviceManager, cacheManager);
    }

    @Override
    public Observable<BaseJson<String>> authCode(String mobile) {
        return mServiceManager.getCommonService().authCode(mobile);
    }

    @Override
    public Observable<BaseJson<JsonObject>> register(String name, String email, String pwd, String code, String invite) {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("agreementConfirmed", "true");
        jsonParams.put("mobilePass", "false");
        jsonParams.put("mail", email);
        jsonParams.put("mobile", name);
        jsonParams.put("securityCode", code);
        jsonParams.put("password", pwd);
        jsonParams.put("invitationCode", invite);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        return mServiceManager.getUserService().register(body);
    }

    @Override
    public Observable<BaseJson<JsonObject>> login(String name, String pwd) {
        return mServiceManager.getUserService().login(name, pwd);
    }

    @Override
    public Observable<BaseJson<String>> logout() {
        return mServiceManager.getUserService().logout();
    }

    @Override
    public Observable<BaseJson<String>> forget(String phone, String pwd, String code) {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("mobile", phone);
        jsonParams.put("password", pwd);
        jsonParams.put("securityCode", code);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        return mServiceManager.getUserService().setpassword(body);
    }

    @Override
    public Observable<BaseJson<List<CouponItem>>> getCoupons(String money, int warehouseRange, int ordertypeRange) {
        return mServiceManager.getUserService().getCoupons(money, warehouseRange, ordertypeRange);
    }

    @Override
    public Observable<BaseJson<JsonObject>> getUserVip() {
        return mServiceManager.getUserService().getUserVip();
    }

    @Override
    public Observable<BaseJson<String>> getPayRecharge(String pay_type_comments, double total_fee) {
        return mServiceManager.getUserService().getPayRecharge(pay_type_comments, total_fee);
    }

    @Override
    public Observable<BaseJson<JsonObject>> verifyResult(Map<String, Object> partMap) {
        return mServiceManager.getUserService().verifyResult(partMap);
    }

    @Override
    public Observable<BaseJson<List<Record>>> getRecords(int pageNo, int pageNumber) {
        return mServiceManager.getUserService().getRecords(pageNo, pageNumber);
    }

    @Override
    public Observable<BaseJson<JsonObject>> alterpassword(String originalPassword, String password, String confirmedPassword) {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("originalPassword", originalPassword);
        jsonParams.put("password", password);
        jsonParams.put("confirmedPassword", confirmedPassword);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        return mServiceManager.getUserService().alterpassword(body);
    }

    @Override
    public Observable<BaseJson<User>> getUserInfo() {
        return mServiceManager.getUserService().getUserInfo();
    }

    @Override
    public Observable<BaseJson<String>> altercustomer(User user) {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("chineseName", user.getNickname());
        jsonParams.put("qq", user.getQq());
        jsonParams.put("firstName", user.getFirstName());
        jsonParams.put("lastName", user.getLastName());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        return mServiceManager.getUserService().altercustomer(body);
    }

    @Override
    public Observable<WarehousJson> getwarehouse() {
        return mServiceManager.getUserService().getwarehouse();
    }

}
