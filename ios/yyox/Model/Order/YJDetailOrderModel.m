//
//  YJDetailOrderModel.m
//  yyox
//
//  Created by ddn on 2017/1/24.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJDetailOrderModel.h"

@implementation YJDetailOrderModel

+ (NSDictionary *)mj_objectClassInArray
{
	return @{@"merchandiseList" : [YJMerchandiseModel class], @"address" : [YJAddressModel class], @"orderchannel": [YJTransferModel class], @"couponList" : [YJCouponModel class]};
}

//- (id)mj_newValueFromOldValue:(id)oldValue property:(MJProperty *)property
//{
//	if ([property.type.typeClass isSubclassOfClass:[NSString class]]) {
//		if (!oldValue || ![oldValue isKindOfClass:[NSString class]] || [oldValue length] == 0) {
//			return @"...";
//		}
//	}
//	if ([property.type.typeClass isSubclassOfClass:[NSNumber class]]) {
//		if (!oldValue || ![oldValue isKindOfClass:[NSNumber class]]) {
//			return @0;
//		}
//	}
//	return oldValue;
//}

@end
