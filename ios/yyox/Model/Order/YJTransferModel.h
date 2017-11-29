//
//  YJTransferModel.h
//  yyox
//
//  Created by ddn on 2017/2/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJTransferModel : NSObject

@property (copy, nonatomic) NSString *code;
@property (copy, nonatomic) NSString *explain;
@property (copy, nonatomic) NSString *priceWeight;
@property (strong, nonatomic) NSNumber *warehouseId;
@property (strong, nonatomic) NSNumber *orderTypeId;
@property (copy, nonatomic) NSString *orderTypeCode;
@property (strong, nonatomic) NSNumber *price;
@property (strong, nonatomic) NSNumber *leadId;

@end
