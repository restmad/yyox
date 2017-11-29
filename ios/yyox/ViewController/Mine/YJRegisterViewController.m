//
//  YJRegisterViewController.m
//  yyox
//
//  Created by ddn on 2017/1/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJRegisterViewController.h"
#import "YJTimerManager.h"
#import "YJAirlinesManager.h"

#import "YJBaseWevViewController.h"
#import "YJTextField.h"
#import "YJUserModel.h"

@interface YJRegisterViewController () <YJTimerManagerDelegate>

@property (weak, nonatomic) IBOutlet YJTextField *authTextField;
@property (weak, nonatomic) IBOutlet YJTextField *phoneTextField;
@property (weak, nonatomic) IBOutlet YJTextField *emailTextField;
@property (weak, nonatomic) IBOutlet YJTextField *passwordTextField;
@property (weak, nonatomic) IBOutlet UIButton *registerButton;
@property (weak, nonatomic) IBOutlet UIButton *authButton;

@property (assign, nonatomic) BOOL reDo;

@end

@implementation YJRegisterViewController

- (void)decorate
{
	self.tableView.contentInset = UIEdgeInsetsMake(-35, 0, 0, 0);
	
	[[YJTimerManager sharedTimerButton] addDelegate:self forKey:@(self.reDo).stringValue];
	
}

- (void)viewDidLoad {
	
	[super viewDidLoad];
	self.tableView.backgroundColor = kGlobalBackgroundColor;
}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	if (self.textFields.count == 0) {
		self.reDo = self.storyboardIdentify == [YJStoryboard storyboardIdentifyFor:YJStoryboardIdentifyReRegister];
		
		if (self.reDo) {
			self.textFields = @[self.phoneTextField, self.passwordTextField, self.authTextField];
			self.passwordTextField.placeholder = @"填写6～12位新密码";
		} else {
			self.textFields = @[self.phoneTextField, self.emailTextField, self.passwordTextField, self.authTextField];
		}
		
		[self.textFields enumerateObjectsUsingBlock:^(UITextField * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
			obj.delegate = self;
		}];
		
		[self decorate];
	}
}


/**
 检查输入框

 @param textField 待检测输入框
 */
- (void)checkTextField:(UITextField *)textField
{
	BOOL error = YES;
	if ([textField isEqual:self.phoneTextField] && [YJRegexte isTelNumber:self.phoneTextField.text]) {
		error = NO;
	}
	else if ([textField isEqual:self.emailTextField] && [YJRegexte isEmail:self.emailTextField.text]) {
		error = NO;
	}
	else if ([textField isEqual:self.passwordTextField] && [YJRegexte isPassword:self.passwordTextField.text]) {
		error = NO;
	}
	else if ([textField isEqual:self.authTextField] && [YJRegexte isAuthCode:self.authTextField.text]) {
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


/**
 获取验证吗

 @param sender 按钮
 */
- (IBAction)clickOnAuth:(id)sender {
	
	[SVProgressHUD showInView:self.view];
	
	if (![YJRegexte isTelNumber:self.phoneTextField.text]) {
		[SVProgressHUD showErrorInView:self.view withStatus:@"请输入正确的手机号"];
		return;
	}
	
	NSDictionary *parmas = @{@"mobile": self.phoneTextField.text};
	
	[YJRouter commonGetModel:AuthCodeApi params:parmas modelClass:nil callback:^(YJRouterResponseModel *result) {
		if (result.code != 0) {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		} else {
			[SVProgressHUD showSuccessInView:self.view withStatus:[NSString stringWithFormat:@"%@%@", result.msg, [result.data isKindOfClass:[NSString class]] ? result.data : @""]];
			[[YJTimerManager sharedTimerButton] addDelegate:self forKey:@(self.reDo).stringValue];
			[self beginTimer];
		}
	}];
}

- (void)beginTimer
{
	YJTimerManager *timerBtnMgr = [YJTimerManager sharedTimerButton];
	[timerBtnMgr startForKey:@(self.reDo).stringValue];
}


/**
 展示条款

 @param sender 按钮
 */
- (IBAction)clickOnClause:(id)sender {
	YJBaseWevViewController *webvc = [YJBaseWevViewController new];
	webvc.title = @"使用条件及会员条款";
	webvc.startUrl = [NSURL yj_URLWithString:ClauseUrl];
	[self.navigationController pushViewController:webvc animated:YES];
}


/**
 注册／找回密码

 @param sender 按钮
 */
- (IBAction)clickOnRegister:(id)sender {
	
	[SVProgressHUD showInView:self.view];
	
	NSString *errorMsg = nil;
	if (![YJRegexte isTelNumber:self.phoneTextField.text]) {
		errorMsg = @"请输入正确的手机号";
	}
	else if (!self.reDo && ![YJRegexte isEmail:self.emailTextField.text]) {
		errorMsg = @"邮箱格式不正确";
	}
	else if (![YJRegexte isPassword:self.passwordTextField.text]) {
		errorMsg = @"请输入6-12位（字母，数字）组合";
	}
	else if (![YJRegexte isAuthCode:self.authTextField.text]) {
		errorMsg = @"验证码格式错误";
	}
	
	if (errorMsg) {
		[SVProgressHUD showErrorInView:self.view withStatus:errorMsg];
		return;
	}
	
	NSMutableDictionary *params = @{}.mutableCopy;
	params[@"mobile"] = self.phoneTextField.text;
	params[@"securityCode"] = self.authTextField.text;
	params[@"password"] = self.passwordTextField.text;
	if (!self.reDo) {
		params[@"mobilePass"] = @"false";
		params[@"agreementConfirmed"] = @"true";
		params[@"mail"] = self.emailTextField.text;
	}
	
	NSString *api = self.reDo ? ReRegisterApi : RegisterApi;
	
	[YJAirlinesManager deleteUser:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
		if (!error) {
			
			NSURLSessionDataTask *task = [YJRouter commonPostModel:api params:params modelClass:[YJUserModel class] callback:^(YJRouterResponseModel *result) {
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

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	
	if (indexPath.row > 4 - self.reDo) {
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
 监听时间变化

 @param time 时间
 */
- (void)timerHasChanged:(NSInteger)time
{
	self.authButton.enabled = time == 0;
	if (time != 0) {
		[self.authButton setTitle:[NSString stringWithFormat:@"%02zd秒", time] forState:UIControlStateDisabled];
	} else {
		[self.authButton setTitle:@"重新发送" forState:UIControlStateNormal];
	}
}

- (void)dealloc
{
	
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
