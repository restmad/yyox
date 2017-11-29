//
//  NSNull+YJExtension.m
//  yyox
//
//  Created by ddn on 2017/1/25.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "NSNull+YJExtension.h"

@implementation NSNull (YJExtension)

- (id)objectForKeyedSubscript:(id)sub
{
	return nil;
}

- (NSInteger)length
{
	return 0;
}

- (NSString *)stringByTrimmingCharactersInSet:(NSCharacterSet *)set
{
	return nil;
}

@end
