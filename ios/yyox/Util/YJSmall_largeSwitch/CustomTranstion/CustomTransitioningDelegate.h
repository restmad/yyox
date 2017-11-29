//
//  CustomTransitioningDelegate.h
//  CustomModalController
//
//  Created by zhangyongjun on 16/2/11.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "CustomAnimatedTransition.h"

@interface CustomTransitioningDelegate : NSObject <UIViewControllerTransitioningDelegate, UINavigationControllerDelegate>

@property (assign, nonatomic) NSTimeInterval duration;
@property (assign, nonatomic) TransAnimationType type;

@property (copy, nonatomic) void(^animationIfCustom)(BOOL gotoNext, UIView *fromView, UIView *toView);

@end
