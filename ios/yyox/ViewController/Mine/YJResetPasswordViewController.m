//
//  YJResetPasswordViewController.m
//  yyox
//
//  Created by ddn on 2017/1/13.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJResetPasswordViewController.h"
#import "YJTextField.h"
#import "YJUserModel.h"

@interface YJResetPasswordViewController ()

@property (weak, nonatomic) IBOutlet YJTextField *originalTextField;
@property (weak, nonatomic) IBOutlet YJTextField *nowTextField;
@property (weak, nonatomic) IBOutlet YJTextField *ackTextField;

@end

@implementation YJResetPasswordViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	
	self.title = @"修改密码";
	self.view.backgroundColor = kGlobalBackgroundColor;
	self.tableView.contentInset = UIEdgeInsetsMake(-35, 0, 0, 0);
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
	return NO;
}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	if (self.textFields.count == 0) {
		
		self.textFields = @[self.originalTextField, self.nowTextField, self.ackTextField];
		
		[self.textFields enumerateObjectsUsingBlock:^(UITextField * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
			obj.delegate = self;
		}];
	}
}

- (void)checkTextField:(UITextField *)textField
{
	BOOL error = YES;
	if ([textField isEqual:self.originalTextField] && self.originalTextField.text && self.originalTextField.text.length > 0) {
		error = NO;
	}
	else if ([textField isEqual:self.nowTextField] && [YJRegexte isPassword:self.nowTextField.text]) {
		error = NO;
	}
	else if ([textField isEqual:self.ackTextField] && [YJRegexte isPassword:self.ackTextField.text]) {
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
 充值密码

 @param sender 按钮
 */
- (IBAction)clickOnDone:(id)sender {
	[SVProgressHUD showInView:self.view];
	
	NSString *errorMsg = nil;
	if (!self.originalTextField.text || self.originalTextField.text.length == 0) {
		errorMsg = @"请填写旧密码";
	}
	else if (![YJRegexte isPassword:self.nowTextField.text]) {
		errorMsg = @"请输入6-12位（字母，数字）组合";
	}
	else if (![self.nowTextField.text isEqualToString:self.ackTextField.text]) {
		errorMsg = @"两次输入的密码不相符！";
	}
	
	if (errorMsg) {
		[SVProgressHUD showErrorInView:self.view withStatus:errorMsg];
		return;
	}
	
	NSMutableDictionary *params = @{}.mutableCopy;
	params[@"originalPassword"] = self.originalTextField.text;
	params[@"password"] = self.nowTextField.text;
	params[@"confirmedPassword"] = self.ackTextField.text;
	
//	[YJAirlinesManager deleteUser:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
//		if (!error) {
	
			NSURLSessionDataTask *task = [YJRouter commonPostModel:ResetPasswordApi params:params modelClass:[YJUserModel class] callback:^(YJRouterResponseModel *result) {
				if (result.code != 0) {
					[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
				} else {
					[SVProgressHUD showSuccessInView:self.view withStatus:result.msg];
					dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//						[YJEnterControllerManager updateLogin:YES];
						[self.navigationController popToRootViewControllerAnimated:YES];
					});
				}
			}];
			[self weakHoldTask:task];
//		}else {
//			[SVProgressHUD showErrorInView:self.view withStatus:@"连接失败，请重试"];
//		}
//	}];

}

@end
