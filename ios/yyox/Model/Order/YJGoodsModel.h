//
//  YJGoodsModel.h
//  yyox
//
//  Created by ddn on 2017/2/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJGoodsModel : NSObject

@property (copy, nonatomic) NSString *productName;
@property (copy, nonatomic) NSString *productNameCNY;
@property (strong, nonatomic) NSNumber *amount;
@property (strong, nonatomic) NSNumber *stock;
@property (copy, nonatomic) NSString *units;
@property (copy, nonatomic) NSString *currencyName;
@property (assign, nonatomic) BOOL goolsType;
@property (copy, nonatomic) NSString *brand;
@property (copy, nonatomic) NSString *brandCNY;
@property (copy, nonatomic) NSString *upc;

@property (strong, nonatomic) NSNumber *id;

@end
