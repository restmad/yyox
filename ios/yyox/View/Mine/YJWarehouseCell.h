//
//  YJWarehouseCell.h
//  yyox
//
//  Created by ddn on 2017/5/17.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJWarehouseModel.h"

@interface YJWarehouseCell : UITableViewCell

@property (strong, nonatomic) YJWarehouseModel *model;
@property (copy, nonatomic) void(^copyCallback)();

@end
