//
//  YJStoryboard.h
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum : NSUInteger {
	YJStoryboardIdentifyLogin,
	YJStoryboardIdentifyRegister,
	YJStoryboardIdentifyReRegister,
	YJStoryboardIdentifyMine,
	YJStoryboardIdentifySetting,
	YJStoryboardIdentifyEditUser,
	YJStoryboardIdentifyResetPassword,
	YJStoryboardIdentifyPredicte,
	YJStoryboardIdentifyAddGoods,
	YJStoryboardIdentifyPayment,
	YJStoryboardIdentifyPayResult,
	YJStoryboardIdentifyRecharge
} YJStoryboardIdentify;

@interface YJStoryboard : NSObject

+ (UIViewController *)storyboardInstanceWithIdentify:(YJStoryboardIdentify)identify;

+ (NSString *)storyboardIdentifyFor:(YJStoryboardIdentify)identify;

@end
