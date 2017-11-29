//
//  UIAlertController+YJExtension.h
//  yyox
//
//  Created by ddn on 2017/1/11.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIAlertController (YJExtension)

+ (void)showByController:(UIViewController *)vc callback:(void(^)())callback;

+ (void)showTitle:(NSString *)title message:(NSString *)message byController:(UIViewController *)vc callback:(void(^)())callback;

+ (void)showTitle:(NSString *)title message:(NSString *)message cancel:(NSString *)cancel sure:(NSString *)sure byController:(UIViewController *)vc callback:(void(^)())callback;

@end
