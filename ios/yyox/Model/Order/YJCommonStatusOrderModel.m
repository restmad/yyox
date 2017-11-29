//
//  YJCommonStatusOrderModel.m
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonStatusOrderModel.h"

@implementation YJCommonStatusOrderModel

MJCodingImplementation

- (NSString *)nickname
{
	if (self.type == 3) {
		return @"合箱发货";
	} else {
		if (self.inventorybasic && self.inventorybasic[0]) {
			return self.inventorybasic[0][@"nickname"];
		}
		return @"";
	}
}

@end
