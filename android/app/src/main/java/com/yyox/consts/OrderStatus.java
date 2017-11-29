package com.yyox.consts;

/**
 * Created by dadaniu on 2017-01-17.
 */

public class OrderStatus {

    public static final int ORDER_STATUS_REPORT = 0;//预报
    public static final int ORDER_STATUS_WAITING_IN_WAREHOUSE = 1;//待入库
    public static final int ORDER_STATUS_PACKAGE_EDIT = 2;//编辑包裹
    public static final int ORDER_STATUS_WAITING_DEAL_WITH = 3;//待处理
    public static final int ORDER_STATUS_BOX_PACKAGE = 4;//合箱
    public static final int ORDER_STATUS_PACKAGES = 5;//包裹详情
    public static final int ORDER_STATUS_CHANNEL = 6;//渠道
    public static final int ORDER_STATUS_PACKAGE_SUBMIT = 7;//包裹-提交订单
    public static final int ORDER_STATUS_PACKAGES_SUBMIT = 8;//合箱-提交订单
    public static final int ORDER_STATUS_ORDER_SUBMIT = 9;//订单-提交订单
    public static final int ORDER_STATUS_PACKAGES_EDIT = 10;//编辑包裹详情
    public static final int ORDER_STATUS_WAITING_OUT_WAREHOUSE = 11;//待出库
    public static final int ORDER_STATUS_OUT_WAREHOUSE = 12;//已出库
    public static final int ORDER_STATUS_CUSTOMS = 13;//清关中
    public static final int ORDER_STATUS_DISPATCHING = 14;//国内配送
    public static final int ORDER_STATUS_COMPLETED = 15;//已完成
    public static final int ORDER_STATUS_TRACKING = 16;//运单追踪信息
    public static final int ORDER_STATUS_DETAIL = 17;//运单详情
    public static final int ORDER_STATUS_TAX_SUBMIT = 18;//待缴税金

}
