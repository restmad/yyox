//
//  UIViewController+AddSel.h
//  CustomModalController
//
//  Created by zhangyongjun on 16/2/12.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CustomTransitioningDelegate.h"

@interface UIViewController (AddSel) <UIGestureRecognizerDelegate>

- (void)customStackWithAnimationType:(TransAnimationType)type andDuration:(NSTimeInterval)duration animationIfNeed:(void(^)(BOOL gotoNext, UIView *fromView, UIView *toView))animation;
- (void)customModalWithAnimationType:(TransAnimationType)type andDuration:(NSTimeInterval)duration animationIfNeed:(void(^)(BOOL gotoNext, UIView *fromView, UIView *toView))animation;

- (instancetype)initWithAnimationType:(TransAnimationType)type andDuration:(NSTimeInterval)duration animationIfNeed:(void(^)(BOOL gotoNext, UIView *fromView, UIView *toView))animation;
- (instancetype)initWithRootVC:(UIViewController *)rootVC animationType:(TransAnimationType)type andDuration:(NSTimeInterval)duration animationIfNeed:(void(^)(BOOL gotoNext, UIView *fromView, UIView *toView))animation;

@property (strong, nonatomic) CustomTransitioningDelegate *modalDelegate;
@property (strong, nonatomic) CustomTransitioningDelegate *stackDelegate;

@end
