//
//  YJApiDefine.m
//  Transfer
//
//  Created by ddn on 16/12/15.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "YJApiDefine.h"

@implementation YJApiDefine
NSString *const BaseUrl = @"http://app.yyox.com/";

//NSString *const BaseUrl = @"http://10.0.0.19:8080/";
//NSString *const BaseUrl = @"http://192.168.4.224:8080/";

/**
 获取图片 GET
 */
NSString *const AmazonUrl = @"app/customer/extractPicture";
/**
 首页信息 GET
 */
NSString *const OrderMainApi = @"app/customer/searchInfoHistory";
/**
 获取手机验证码 GET
 */
NSString *const AuthCodeApi = @"app/sms/user/authCode";
/**
 注册 POST json	
 */
NSString *const RegisterApi = @"app/register";
/**
 找回密码 POST json
 */
NSString *const ReRegisterApi = @"app/setpassword";
/**
 修改密码 POST json
 */
NSString *const ResetPasswordApi = @"app/customer/modifypassword";
/**
 登录 POST
 */
NSString *const LoginApi = @"app/login/send";
/**
 退出登录 GET
 */
NSString *const LogoutApi = @"app/login/logout";
/**
 用户信息 GET
 */
NSString *const UserInfoApi = @"app/customer";
NSString *const UserInfoApi2 = @"app/customer/detail";
/**
 上传用户头像 POST
 */
NSString *const UploadUserIconApi = @"app/customer/avatar/upload";
/**
 保存用户信息 POST json
 */
NSString *const EditUserInfoApi = @"app/customer/detail";
/**
 地址列表 GET
 */
NSString *const AddressListApi = @"app/customer/address/all";
/**
 地址详情 GET url
 */
NSString *const DetailAddressApi = @"app/customer/address";
/**
 删除地址 DELETE
 */
NSString *const DeleteAddressApi = @"app/customer/address";
/**
 添加&修改地址 POST
 */
NSString *const SaveAddressApi = @"app/customer/address";
/**
 上传身份证/身份证号 POST
 */
NSString *const SpecUploadPersonInfoApi = @"app/customer/address/update";
/**
 上传身份证 POST form
 */
NSString *const UploadCardApi = @"app/customer/address/uploadIDCard";
/**
 设置默认地址 PUT
 */
NSString *const setDefaultAddressApi = @"app/customer/address/setDefault";
/**
 三级地址列表 GET
 */
NSString *const RegionsApi = @"app/region/regions";
/**
 仓库列表 GET
 */
NSString *const WarehoushListApi = @"app/warehouse/list";
NSString *const WarehoushListApi2 = @"app/order/warehouseList";
/**
 待出库&已出库&清关中&国内配送&已完成 POST json
 
 */
NSString *const OrderStatusListApi = @"app/customer/orderSearch";
/**
 订单历史消息 GET
 */
NSString *const OrderStatusMsgListApi = @"app/track/searchhistory";
/**
 删除包裹 DELETE url
 */
NSString *const DeletePendingOrderApi = @"app/api";
/**
 预报 POST JSON
 */
NSString *const PutPendingOrderApi = @"app/api/oneKeyInventory";
/**
 上传预报截图 POST form
 */
NSString *const UploadPredictePicApi = @"app/inventory/receipt/upload";
/**
 订单详情 GET url
 */
NSString *const OrderDetailApi = @"app/order";
/**
 计算运费 POST json
 */
NSString *const CalculateOrderPayApi = @"app/order/calculateOrderMoney";
/**
 运输服务列表 GET
 */
NSString *const TransformListApi = @"app/order/initChannel";
/**
 待处理列表 GET
 */
NSString *const WaitDoListApi = @"app/api/inventory/waitForDispose";
/**
 包裹列表&待入库列表 POST json
 */
NSString *const PendingOrderApi = @"app/api/inventory/notInfoInventorys";
/**
 包裹详情 POST json
 */
NSString *const PackageDetailApi = @"app/api/inventory";
/**
 修改包裹名称 POST form
 */
NSString *const UpdateNickNameApi = @"app/api/updateNickname";
/**
 商品修改 GET
 */
NSString *const UpdatePackageApi = @"app/inventory/toUpdateMerchandise";
/**
 商品删除 GET
 */
NSString *const DeleteGoodsApi = @"app/inventory/delInventoryMerchandis";
/**
 提交订单 POST form
 */
NSString *const CommitOrderApi = @"app/order/commitOrder";
/**
 取消合箱 DELETE
 */
NSString *const CancelPackageApi = @"app/customer/orderDel";
/**
 验证是否需要上传身份证 GET
 */
NSString *const CheckNeedUploadCardInfoApi = @"app/order/checkcard";
/**
 拼接订单信息 GET
 */
NSString *const AliPayApi = @"app/customer/processData";
/**
 余额支付 GET
 */
NSString *const InnerPayApi = @"app/customer/goPayAppOrder";
/**
 支付状态&验证&余额 POST
 */
NSString *const PaymentStatusApi = @"app/payment/payReturn";
/**
 优惠券列表 GET
 */
NSString *const CouponApi = @"app/customer/coupon";
/**
 充值页详情 GET
 */
NSString *const PaymentDetailApi = @"app/customer/payment";
/**
 充值拼单 GET
 */
NSString *const RechargeAliPayApi = @"app/customer/processDataForDeposit";
/**
 充值验证 POST
 */
NSString *const RechargeVerifyApi = @"app/payment/verifyResult";
/**
 上传deviceToken GET
 */
NSString *const UploadDeviceTokenApi = @"app/track/getDeviceToken";
/**
 获取消费列表 POST
 */
NSString *const PayHistoryApi = @"app/customer/statementSearch";




//web
NSString *const ClauseUrl = @"app/mesage/condition/usecondition.html";
NSString *const UseConditionUrl = @"app/mesage/member/clause.html";

@end
