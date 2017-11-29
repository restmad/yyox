//
//  NSURL+YJExtension.m
//  yyox
//
//  Created by ddn on 2017/2/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "NSURL+YJExtension.h"

@implementation NSURL (YJExtension)

+ (NSURL *)yj_URLWithString:(NSString *)string
{
	if (!string || string.length == 0) return nil;
	if (![string hasPrefix:@"http"]) {
		string = [[BaseUrl stringByAppendingString:string] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	}
	return [self URLWithString:string];
}

@end
