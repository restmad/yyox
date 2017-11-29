//
//  YJEnterControllerManager.m
//  yyox
//
//  Created by ddn on 2016/12/29.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJEnterControllerManager.h"
#import "YJMainTabViewController.h"
#import "YJAppDelegate.h"

#import "CookiesManager.h"

#import "OnboardingViewController.h"
#import "OnboardingContentViewController.h"

@implementation YJEnterControllerManager

+ (BOOL)isInstalled
{
	NSString *installed = [NSUD objectForKey:@"installed"];
	if (installed) {
		return YES;
	}
	[NSUD setObject:@"installed" forKey:@"installed"];
	return NO;
}

+ (BOOL)isNewVersion
{
	NSString *currentVersion = [UIApplication sharedApplication].appVersion;
	NSString *lastVersion = [NSUD objectForKey:@"appVersion"];
	if (!lastVersion || [currentVersion compare:lastVersion] == NSOrderedDescending) {
		return YES;
	}
	return NO;
}

+ (void)updateVersion
{
	NSString *currentVersion = [UIApplication sharedApplication].appVersion;
	[NSUD setObject:currentVersion forKey:@"appVersion"];
}

+ (BOOL)isLogin
{
	return [CookiesManager setCookie];
}

+ (void)updateLogin:(BOOL)login
{
	if (login) {
		[CookiesManager saveCookieWithTask:nil];
	} else {
		[[RLMRealm defaultRealm] beginWriteTransaction];
		[[RLMRealm defaultRealm] deleteAllObjects];
		[[RLMRealm defaultRealm] commitWriteTransaction];
		[CookiesManager deleteCookie];
		[NSDC postNotificationName:DidLogOutNotification object:nil];
	}
}

+ (UIViewController *)enterViewController
{
//	if ([self isNewVersion]) {
//		return [self newFeatureViewController];
//	}
	return [YJMainTabViewController new];
}

+ (UIViewController *)newFeatureViewController
{
	OnboardingContentViewController *firstPage = [OnboardingContentViewController contentWithTitle:nil body:nil image:[UIImage imageNamed:@"72df657d7774182b6e760d5d6d80c91e.png"] buttonText:nil action:nil];
	OnboardingContentViewController *secondPage = [OnboardingContentViewController contentWithTitle:nil body:nil image:[UIImage imageNamed:@"72df657d7774182b6e760d5d6d80c91e.png"] buttonText:@"开启应用" action:^{
		[YJEnterControllerManager updateVersion];
		[YJEnterControllerManager switchEnterVCFrom:nil animation:NO];
	}];
	firstPage.iconWidth = secondPage.iconWidth = kScreenWidth;
	firstPage.iconHeight = secondPage.iconHeight = kScreenHeight;
	
	OnboardingViewController *onboardVc = [OnboardingViewController onboardWithBackgroundImage:nil contents:@[firstPage, secondPage]];
	return onboardVc;
}

+ (void)switchEnterVCFrom:(UIViewController *)currentVC animation:(BOOL)animate
{
	UIViewController *vc = [self enterViewController];
	YJAppDelegate *app = (YJAppDelegate *)UIAP.delegate;
	app.window.rootViewController = vc;
}

+ (void)switchToMainViewFrom:(UIViewController *)viewController
{
	[viewController.navigationController popViewControllerAnimated:NO];
	
	[NSDC postNotificationName:ChangeTabItemNotification object:nil userInfo:@{ChangeTabItemNotificationKey: @"运单"}];
}

@end
