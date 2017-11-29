//
//  YJShowPackageViewController.h
//  yyox
//
//  Created by ddn on 2017/2/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJToDoModel.h"
#import "YJDetailOrderModel.h"

@interface YJShowPackageViewController : UIViewController

+ (YJShowPackageViewController *)instanceWithEditable:(BOOL)editable model:(YJToDoModel *)model;

@property (assign, nonatomic) BOOL shrinkable;

@property (strong, nonatomic) YJDetailOrderModel *detailOrderModel;

- (void)addCancelOpt;

- (void)showBottom;
- (void)hideBottom;

@end
