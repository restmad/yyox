//
//  YJEditUserViewController.m
//  yyox
//
//  Created by ddn on 2017/1/13.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJEditUserViewController.h"
#import "YJImagePickerManager.h"
#import "YJTextField.h"
#import "YJUserModel.h"

@interface YJEditUserViewController ()

@property (weak, nonatomic) IBOutlet YJTextField *userIDTextField;
@property (weak, nonatomic) IBOutlet YJTextField *levelTextField;
@property (weak, nonatomic) IBOutlet YJTextField *phoneTextField;
@property (weak, nonatomic) IBOutlet YJTextField *nameTextField;
@property (weak, nonatomic) IBOutlet YJTextField *firstNameTextField;
@property (weak, nonatomic) IBOutlet YJTextField *lastNameTextField;
@property (weak, nonatomic) IBOutlet YJTextField *emailTextField;
@property (weak, nonatomic) IBOutlet YJTextField *QQTextField;

@end

@implementation YJEditUserViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
	self.title = @"编辑个人信息";
	self.view.backgroundColor = kGlobalBackgroundColor;
	self.tableView.contentInset = UIEdgeInsetsMake(-35, 0, 0, 0);
	
	self.tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
	[self.tableView.mj_header setIgnoredScrollViewContentInsetTop:-35];
	self.autoShowKeyboard = NO;
}


/**
 下拉刷新，获取用户信息
 */
- (void)headerRefresh
{
	NSURLSessionDataTask *task = [YJRouter commonGetModel:UserInfoApi2 params:nil modelClass:[YJUserModel class] callback:^(YJRouterResponseModel *result) {
		[self.tableView.mj_header endRefreshing];
		if (result.code == 0) {
			[self reloadInfoWithUser:result.data];
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
}

- (void)reloadInfoWithUser:(YJUserModel *)user
{
	if (!user) return;
	self.userIDTextField.text = user.identifier;
	self.levelTextField.text = user.level;
	self.phoneTextField.text = user.mobile;
	self.nameTextField.text = user.nickname;
	self.emailTextField.text = user.mail;
	self.QQTextField.text = user.qq;
	self.firstNameTextField.text = user.firstName;
	self.lastNameTextField.text = user.lastName;
}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	if (self.textFields.count == 0) {
		self.textFields = @[self.nameTextField, self.firstNameTextField, self.lastNameTextField, self.QQTextField];
		[self.textFields enumerateObjectsUsingBlock:^(UITextField * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
			obj.delegate = self;
		}];
	}
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	if (self.autoRefresh) {
		[self.tableView.mj_header beginRefreshing];
		self.autoRefresh = NO;
	}
}


/**
 检查输入信息

 @param textField 待检测输入框
 */
- (void)checkTextField:(UITextField *)textField
{
	BOOL error = YES;
	if ([textField isEqual:self.QQTextField] && ([YJRegexte isOnlyNumber:self.QQTextField.text] || !textField.text || textField.text.length == 0)) {
		error = NO;
	} else if ([textField isEqual:self.nameTextField] || [textField isEqual:self.firstNameTextField] || [textField isEqual:self.lastNameTextField]) {
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

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (indexPath.row > 6) {
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
 保存信息

 @param sender 按钮
 */
- (IBAction)clickOnDone:(id)sender {
	if (self.QQTextField.text && self.QQTextField.text.length > 0 && ![YJRegexte isOnlyNumber:self.QQTextField.text]) {
		return [SVProgressHUD showErrorInView:self.view withStatus:@"请输入数字"];
	}
	[SVProgressHUD showInView:self.view];
	NSURLSessionDataTask *task = [YJRouter commonPostModel:EditUserInfoApi params:@{@"chineseName": self.nameTextField.text ?: @"", @"qq": self.QQTextField.text ?: @"", @"firstName": self.firstNameTextField.text ?: @"", @"lastName": self.lastNameTextField.text ?: @""} modelClass:nil callback:^(YJRouterResponseModel *result) {
		if (result.code == 0) {
			[SVProgressHUD showSuccessInView:self.view withStatus:result.msg];
			dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
				[self.navigationController popViewControllerAnimated:YES];
			});
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
}

@end
