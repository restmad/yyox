//
//  UIView+YJLineLayout.m
//  Transfer
//
//  Created by ddn on 16/12/15.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "UIView+YJLineLayout.h"

@implementation UIView (YJLineLayout)

- (void)lineLayout:(NSArray *)views withSetting:(void(^)(NSInteger idx, UIView *view))setting withInset:(UIEdgeInsets)insets {
    UIView *preView;
    for (NSInteger i = 0; i < views.count; i ++) {
        UIView *view = views[i];
        [self addSubview:view];
        [view mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(insets.top);
            make.bottom.mas_equalTo(-insets.bottom);
        }];
        if (setting) {
            setting(i, view);
        }
        if (i == 0) {
            [view mas_makeConstraints:^(MASConstraintMaker *make) {
                make.left.equalTo(self.mas_left).offset(insets.left);
            }];
        }else if (i == views.count - 1) {
            [view mas_makeConstraints:^(MASConstraintMaker *make) {
                make.left.equalTo(preView.mas_right);
                make.width.equalTo(preView.mas_width);
                make.right.equalTo(self.mas_right).offset(-insets.right);
            }];
        }else {
            [view mas_makeConstraints:^(MASConstraintMaker *make) {
                make.left.equalTo(preView.mas_right);
                make.width.equalTo(preView.mas_width);
            }];
        }
        
        preView = view;
    }
}

- (void)lineLayout:(NSArray *)views withSetting:(void(^)(NSInteger idx, UIView *view))setting withInset:(UIEdgeInsets)insets andSpace:(CGFloat)space {
    UIView *preView;
    for (NSInteger i = 0; i < views.count; i ++) {
        UIView *view = views[i];
        [view mas_updateConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(insets.top);
            make.bottom.mas_equalTo(-insets.bottom);
        }];
        if (setting) {
            setting(i, view);
        }
        if (i == 0) {
            [view mas_updateConstraints:^(MASConstraintMaker *make) {
                make.left.mas_equalTo(self.mas_left).offset(insets.left);
            }];
        }else if (i == views.count - 1) {
            [view mas_updateConstraints:^(MASConstraintMaker *make) {
                make.left.mas_equalTo(preView.mas_right).offset(space);
                make.width.mas_equalTo(preView.mas_width);
                make.right.mas_equalTo(self.mas_right).offset(-insets.right);
            }];
        }else {
            [view mas_updateConstraints:^(MASConstraintMaker *make) {
                make.left.mas_equalTo(preView.mas_right).offset(space);
                make.width.mas_equalTo(preView.mas_width);
            }];
        }
        
        preView = view;
    }
}

- (void)lineButNoEqualWithLayout:(NSArray *)views withSetting:(void(^)(NSInteger idx, UIView *view))setting withInset:(UIEdgeInsets)insets andSpace:(CGFloat)space {
    UIView *preView;
    for (NSInteger i = 0; i < views.count; i ++) {
        UIView *view = views[i];
        [view mas_updateConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(insets.top);
            make.bottom.mas_equalTo(-insets.bottom);
        }];
        if (setting) {
            setting(i, view);
        }
        if (i == 0) {
            [view mas_updateConstraints:^(MASConstraintMaker *make) {
                make.left.equalTo(self.mas_left).offset(insets.left);
            }];
        }else if (i == views.count - 1) {
            [view mas_updateConstraints:^(MASConstraintMaker *make) {
                make.left.equalTo(preView.mas_right).offset(space);
            }];
        }else {
            [view mas_updateConstraints:^(MASConstraintMaker *make) {
                make.left.equalTo(preView.mas_right).offset(space);
            }];
        }
        
        preView = view;
    }
}

- (void)VlineLayout:(NSArray *)views withSetting:(void(^)(NSInteger idx, UIView *view))setting withInset:(UIEdgeInsets)insets andSpace:(CGFloat)space {
    UIView *preView;
    for (NSInteger i = 0; i < views.count; i ++) {
        UIView *view = views[i];
        [view mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(insets.left);
            make.right.mas_equalTo(-insets.right);
        }];
        if (setting) {
            setting(i, view);
        }
        if (i == 0) {
            [view mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.equalTo(self.mas_top).offset(insets.top);
            }];
        }else if (i == views.count - 1) {
            [view mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.equalTo(preView.mas_bottom).offset(space);
                make.height.equalTo(preView.mas_height);
                make.bottom.equalTo(self.mas_bottom).offset(-insets.bottom);
            }];
        }else {
            [view mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.equalTo(preView.mas_bottom).offset(space);
                make.height.equalTo(preView.mas_height);
            }];
        }
        
        preView = view;
    }
}


@end






















