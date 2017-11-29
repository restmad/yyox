//
//  YJAirlinesManager.m
//  yyox
//
//  Created by ddn on 2017/4/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJAirlinesManager.h"

#import <KF5SDK/KF5SDK.h>
#import "KFUserManager.h"
#import "KF5SDKChat.h"
#import "YJUserModel.h"

#import "KF5SDKTicket.h"

#import "YJNavViewController.h"
#import "UIBarButtonItem+YJExtension.h"

#import "UIWindow+YJExtension.h"

@implementation YJAirlinesManager

YJSingleton_m(AirlinesManager)

+ (void)setup
{
	YJAirlinesManager *mgr = [self sharedAirlinesManager];
	
	[NSDC addObserver:mgr selector:@selector(showAirlines) name:ShowAirlinesNotification object:nil];
	[NSDC addObserver:mgr selector:@selector(showTickets) name:ShowTicketsNotification object:nil];
}

+ (void)deleteUser:(void (^)(NSDictionary * result, NSError * error))callback
{
	if (![KFConfig shareConfig].hostName || [KFConfig shareConfig].hostName.length == 0) {
		[[KFConfig shareConfig] initializeWithHostName:KF5HOSTNAME appId:KF5APPID];
	}
	KFUser *preUser = [KFUserManager shareUserManager].user;
	if (preUser && preUser.deviceTokens.count > 0) {
		dispatch_group_t group = dispatch_group_create();
		__block NSMutableArray *errors = [NSMutableArray array];
		for (NSInteger i=0; i<preUser.deviceTokens.count; i++) {
			for (NSInteger j=0; j<[preUser.deviceTokens.allValues[i] count]; j++) {
				dispatch_group_enter(group);
				id deviceToken = preUser.deviceTokens.allValues[i][j];
				[KFHttpTool deleteTokenWithParams:@{KF5UserToken: preUser.userToken ?: @"", KF5DeviceToken: deviceToken} completion:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
					dispatch_group_leave(group);
					if (!error) {
					} else {
						[errors addObject:error];
					}
				}];
			}
		}
		
		dispatch_group_notify(group, dispatch_get_main_queue(), ^{
			if (callback) {
//				callback(nil, errors.firstObject);
				callback(nil, nil);
			}
		});
		
	} else {
		if (callback) {
			dispatch_async(dispatch_get_main_queue(), ^{
				callback(nil, nil);
			});
		}
	}
}

- (void)showAirlines
{
	UIViewController *topVc = getCurrentVC(nil);
	if ([topVc isKindOfClass:[KFChatViewController class]]) {
		return;
	}
	if (![KFConfig shareConfig].hostName || [KFConfig shareConfig].hostName.length == 0) {
		[[KFConfig shareConfig] initializeWithHostName:KF5HOSTNAME appId:KF5APPID];
	}
	YJUserModel *currentUser = [YJUserModel allObjects].firstObject;
	NSString *email = nil;
	if (currentUser && !currentUser.isInvalidated) {
		email = currentUser.mail;
	} else {
		return [SVProgressHUD showInfoInView:topVc.view withStatus:@"请登录后使用"];
	}
	[SVProgressHUD showInView:topVc.view];
	//删除之前用户与该设备的deviceToken绑定
	[self.class deleteUser:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
		if (!error) {
			[[KFUserManager shareUserManager]initializeWithEmail:email completion:^(KFUser * _Nullable user, NSError * _Nullable error) {
				//删除当前用户与其他设备的deviceToken绑定
				[self.class deleteUser:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
					if (!error) {
						if (currentUser && !currentUser.isInvalidated) {
							NSString *deviceToken = [[NSUserDefaults standardUserDefaults]objectForKey:@"deviceToken"];
							if (deviceToken.length > 0) {
								NSDictionary *params = @{
														 KF5UserToken:[KFUserManager shareUserManager].user.userToken?:@"",
														 KF5DeviceToken:deviceToken
														 };
								[KFHttpTool saveTokenWithParams:params completion:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
									if (!error) {
										[self pushAirlinesFrom:topVc];
									}
								}];
							} else {
								[self pushAirlinesFrom:topVc];
							}
						} else {
							dispatch_async(dispatch_get_main_queue(), ^{
								[SVProgressHUD showInfoInView:topVc.view withStatus:@"登录信息有误，请退出重新登录"];
							});
						}
					} else {
						dispatch_async(dispatch_get_main_queue(), ^{
							[SVProgressHUD showErrorInView:topVc.view withStatus:@"连接失败，请重试"];
						});
					}
				}];
			}];
		} else {
			dispatch_async(dispatch_get_main_queue(), ^{
				[SVProgressHUD showErrorInView:topVc.view withStatus:@"连接失败，请重试"];
			});
		}
	}];
}

