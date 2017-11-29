package com.yyox.mvp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.ArrayMap;

import com.google.gson.JsonObject;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BaseModel;
import com.yyox.mvp.contract.OrderContract;
import com.yyox.mvp.model.api.cache.CacheManager;
import com.yyox.mvp.model.api.service.ServiceManager;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.Channel;
import com.yyox.mvp.model.entity.Fee;
import com.yyox.mvp.model.entity.Order;
import com.yyox.mvp.model.entity.OrderDetail;
import com.yyox.mvp.model.entity.OrderHistoryItem;
import com.yyox.mvp.model.entity.OrderPending;
import com.yyox.mvp.model.entity.PriceQuery;
import com.yyox.mvp.model.entity.Question;
import com.yyox.mvp.model.entity.QuestionItem;
import com.yyox.mvp.model.entity.Site;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import common.WEApplication;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by dadaniu on 2017-02-03.
 */
@ActivityScope
public class OrderModel extends BaseModel<ServiceManager, CacheManager> implements OrderContract.Model {

    @Inject
    public OrderModel(ServiceManager serviceManager, CacheManager cacheManager) {
        super(serviceManager, cacheManager);
    }

    @Override
    public Observable<BaseJson<List<Order>>> getOrders(String pageNo, String statusCodeC, String showCancleOrder, int pageNumber) {
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("pageNo", pageNo);
        jsonParams.put("pageNumber", pageNumber);
        jsonParams.put("statusStr", statusCodeC);
        jsonParams.put("showCancleOrder", showCancleOrder);
        if(statusCodeC.equals("已完成")){
            SharedPreferences sharedPreferences =  WEApplication.getContext().getSharedPreferences("userEmail", Context.MODE_PRIVATE);
            String email = sharedPreferences.getString("Email", "");
            SharedPreferences sharedPreferencess =  WEApplication.getContext().getSharedPreferences(email, Context.MODE_PRIVATE);
            int number = sharedPreferencess.getInt("number", 0);
            jsonParams.put("checkCount", number);
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        return mServiceManager.getOrderService().getOrders(body);
    }

    @Override
    public Observable<BaseJson<OrderHistoryItem>> orderTracking(String orderNo) {
        return mServiceManager.getOrderService().getOrderHistory(orderNo);
    }

    @Override
    public Observable<BaseJson<OrderDetail>> getOrderDetail(String orderNo) {
        return mServiceManager.getOrderService().getOrderDetail(orderNo);
    }

    @Override
    public Observable<BaseJson<List<OrderPending>>> getOrderPendings(int pageNo, int pageNumber) {
        return mServiceManager.getOrderService().getOrderPendings(pageNo, pageNumber);
    }

    @Override
    public Observable<BaseJson<List<Channel>>> getChannels(int warehouseId, double weight) {
        return mServiceManager.getOrderService().getChannels(warehouseId, weight);
    }

    @Override
    public Observable<BaseJson<Fee>> calculateFee(List<Integer> inventorys, int warehouseId, int numberInventory, double estimatedWeight, String ordertype, String couponCode, int ordertypeId, int leadId, String orderNo) {
        Map<String, Object> orderdetailParams = new ArrayMap<>();
        orderdetailParams.put("estimatedWeight", estimatedWeight);
        orderdetailParams.put("numberInventory", numberInventory);
        orderdetailParams.put("autoConfirm", false);
        orderdetailParams.put("useKD100", false);

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("couponCode", couponCode/*"removeCoupon"*/);
        jsonParams.put("orderdetail", orderdetailParams);
        jsonParams.put("ordertype", ordertype);
        jsonParams.put("warehouseId", warehouseId);
        jsonParams.put("inventorys", inventorys);
        jsonParams.put("ordertypeId", ordertypeId);
        jsonParams.put("valueaddedlist", null);
        jsonParams.put("leadId", leadId);
        jsonParams.put("orderNo", orderNo);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        return mServiceManager.getOrderService().calculateFee(body);
    }

    @Override
    public Observable<BaseJson<JsonObject>> commitOrder(List<Integer> inventoryIds, String orderType, int customerAddressId, double money, double weight, List<String> list, String couponcode, String orderNo) {
        String strValueAddedlist = "";
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    strValueAddedlist += list.get(i) + ',';
                } else {
                    strValueAddedlist += list.get(i);
                }
            }
        }
        return mServiceManager.getOrderService().commitOrder(inventoryIds, orderType, customerAddressId, money, weight, strValueAddedlist, couponcode, orderNo);
    }

    @Override
    public Observable<BaseJson<String>> cancelBox(String orderNo) {
        return mServiceManager.getOrderService().cancelBox(orderNo);
    }

    @Override
    public Observable<BaseJson<String>> getPayOder(String orderId, String comments, double price, String payTypeBackSide) {
        return mServiceManager.getOrderService().getPayOder(orderId, comments, price, payTypeBackSide);
    }


    @Override
    public Observable<BaseJson<String>> payBalance(String orderNo, String payType) {
        return mServiceManager.getOrderService().payBalance(orderNo, payType);
    }

    @Override
    public Observable<BaseJson<JsonObject>> cardCheck(String orderNo) {
        return mServiceManager.getOrderService().cardCheck(orderNo);
    }

    @Override
    public Observable<BaseJson<JsonObject>> payVerifyResult(String orderNo, Map<String, Object> partMap) {
        return mServiceManager.getOrderService().payVerifyResult(orderNo, partMap);
    }

    @Override
    public Observable<List<QuestionItem>> getQuestionItems(String value) {
        List<QuestionItem> questionItems = new ArrayList<QuestionItem>();

        try {
            JSONObject jsonObject = new JSONObject(value);
            JSONArray array = jsonObject.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                JSONArray array1 = object.getJSONArray("list");
                List<Question> questions = new ArrayList<Question>();
                for (int j = 0; j < array1.length(); j++) {
                    JSONObject object1 = array1.getJSONObject(j);
                    Question question = new Question();
                    question.setAnswer(object1.getString("desc"));
                    question.setQuestion(object1.getString("title"));
                    questions.add(question);
                }
                QuestionItem questionItem = new QuestionItem();
                questionItem.setName(object.getString("category"));
                questionItem.setQuestion(questions);
                questionItems.add(questionItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Observable.just(questionItems);
    }

    @Override
    public Observable<BaseJson<List<Site>>> getSites() {
        List<Site> sites = new ArrayList<Site>();

        Site siteObj1 = new Site();
        siteObj1.setId(1);
        siteObj1.setName("RebatesMe");
        siteObj1.setDetail("RebatesMe RebatesMe");
        siteObj1.setLogo("");
        siteObj1.setUrl("http://www.rebatesme.com/");
        siteObj1.setStatus(1);

        Site siteObj2 = new Site();
        siteObj2.setId(1);
        siteObj2.setName("55海淘");
        siteObj2.setDetail("55海淘 55海淘");
        siteObj2.setLogo("");
        siteObj2.setUrl("http://www.55haitao.com/");
        siteObj2.setStatus(1);

        Site siteObj3 = new Site();
        siteObj3.setId(1);
        siteObj3.setName("LetsEbuy");
        siteObj3.setDetail("LetsEbuy LetsEbuy");
        siteObj3.setLogo("");
        siteObj3.setUrl("http://www.letsebuy.com/");
        siteObj3.setStatus(1);

        Site siteObj4 = new Site();
        siteObj4.setId(1);
        siteObj4.setName("什么值得买");
        siteObj4.setDetail("什么值得买 什么值得买");
        siteObj4.setLogo("");
        siteObj4.setUrl("http://www.smzdm.com/");
        siteObj4.setStatus(1);

        Site siteObj5 = new Site();
        siteObj5.setId(1);
        siteObj5.setName("豆瓣东西");
        siteObj5.setDetail("豆瓣东西 豆瓣东西");
        siteObj5.setLogo("");
        siteObj5.setUrl("https://dongxi.douban.com/haitao/");
        siteObj5.setStatus(1);

        Site siteObj6 = new Site();
        siteObj6.setId(1);
        siteObj6.setName("哪里最便宜");
        siteObj6.setDetail("哪里最便宜 哪里最便宜");
        siteObj6.setLogo("");
        siteObj6.setUrl("http://www.nlzpy.com/");
        siteObj6.setStatus(1);

        sites.add(siteObj1);
        sites.add(siteObj2);
        sites.add(siteObj3);
        sites.add(siteObj4);
        sites.add(siteObj5);
        sites.add(siteObj6);

        BaseJson<List<Site>> listBaseJson = new BaseJson<List<Site>>();
        listBaseJson.setStatus(0);
        listBaseJson.setMsgs("msg");
        listBaseJson.setData(sites);

        return Observable.just(listBaseJson);
    }

    @Override
    public Observable<BaseJson<List<PriceQuery>>> PriceQuery() {
        return mServiceManager.getOrderService().PriceQuery();
    }

    @Override
    public Observable<BaseJson<JsonObject>> calculateFeeTool(String orderType, double estimatedWeight, int warehouseId, int ordertypeId, int leadId) {
        Map<String, Object> orderdetailParams = new ArrayMap<>();
        orderdetailParams.put("estimatedWeight", estimatedWeight);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("orderDetail", orderdetailParams);
        jsonParams.put("orderType", orderType);
        jsonParams.put("warehouseId", warehouseId);
        jsonParams.put("ordertypeId", ordertypeId);
        jsonParams.put("leadId", leadId);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
        return mServiceManager.getOrderService().calculateFeeTool(body);
    }

    @Override
    public Observable<BaseJson<String>> extractPicture(String url) {
        return mServiceManager.getCommonService().extractPicture(url);
    }
}
