//
//  TransitionHolder.h
//  CustomModalController
//
//  Created by zhangyongjun on 16/4/12.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, TransAnimationType) {
    TransAnimationTypeNone = 0,
//以下四种可混用（present和dismiss可以不一样）
    TransAnimationTypeRotateY,
    TransAnimationTypeRotateX,
    TransAnimationTypeCure,
    TransAnimationTypeSystemAlpha,
    
//以下五种可混用（present和dismiss可以不一样）
    TransAnimationTypeAlpha,
    TransAnimationTypeSameToNav,
    TransAnimationTypeSize,
    TransAnimationTypeMaskCircle,
    TransAnimationTypeCustom,
	
	TransAnimationTypeSizeToSize
};


@interface TransitionHolder : NSObject

@property (assign, nonatomic) BOOL presented;
@property (assign, nonatomic) CGFloat duration;
@property (assign, nonatomic) TransAnimationType type;
@property (assign, nonatomic) CGPoint maskCenter;

@property (copy, nonatomic) void(^animation)(BOOL gotoNext, UIView *fromView, UIView *toView);

+ (BOOL)changeType:(TransAnimationType)type toType:(TransAnimationType)toType;

@end
