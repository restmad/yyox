//
//  YJBottomFloatView.h
//  yyox
//
//  Created by ddn on 2017/2/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YJBottomFloatView : UIView

@property (strong, nonatomic) UIButton *rightButton;
@property (strong, nonatomic) UILabel *titleLabel;
@property (strong, nonatomic) UILabel *valueLabel;
@property (strong, nonatomic) UILabel *unitsLabel;

@property (strong, nonatomic) UILabel *subTitleLabel;
@property (strong, nonatomic) UILabel *subValueLabel;

@property (assign, nonatomic) BOOL showSub;

@property (copy, nonatomic) void((^clickOnButton)());

- (void)setupConstraints;

@end
