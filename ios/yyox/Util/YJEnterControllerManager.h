//
//  YJEnterControllerManager.h
//  yyox
//
//  Created by ddn on 2016/12/29.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJEnterControllerManager : NSObject

/**
 是否安装过

 @return bool
 */
+ (BOOL)isInstalled;

/**
 是否为新版本

 @return bool
 */
+ (BOOL)isNewVersion;

/**
 获取进入视图控制器

 @return vc
 */
+ (UIViewController *)enterViewController;

/**
 切换根控制器

 @param currentVC 当前vc
 @param animate 是否需要动画
 */
+ (void)switchEnterVCFrom:(UIViewController *)currentVC animation:(BOOL)animate;

/**
 更新版本信息
 */
+ (void)updateVersion;

/**
 刷新登录状态

 @param login 是否登录
 */
+ (void)updateLogin:(BOOL)login;

/**
 是否已经等录

 @return bool
 */
+ (BOOL)isLogin;

/**
 切换的主视图

 @param viewController 当前vc
 */
+ (void)switchToMainViewFrom:(UIViewController *)viewController;

//+ (void)setDefaultRealmForUser:(NSString *)userID;

@end
