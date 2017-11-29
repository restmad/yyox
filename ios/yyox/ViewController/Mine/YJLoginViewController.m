//
//  YJLoginViewController.m
//  yyox
//
//  Created by ddn on 2017/1/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJLoginViewController.h"
#import "YJRegisterViewController.h"
#import "YJTextField.h"
#import "YJUserModel.h"

#import "YJAirlinesManager.h"

@interface YJLoginViewController ()

@property (weak, nonatomic) IBOutlet YJTextField *phoneTextField;
@property (weak, nonatomic) IBOutlet YJTextField *passwordTextField;

@property (weak, nonatomic) IBOutlet UILabel *wrongLabel;

@end

@implementation YJLoginViewController

- (void)decorate
{
	self.tableView.contentInset = UIEdgeInsetsMake(-35, 0, 0, 0);
	
	self.wrongLabel.hidden = YES;
}

- (void)viewDidLoad {
	[super viewDidLoad];
	
	[self decorate];
	self.tableView.backgroundColor = kGlobalBackgroundColor;
	
}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	if (self.textFields.count == 0) {
		
		self.textFields = @[self.phoneTextField, self.passwordTextField];
		[self.textFields enumerateObjectsUsingBlock:^(UITextField * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
			obj.delegate = self;
		}];
		
		UIBarButtonItem *registerItem = [[UIBarButtonItem alloc] initWithTitle:@"注册" style:UIBarButtonItemStyleDone target:self action:@selector(clickOnRegister)];
		self.navigationItem.rightBarButtonItem = registerItem;
	}
}


/**
 点击注册
 */
- (void)clickOnRegister
{
	YJRegisterViewController *registerVc = (YJRegisterViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyRegister];
	[self.navigationController pushViewController:registerVc animated:YES];
}


/**
 点击登录

 @param sender 登录按钮
 */
- (IBAction)clickOnLogin:(id)sender {
	[SVProgressHUD showInView:self.view];
	
	if (![YJRegexte isTelNumber:self.phoneTextField.text] && ![YJRegexte isEmail:self.phoneTextField.text]) {
		[SVProgressHUD showErrorInView:self.view withStatus:@"请输入正确的手机号或邮箱"];
		return;
	}
	if (self.passwordTextField.text.length == 0) {
		[SVProgressHUD showErrorInView:self.view withStatus:@"请输入密码"];
		return;
	}
	
	[YJAirlinesManager deleteUser:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
		if (!error) {
			NSMutableDictionary *params = @{}.mutableCopy;
			params[@"username"] = self.phoneTextField.text;
			params[@"password"] = self.passwordTextField.text;
			
			NSURLSessionDataTask *task = [YJRouter commonPostModel:LoginApi params:params modelClass:[YJUserModel class] callback:^(YJRouterResponseModel *result) {
				if (result.code == 1) {
					self.wrongLabel.hidden = NO;
				}
				if (result.code != 0) {
					[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
				} else {
					YJUserModel *user = result.data;
					if (user) {
						[[RLMRealm defaultRealm] beginWriteTransaction];
						[[RLMRealm defaultRealm] addOrUpdateObject:user];
						[[RLMRealm defaultRealm] commitWriteTransaction];
					}
					[SVProgressHUD showSuccessInView:self.view withStatus:result.msg];
					dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
						[YJEnterControllerManager updateLogin:YES];
						//				[YJEnterControllerManager switchEnterVCFrom:self animation:NO];
						if (![self.navigationController.viewControllers[0] isKindOfClass:NSClassFromString(@"YJMineViewController")]) {
							[self.navigationController.viewControllers[0] setValue:@YES forKey:@"autoRefresh"];
							self.navigationController.tabBarController.selectedIndex = 0;
						}
						[self.navigationController popToRootViewControllerAnimated:YES];
					});
				}
			}];
			[self weakHoldTask:task];
		}else {
			[SVProgressHUD showErrorInView:self.view withStatus:@"连接失败，请重试"];
		}
	}];
}


/**
 点击忘记密码

 @param sender 按钮
 */
- (IBAction)clickOnForgetPassword:(id)sender {
	YJRegisterViewController *reRegisterVc = (YJRegisterViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyReRegister];
	[self.navigationController pushViewController:reRegisterVc animated:YES];
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	
	if (indexPath.row > 1) {
		return;
	}
	
	cell.separatorInset = UIEdgeInsetsZero;
	cell.layoutMargins = UIEdgeInsetsZero;
	cell.preservesSuperviewLayoutMargins = NO;
}

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	return NO;
}


/**
 检查输入框

 @param textField 待检测输入框
 */
- (void)checkTextField:(UITextField *)textField
{
	self.wrongLabel.hidden = YES;
	BOOL error = YES;
	if ([textField isEqual:self.phoneTextField] && ([YJRegexte isTelNumber:self.phoneTextField.text] || [YJRegexte isEmail:self.phoneTextField.text])) {
		error = NO;
	}
	else if ([textField isEqual:self.passwordTextField] && [YJRegexte isLoginPassword:self.passwordTextField.text]) {
		error = NO;
	}
	if (error) {
		//end edit
		if (!self.currentTextField) {
			[(YJTextField *)textField setErrored:YES];
		}
		
		if ([(YJTextField *)textField errored]) {
			[(YJTextField *)textField showErrorImage];
		}
	} else {
		//end edit
		if (!self.currentTextField) {
			[(YJTextField *)textField setErrored:NO];
		}
		
		[(YJTextField *)textField hideErrorImage];
	}
}

@end
