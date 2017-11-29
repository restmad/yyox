//
//  YJPredicteViewController.h
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJMerchandiseModel.h"
#import "YJKeyboardViewController.h"

@interface YJPredicteViewController : YJKeyboardViewController

- (void)initialWithOrder:(YJMerchandiseModel *)model;

@property (copy, nonatomic) void(^callback)(YJMerchandiseModel *model);

@end
