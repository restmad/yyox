//
//  SVProgressHUD+YJExtension.h
//  yyox
//
//  Created by ddn on 2017/1/13.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <SVProgressHUD/SVProgressHUD.h>

@interface SVProgressHUD (YJExtension)

+ (void)defaultSet;

+ (void)showInView:(UIView *)view;

+ (void)showInView:(UIView *)view withStatus:(NSString *)status;

+ (void)showInView:(UIView *)view withStatus:(NSString *)status userEnable:(BOOL)userEnable;

+ (void)showSuccessInView:(UIView *)view withStatus:(NSString *)status;

+ (void)showErrorInView:(UIView *)view withStatus:(NSString *)status;

+ (void)showInfoInView:(UIView *)view withStatus:(NSString *)status;

+ (void)showMessageInView:(UIView *)view withStatus:(NSString *)status;

+ (void)dismissFromView:(UIView *)view;

- (void)dismiss;
- (void)showProgress:(float)progress status:(NSString*)status;
- (void)showImage:(UIImage*)image status:(NSString*)status duration:(NSTimeInterval)duration;

@end
