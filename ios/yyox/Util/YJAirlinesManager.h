//
//  YJAirlinesManager.h
//  yyox
//
//  Created by ddn on 2017/4/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJAirlinesManager : NSObject

YJSingleton_h(AirlinesManager)

/**
 初始化客服系统
 */
+ (void)setup;

/**
 删除所保存用户deviceToken

 @param callback 回调
 */
+ (void)deleteUser:(void(^)(NSDictionary * result, NSError * error))callback;

@end
