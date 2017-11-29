//
//  UIAlertController+YJExtension.m
//  yyox
//
//  Created by ddn on 2017/1/11.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "UIAlertController+YJExtension.h"

@implementation UIAlertController (YJExtension)

+ (void)showByController:(UIViewController *)vc callback:(void (^)())callback
{
	if (vc.presentedViewController && [vc.presentedViewController isKindOfClass:[UIAlertController class]]) {
		return;
	}
	
	UIAlertController *alertVc = [UIAlertController alertControllerWithTitle:@"确定要删除吗？" message:nil preferredStyle:UIAlertControllerStyleAlert];
	
	UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
		[alertVc dismissViewControllerAnimated:YES completion:nil];
	}];
	UIAlertAction *sure = [UIAlertAction actionWithTitle:@"删除" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
		if (callback) {
			callback();
		}
	}];
	
	[alertVc addAction:cancel];
	[alertVc addAction:sure];
	
	dispatch_async(dispatch_get_main_queue(), ^{
		[vc presentViewController:alertVc animated:YES completion:nil];
	});
}

+ (void)showTitle:(NSString *)title message:(NSString *)message byController:(UIViewController *)vc callback:(void (^)())callback
{
	[self showTitle:title message:message cancel:@"取消" sure:@"确定" byController:vc callback:callback];
}

+ (void)showTitle:(NSString *)title message:(NSString *)message cancel:(NSString *)cancel sure:(NSString *)sure byController:(UIViewController *)vc callback:(void (^)())callback
{
	if (vc.presentedViewController && [vc.presentedViewController isKindOfClass:[UIAlertController class]]) {
		return;
	}
	UIAlertController *alertVc = [UIAlertController alertControllerWithTitle:title message:message preferredStyle:UIAlertControllerStyleAlert];
	
	UIAlertAction *cancelbtn = [UIAlertAction actionWithTitle:cancel style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
		[alertVc dismissViewControllerAnimated:YES completion:nil];
	}];
	UIAlertAction *surebtn = [UIAlertAction actionWithTitle:sure style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
		if (callback) {
			callback();
		}
	}];
	
	[alertVc addAction:cancelbtn];
	[alertVc addAction:surebtn];
	
	dispatch_async(dispatch_get_main_queue(), ^{
		[vc presentViewController:alertVc animated:YES completion:nil];
	});
}

@end
