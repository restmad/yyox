//
//  YJEditAddressViewController.h
//  yyox
//
//  Created by ddn on 2017/1/11.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJAddressModel.h"

typedef enum : NSUInteger {
	YJEditAddressEditAll,
	YJEditAddressCard,
	YJEditAddressNone,
} YJEditAddress;

@interface YJEditAddressViewController : UIViewController

@property (strong, nonatomic) YJAddressModel *addressModel;

@property (assign, nonatomic) YJEditAddress type;

@property (copy, nonatomic) void(^callback)(BOOL changeDefault);

@end
