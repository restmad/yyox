//
//  NSString+YJExtension.m
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "NSString+YJExtension.h"

@implementation NSString (YJExtension)

+ (NSString *)timeDistanceTo:(NSString *)time
{
	NSDate *date = [NSDate dateWithString:time format:@"yyyy-MM-dd HH:mm:ss"];
	if (!date) return time;
	
	NSDate *now = [NSDate date];
	
	NSTimeInterval distance = [now timeIntervalSinceDate:date];
	
	if (distance <= 60 * 60) {
		return @"刚刚";
	} else if (distance <= 60 * 60 * 24) {
		NSInteger h = floor(distance / 60. / 60.);
		return [NSString stringWithFormat:@"%zd小时前", h];
	} else if ([date year] == [now year]) {
		return [NSString stringWithFormat:@"%02zd月%02zd日", [date month], [date day]];
	} else {
		return [NSString stringWithFormat:@"%zd年%02zd月%02zd日", [date year], [date month], [date day]];
	}
}

- (NSString *)transformToCurrency
{
	return self;
}

- (BOOL)containsOneOfStrings:(NSArray<NSString *> *)strings
{
	if (!self || self.length == 0 || !strings || strings.count == 0) return NO;
	NSMutableString *regex = [NSMutableString string];
	for (NSInteger i=0; i<strings.count; i++) {
		if (i == 0) {
			[regex appendFormat:@"(%@)", strings[i]];
		} else {
			[regex appendFormat:@"|(%@)", strings[i]];
		}
	}
	NSRegularExpression *regular = [[NSRegularExpression alloc] initWithPattern:regex options:0 error:nil];
	NSTextCheckingResult *result = [regular firstMatchInString:self options:0 range:NSMakeRange(0, self.length)];
	return result != nil;
}

@end
