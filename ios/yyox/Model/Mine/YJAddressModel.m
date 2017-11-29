//
//  YJAddressModel.m
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJAddressModel.h"

@implementation YJAddressModel

MJCodingImplementation

+ (NSDictionary *)mj_replacedKeyFromPropertyName
{
	return @{@"idCardNumber": @"customerCard.cardNo", @"cardImageFrontUrl": @"customerCard.cardImageFrontUrl", @"cardImageBackUrl": @"customerCard.cardImageBackUrl"};
}

- (NSString *)country
{
	if (!_country) {
		_country = @"中国";
	}
	return _country;
}

@end
