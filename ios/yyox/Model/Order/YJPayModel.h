//
//  YJPayModel.h
//  yyox
//
//  Created by ddn on 2017/2/27.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJPayModel : NSObject

@property (copy, nonatomic) NSString *orderNo;//订单号或者运单号
@property (copy, nonatomic) NSString *payType;
@property (copy, nonatomic) NSString *payTypeComments;

@property (strong, nonatomic) NSNumber *totalAmount;
@property (strong, nonatomic) NSNumber *balanceCny;
@property (strong, nonatomic) NSNumber *payStatus;

@property (copy, nonatomic) NSString *msgs;

- (void)holdon;
+ (void)giveup;
+ (instancetype)pickup;

@end
