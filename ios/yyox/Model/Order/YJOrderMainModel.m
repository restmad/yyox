//
//  YJOrderMainModel.m
//  yyox
//
//  Created by ddn on 2017/1/24.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJOrderMainModel.h"

@implementation YJOrderMainCountModel

@end

@implementation YJOrderMainMsgModel

- (NSString *)operateTime
{
	if (!_operateTime || _operateTime.length == 0) {
		return _operateTime;
	}
	return [_operateTime stringByReplacingOccurrencesOfString:@"\n\t\t" withString:@" "];
}

@end

@implementation YJOrderMainModel

+ (NSDictionary *)mj_objectClassInArray
{
	return @{@"result": [YJOrderMainMsgModel class]};
}

+ (NSDictionary *)mj_replacedKeyFromPropertyName
{
	return @{@"mainCount": @"initCustomer"};
}

@end
