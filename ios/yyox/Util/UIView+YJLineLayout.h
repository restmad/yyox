//
//  UIView+YJLineLayout.h
//  Transfer
//
//  Created by ddn on 16/12/15.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIView (YJLineLayout)

- (void)lineLayout:(NSArray *)views withSetting:(void(^)(NSInteger idx, UIView *view))setting withInset:(UIEdgeInsets)insets;

- (void)lineLayout:(NSArray *)views withSetting:(void(^)(NSInteger idx, UIView *view))setting withInset:(UIEdgeInsets)insets andSpace:(CGFloat)space;
- (void)VlineLayout:(NSArray *)views withSetting:(void(^)(NSInteger idx, UIView *view))setting withInset:(UIEdgeInsets)insets andSpace:(CGFloat)space;
- (void)lineButNoEqualWithLayout:(NSArray *)views withSetting:(void(^)(NSInteger idx, UIView *view))setting withInset:(UIEdgeInsets)insets andSpace:(CGFloat)space;

@end
