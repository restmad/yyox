//
//  YJOrderMainModel.h
//  yyox
//
//  Created by ddn on 2017/1/24.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>
/*
 类型 state 说明
 
 1:包裹历史记录
 2:订单历史信息
 3:余额变动
 //4:身份证未上传
 5:优惠券
 对每种类型说明
 类型1：包裹历史记录
 Id:为包裹ID
 Information：消息内容
 operateTime：操作时间
 source_name：消息触发者
 customerId：当前客户ID
 
 类型2：:订单历史信息
 id:为订单ID
 Information：消息内容
 operateTime：操作时间
 source_name：消息触发者
 customerId：当前客户ID
 otherNo :订单号
 flightNo：航班号（如果航班号为空，则返回的值中不显示，需前段做判断）
 Destination：订单发往的目的地
 对订单显示 的详细描述：
 Information 中包含 “) 已创建”的内容，则显示消息内容”您的订单(otherNo )已经创建”
 Information 中包含”仓库订单已分拣”,则显示消息内容”您的订单已经打印完毕”
 3.Information 中包含”仓库订单已下架”,则显示消息内容”您的订单已经完成捡货”
 4.Information 中包含”请及时上传身份证以便订单清关”,则显示消息内容”您的订单(otherNo )已经创建，等待提供身份证信息”
 5.Information 中包含”仓库订单已出库”,则显示消息内容”您的订单(otherNo )已经打包成功”
 6.Information 中包含”仓库订单已出库”,则显示消息内容”您的订单(otherNo )已经打包成功”
 7.Information 中包含”订单清关已完成，正在派送中”,您的订单海关已放行，已交往"快递公司",单号"快递单号”(该消息内容示例”订单清关已完成，正在派送中 邮政国际 0306366526555”),需要对该内容截取快递公司和快递单号
 8.Information 中包含”发往机场，预计起飞时间”,则显示消息内容”您的订单已经离开source_name,发往destination,(航班号为 flightNo(为空的话，该航班号不显示))”
 9.Information 中包含”机场，正在提货中”,则显示消息内容”您的订单已抵达口岸机场”
 10.Information 中包含”海关清关中”,则显示消息内容”您的订单已从机场提货，送交海关清关中”
 11.Information 中包含”海关查验中”,则显示消息内容”您的订单正在海关查验中”
 12.Information 中包含”请尽快支付税金以便订单及时派送”,则显示消息内容”您的订单等待补缴税款后放行”
 
 类型3：余额变动
 Id: 充值时为客户ID，消费时为订单ID
 Information：消息内容
 operateTime：操作时间
 source_name：消息触发者
 customerId：当前客户ID
 
 //类型4：身份证未上传
 //Id: 请求ID
 //Information：消息内容
 //operateTime：操作时间
 //source_name：消息触发者
 //customerId：当前客户ID
 
 类型5：优惠券
 Id: 优惠券ID
 Information：消息内容
 operateTime：操作时间
 source_name：消息触发者
 customerId：当前客户ID

 */
@interface YJOrderMainMsgModel : NSObject

@property (strong, nonatomic) NSNumber *id;
@property (copy, nonatomic) NSString *information;
@property (strong, nonatomic) NSNumber *state;
@property (copy, nonatomic) NSString *operateTime;
@property (copy, nonatomic) NSString *otherNo;
@property (copy, nonatomic) NSString *source_name;
@property (copy, nonatomic) NSString *customerId;
@property (copy, nonatomic) NSString *destination;
@property (copy, nonatomic) NSString *flightNo;
@property (copy, nonatomic) NSString *orderStatus;
@property (copy, nonatomic) NSString *customerName;
@property (strong, nonatomic) NSNumber *sourceType;

@end

@interface YJOrderMainCountModel : NSObject

@property (strong, nonatomic) NSNumber *notputNo;
@property (strong, nonatomic) NSNumber *foroutboundNo;
@property (strong, nonatomic) NSNumber *haveoutboundNo;
@property (strong, nonatomic) NSNumber *clear;
@property (strong, nonatomic) NSNumber *delivering;
@property (strong, nonatomic) NSNumber *finish;
@property (strong, nonatomic) NSNumber *waitForDispose;

@end

@interface YJOrderMainModel : NSObject

@property (strong, nonatomic) YJOrderMainCountModel *mainCount;
@property (copy, nonatomic) NSArray<YJOrderMainMsgModel *> *result;

@end
