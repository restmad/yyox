//
//  NSObject+YJWeaks.m
//  yyox
//
//  Created by ddn on 2017/7/12.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "NSObject+YJWeaks.h"

@implementation NSObject (YJWeaks)


YYSYNTH_DYNAMIC_PROPERTY_OBJECT(weaks, setWeaks, RETAIN_NONATOMIC, NSPointerArray *)

- (void)weakHoldTask:(NSURLSessionDataTask *)task
{
	if (!task) return;
	if (!self.weaks) {
		self.weaks = [NSPointerArray weakObjectsPointerArray];
	}
	[self.weaks addPointer:(__bridge void * _Nullable)(task)];
}

- (void)cancelTasks
{
	[self.weaks.allObjects enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
		if ([obj isKindOfClass:[NSURLSessionDataTask class]]) {
			[(NSURLSessionDataTask *)obj cancel];
			NSLog(@"task cancel %@", self);
		}
	}];
}

@end
