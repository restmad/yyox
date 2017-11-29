//
//  YJCommonStatusMsgModel.m
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonStatusMsgModel.h"

@implementation YJCommonStatusDetailMsgModel



@end

@implementation YJCommonStatusMsgModel

+ (NSDictionary *)mj_objectClassInArray
{
	return @{@"list": [YJCommonStatusDetailMsgModel class]};
}

@end
