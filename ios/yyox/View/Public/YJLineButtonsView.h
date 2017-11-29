//
//  YJLineButtonsView.h
//  yyox
//
//  Created by ddn on 2017/2/24.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YJLineButtonsView : UIView

- (instancetype)initWithFrame:(CGRect)frame titles:(NSArray *)titles callback:(void(^)(NSString *title))callback;

- (void)setupFont:(UIFont *)font color:(UIColor *)color;

- (void)setBackgroundImage:(UIImage *)image;

@property (assign, nonatomic) UIEdgeInsets layoutInsets;

@end
