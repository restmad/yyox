//
//  YJCommonTableViewCell.h
//  yyox
//
//  Created by ddn on 2017/2/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YJCommonTableViewCell : UITableViewCell

@property (strong, nonatomic) UIImageView *iconImageView;
@property (strong, nonatomic) UILabel *nameLabel;
@property (strong, nonatomic) UILabel *subnameLabel;
@property (strong, nonatomic) UIButton *editButton;
@property (strong, nonatomic) UIButton *moreInfoButton;

@property (strong, nonatomic) UILabel *ssubnameLabel;

@property (assign, nonatomic) BOOL doing;
@property (assign, nonatomic) BOOL showing;

@property (copy, nonatomic) void((^clickOnEdit)(BOOL));
@property (copy, nonatomic) void((^clickOnShowMore)(BOOL));

@end
