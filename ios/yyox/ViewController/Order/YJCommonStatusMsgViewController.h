//
//  YJCommonStatusMsgViewController.h
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJCommonStatusOrderViewController.h"

@interface YJCommonStatusMsgViewController : UIViewController

@property (assign, nonatomic) YJOrderStatus orderStatus;

@property (copy, nonatomic) NSString *searchNo;

@end
