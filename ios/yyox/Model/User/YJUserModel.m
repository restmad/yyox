//
//  YJUserModel.m
//  yyox
//
//  Created by ddn on 2017/1/17.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJUserModel.h"

@implementation YJUserModel

+ (NSString *)primaryKey
{
	return @"id";
}

- (void)setMember:(NSString *)member
{
//	NSScanner *scanner = [[NSScanner alloc] initWithString:member];
//	NSCharacterSet *numSet = [NSCharacterSet decimalDigitCharacterSet];
//	if ([scanner scanUpToCharactersFromSet:numSet intoString:NULL]) {
//		NSInteger m=0;
//		BOOL re = [scanner scanInteger:&m];
//		if (re) {
//			_member = [NSString stringWithFormat:@"%zd", m];
//		}
//	}
	_member = nil;
}

//+ (NSDictionary *)defaultPropertyValues
//{
//	return @{@"member": @"0", @"couponCount": @0, @"name": @"", @"difBalanceCny": @0.00};
//}

@end
