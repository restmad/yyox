//
//  UIView+TransitionAnimation.m
//  CustomModalController
//
//  Created by zhangyongjun on 16/2/12.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "UIView+TransitionAnimation.h"
#import "UIView+Extension.h"
#import <objc/runtime.h>

@implementation UIView (TransitionAnimation)
//左右
+ (void)gotoNext:(BOOL)gotoNext SameToNavTransitionFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void (^)())complete
{
    if (gotoNext) {
        toView.x = toView.width;
        [UIView animateWithDuration:duration animations:^{
            toView.x = 0;
        } completion:^(BOOL finished) {
            if (finished) {
                if (complete) {
                    complete();
                }
            }
        }];
    }else {
        fromView.x = 0;
        [UIView animateWithDuration:duration animations:^{
            fromView.x = fromView.width;
        } completion:^(BOOL finished) {
            if (finished) {
                if (complete) {
                    complete();
                }
            }
        }];
    }
}

//大小
+ (void)gotoNext:(BOOL)gotoNext SizeTransitionFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void (^)())complete
{
    if (gotoNext) {
        toView.transform = CGAffineTransformMakeScale(0.05, 0.05);
        [UIView animateWithDuration:duration animations:^{
            toView.transform = CGAffineTransformIdentity;
        } completion:^(BOOL finished) {
            if (finished) {
                if (complete) {
                    complete();
                }
            }
        }];
    }else {
        [UIView animateWithDuration:duration animations:^{
            fromView.transform = CGAffineTransformMakeScale(0.05, 0.05);
        } completion:^(BOOL finished) {
            if (finished) {
                if (complete) {
                    complete();
                }
            }
        }];
    }
}

//y旋转
+ (void)gotoNext:(BOOL)gotoNext RotateYFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void (^)())complete
{
    if (gotoNext) {
        [UIView transitionFromView:fromView toView:toView duration:duration options:UIViewAnimationOptionTransitionFlipFromRight completion:^(BOOL finished) {
            if (finished) {
                if (complete) {
                    complete();
                }
            }
        }];
    }else {
        [UIView transitionFromView:fromView toView:toView duration:duration options:UIViewAnimationOptionTransitionFlipFromLeft completion:^(BOOL finished) {
            if (finished) {
                if (complete) {
                    complete();
                }
            }
        }];
    }
    
#pragma mark - 有问题！！！
//    UIViewAnimationTransition transition;
//    
//    if (gotoNext) {
//        transition = UIViewAnimationTransitionFlipFromRight;
//    }else {
//        transition = UIViewAnimationTransitionFlipFromLeft;
//    }
//    
//    CGContextRef context = UIGraphicsGetCurrentContext(); //返回当前视图堆栈顶部的图形上下文
//    [UIView beginAnimations:nil context:context];
//    
//    [UIView setAnimationCurve:UIViewAnimationCurveEaseInOut];
//    [UIView setAnimationDuration:duration];
//    [fromView.window exchangeSubviewAtIndex:0 withSubviewAtIndex:1];
//    [UIView setAnimationTransition:transition forView:fromView.window cache:NO];
//    
//    [UIView commitAnimations];
}

//x旋转
+ (void)gotoNext:(BOOL)gotoNext RotateXFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void (^)())complete
{
    if (gotoNext) {
        [UIView transitionFromView:fromView toView:toView duration:duration options:UIViewAnimationOptionTransitionFlipFromTop completion:^(BOOL finished) {
            if (finished) {
                if (complete) {
                    complete();
                }
            }
        }];
    }else {
        [UIView transitionFromView:fromView toView:toView duration:duration options:UIViewAnimationOptionTransitionFlipFromBottom completion:^(BOOL finished) {
            if (finished) {
                if (complete) {
                    complete();
                }
            }
        }];
    }
}

//翻页
+ (void)gotoNext:(BOOL)gotoNext CureFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void (^)())complete
{
    if (gotoNext) {
        [UIView transitionFromView:fromView toView:toView duration:duration options:UIViewAnimationOptionTransitionCurlUp completion:^(BOOL finished) {
            if (finished) {
                if (complete) {
                    complete();
                }
            }
        }];
    }else {
        [UIView transitionFromView:fromView toView:toView duration:duration options:UIViewAnimationOptionTransitionCurlDown completion:^(BOOL finished) {
            if (finished) {
                if (complete) {
                    complete();
                }
            }
        }];
    }
}

//渐变
+ (void)gotoNext:(BOOL)gotoNext TransitionByAlphaFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void (^)())complete
{
    fromView.alpha = 1.;
    toView.alpha = 0.;
    [UIView animateWithDuration:duration animations:^{
        fromView.alpha = 0.;
        toView.alpha = 1.;
    } completion:^(BOOL finished) {
        if (finished) {
            if (complete) {
                complete();
            }
        }
    }];
}

+ (void)gotoNext:(BOOL)gotoNext TransitionBySysAlphaFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void (^)())complete
{
    [UIView transitionFromView:fromView toView:toView duration:duration options:UIViewAnimationOptionTransitionCrossDissolve completion:^(BOOL finished) {
        if (finished) {
            if (complete) {
                complete();
            }
        }
    }];
}

