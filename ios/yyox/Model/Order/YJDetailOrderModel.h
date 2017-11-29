//
//  YJDetailOrderModel.h
//  yyox
//
//  Created by ddn on 2017/1/24.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJMerchandiseModel.h"
#import "YJAddressModel.h"
#import "YJTransferModel.h"
#import "YJCouponModel.h"

@interface YJDetailOrderModel : NSObject

//address
@property (strong, nonatomic) YJAddressModel *address;

//transform
@property (strong, nonatomic) YJTransferModel *orderchannel;

//order
@property (copy, nonatomic) NSString *orderNo;
@property (assign, nonatomic) int type;
@property (copy, nonatomic) NSString *alias;
@property (strong, nonatomic) NSNumber *YouBi;
@property (assign, nonatomic) int couponNumber;
@property (strong, nonatomic) NSNumber *originalCost;
@property (strong, nonatomic) NSNumber *realcost;
@property (copy, nonatomic) NSArray *valueAddedlist;
@property (strong, nonatomic) YJCouponModel *couponList;
@property (strong, nonatomic) NSNumber *taxRepay;


@property (copy, nonatomic) NSString *inWeight; //出库重量
@property (strong, nonatomic) NSNumber *realWeight; //入库重量

//merchandise
@property (copy, nonatomic) NSArray<YJMerchandiseModel *> *merchandiseList;

@end
