//
//  YJAddressModel.h
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJAddressModel : NSObject <NSCoding>

@property (strong, nonatomic) NSNumber *id;

@property (assign, nonatomic) BOOL isdefault;

@property (copy, nonatomic) NSString *name;
@property (copy, nonatomic) NSString *mobile;
@property (copy, nonatomic) NSString *detailaddress;
@property (copy, nonatomic) NSString *region;
@property (copy, nonatomic) NSString *zipcode;

@property (copy, nonatomic) NSString *country;

@property (copy, nonatomic) NSString *province;
@property (copy, nonatomic) NSString *city;
@property (copy, nonatomic) NSString *district;

@property (copy, nonatomic) NSString *idCardNumber;

@property (copy, nonatomic) NSString *cardImageFrontUrl;
@property (copy, nonatomic) NSString *cardImageBackUrl;

@property (copy, nonatomic) NSString *fullDetailaddress;

@end
