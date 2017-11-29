//
//  NSArray+YJExtension.m
//  yyox
//
//  Created by ddn on 2017/3/7.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "NSArray+YJExtension.h"

@implementation NSArray (YJExtension)

- (NSArray *)selectIndexs:(NSArray *)indexs
{
	NSMutableArray *arr = [NSMutableArray array];
	for (NSNumber *index in indexs) {
		if (self.count > index.integerValue) {
			[arr addObject:self[index.integerValue]];
		}
	}
	return arr;
}

@end
