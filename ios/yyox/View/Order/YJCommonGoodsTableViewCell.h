//
//  YJCommonGoodsTableViewCell.h
//  yyox
//
//  Created by ddn on 2017/2/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YJCommonGoodsTableViewCell : UITableViewCell

@property (strong, nonatomic) UILabel *nameLabel;
@property (strong, nonatomic) UILabel *leftSubnameLabel;
@property (strong, nonatomic) UILabel *rightSubnameLabel;
@property (strong, nonatomic) UIButton *deleteBtn;

@property (copy, nonatomic) void(^clickOnDelete)();

@end
