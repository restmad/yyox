//
//  UIView+TransitionAnimation.h
//  CustomModalController
//
//  Created by zhangyongjun on 16/2/12.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIView (TransitionAnimation)

@property (assign, nonatomic) CGPoint maskCenter;
@property (assign, nonatomic) CGSize maskSize;

+ (void)gotoNext:(BOOL)gotoNext SameToNavTransitionFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void(^)())complete;

+ (void)gotoNext:(BOOL)gotoNext SizeTransitionFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void(^)())complete;

+ (void)gotoNext:(BOOL)gotoNext RotateYFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void(^)())complete;

+ (void)gotoNext:(BOOL)gotoNext RotateXFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void(^)())complete;

+ (void)gotoNext:(BOOL)gotoNext CureFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void(^)())complete;

+ (void)gotoNext:(BOOL)gotoNext TransitionByAlphaFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void(^)())complete;

+ (void)gotoNext:(BOOL)gotoNext TransitionBySysAlphaFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void(^)())complete;

+ (void)gotoNext:(BOOL)gotoNext transitionByMaskShapeFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void(^)())complete;

+ (void)gotoNext:(BOOL)gotoNext sizeToSizeFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void(^)())complete;

@end
