//
//  YJCombineCell.h
//  yyox
//
//  Created by ddn on 2017/2/28.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJMerchandiseModel.h"

@interface YJCombineCell : UITableViewCell

@property (strong, nonatomic) YJMerchandiseModel *model;

@property (copy, nonatomic) void(^clickOnSelect)();

@property (assign, nonatomic) BOOL isWant;

@end
