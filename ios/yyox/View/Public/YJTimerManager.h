//
//  YJTimerManager.h
//  yyox
//
//  Created by ddn on 2017/1/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

#define kDefaultDuration 60

@protocol YJTimerManagerDelegate <NSObject>

- (void)timerHasChanged:(NSInteger)time;

@end

@interface YJTimerManager : NSObject

YJSingleton_h(TimerButton)

- (void)startForKey:(NSString *)key;
- (void)endForKey:(NSString *)key;

- (void)setDuration:(NSTimeInterval)duration forKey:(NSString *)key;
- (NSTimeInterval)durationForKey:(NSString *)key;

- (void)addDelegate:(id<YJTimerManagerDelegate>)delegate forKey:(NSString *)key;

@end
