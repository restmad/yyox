//
//  YJTimerButton.m
//  yyox
//
//  Created by ddn on 2017/1/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJTimerManager.h"

#define kDuration(key) ([[(YJTimerManager *)[YJTimerManager sharedTimerButton] durations][key] doubleValue] ?: kDefaultDuration)
@interface YJTimerModel : NSObject

@property (weak, nonatomic) id<YJTimerManagerDelegate> delegate;
@property (strong, nonatomic) NSTimer *timer;
@property (assign, nonatomic) NSInteger time;
@property (copy, nonatomic) NSString *key;

@end

@implementation YJTimerModel

@end

@interface YJTimerManager()

@property (strong, nonatomic) NSMutableArray *timers;

@property (strong, nonatomic) NSMutableDictionary *durations;

@end

@implementation YJTimerManager

YJSingleton_m(TimerButton)

- (NSMutableDictionary *)durations
{
	if (!_durations) {
		_durations = [NSMutableDictionary dictionary];
	}
	return _durations;
}

- (NSMutableArray *)timers
{
	if (!_timers) {
		_timers = [NSMutableArray array];
	}
	return _timers;
}

+ (YJTimerModel *)timerModelForKey:(NSString *)key
{
	YJTimerManager *mgr = [YJTimerManager sharedTimerButton];
	
	__block YJTimerModel *timerModel = nil;
	
	[mgr.timers enumerateObjectsUsingBlock:^(YJTimerModel * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
		if ([obj.key isEqualToString:key]) {
			timerModel = obj;
			*stop = YES;
		}
	}];
	
	if (!timerModel) {
		timerModel = [YJTimerModel new];
		timerModel.key = key;
		[mgr.timers addObject:timerModel];
	}
	
	NSTimer *timer = timerModel.timer;
	if (!timer || !timer.isValid) {
		timerModel.timer = [NSTimer timerWithTimeInterval:1. target:mgr selector:@selector(followTimer:) userInfo:@{@"key": key} repeats:YES];
		timerModel.time = kDuration(key);
	}
	
	return timerModel;
}

- (void)addDelegate:(id<YJTimerManagerDelegate>)delegate forKey:(NSString *)key
{
	YJTimerModel *timerModel = [self.class timerModelForKey:key];
	timerModel.delegate = delegate;
}

- (void)setDuration:(NSTimeInterval)duration forKey:(NSString *)key
{
	self.durations[key] = @(duration);
}

- (NSTimeInterval)durationForKey:(NSString *)key
{
	return [self.durations[key] doubleValue] ?: 0;
}

- (void)startForKey:(NSString *)key
{
	YJTimerModel *timerModel = [self.class timerModelForKey:key];
	if (timerModel.time < kDuration(key)) return;
	NSTimer *timer = timerModel.timer;
	
	[[NSRunLoop mainRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
}

- (void)endForKey:(NSString *)key
{
	[self.timers enumerateObjectsUsingBlock:^(YJTimerModel * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
		if ([obj.key isEqualToString:key]) {
			[obj.timer invalidate];
			[self.timers removeObjectAtIndex:idx];
			[self.durations removeObjectForKey:key];
			*stop = YES;
		}
	}];
}

- (void)followTimer:(NSTimer *)timer
{
	
	NSString *key = timer.userInfo[@"key"];
	if (!key) return;
	
	YJTimerModel *timerModel = [self.class timerModelForKey:key];
	
	timerModel.time --;
	id<YJTimerManagerDelegate> delegate = timerModel.delegate;
	if (delegate && [delegate respondsToSelector:@selector(timerHasChanged:)]) {
		[delegate timerHasChanged:timerModel.time];
	}
	
	if (timerModel.time == 0) {
		[self endForKey:key];
	}
}

- (void)dealloc
{
	[_timers enumerateObjectsUsingBlock:^(YJTimerModel * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
		if (obj.timer.isValid) {
			[obj.timer invalidate];
			[_timers removeObjectAtIndex:idx];
		}
	}];
}

@end




