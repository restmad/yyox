//
//  YJCommonWithFooterTableViewCell.h
//  yyox
//
//  Created by ddn on 2017/2/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJLineImageViews.h"

@interface YJCommonWithFooterTableViewCell : UITableViewCell

@property (strong, nonatomic) UIImageView *iconImageView;
@property (strong, nonatomic) UILabel *nameLabel;
@property (strong, nonatomic) UILabel *subNameLabel;
@property (strong, nonatomic) UIView *middleLineView;
@property (strong, nonatomic) UILabel *leftLabel;
@property (strong, nonatomic) YJLineImageViews *imageViews;

@property (strong, nonatomic) UILabel *ssubNameLabel;

@property (strong, nonatomic) UIButton *moreInfoButton;
@property (strong, nonatomic) UIButton *editButton;

@property (assign, nonatomic) BOOL hasShowOpt;

@property (assign, nonatomic) BOOL showing;
@property (copy, nonatomic) void((^clickOnShowMore)(BOOL));
@property (copy, nonatomic) void((^clickOnEdit)());

- (void)hideFooter;
- (void)showFooter;

@end
