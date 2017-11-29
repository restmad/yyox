//
//  YJCouponModel.h
//  yyox
//
//  Created by ddn on 2017/3/25.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJCouponDetailModel : NSObject

@property (strong, nonatomic) NSNumber *discountAmountInteger;
@property (strong, nonatomic) NSNumber *discountAmount;
@property (copy, nonatomic) NSString *comments;
@property (copy, nonatomic) NSString *name;

@end

@interface YJCouponModel : NSObject

@property (strong, nonatomic) YJCouponDetailModel *coupon;
@property (copy, nonatomic) NSString *code;
@property (copy, nonatomic) NSString *name;
@property (copy, nonatomic) NSString *limitUse;
@property (copy, nonatomic) NSString *validTo;
@property (copy, nonatomic) NSString *status;//Status:是否过期（NOT_USED,EXPIRED），是否可用(OUT_OF_COMMISSION,CAN_USE) NOT_USED　：没有使用，（我的） EXPIRED ：过期（我的） OUT_OF_COMMISSION 不可用（订单界面） CAN_USE　可用　（订单界面）


@end
