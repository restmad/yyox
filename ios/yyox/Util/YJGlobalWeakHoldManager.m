//
//  YJGlobalWeakHoldManager.m
//  yyox
//
//  Created by ddn on 2017/2/27.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJGlobalWeakHoldManager.h"

@implementation YJGlobalWeakHoldManager

YJSingleton_m(GlobalWeakHoldManager)

- (NSPointerArray *)weaks
{
	if (!_weaks) {
		_weaks = [NSPointerArray weakObjectsPointerArray];
	}
	return _weaks;
}

- (NSMapTable *)weakMaps
{
	if (!_weakMaps) {
		_weakMaps = [NSMapTable weakToWeakObjectsMapTable];
	}
	return _weakMaps;
}

+ (void)yj_addValue:(id)value
{
	YJGlobalWeakHoldManager *mgr = [self sharedGlobalWeakHoldManager];
	[mgr.weaks addPointer:(__bridge void * _Nullable)(value)];
}

+ (void)yj_findValue:(BOOL (^)(id))callback
{
	[[YJGlobalWeakHoldManager sharedGlobalWeakHoldManager].weaks.allObjects enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
		if (obj) {
			if (callback) {
				*stop = callback(obj);
			} else {
				*stop = YES;
			}
		}
	}];
}

+ (void)yj_setValue:(id)value forKey:(id)key
{
	YJGlobalWeakHoldManager *mgr = [self sharedGlobalWeakHoldManager];
	[mgr.weakMaps setObject:value forKey:key];
}

+ (id)yj_getValueForKey:(id)key
{
	YJGlobalWeakHoldManager *mgr = [self sharedGlobalWeakHoldManager];
	return [mgr.weakMaps objectForKey:key];
}

@end
