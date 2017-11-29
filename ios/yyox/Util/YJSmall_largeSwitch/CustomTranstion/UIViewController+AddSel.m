//
//  UIViewController+AddSel.m
//  CustomModalController
//
//  Created by zhangyongjun on 16/2/12.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "UIViewController+AddSel.h"
#import <objc/runtime.h>

@implementation UIViewController (AddSel) 

+ (void)load
{
    Method method = class_getInstanceMethod([self class], NSSelectorFromString(@"dealloc"));
    Method my_method = class_getInstanceMethod([self class], @selector(myDealloc));
    method_exchangeImplementations(my_method, method);
}

- (void)myDealloc
{
    
    if (self.stackDelegate) {
        self.stackDelegate = nil;
    }
    
    if (self.modalDelegate) {
        self.modalDelegate = nil;
    }
    
    [self myDealloc];
}

- (void)customStackWithAnimationType:(TransAnimationType)type andDuration:(NSTimeInterval)duration animationIfNeed:(void(^)(BOOL gotoNext, UIView *fromView, UIView *toView))animation {
    if (![self isKindOfClass:[UINavigationController class]]) NSAssert([self isKindOfClass:[UINavigationController class]], @"controller must be navClass");
    
    if (type == TransAnimationTypeNone) return;
    
    self.stackDelegate = nil;
    
    CustomTransitioningDelegate *customDelegate = [CustomTransitioningDelegate new];
    customDelegate.duration = duration;
    if (customDelegate.type == TransAnimationTypeNone || [TransitionHolder changeType:customDelegate.type toType:type]) {
        customDelegate.type = type;
    }
    customDelegate.animationIfCustom = animation;
    UINavigationController *nav = (UINavigationController *)self;
    nav.stackDelegate = customDelegate;
    nav.delegate = customDelegate;
    
    //全局手势pop
    id target = nav.interactivePopGestureRecognizer.delegate;
    SEL handleNavigationTransition = NSSelectorFromString(@"handleNavigationTransition:");
    UIPanGestureRecognizer *pop = [[UIPanGestureRecognizer alloc]initWithTarget:target action:handleNavigationTransition];
    pop.delegate = self;
    [nav.view addGestureRecognizer:pop];
    nav.interactivePopGestureRecognizer.enabled = NO;

}

- (void)customModalWithAnimationType:(TransAnimationType)type andDuration:(NSTimeInterval)duration animationIfNeed:(void(^)(BOOL gotoNext, UIView *fromView, UIView *toView))animation {
    
    if (type == TransAnimationTypeNone) return;
    
    self.modalDelegate = nil;
    
    self.modalPresentationStyle = UIModalPresentationCustom;
    CustomTransitioningDelegate *customDelegate = [CustomTransitioningDelegate new];
    customDelegate.duration = duration;
    if (customDelegate.type == TransAnimationTypeNone || [TransitionHolder changeType:customDelegate.type toType:type]) {
        customDelegate.type = type;
    }
    customDelegate.animationIfCustom = animation;
    self.modalDelegate = customDelegate;
    self.transitioningDelegate = customDelegate;
}

- (instancetype)initWithAnimationType:(TransAnimationType)type andDuration:(NSTimeInterval)duration animationIfNeed:(void(^)(BOOL gotoNext, UIView *fromView, UIView *toView))animation {
    self = [self init];
    if (self) {
        [self customModalWithAnimationType:type andDuration:duration animationIfNeed:animation];
    }
    return self;
}

- (instancetype)initWithRootVC:(UIViewController *)rootVC animationType:(TransAnimationType)type andDuration:(NSTimeInterval)duration animationIfNeed:(void(^)(BOOL gotoNext, UIView *fromView, UIView *toView))animation {
    UINavigationController *nav = (UINavigationController *)self;
    nav = [nav initWithRootViewController:rootVC];
    if (nav) {
        [nav customStackWithAnimationType:type andDuration:duration animationIfNeed:animation];
    }
    return nav;
}

- (void)setModalDelegate:(CustomTransitioningDelegate *)modalDelegate
{
    [self willChangeValueForKey:@"modalDelegate"];
    objc_setAssociatedObject(self, _cmd, modalDelegate, OBJC_ASSOCIATION_RETAIN);
    [self didChangeValueForKey:@"modalDelegate"];
}

- (CustomTransitioningDelegate *)modalDelegate
{
    return objc_getAssociatedObject(self, @selector(setModalDelegate:));
}

- (void)setStackDelegate:(CustomTransitioningDelegate *)stackDelegate
{
    [self willChangeValueForKey:@"stackDelegate"];
    objc_setAssociatedObject(self, _cmd, stackDelegate, OBJC_ASSOCIATION_RETAIN);
    [self didChangeValueForKey:@"stackDelegate"];
}

- (CustomTransitioningDelegate *)stackDelegate
{
    return objc_getAssociatedObject(self, @selector(setStackDelegate:));
}

@end
