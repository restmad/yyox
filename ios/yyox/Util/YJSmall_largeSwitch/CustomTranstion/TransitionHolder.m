//
//  TransitionHolder.m
//  CustomModalController
//
//  Created by zhangyongjun on 16/4/12.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "TransitionHolder.h"

@implementation TransitionHolder

- (void)dealloc
{
    _animation = nil;
}

+ (NSArray *)sysTypes
{
    return @[@(TransAnimationTypeRotateY), @(TransAnimationTypeRotateX), @(TransAnimationTypeCure), @(TransAnimationTypeSystemAlpha)];
}

+ (NSArray *)customTypes
{
    return @[@(TransAnimationTypeAlpha), @(TransAnimationTypeSameToNav), @(TransAnimationTypeSize), @(TransAnimationTypeMaskCircle), @(TransAnimationTypeCustom), @(TransAnimationTypeSizeToSize)];
}

+ (BOOL)changeType:(TransAnimationType)type toType:(TransAnimationType)toType
{
    NSArray *sysArr = [self sysTypes];
    NSArray *customArr = [self customTypes];
    if (([sysArr containsObject:@(type)] && [sysArr containsObject:@(toType)]) || ([customArr containsObject:@(type)] && [customArr containsObject:@(toType)])) {
        return YES;
    }else {
        return NO;
    }
}

@end