- (void)pushAirlinesFrom:(UIViewController *)topVc
{
	dispatch_async(dispatch_get_main_queue(), ^{
		[SVProgressHUD dismissFromView:topVc.view];
		KFChatViewController *chatVc = [[KFChatViewController alloc] initWithMetadata:@[@{@"name":@"系统",@"value":@"iOS"},@{@"name":@"SDK版本",@"value":[[KFConfig shareConfig] version]}]];
		YJNavViewController *nav = [[YJNavViewController alloc] initWithRootViewController:chatVc];
		chatVc.navigationItem.leftBarButtonItem = [UIBarButtonItem customView:@"nav_back" title:@"返回" withTarget:self action:@selector(pop)];
		if (![topVc isKindOfClass:[KFChatViewController class]]) {
			[topVc presentViewController:nav animated:YES completion:nil];
		}
	});
}

- (void)showTickets
{
	UIViewController *topVc = getCurrentVC(nil);
	if ([topVc isKindOfClass:[KFChatViewController class]]) {
		return;
	}
	if (![KFConfig shareConfig].hostName || [KFConfig shareConfig].hostName.length == 0) {
		[[KFConfig shareConfig] initializeWithHostName:KF5HOSTNAME appId:KF5APPID];
	}
	YJUserModel *currentUser = [YJUserModel allObjects].firstObject;
	NSString *email = nil;
	if (currentUser && !currentUser.isInvalidated) {
		email = currentUser.mail;
	} else {
		return [SVProgressHUD showInfoInView:topVc.view withStatus:@"请登录后使用"];
	}
	[SVProgressHUD showInView:topVc.view];
	[self.class deleteUser:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
		if (!error) {
			[[KFUserManager shareUserManager]initializeWithEmail:email completion:^(KFUser * _Nullable user, NSError * _Nullable error) {
				[self.class deleteUser:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
					if (!error) {
						if (currentUser && !currentUser.isInvalidated) {
							[self pushTicketsFrom:topVc];
						} else {
							[SVProgressHUD showInfoInView:topVc.view withStatus:@"登录信息有误，请退出重新登录"];
						}
					} else {
						dispatch_async(dispatch_get_main_queue(), ^{
							[SVProgressHUD showErrorInView:topVc.view withStatus:@"连接失败，请重试"];
						});
					}
				}];
			}];
		} else {
			dispatch_async(dispatch_get_main_queue(), ^{
				[SVProgressHUD showErrorInView:topVc.view withStatus:@"连接失败，请重试"];
			});
		}
	}];
}

- (void)pushTicketsFrom:(UIViewController *)topVc
{
	dispatch_async(dispatch_get_main_queue(), ^{
		[SVProgressHUD dismissFromView:topVc.view];
		KFTicketListViewController *ticketList = [[KFTicketListViewController alloc]init];
		YJNavViewController *nav = [[YJNavViewController alloc] initWithRootViewController:ticketList];
		ticketList.navigationItem.leftBarButtonItem = [UIBarButtonItem customView:@"nav_back" title:@"返回" withTarget:self action:@selector(pop)];
		if (![topVc isKindOfClass:[KFTicketListViewController class]]) {
			[topVc presentViewController:nav animated:YES completion:nil];
		}
	});
}

- (void)pop
{
	[getCurrentVC(nil) dismissViewControllerAnimated:YES completion:nil];
}

- (void)dealloc
{
	[NSDC removeObserver:self name:ShowAirlinesNotification object:nil];
	[NSDC removeObserver:self name:ShowTicketsNotification object:nil];
}


@end
