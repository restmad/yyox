//
//  RNBridgeManager.m
//  Transfer
//
//  Created by ddn on 16/12/19.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "RNBridgeManager.h"
#import "UIWindow+YJExtension.h"
#import "YJPickerView.h"
#import "YJWarehouseModel.h"

@implementation RNBridgeManager

/**
 展示登录弹窗
 */
void showLogin()
{
	UIViewController *topVc = getCurrentVC(nil);
	[UIAlertController showTitle:@"提示" message:@"您还未登录，请先登录" cancel:@"下次" sure:@"登录" byController:topVc callback:^{
		UIViewController *loginVc = [YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyLogin];
		[topVc.navigationController pushViewController:loginVc animated:YES];
	}];
}

RCT_EXPORT_MODULE()

//点击底部选项卡
RCT_EXPORT_METHOD(changeItem:(NSString *)itemTitle)
{
	[NSDC postNotificationOnMainThreadWithName:ChangeTabItemNotification object:nil userInfo:@{ChangeTabItemNotificationKey: itemTitle}];
}

//点击中间按钮
RCT_EXPORT_METHOD(clickOnBtns:(NSString *)btnTitle callback:(RCTResponseSenderBlock)callback)
{
	if ([btnTitle containsString:@"在线客服"]) {
		
		BOOL isLogin = [YJEnterControllerManager isLogin];
		if (!isLogin) {
			showLogin();
		} else {
			[NSDC postNotificationOnMainThreadWithName:ShowAirlinesNotification object:nil userInfo:nil];
		}
		callback(@[@(isLogin)]);
		
	}
	else if ([btnTitle containsString:@"在线工单"]) {
		BOOL isLogin = [YJEnterControllerManager isLogin];
		if (!isLogin) {
			showLogin();
		} else {
			[NSDC postNotificationOnMainThreadWithName:ShowTicketsNotification object:nil userInfo:nil];
		}
		callback(@[@(isLogin)]);
	}
	else if ([btnTitle containsString:@"价格"]) {
		BOOL isLogin = [YJEnterControllerManager isLogin];
		if (!isLogin) {
			showLogin();
		}
		callback(@[@(isLogin)]);
	} else {
		callback(@[@YES]);
	}
}

//展示底部弹出视图
RCT_EXPORT_METHOD(showPickViewWithCallback:(NSArray *)datas callback:(RCTResponseSenderBlock)callback)
{
	if (!datas || datas.count == 0) {
//		callback(@[]);
		return;
	}
	dispatch_async(dispatch_get_main_queue(), ^{
		YJPickerView *pickView = [YJPickerView new];
		pickView.height = 180.5;
		pickView.width = kScreenWidth;
		pickView.componentWidth = pickView.width;
		pickView.left = 0;
		[pickView initialData:@[[datas valueForKeyPath:@"name"]]];
		@weakify(pickView);
		[pickView setCallback:^(NSArray *values, BOOL cancel) {
			@strongify(pickView)
			if (!cancel) {
				for (YJPickerViewItem *item in values) {
					callback(@[@(item.row)]);
				}
			}
			[pickView dismiss];
		}];
		[pickView show];
	});
}

RCT_EXPORT_METHOD(showInfo:(NSString *)info)
{
	dispatch_async(dispatch_get_main_queue(), ^{
		[SVProgressHUD showInfoWithStatus:info];
	});
}

RCT_EXPORT_METHOD(showLoading:(NSString *)info)
{
	dispatch_async(dispatch_get_main_queue(), ^{
		[SVProgressHUD showWithStatus:info];
	});
}

RCT_EXPORT_METHOD(showSuccess:(NSString *)info)
{
	dispatch_async(dispatch_get_main_queue(), ^{
		[SVProgressHUD showSuccessWithStatus:info];
	});
}

RCT_EXPORT_METHOD(showError:(NSString *)info)
{
	dispatch_async(dispatch_get_main_queue(), ^{
		[SVProgressHUD showErrorWithStatus:info];
	});
}

RCT_EXPORT_METHOD(hideHUD)
{
	dispatch_async(dispatch_get_main_queue(), ^{
		[SVProgressHUD dismiss];
	});
}

- (NSDictionary<NSString *,id> *)constantsToExport
{
	return @{@"key": @"value"};
}



@end
