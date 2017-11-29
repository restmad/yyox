//
//  CustomAnimatedTransition.m
//  CustomModalController
//
//  Created by zhangyongjun on 16/2/11.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "CustomAnimatedTransition.h"
#import "UIView+TransitionAnimation.h"

@interface CustomAnimatedTransition ()


@end

@implementation CustomAnimatedTransition

- (instancetype)init {
    self = [super init];
    if (self) {
        
    }
    return self;
}

- (NSTimeInterval)transitionDuration:(id)transitionContext {
    return self.holder.duration;
}

- (void)animateTransition:(id)transitionContext {
    
    UIView *toView = [[transitionContext viewControllerForKey:UITransitionContextToViewControllerKey] view];
    UIView *fromView = [[transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey] view];
    
    switch (self.holder.type) {
        case TransAnimationTypeSystemAlpha:
        {
            [UIView gotoNext:self.holder.presented TransitionBySysAlphaFromView:fromView toView:toView inDuration:self.holder.duration andComplete:^{
                [transitionContext completeTransition:YES];
            }];
        }
            break;
        case TransAnimationTypeAlpha:
        {
            [UIView gotoNext:self.holder.presented TransitionByAlphaFromView:fromView toView:toView inDuration:self.holder.duration andComplete:^{
                [transitionContext completeTransition:YES];
            }];
        }
            break;
        case TransAnimationTypeRotateX:
        {
            [UIView gotoNext:self.holder.presented RotateXFromView:fromView toView:toView inDuration:self.holder.duration andComplete:^{
                
                [transitionContext completeTransition:YES];
            }];
        }
            break;
        case TransAnimationTypeRotateY:
        {
            [UIView gotoNext:self.holder.presented RotateYFromView:fromView toView:toView inDuration:self.holder.duration andComplete:^{
                
                [transitionContext completeTransition:YES];
            }];
        }
            break;
        case TransAnimationTypeCure:
        {
            [UIView gotoNext:self.holder.presented CureFromView:fromView toView:toView inDuration:self.holder.duration andComplete:^{
                
                [transitionContext completeTransition:YES];
            }];
        }
            break;
        case TransAnimationTypeSameToNav:
        {
            [UIView gotoNext:self.holder.presented SameToNavTransitionFromView:fromView toView:toView inDuration:self.holder.duration andComplete:^{
                
                [transitionContext completeTransition:YES];
            }];
        }
            break;
        case TransAnimationTypeSize:
        {
            [UIView gotoNext:self.holder.presented SizeTransitionFromView:fromView toView:toView inDuration:self.holder.duration andComplete:^{
                
                [transitionContext completeTransition:YES];
            }];
        }
            break;
        case TransAnimationTypeMaskCircle:
        {
            [UIView gotoNext:self.holder.presented transitionByMaskShapeFromView:fromView toView:toView inDuration:self.holder.duration andComplete:^{
                [transitionContext completeTransition:YES];
            }];
        }
            break;
        case TransAnimationTypeCustom:
        {
            if (self.holder.animation) {
                self.holder.animation(self.holder.presented, fromView, toView);
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(self.holder.duration * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    [transitionContext completeTransition:YES];
                });
            }
        }
            break;
		case TransAnimationTypeSizeToSize:
		{
			[UIView gotoNext:self.holder.presented sizeToSizeFromView:fromView toView:toView inDuration:self.holder.duration andComplete:^{
				[transitionContext completeTransition:YES];
			}];
		}
		default:
			break;
    }
}

@end
