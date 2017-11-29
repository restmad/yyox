//
//  YJGlobalWeakHoldManager.h
//  yyox
//
//  Created by ddn on 2017/2/27.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJGlobalWeakHoldManager : NSObject

YJSingleton_h(GlobalWeakHoldManager)

@property (strong, nonatomic) NSPointerArray *weaks;

@property (strong, nonatomic) NSMapTable<id, id> *weakMaps;

/**
 添加值

 @param value 值
 */
+ (void)yj_addValue:(id)value;

/**
 查找值

 @param callback 回调
 */
+ (void)yj_findValue:(BOOL(^)(id value))callback;

/**
 设置值

 @param value 值
 @param key 键
 */
+ (void)yj_setValue:(id)value forKey:(id)key;

/**
 获取值

 @param key 键
 @return 值
 */
+ (id)yj_getValueForKey:(id)key;

@end
