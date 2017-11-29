//
//  YJMineViewController.m
//  yyox
//
//  Created by ddn on 2016/12/26.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJMineViewController.h"
#import "YJAdressViewController.h"
#import "YJSettingViewController.h"
#import "YJEditUserViewController.h"
#import "YJWarehouseViewController.h"

#import "YJPaymentViewController.h"
#import "YJCouponViewController.h"
#import "YJRechargeViewController.h"

#import "YJUserModel.h"

#if DEBUG
#import <FLEX/FLEX.h>
#endif

@interface YJMineViewController ()

@property (weak, nonatomic) IBOutlet UIButton *loginButton;
@property (weak, nonatomic) IBOutlet UIButton *discountButton;

@property (weak, nonatomic) IBOutlet UIImageView *iconImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *moneyLabel;

@property (weak, nonatomic) IBOutlet UIImageView *vipImageView;
@property (weak, nonatomic) IBOutlet UILabel *vipLabel;

@property (strong, nonatomic) YJUserModel *user;

@property (strong, nonatomic) RLMNotificationToken *notify;

@end

@implementation YJMineViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	self.tableView.backgroundColor = kGlobalBackgroundColor;
	self.tableView.contentInset = UIEdgeInsetsMake(-35, 0, 0, 0);
	
	self.tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
	[(MJRefreshNormalHeader *)self.tableView.mj_header lastUpdatedTimeLabel].hidden = YES;
	self.tableView.mj_header.ignoredScrollViewContentInsetTop = -35;
	
	[self resetHeader];
	YJUserModel *user = [YJUserModel allObjects].firstObject;
	[self bindingUser:user];
	
#if DEBUG
	self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"调试" style:UIBarButtonItemStyleDone target:self action:@selector(clickOnDebug)];
#endif
}

- (void)clickOnDebug
{
#if DEBUG
	[[FLEXManager sharedManager] showExplorer];
#endif
}


/**
 绑定用户对象

 @param user 用户对象
 */
- (void)bindingUser:(YJUserModel *)user
{
	if (!self.user) {
		if (user) {
			self.user = user;
			[self setHeaderInfo];
			[self.tableView reloadData];
			self.notify = [self.user.realm addNotificationBlock:^(NSString * _Nonnull notification, RLMRealm * _Nonnull realm) {
				[self.tableView reloadData];
				
				if (!self.user || self.user.isInvalidated) {
					[self resetHeader];
					self.user = nil;
				} else {
					[self setHeaderInfo];
				}
			}];
		}
	}
}


/**
 设置头部视图信息
 */
- (void)setHeaderInfo
{
	if (!self.user.member) {
		self.vipImageView.hidden = self.vipLabel.hidden = YES;
	} else {
		self.vipImageView.hidden = self.vipLabel.hidden = NO;
		self.vipLabel.text = [NSString stringWithFormat:@"V%@", self.user.member];
	}
	
	self.nameLabel.text = self.user.name;
	[self.iconImageView yj_setBase64Image:self.user.avatarUrl placeHolder:[UIImage imageNamed:@"mine_default_icon"]];
	self.moneyLabel.text = [NSString stringWithFormat:@"余额：¥%.2f", self.user.balanceCnyFormat2.doubleValue];
	
	NSString *str = [NSString stringWithFormat:@"优惠券(%zd)", self.user.couponCount.intValue];
	NSMutableAttributedString *attstr = [[NSMutableAttributedString alloc] initWithString:str];
	[attstr yy_setColor:[UIColor colorWithRGB:0x333333] range:NSMakeRange(0, 3)];
	[attstr yy_setFont:[UIFont systemFontOfSize:13] range:NSMakeRange(0, 3)];
	[attstr yy_setColor:[UIColor colorWithRGB:0x999999] range:NSMakeRange(3, str.length-3)];
	[attstr yy_setFont:[UIFont systemFontOfSize:12] range:NSMakeRange(3, str.length-3)];
	[self.discountButton setAttributedTitle:attstr forState:UIControlStateNormal];
}


/**
 复位头部信息
 */
