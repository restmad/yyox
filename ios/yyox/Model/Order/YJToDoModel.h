//
//  YJToDoModel.h
//  yyox
//
//  Created by ddn on 2017/2/17.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJToDoModel : NSObject

@property (assign, nonatomic) int type;//1.单个包裹(待创建订单) 2.订单 3.合箱
@property (assign, nonatomic) int status;//1.待创建订单 2.待支付 3.待上传身份证 4.待缴税金
@property (copy, nonatomic) NSString *no;//订单号或者运单号
@property (copy, nonatomic) NSString *inventoryId;//包裹id
@property (copy, nonatomic) NSArray *inventorys;
@property (copy, nonatomic) NSString *nickname;
@property (copy, nonatomic) NSString *productName;
@property (copy, nonatomic) NSString *warehouseName;
@property (copy, nonatomic) NSString *date;
@property (strong, nonatomic) NSNumber *packageNum;
@property (copy, nonatomic) NSString *orderStatus;
@property (copy, nonatomic) NSString *statement;
@property (strong, nonatomic) NSNumber *addressId;

@property (copy, nonatomic) NSString *currency;

@end
