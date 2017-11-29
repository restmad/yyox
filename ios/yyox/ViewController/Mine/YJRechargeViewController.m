//
//  YJRechargeViewController.m
//  yyox
//
//  Created by ddn on 2017/3/30.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJRechargeViewController.h"
#import "YJRegexte.h"
#import "YJPayManager.h"

@interface YJRechargeViewController () <UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UILabel *vipLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UIButton *doneButton;
@property (weak, nonatomic) IBOutlet UITextField *lastMoneyLabel;
@property (weak, nonatomic) IBOutlet UITextField *rechargeTextField;

@property (weak, nonatomic) IBOutlet UIButton *aliCircleButton;
@property (weak, nonatomic) IBOutlet UIButton *aliButton;

@property (assign, nonatomic) NSInteger payType;//0. ali
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *textFieldW;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *yuanLeft;

@end

@implementation YJRechargeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	
	self.tableView.contentInset = UIEdgeInsetsMake(-35, 0, 0, 0);
	self.title = @"充值";
	self.payType = 0;
	
	self.tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
	[(MJRefreshNormalHeader *)self.tableView.mj_header lastUpdatedTimeLabel].hidden = YES;
	self.tableView.mj_header.ignoredScrollViewContentInsetTop = -35;
	
	[NSDC addObserver:self selector:@selector(textFieldTextDidChange:) name:UITextFieldTextDidChangeNotification object:nil];
}


/**
 下拉刷新，获取数据
 */
- (void)headerRefresh
{
	NSURLSessionDataTask *task = [YJRouter commonGetModel:PaymentDetailApi params:nil modelClass:nil callback:^(YJRouterResponseModel *result) {
		[self.tableView.mj_header endRefreshing];
		if (result.code == 0) {
			if (result.data) {
				self.vipLabel.text = result.data[@"member"];
				self.timeLabel.text = result.data[@"expiryDate"];
				self.lastMoneyLabel.text = [NSString stringWithFormat:@"%@元", result.data[@"balanceCny"]];
			}
		} else {
			[SVProgressHUD showErrorInView:self.vipLabel withStatus:result.msg];
		}
		[self.tableView reloadData];
	}];
	[self weakHoldTask:task];
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	[self.tableView.mj_header beginRefreshing];
}

- (void)setPayType:(NSInteger)payType
{
	_payType = payType;
	self.aliCircleButton.selected = payType == 0;
}


/**
 选择支付宝

 @param sender 按钮
 */
- (IBAction)clickOnAlipay:(id)sender {
	self.payType = (([sender isEqual:self.aliCircleButton] || [sender isEqual:self.aliButton]) && self.payType != 0) ? 0 : -1;
}


/**
 充值

 @param sender 按钮
 */
- (IBAction)clickOnDone:(id)sender {
	NSMutableDictionary *params = @{}.mutableCopy;
	NSString *api = nil;
	if (self.payType == 0) {
		api = RechargeAliPayApi;
		
		params[@"pay_type_comments"] = @"充值";
		params[@"total_fee"] = @(self.rechargeTextField.text.doubleValue);
	} else if (self.payType == 1) {
		
	}
	if (!api) return [SVProgressHUD showInfoInView:self.view withStatus:@"请选择支付方式"];
	if (self.rechargeTextField.text.doubleValue < 0.1) return [SVProgressHUD showErrorInView:self.view withStatus:@"充值金额不能低于0.1元"];
	if (self.rechargeTextField.text.doubleValue >= 1000000) return [SVProgressHUD showErrorInView:self.view withStatus:@"充值金额不能超过999999.99元"];
	
	[SVProgressHUD showInView:self.view];
	NSURLSessionDataTask *task = [YJRouter commonGetModel:api params:params modelClass:nil callback:^(YJRouterResponseModel *result) {
		if (result.code == 0) {
			[SVProgressHUD dismissFromView:self.view];
			if (self.payType == 0) {
				YJPayModel *payModel = [YJPayModel new];
				payModel.payTypeComments = params[@"pay_type_comments"];
				payModel.totalAmount = params[@"total_fee"];
				[YJPayManager recharge:payModel orderString:result.data callback:^(YJRouterResponseModel *payResult) {
					if (payResult && (payResult.code == 0 || payResult.code == 9000)) {
						[SVProgressHUD showSuccessInView:self.view withStatus:@"充值成功"];
						dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
							[self.navigationController popViewControllerAnimated:YES];
						});
					} else {
						[SVProgressHUD showErrorInView:self.view withStatus:payResult.msg && payResult.msg.length > 0 ? payResult.msg : @"订单支付失败"];
					}
				}];
			} else if (self.payType == 1) {

			}
			
			
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
	[textField resignFirstResponder];
	return YES;
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
	if (!textField.text || textField.text.length == 0) return [self.doneButton setTitle:@"确认支付0.00元" forState:UIControlStateNormal];
	textField.text = [NSString stringWithFormat:@"%.2f", textField.text.doubleValue];
	if (![YJRegexte isMoney:textField.text]) {
		[SVProgressHUD showErrorInView:self.view withStatus:@"输入金额的格式不正确"];
	} else {
		[self.doneButton setTitle:[NSString stringWithFormat:@"确认支付%.2f元", textField.text.doubleValue] forState:UIControlStateNormal];
	}
}

- (void)textFieldTextDidChange:(NSNotification *)notify
{
	UITextField *textField = notify.object;
	NSString *text = textField.text;
	if (!text || textField.text.length == 0) {
		return [self.doneButton setTitle:@"确认支付0.00元" forState:UIControlStateNormal];
	}
	if ([YJRegexte isMoney:text]) {
		text = [NSString stringWithFormat:@"%.2f", text.doubleValue];
		[self.doneButton setTitle:[NSString stringWithFormat:@"确认支付%.2f元", text.doubleValue] forState:UIControlStateNormal];
	} else {
		[self.doneButton setTitle:@"确认支付0.00元" forState:UIControlStateNormal];
	}
}

- (void)dealloc
{
	[NSDC removeObserver:self];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (indexPath.section == 0 && indexPath.row == 1) {
		if ([self.vipLabel.text containsString:@"普通"]) {
			return 0;
		}
	}
	return [super tableView:tableView heightForRowAtIndexPath:indexPath];
}

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	return NO;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (indexPath.section == 1 && indexPath.row >= 1) {
		return;
	}
	cell.separatorInset = UIEdgeInsetsZero;
	cell.layoutMargins = UIEdgeInsetsZero;
	cell.preservesSuperviewLayoutMargins = NO;
}

@end
