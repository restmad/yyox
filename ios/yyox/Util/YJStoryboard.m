//
//  YJStoryboard.m
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJStoryboard.h"

@implementation YJStoryboard

+ (UIViewController *)storyboardInstanceWithIdentify:(YJStoryboardIdentify)identify
{
	UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"YJUser" bundle:[NSBundle mainBundle]];
	NSString *identifyStr = [YJStoryboard storyboardIdentifyFor:identify];
	UIViewController *vc = [storyboard instantiateViewControllerWithIdentifier:identifyStr];
	
	vc.storyboardIdentify = identifyStr;
	
	return vc;
}

+ (NSString *)storyboardIdentifyFor:(YJStoryboardIdentify)identify
{
	switch (identify) {
		case YJStoryboardIdentifyLogin:
			return @"login";
		case YJStoryboardIdentifyRegister:
			return @"register";
		case YJStoryboardIdentifyReRegister:
			return @"reRegister";
		case YJStoryboardIdentifyMine:
			return @"mine";
		case YJStoryboardIdentifySetting:
			return @"setting";
		case YJStoryboardIdentifyEditUser:
			return @"editUser";
		case YJStoryboardIdentifyResetPassword:
			return @"resetPassword";
		case YJStoryboardIdentifyPredicte:
			return @"predicte";
		case YJStoryboardIdentifyAddGoods:
			return @"addGoods";
		case YJStoryboardIdentifyPayment:
			return @"payment";
		case YJStoryboardIdentifyPayResult:
			return @"payResult";
		case YJStoryboardIdentifyRecharge:
			return @"recharge";
		default:
			return nil;
	}
}

@end
