//
//  YJPayModel.m
//  yyox
//
//  Created by ddn on 2017/2/27.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPayModel.h"

@implementation YJPayModel

MJCodingImplementation

- (void)holdon
{
	NSData *data = [NSKeyedArchiver archivedDataWithRootObject:self];
	[NSUD setObject:data forKey:@"payModel"];
}

+ (void)giveup
{
	[NSUD removeObjectForKey:@"payModel"];
}

+ (instancetype)pickup
{
	NSData *data = [NSUD objectForKey:@"payModel"];
	if (data) {
		return [NSKeyedUnarchiver unarchiveObjectWithData:data];
	}
	return nil;
}

@end