//遮罩
+ (void)gotoNext:(BOOL)gotoNext transitionByMaskShapeFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void (^)())complete
{
    UIBezierPath *fromPath;
    UIBezierPath *toPath;
    __block CAShapeLayer *maskLayer = [CAShapeLayer layer];
    UIView *animationView;
    CGFloat radius = 0;
    if (gotoNext) {
        fromPath = [UIBezierPath bezierPathWithArcCenter:fromView.maskCenter radius:1 startAngle:0 endAngle:M_PI * 2 clockwise:NO];
        CGFloat w = toView.maskCenter.x > toView.bounds.size.width/2 ? toView.maskCenter.x : (toView.bounds.size.width - toView.maskCenter.x);
        CGFloat h = toView.maskCenter.y > toView.bounds.size.height/2 ? toView.maskCenter.y : (toView.bounds.size.height - toView.maskCenter.y);
        radius = sqrtf(powf(w, 2) + powf(h, 2));
        toPath = [UIBezierPath bezierPathWithArcCenter:toView.maskCenter radius:radius startAngle:0 endAngle:M_PI * 2 clockwise:NO];
        animationView = toView;
    }else {
        toPath = [UIBezierPath bezierPathWithArcCenter:toView.maskCenter radius:1 startAngle:0 endAngle:M_PI * 2 clockwise:NO];
        
        CGFloat w = fromView.maskCenter.x > fromView.bounds.size.width/2 ? fromView.maskCenter.x : (fromView.bounds.size.width - fromView.maskCenter.x);
        CGFloat h = fromView.maskCenter.y > fromView.bounds.size.height/2 ? fromView.maskCenter.y : (fromView.bounds.size.height - fromView.maskCenter.y);
        radius = sqrtf(powf(w, 2) + powf(h, 2));
        fromPath = [UIBezierPath bezierPathWithArcCenter:fromView.maskCenter radius:radius startAngle:0 endAngle:M_PI * 2 clockwise:NO];
        animationView = fromView;
    }

    maskLayer.path = fromPath.CGPath;
    animationView.layer.mask = maskLayer;
    
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"path"];
    animation.fromValue = (id)fromPath.CGPath;
    animation.toValue = (id)toPath.CGPath;
    animation.fillMode = kCAFillModeForwards;
    animation.removedOnCompletion = NO;
    animation.duration = duration;
    animation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
    
    [animationView.layer.mask addAnimation:animation forKey:nil];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(duration * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        if (complete) {
            complete();
        }
        [animationView.layer.mask removeFromSuperlayer];
        animationView.layer.mask = nil;
    });
}

+ (void)gotoNext:(BOOL)gotoNext sizeToSizeFromView:(UIView *)fromView toView:(UIView *)toView inDuration:(NSTimeInterval)duration andComplete:(void (^)())complete
{
	if (gotoNext) {
		UIColor *color = toView.backgroundColor;
		toView.transform = CGAffineTransformMakeScale(toView.maskSize.width / toView.width, toView.maskSize.height / toView.height);
		toView.layer.position = toView.maskCenter;
		toView.backgroundColor = [UIColor clearColor];
		[UIView animateWithDuration:duration animations:^{
			toView.transform = CGAffineTransformIdentity;
			toView.layer.position = CGPointMake(kScreenWidth/2, kScreenHeight/2);
			toView.backgroundColor = color;
		} completion:^(BOOL finished) {
			if (finished) {
				if (complete) {
					complete();
				}
			}
		}];
	}else {
		[UIView animateWithDuration:duration animations:^{
			fromView.transform = CGAffineTransformMakeScale(fromView.maskSize.width / fromView.width, fromView.maskSize.height / fromView.height);
			fromView.layer.position = fromView.maskCenter;
			fromView.backgroundColor = [UIColor clearColor];
		} completion:^(BOOL finished) {
			if (finished) {
				if (complete) {
					complete();
				}
			}
		}];
	}
}

static const char MASKCENTER = '\0';
- (void)setMaskCenter:(CGPoint)maskCenter
{
    [self willChangeValueForKey:@"maskCenter"];
    NSValue *value = [NSValue valueWithCGPoint:maskCenter];
    objc_setAssociatedObject(self, &MASKCENTER, value, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    [self didChangeValueForKey:@"maskCenter"];
}

- (CGPoint)maskCenter
{
    NSValue *value = objc_getAssociatedObject(self, &MASKCENTER);
    if (value == nil) {
        value = [NSValue valueWithCGPoint:self.center];
    }
    return [value CGPointValue];
}

- (void)setMaskSize:(CGSize)maskSize
{
	[self willChangeValueForKey:@"maskSize"];
	NSValue *value = [NSValue valueWithCGSize:maskSize];
	objc_setAssociatedObject(self, _cmd, value, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
	[self didChangeValueForKey:@"maskSize"];
}

- (CGSize)maskSize
{
	NSValue *value = objc_getAssociatedObject(self, @selector(setMaskSize:));
	if (value == nil) {
		value = [NSValue valueWithCGSize:CGSizeZero];
	}
	return [value CGSizeValue];
}

@end










