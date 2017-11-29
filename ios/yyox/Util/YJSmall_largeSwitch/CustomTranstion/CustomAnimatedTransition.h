//
//  CustomAnimatedTransition.h
//  CustomModalController
//
//  Created by zhangyongjun on 16/2/11.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "TransitionHolder.h"

@interface CustomAnimatedTransition : NSObject <UIViewControllerAnimatedTransitioning>

@property (strong, nonatomic) TransitionHolder *holder;

@end
