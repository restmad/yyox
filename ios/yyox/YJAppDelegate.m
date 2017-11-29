/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

#import "YJAppDelegate.h"

#import "RCTBundleURLProvider.h"
#import "RCTRootView.h"
#import "YJServiceViewController.h"

#import "YJPayManager.h"
#import "UMessage.h"
#ifdef NSFoundationVersionNumber_iOS_9_x_Max
#import <UserNotifications/UserNotifications.h>
#endif
#import <KF5SDK/KF5SDK.h>

#import "YJWaitDoViewController.h"
#import "YJPayResultViewController.h"

#import "YJAirlinesManager.h"
#import "UIWindow+YJExtension.h"
#import "YJShareManager.h"

#if DEBUG
#import <FLEX/FLEX.h>
#endif

@interface YJAppDelegate() <UNUserNotificationCenterDelegate>

@property (strong, nonatomic) UIView *rnView;

@end

@implementation YJAppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
#if DEBUG
	[[FLEXManager sharedManager] setNetworkDebuggingEnabled:YES];
#endif
	
	[YJShareManager initialShare];
	
	NSURL *jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index.ios" fallbackResource:nil];
	_rnView = [[RCTRootView alloc] initWithBundleURL:jsCodeLocation
											  moduleName:@"App"
									   initialProperties:nil
										   launchOptions:launchOptions];
	
	[YJAirlinesManager setup];
	
#ifdef DEBUG
	[KFLogger enable:YES];
#else
	[KFLogger enable:NO];
#endif
	
	[UMessage startWithAppkey:UMESSAGEAPPKEY launchOptions:launchOptions httpsEnable:YES];
	[UMessage registerForRemoteNotifications];
	//iOS10必须加下面这段代码。
	UNUserNotificationCenter *center = [UNUserNotificationCenter currentNotificationCenter];
	center.delegate=self;
	UNAuthorizationOptions types10=UNAuthorizationOptionBadge|UNAuthorizationOptionAlert|UNAuthorizationOptionSound;
	[center requestAuthorizationWithOptions:types10 completionHandler:^(BOOL granted, NSError * _Nullable error) {
		if (granted) {
			//点击允许
		} else {
			//点击不允许
			
		}
	}];
	[UMessage setLogEnabled:YES];
	[UMessage setAutoAlert:NO];
	
	[SVProgressHUD defaultSet];
	
    self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
	if (launchOptions) {
		self.window.rootViewController = [YJEnterControllerManager enterViewController];
	} else {
		self.window.rootViewController = [YJWelcomeViewController new];
	}
    [self.window makeKeyAndVisible];
	
    return YES;
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
	NSString *token = [NSString stringWithFormat:@"%@",deviceToken];
	token = [token stringByReplacingOccurrencesOfString:@" " withString:@""];
	token = [token stringByReplacingOccurrencesOfString:@">" withString:@""];
	token = [token stringByReplacingOccurrencesOfString:@"<" withString:@""];
	[[NSUserDefaults standardUserDefaults] setObject:token forKey:@"deviceToken"];
	[UMessage registerDeviceToken:deviceToken];
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler
{
	//关闭友盟自带的弹出框
	[UMessage setAutoAlert:NO];
	//统计点击数
	[UMessage didReceiveRemoteNotification:userInfo];
	
	if (application.applicationState == UIApplicationStateInactive) {
		[NSDC postNotificationOnMainThreadWithName:ShowAirlinesNotification object:nil userInfo:nil];
	}
	
	completionHandler(UIBackgroundFetchResultNewData);
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
	//关闭友盟自带的弹出框
	[UMessage setAutoAlert:NO];
	//统计点击数
	[UMessage didReceiveRemoteNotification:userInfo];
	
	[NSDC postNotificationOnMainThreadWithName:ShowAirlinesNotification object:nil userInfo:nil];
	
}

//iOS10新增：处理前台收到通知的代理方法，即将展示
-(void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler{
	NSDictionary * userInfo = notification.request.content.userInfo;
	if([notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
		[UMessage setAutoAlert:NO];
		//应用处于前台时的远程推送接受
		//必须加这句代码
		[UMessage didReceiveRemoteNotification:userInfo];
		
	}else{
		//应用处于前台时的本地推送接受
	}
	completionHandler(UNNotificationPresentationOptionSound|UNNotificationPresentationOptionBadge|UNNotificationPresentationOptionAlert);
}

//iOS10新增：处理点击通知的代理方法
-(void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)())completionHandler{
	NSDictionary * userInfo = response.notification.request.content.userInfo;
	if([response.notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
		//远程推送接受
		//必须加这句代码
		[UMessage didReceiveRemoteNotification:userInfo];
		
		[NSDC postNotificationOnMainThreadWithName:ShowAirlinesNotification object:nil userInfo:nil];
		
	}else{
		//本地推送接受
	}
	completionHandler();
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
	// 进入后台是设置用户离线
	[[KFChatManager sharedChatManager]setUserOffline];
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
	if ([url.absoluteString containsString:@"pay"]) {
		[self paybackForUrl:url];
	}
	return YES;
}

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<NSString*, id> *)options
{
	if ([url.absoluteString containsString:@"pay"]) {
		[self paybackForUrl:url];
	}
	return YES;
}

- (void)paybackForUrl:(NSURL *)url
{
	UITabBarController *tabvc = (UITabBarController *)self.window.rootViewController;
	YJPayModel *payModel = [YJPayModel pickup];
	if ([payModel.payTypeComments isEqualToString:@"充值"]) {
		[tabvc setSelectedIndex:tabvc.viewControllers.count-1];
	}
	[YJPayManager applicationOpenUrl:url callback:^(YJRouterResponseModel *result) {
		if (result.code == 0 || result.code == 9000) {
			if ([payModel.payTypeComments isEqualToString:@"充值"]) {
				UIViewController *vc = getCurrentVC(nil);
				if ([vc isKindOfClass:NSClassFromString(@"YJRechargeViewController")]) {
					[SVProgressHUD showSuccessInView:vc.view withStatus:@"充值成功"];
					dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
						[vc.navigationController popViewControllerAnimated:YES];
					});
				}
			} else {
				UINavigationController *nav = tabvc.viewControllers[0];
				if (nav.viewControllers.count == 1) {
					[nav pushViewController:[YJWaitDoViewController new] animated:NO];
				}
				YJPayResultViewController *resultVc = (YJPayResultViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyPayResult];
				resultVc.model = payModel;
				[nav pushViewController:resultVc animated:YES];
			}
		} else {
			[SVProgressHUD showErrorInView:tabvc.selectedViewController.view withStatus:result.msg && result.msg.length > 0 ? result.msg : @"订单支付失败"];
		}
	}];
}

@end
