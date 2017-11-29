//
//  YJMerchandiseModel.h
//  yyox
//
//  Created by ddn on 2017/2/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJGoodsModel.h"

@interface YJMerchandiseModel : NSObject

@property (strong, nonatomic) NSNumber *id;
@property (copy, nonatomic) NSString *icon;

@property (copy, nonatomic) NSString *productName;

@property (copy, nonatomic) NSArray<YJGoodsModel *> *goodList;
@property (copy, nonatomic) NSArray<YJGoodsModel *> *unreadyGoodsList;
@property (copy, nonatomic) NSArray<YJGoodsModel *> *readyGoodsList;

@property (copy, nonatomic) NSString *nickname;

@property (strong, nonatomic) NSNumber *weight;

@property (copy, nonatomic) NSString *bikeUPS;
//@property (copy, nonatomic) NSString *orderNo;
@property (copy, nonatomic) NSString *carrierNo;

@property (copy, nonatomic) NSArray *orderSreenshot;

@property (copy, nonatomic) NSString *customerAliasStatus;
@property (copy, nonatomic) NSString *status;

@property (copy, nonatomic) NSString *inventoryStatusCode;
@property (strong, nonatomic) NSNumber *inventoryId;
@property (copy, nonatomic) NSString *inventoryStatus;


@property (copy, nonatomic) NSString *inBoundDate;
@property (copy, nonatomic) NSString *inStockTime;
@property (copy, nonatomic) NSString *actionDate;
@property (copy, nonatomic) NSString *date;

@property (copy, nonatomic) NSString *warehouseName;
@property (copy, nonatomic) NSString *warehouseId;

@end
