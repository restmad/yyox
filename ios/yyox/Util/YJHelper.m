//
//  YJHelper.m
//  yyox
//
//  Created by ddn on 2017/3/20.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJHelper.h"

@implementation YJHelper

BOOL containInOrderStatus(NSString *status)
{
	NSArray *statuses = @[@"未入库", @"待出库", @"已出库", @"清关中", @"派送中", @"已完成"];
	return [statuses containsObject:status];
}

YJOrderStatus putUpOrderStatus(NSString *status)
{
	if (containInOrderStatus(status)) {
		NSArray *statuses = @[@"未入库", @"待出库", @"已出库", @"清关中", @"派送中", @"已完成"];
		return [statuses indexOfObject:status];
	}
	return YJOrderStatusNone;
}

NSString *explainOrderStatus(YJOrderStatus status)
{
	NSString *paramsValue = nil;
	
	switch (status) {
		case YJOrderStatusWaitIn:
			paramsValue = @"未入库";
			break;
		case YJOrderStatusWaitOut:
			paramsValue = @"待出库";
			break;
		case YJOrderStatusDidOut:
			paramsValue = @"已出库";
			break;
		case YJOrderStatusRunning:
			paramsValue = @"清关中";
			break;
		case YJOrderStatusInner:
			paramsValue = @"派送中";
			break;
		case YJOrderStatusDone:
			paramsValue = @"已完成";
			break;
			
		default:
			break;
	}
	return paramsValue;
}

NSString *switchOrderStatusToTableName(YJOrderStatus status)
{
	NSString *tableName;
	switch (status) {
		case YJOrderStatusWaitOut:
			tableName = ORDER_WAITOUT_DATABASE;
			break;
		case YJOrderStatusDidOut:
			tableName = ORDER_DIDOUT_DATABASE;
			break;
		case YJOrderStatusRunning:
			tableName = ORDER_RUNNING_DATABASE;
			break;
		case YJOrderStatusInner:
			tableName = ORDER_INNER_DATABASE;
			break;
		case YJOrderStatusDone:
			tableName = ORDER_DONE_DATABASE;
			break;
		default:
			break;
	}
	return tableName;
}

BOOL isRemoteNotify()
{
	BOOL result = NO;
//	if (kiOSLater(10)) {
//		
//	} else {
		UIUserNotificationSettings *settings = [[UIApplication sharedApplication] currentUserNotificationSettings];
		if (UIUserNotificationTypeNone != settings.types) {
			result = YES;
		} else {
			result = NO;
		}
//	}
	return result;
}

@end
