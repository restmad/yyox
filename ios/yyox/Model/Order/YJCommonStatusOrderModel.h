//
//  YJCommonStatusOrderModel.h
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJMerchandiseModel.h"

@interface YJCommonStatusOrderModel : NSObject <NSCoding>

@property (copy, nonatomic) NSString *id;
@property (copy, nonatomic) NSString *orderNo;
@property (copy, nonatomic) NSString *orStatusDate;
@property (copy, nonatomic) NSString *orderStatus;
@property (assign, nonatomic) int type;

@property (copy, nonatomic) NSArray *inventorybasic;
@property (copy, nonatomic) NSString *nickname;

@end
