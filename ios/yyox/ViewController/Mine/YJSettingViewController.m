//
//  YJSettingViewController.m
//  yyox
//
//  Created by ddn on 2017/1/13.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJSettingViewController.h"
#import "YJEditUserViewController.h"
#import "YJResetPasswordViewController.h"

#import "UMessage.h"
#import "YJAirlinesManager.h"

@interface YJSettingViewController ()

@property (weak, nonatomic) IBOutlet UISwitch *remoteSwitch;

@end
 
@implementation YJSettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	
	self.title = @"系统设置";
	self.view.backgroundColor = kGlobalBackgroundColor;
	self.tableView.contentInset = UIEdgeInsetsMake(-35, 0, 0, 0);
	
	self.remoteSwitch.on = isRemoteNotify();
	[NSDC addObserver:self selector:@selector(observerApp) name:UIApplicationDidBecomeActiveNotification object:nil];
}


/**
 监听应用从后台回到前台
 */
- (void)observerApp
{
	self.remoteSwitch.on = isRemoteNotify();
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	
	if (indexPath.row > 2) {
		return;
	}
	
	cell.separatorInset = UIEdgeInsetsZero;
	cell.layoutMargins = UIEdgeInsetsZero;
	cell.preservesSuperviewLayoutMargins = NO;
}

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (indexPath.row > 1) {
		return NO;
	}
	return YES;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	
	switch (indexPath.row) {
		case 0:
		{
			YJEditUserViewController *editUserVc = (YJEditUserViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyEditUser];
			[self.navigationController pushViewController:editUserVc animated:YES];
		}
				break;
		case 1:
		{
			YJResetPasswordViewController *resetPasswordVc = (YJResetPasswordViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyResetPassword];
			[self.navigationController pushViewController:resetPasswordVc animated:YES];
		}
			break;
		default:
			break;
	}
}


/**
 切换推送

 @param sender 切换键
 */
- (IBAction)switchNotify:(UISwitch *)sender {
	NSURL *url = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
	if ([[UIApplication sharedApplication] canOpenURL:url]) {
		if (kiOSLater(10)) {
			[[UIApplication sharedApplication] openURL:url options:@{} completionHandler:^(BOOL success) {
				
			}];
		} else {
			[[UIApplication sharedApplication] openURL:url];
		}
	}
}


/**
 登出

 @param sender 按钮
 */
- (IBAction)clickOnLogout:(UIButton *)sender {
	[YJAirlinesManager deleteUser:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
		if (!error) {
			[YJEnterControllerManager updateLogin:NO];
			[self.navigationController popViewControllerAnimated:YES];
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:@"退出登录失败，请重试"];
		}
	}];
}

- (void)dealloc
{
	[NSDC removeObserver:self];
}

@end
