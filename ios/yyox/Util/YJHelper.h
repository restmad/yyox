//
//  YJHelper.h
//  yyox
//
//  Created by ddn on 2017/3/20.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum : NSInteger {
	YJOrderStatusNone = -1,
	YJOrderStatusWaitIn = 0, //待入库
	YJOrderStatusWaitOut, //待出库
	YJOrderStatusDidOut, //已出库
	YJOrderStatusRunning, //清关中
	YJOrderStatusInner, //国内配送
	YJOrderStatusDone //已完成
} YJOrderStatus;

@interface YJHelper : NSObject

BOOL containInOrderStatus(NSString *status);
NSString *explainOrderStatus(YJOrderStatus status);
YJOrderStatus putUpOrderStatus(NSString *status);
NSString *switchOrderStatusToTableName(YJOrderStatus status);

BOOL isRemoteNotify();

@end
