package com.yyox.consts;

/**
 * Created by dadaniu on 2017-01-17.
 */

public class CodeDefine {

    public static final int LOGIN_REQUEST = 8000;
    public static final int LOGIN_RESULT = 8001;

    public static final int REGISTER_REQUEST = 8002;
    public static final int REGISTER_RESULT = 8003;

    public static final int FORGET_REQUEST = 8004;
    public static final int FORGET_RESULT = 8005;

    public static final int SETTING_REQUEST = 8006;
    public static final int SETTING_RESULT = 8007;

    public static final int IDCARDA_REQUEST = 8008;
    public static final int IDCARDB_REQUEST = 8009;

    public static final int ADDRESS_SAVE_REQUEST = 8012;
    public static final int ADDRESS_SAVE_RESULT = 8013;

    public static final int ORDER_CHANNEL_REQUEST = 8014;
    public static final int ORDER_CHANNEL_RESULT = 8015;

    public static final int ORDER_DETAIL_ADDRESS_REQUEST = 8016;
    public static final int ORDER_DETAIL_ADDRESS_RESULT = 8017;

    public static final int ORDER_DETAIL_CHANNEL_REQUEST = 8018;
    public static final int ORDER_DETAIL_CHANNEL_RESULT = 8019;

    public static final int ADDRESS_REQUEST = 8020;//地址>服务
    public static final int ADDRESS_RESULT = 8021;//服务>地址

    public static final int PACKAGES_DETAILS_REQUEST = 8022;//包裹>地址
    public static final int PACKAGES_DETAILS_RESULT = 8023;

    public static final int ORDER_DETAIL_REQUEST = 8024;
    public static final int ORDER_DETAIL_RESULT = 8025;

    public static final int ORDER_PAY_REQUEST = 8026;
    public static final int ORDER_PAY_RESULT = 8027;

    public static final int ORDER_PAY_SUCCESS_REQUEST = 8028;
    public static final int ORDER_PAY_SUCCESS_RESULT = 8029;

    public static final int PACKAGE_REQUEST = 8030;
    public static final int PACKAGE_RESULT = 8031;

    public static final int ORDER_DETAIL_PACKAGE_REQUEST = 8032;
    public static final int ORDER_DETAIL_PACKAGE_RESULT = 8033;

    public static final int ORDER_DETAIL_PAY_RESULT = 8034;//支付界面返回
    public static final int ADDRESS_CARD_RESULT = 8035;     //地址详情回退到待处理

    public static final int ORDER_FRAGMENT_IMAGE_REQUEST = 8036;
    public static final int ORDER_FRAGMENT_IMAGE_RESULT = 8037;

    public static final int SERVICE_REQUEST = 8038;//跳转客服
    public static final int SERVICE_RESULT = 8039;//客服回退

    public static final int HOME_ORDER_REQUEST = 8040;//首页跳转待处理
    public static final int HOME_ORDER__RESULT = 8041;//待处理回退首页

    public static final int HOME_PACKGE_REQUEST = 8040;//首页跳转待入库
    public static final int HOME_PACKGE__RESULT = 8041;//待入库回退首页

    public static final int ADDRESS_ORDER_RESULT = 8042;//地址详情回退到订单详情

    public static final int PACKAGE_IMAGE_REQUEST = 8043;//预报进入截图详情
    public static final int PACKAGE_IMAGE__RESULT = 8044;//截图详情回退首页

    public static final int IMAGE_SHOW_REQUEST = 8045;
    public static final int IMAGE_SHOW_RESULT = 8046;

    public static final int MINE_PEGISTER_REQUEST = 8047;//我的进入充值界面
    public static final int MINE_PEGISTER_RESULT = 8048; //充值界面返回我的

    public static final int ORDER_COUPON_REQUEST = 8049;//订单详情进入优惠卷
    public static final int ORDER_COUPON_RESULT = 8050;//优惠卷返回订单详情

    public static final int MINE_STTINFS_REQUEST = 8051;//我的界面进入系统设置
    public static final int MINE_STTINGS_RESULT = 8052;//系统设置返回我的界面

    public static final int MINE_EDIT_REQUEST = 8053;//我的界面进入个人信息设置
    public static final int MINE_EDIT_RESULT = 8054;//个人信息设置返回我的界面

    public static final int STTINGS_EDIT_REQUEST = 8055;//系统设置进入个人信息
    public static final int STTINGS_EDIT_RESULT = 8056;//个人信息返回系统设置再返回我的

    public static final int HOME_finish_REQUEST = 8057;//首页跳转已完成
    public static final int HOME_finish_RESULT = 8058;//已完成回退首页

    public static final int BOX_BACKAGE_REQUEST = 8057;//合箱跳转包裹列表
    public static final int BOX_BACKAGE_RESULT = 8058;//包裹列表返回合箱
}
