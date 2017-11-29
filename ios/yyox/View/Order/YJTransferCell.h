//
//  YJTransferCell.h
//  yyox
//
//  Created by ddn on 2017/2/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJTransferModel.h"

@interface YJTransferCell : UITableViewCell

@property (strong, nonatomic) YJTransferModel *model;

@property (assign, nonatomic) BOOL isWant;

@property (copy, nonatomic) void(^clickOnSelected)();

@end
