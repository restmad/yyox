//
//  YJStatusProcessView.h
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJCommonStatusOrderViewController.h"

@interface YJStatusProcessView : UIView

@property (copy, nonatomic) NSString *start;
@property (copy, nonatomic) NSString *end;

@property (assign, nonatomic) YJOrderStatus orderStatus;

@end
