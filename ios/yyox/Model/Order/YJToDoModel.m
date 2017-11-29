//
//  YJToDoModel.m
//  yyox
//
//  Created by ddn on 2017/2/17.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJToDoModel.h"

@implementation YJToDoModel

- (NSString *)nickname
{
	if (!_nickname || _nickname.length == 0) {
		return [NSString stringWithFormat:@"%@%@", self.type == 1 ? @"运单号：" : @"邮客单号：", self.no ?: self.inventoryId];
	} else {
		return [NSString stringWithFormat:@"包裹昵称：%@", _nickname];
	}
}

@end
