//
//  UIViewController+YJAddProperty.h
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIViewController (YJAddProperty)

@property (copy, nonatomic) NSString *storyboardIdentify;

@property (assign, nonatomic) BOOL autoRefresh;

@property (assign, nonatomic) BOOL cancelTasksWhenViewDisappeared;

- (void)startLoading;

- (void)stopLoading;

@end
