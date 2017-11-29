//
//  YJCouponModel.m
//  yyox
//
//  Created by ddn on 2017/3/25.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCouponModel.h"

@implementation YJCouponDetailModel



@end

@implementation YJCouponModel

+ (NSDictionary *)mj_objectClassInArray
{
	return @{@"coupon" : [YJCouponDetailModel class]};
}

@end
