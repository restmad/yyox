//
//  YJPayManager.h
//  yyox
//
//  Created by ddn on 2017/2/15.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJRouterResponseModel.h"
#import "YJPayModel.h"

@interface YJPayManager : NSObject

/**
 应用跳转

 @param url url
 @param callback 回调
 */
+ (void)applicationOpenUrl:(NSURL *)url callback:(void(^)(YJRouterResponseModel *result))callback;

/**
 支付

 @param pay 支付信息
 @param orderString 订单
 @param callback 回调
 */
+ (void)payOrder:(YJPayModel *)pay orderString:(NSString *)orderString callback:(void(^)(YJRouterResponseModel *))callback;

/**
 充值

 @param pay 支付信息
 @param orderString 订单
 @param callback 回调
 */
+ (void)recharge:(YJPayModel *)pay orderString:(NSString *)orderString callback:(void(^)(YJRouterResponseModel *))callback;

@end
