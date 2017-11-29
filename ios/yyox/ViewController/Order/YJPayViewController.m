//
//  YJPayViewController.m
//  yyox
//
//  Created by ddn on 2017/2/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPayViewController.h"
#import "YJPayResultViewController.h"
#import "YJShowPackageViewController.h"
#import "YJSendInnerViewController.h"
#import "YJPayManager.h"
#import "UIBarButtonItem+YJExtension.h"

@interface YJPayViewController ()

@property (weak, nonatomic) IBOutlet UILabel *orderLabel;
@property (weak, nonatomic) IBOutlet UILabel *originalPriceLabel;
@property (weak, nonatomic) IBOutlet UILabel *innerMoneyLabel;
@property (weak, nonatomic) IBOutlet UIButton *doneButton;

@property (weak, nonatomic) IBOutlet UIButton *aliCircleButton;
@property (weak, nonatomic) IBOutlet UIButton *aliButton;
@property (weak, nonatomic) IBOutlet UIButton *innerPayButton;

@property (assign, nonatomic) NSInteger payType;//0. ali  1.inner

@end

@implementation YJPayViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	
	self.tableView.contentInset = UIEdgeInsetsMake(-35, 0, 0, 0);
	self.title = @"支付";
	self.payType = 0;
	[self.navigationItem.leftBarButtonItem changeTarget:self action:@selector(clickOnBack)];
	self.tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
	[(MJRefreshNormalHeader *)self.tableView.mj_header lastUpdatedTimeLabel].hidden = YES;
	self.tableView.mj_header.ignoredScrollViewContentInsetTop = -35;
}

- (void)clickOnBack
{
	[UIAlertController showTitle:@"提示" message:@"确定放弃本次订单支付？" cancel:@"否" sure:@"是" byController:self callback:^{
		
		[self.navigationController popViewControllerAnimated:YES];
	}];
}


/**
 下拉刷新，获取数据／判断支付状态
 */
- (void)headerRefresh
{
	NSURLSessionDataTask *task = [YJRouter commonPostModel:PaymentStatusApi params:@{@"orderNo": self.model.orderNo} modelClass:[YJPayModel class] callback:^(YJRouterResponseModel *result) {
		[self.tableView.mj_header endRefreshing];
		if (result.code != 0) {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		} else {
			[SVProgressHUD dismissFromView:self.view];
			self.model.totalAmount = [result.data totalAmount];
			self.model.balanceCny = [result.data balanceCny];
			self.model.payStatus = [result.data payStatus];
			
			if ([self.model.payStatus isEqualToNumber:@0]) {
				//已支付，且成功
				[self showResultVc];
			} else if ([self.model.payStatus isEqualToNumber:@1]) {
				//已支付，但失败
				[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
			} else if ([self.model.payStatus isEqualToNumber:@2]) {
				//未支付
				self.originalPriceLabel.text = [NSString stringWithFormat:@"%.2f元", self.model.totalAmount.doubleValue];
				self.innerMoneyLabel.text = [NSString stringWithFormat:@"%.2f元", self.model.balanceCny.doubleValue];
				[self.doneButton setTitle:[NSString stringWithFormat:@"确认支付%.2f元", self.model.totalAmount.doubleValue] forState:UIControlStateNormal];
			}
		}
	}];
	[self weakHoldTask:task];
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	self.orderLabel.text = self.model.orderNo;
	
	[SVProgressHUD showInView:self.view];
	[self headerRefresh];
}

- (void)setPayType:(NSInteger)payType
{
	if (payType == 1) {
		if (self.model.balanceCny.doubleValue < self.model.totalAmount.doubleValue) {
			return [SVProgressHUD showInfoInView:self.view withStatus:@"余额不足，不能使用余额支付"];
		}
	}
	_payType = payType;
	self.aliCircleButton.selected = payType == 0;
	self.innerPayButton.selected = payType == 1;
}


/**
 选中邮币支付

 @param sender 按钮
 */
- (IBAction)clickOnInnerPay:(id)sender {
	self.payType = ([sender isEqual:self.innerPayButton] && self.payType != 1) ? 1 : -1;
}


/**
 选中支付宝

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
		api = AliPayApi;
		
		params[@"orderId"] = self.model.orderNo;
		params[@"pay_type_comments"] = self.model.payTypeComments;
		params[@"payTypeBackSide"] = self.model.payType;
	} else if (self.payType == 1) {
		api = InnerPayApi;
		
		params[@"orderNo"] = self.model.orderNo;
		params[@"payType"] = [self.model.payType isEqualToString:@"SHIPPINGCOST"] ? @0 : @1;
	}
	if (!api) return [SVProgressHUD showInfoInView:self.view withStatus:@"请选择支付方式"];
	if (!self.model.totalAmount || self.model.totalAmount.doubleValue == 0) return [SVProgressHUD showErrorInView:self.view withStatus:@"订单信息有误"];
	
	[SVProgressHUD showInView:self.view];
	NSURLSessionDataTask *task = [YJRouter commonGetModel:api params:params modelClass:nil callback:^(YJRouterResponseModel *result) {
		if (result.code == 0) {
			[SVProgressHUD dismissFromView:self.view];
			if (self.payType == 0) {
				[YJPayManager payOrder:self.model orderString:result.data callback:^(YJRouterResponseModel *payResult) {
					if (payResult && (payResult.code == 0 || payResult.code == 9000)) {
						[self showResultVc];
					} else {
						[SVProgressHUD showErrorInView:self.view withStatus:payResult.msg && payResult.msg.length > 0 ? payResult.msg : @"订单支付失败"];
					}
				}];
			} else if (self.payType == 1) {
				[self showResultVc];
			}
			
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
	
}


/**
 跳转支付结果页面
 */
- (void)showResultVc
{
	YJPayResultViewController *resultVc = (YJPayResultViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyPayResult];
	resultVc.model = self.model;
	[self.navigationController pushViewController:resultVc animated:YES];
}


/**
 包裹详情

 @param sender 按钮
 */
- (IBAction)clickOnMoreInfo:(id)sender {
	
	YJToDoModel *todoModel = [YJToDoModel new];
	todoModel.no = self.model.orderNo;
	todoModel.type = 2;
	YJShowPackageViewController *detail = [YJShowPackageViewController instanceWithEditable:NO model:todoModel];
	detail.shrinkable = YES;
	detail.title = @"包裹详情";
	[self.navigationController pushViewController:detail animated:YES];
}

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	return NO;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (indexPath.section == 2 && indexPath.row == 1) {
		return;
	}
	cell.separatorInset = UIEdgeInsetsZero;
	cell.layoutMargins = UIEdgeInsetsZero;
	cell.preservesSuperviewLayoutMargins = NO;
}

@end
