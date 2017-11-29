//
//  YJWarehouseModel.h
//  yyox
//
//  Created by ddn on 2017/2/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum : NSUInteger {
	YJWarehouseCountryAmerica = 1,
	YJWarehouseCountryGermany,
	YJWarehouseCountryJapan,
	YJWarehouseCountryAustralia
} YJWarehouseCountry;

@interface YJWarehouseModel : NSObject

@property (copy, nonatomic) NSString *id;
@property (copy, nonatomic) NSString *name;
@property (copy, nonatomic) NSString *code;
@property (copy, nonatomic) NSString *country;

@property (copy, nonatomic) NSString *value;
@property (assign, nonatomic) YJWarehouseCountry type;

@end
