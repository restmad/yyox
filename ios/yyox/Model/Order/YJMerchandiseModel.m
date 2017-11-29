//
//  YJMerchandiseModel.m
//  yyox
//
//  Created by ddn on 2017/2/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJMerchandiseModel.h"

@implementation YJMerchandiseModel

+ (NSDictionary *)mj_objectClassInArray
{
	return @{@"goodList": [YJGoodsModel class]};
}

+ (NSDictionary *)mj_replacedKeyFromPropertyName
{
	return @{@"inventoryStatusCode" : @"inventoryStatus.code", @"inventoryStatus" : @"inventoryStatus.status"};
}

- (NSArray<YJGoodsModel *> *)unreadyGoodsList
{
	return [self.goodList filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"goolsType=0"]];
}

- (NSArray<YJGoodsModel *> *)readyGoodsList
{
	return [self.goodList filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"goolsType=1"]];
}

- (NSNumber *)id
{
	if (!_id) return _inventoryId;
	return _id;
}

@end
