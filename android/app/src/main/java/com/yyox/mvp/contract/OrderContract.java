package com.yyox.mvp.contract;

import com.google.gson.JsonObject;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.mvp.BaseView;
import com.jess.arms.mvp.IModel;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.Channel;
import com.yyox.mvp.model.entity.Fee;
import com.yyox.mvp.model.entity.Order;
import com.yyox.mvp.model.entity.OrderDetail;
import com.yyox.mvp.model.entity.OrderHistoryItem;
import com.yyox.mvp.model.entity.OrderPending;
import com.yyox.mvp.model.entity.PriceQuery;
import com.yyox.mvp.model.entity.QuestionItem;
import com.yyox.mvp.model.entity.Site;
import com.yyox.mvp.ui.adapter.OrderPackageDetailAdapter;
import com.yyox.mvp.ui.adapter.PackageExpandableAdapter;
import com.yyox.mvp.ui.adapter.QuestionExpandableAdapter;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by dadaniu on 2017-02-03.
 */

public interface OrderContract {

    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        void setAdapter(DefaultAdapter adapter);

        void setPackageAdapter(PackageExpandableAdapter adapter);

        void setOrderPackageAdapter(OrderPackageDetailAdapter adapter);

        void setQuestionAdapter(QuestionExpandableAdapter adapter);

        void startLoadMore();

        void endLoadMore();

        //申请权限
        RxPermissions getRxPermissions();

        void setUIValue(OrderDetail orderDetailJson);

        void setFeeValue(Fee fee);

        void setPayMoney(int type, double totalAmount, double balanceCny, int payStatus);

        void setOrderInfo(String orderInfo);

        void setPayBalance(int status);

        void setCardCheck(boolean check, int id);

        void dataFew();

        void getData(List<PriceQuery> mPriceQuery);

        void priceFee(String shippingCostStr);

        void getShare(OrderHistoryItem orderHistoryItem);

        void setOnClick();
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        Observable<BaseJson<List<Order>>> getOrders(String pageNo, String statusCodeC, String showCancleOrder, int pageNumber);

        Observable<BaseJson<OrderHistoryItem>> orderTracking(String orderNo);

        Observable<BaseJson<OrderDetail>> getOrderDetail(String orderNo);

        Observable<BaseJson<List<OrderPending>>> getOrderPendings(int pageNo, int pageNumber);

        Observable<BaseJson<List<Channel>>> getChannels(int warehouseId, double weight);

        Observable<BaseJson<Fee>> calculateFee(List<Integer> inventorys, int warehouseId, int numberInventory, double estimatedWeight, String ordertype, String couponCode, int ordertypeId, int leadId, String orderNo);

        Observable<BaseJson<JsonObject>> commitOrder(List<Integer> inventoryIds, String orderType, int customerAddressId, double money, double weight, List<String> list, String couponcode, String orderNo);

        Observable<BaseJson<String>> cancelBox(String orderNo);

        Observable<BaseJson<String>> getPayOder(String orderId, String comments, double price, String payTypeBackSide);

        Observable<BaseJson<String>> payBalance(String orderNo, String payType);

        Observable<BaseJson<JsonObject>> cardCheck(String orderNo);

        Observable<BaseJson<JsonObject>> payVerifyResult(String orderNo, Map<String, Object> partMap);

        Observable<List<QuestionItem>> getQuestionItems(String value);

        Observable<BaseJson<List<Site>>> getSites();

        Observable<BaseJson<List<PriceQuery>>> PriceQuery();

        Observable<BaseJson<JsonObject>> calculateFeeTool(String orderType, double estimatedWeight, int warehouseId, int ordertypeId, int leadId);

        Observable<BaseJson<String>> extractPicture(String s);
    }

}