- (void)resetHeader
{
	self.vipLabel.text = @"V0";
	self.vipImageView.hidden = self.vipLabel.hidden = YES;
	self.nameLabel.text = @"";
	self.moneyLabel.text = @"余额：¥0.00";
	NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:@"优惠券"];
	[str yy_setColor:[UIColor colorWithRGB:0x333333] range:NSMakeRange(0, 3)];
	[str yy_setFont:[UIFont systemFontOfSize:13] range:NSMakeRange(0, 3)];
	[self.discountButton setAttributedTitle:str forState:UIControlStateNormal];
	self.iconImageView.image = [UIImage imageNamed:@"mine_default_icon"];
}


/**
 下拉刷新，获取数据
 */
- (void)headerRefresh
{
	NSURLSessionDataTask *task = [YJRouter commonGetModel:UserInfoApi params:nil modelClass:[YJUserModel class] callback:^(YJRouterResponseModel *result) {
		[self.tableView.mj_header endRefreshing];
		if (result.code == 0) {
			YJUserModel *user = result.data;
			if (user) {
				[[RLMRealm defaultRealm] beginWriteTransaction];
				[[RLMRealm defaultRealm] addOrUpdateObject:user];
				[[RLMRealm defaultRealm] commitWriteTransaction];
				
				[self bindingUser:user];
			}
		} else if (result.code == 2) {
			[SVProgressHUD dismissFromView:self.view];
			[YJEnterControllerManager updateLogin:NO];
		}  else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	if ([YJEnterControllerManager isLogin] && self.autoRefresh) {
		[self.tableView.mj_header beginRefreshing];
	}
	self.tableView.mj_header.hidden = ![YJEnterControllerManager isLogin];
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (indexPath.row == 4) {
		self.loginButton.hidden = [YJEnterControllerManager isLogin];
		return;
	}
	
	cell.layoutMargins = UIEdgeInsetsZero;
	cell.preservesSuperviewLayoutMargins = NO;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell;
	if (![YJEnterControllerManager isLogin] && indexPath.row + indexPath.section == 0) {
		cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
		if (!cell) {
			cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cell"];
			cell.imageView.image = [UIImage imageNamed:@"mine_default_icon"];
			cell.textLabel.text = @"您还未登录";
			cell.textLabel.textColor = [UIColor colorWithRGB:0x333333];
			cell.accessoryType = UITableViewCellAccessoryNone;
			cell.separatorInset = UIEdgeInsetsZero;
		}
	} else {
		cell = [super tableView:tableView cellForRowAtIndexPath:indexPath];
	}
	return cell;
}

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	if ((indexPath.section == 0 && indexPath.row == 1) || (indexPath.section == 1 && indexPath.row == 4)) {
		return NO;
	}
	return YES;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	if (indexPath.section == 0) {
		YJEditUserViewController *editUserVc = (YJEditUserViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyEditUser];
		[self.navigationController pushViewController:editUserVc animated:YES];
	}
 	else if (indexPath.section == 1) {
		switch (indexPath.row) {
			case 0:
			{
				YJWarehouseViewController *warehouseVc = [YJWarehouseViewController new];
				[self.navigationController pushViewController:warehouseVc animated:YES];
				break;
			}
			case 1:
			{
				YJAdressViewController *addressVc = [YJAdressViewController new];
				[self.navigationController pushViewController:addressVc animated:YES];
				break;
			}
			case 2:
			{
				YJPaymentViewController *pageVc = [YJPaymentViewController new];
				pageVc.title = @"消费记录";
				[self.navigationController pushViewController:pageVc animated:YES];
				break;
			}
			case 3:
			{
				YJSettingViewController *settingVc = (YJSettingViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifySetting];
				[self.navigationController pushViewController:settingVc animated:YES];
				break;
			}
			default:
				break;
		}
	}
}


/**
 点击优惠券

 @param sender 按钮
 */
- (IBAction)clickOnDiscount:(id)sender {
	YJCouponViewController *couponVc = [YJCouponViewController new];
	[self.navigationController pushViewController:couponVc animated:YES];
}


/**
 点击充值

 @param sender 按钮
 */
- (IBAction)clickOnRecharge:(id)sender {
	YJRechargeViewController *rechargeVc = (YJRechargeViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyRecharge];
	[self.navigationController pushViewController:rechargeVc animated:YES];
}


/**
 点击登录

 @param sender 按钮
 */
- (IBAction)clickLoginButton:(id)sender {
	UIViewController *loginVc = [YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyLogin];
	[self.navigationController pushViewController:loginVc animated:YES];
}

@end
