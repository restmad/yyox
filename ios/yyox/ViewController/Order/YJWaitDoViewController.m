//
//  YJWaitDoViewController.m
//  yyox
//
//  Created by ddn on 2017/2/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJWaitDoViewController.h"
#import "YJWaitDoViewModel.h"
#import "YJWaitDoCell.h"
#import "YJToDoModel.h"

#import "YJSendInnerViewController.h"
#import "YJShowPackageViewController.h"
#import "YJCombineSendViewController.h"
#import "YJPayViewController.h"
#import "YJEditAddressViewController.h"

@interface YJWaitDoViewController ()

@end

@implementation YJWaitDoViewController

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.lineSpace = 10;
		self.refreshViewModelClass = [YJWaitDoViewModel class];
	}
	return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"合箱发货" style:UIBarButtonItemStyleDone target:self action:@selector(clickOnCombine)];
}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	self.autoRefresh = YES;
}

- (void)clickOnCombine
{
	YJCombineSendViewController *combine = [YJCombineSendViewController new];
	[self.navigationController pushViewController:combine animated:YES];
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
	YJWaitDoCell *cell = [super collectionView:collectionView cellForItemAtIndexPath:indexPath];
	YJToDoModel *model = (YJToDoModel *)cell.cellModel.model;
	@weakify(self)
	[cell setClickOnInfo:^(NSString *info) {
		@strongify(self)
		if ([info containsString:@"支付"]) {
			YJSendInnerViewController *sendInner = [YJSendInnerViewController instanceWithOptions:(model.type != 2 ? YJDetailOrderOptionsAll : (YJDetailOrderOptionsAll ^ YJDetailOrderOptionsShrinkable)) ^ YJDetailOrderOptionsShowLastCost andModel:model withSpec:^(UITableView *tableView, YJBottomFloatView *floatView) {
				floatView.titleLabel.text = @"运费：";
				[floatView.rightButton setTitle:@"去支付" forState:UIControlStateNormal];
			}];
			sendInner.title = @"提交订单";
			[self.navigationController pushViewController:sendInner animated:YES];
		} else if ([info containsString:@"缴税金"]) {
			YJPayViewController *pay = (YJPayViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyPayment];
			YJPayModel *payModel = [YJPayModel new];
			payModel.orderNo = model.no;
			payModel.payType = @"TAXREPAY";
			payModel.payTypeComments = @"支付税金";
			pay.model = payModel;
			[self.navigationController pushViewController:pay animated:YES];
		} else if ([info containsString:@"身份证"]) {
			YJEditAddressViewController *editAddress = [YJEditAddressViewController new];
			editAddress.title = @"上传身份证";
			editAddress.type = YJEditAddressCard;
			YJAddressModel *address = [YJAddressModel new];
			address.id = model.addressId;
			editAddress.addressModel = address;
			[self.navigationController pushViewController:editAddress animated:YES];
		}
	}];
	return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
	YJToDoModel *model = (YJToDoModel *)[(YJWaitDoCellModel *)[self.refreshViewModel modelForIndexPath:indexPath] model];
	if (model.status == 4) {
		YJSendInnerViewController *sendInner = [YJSendInnerViewController instanceWithOptions:(model.type != 2 ? (YJDetailOrderOptionsCommitable & YJDetailOrderOptionsShrinkable) : YJDetailOrderOptionsCommitable) | YJDetailOrderOptionsShowLastCost andModel:model withSpec:^(UITableView *tableView, YJBottomFloatView *floatView) {
			floatView.titleLabel.text = @"税金：";
			[floatView.rightButton setTitle:@"去支付" forState:UIControlStateNormal];
		}];
		sendInner.title = @"订单详情";
		[self.navigationController pushViewController:sendInner animated:YES];
	}
	else if (model.status == 2) {
		YJSendInnerViewController *sendInner = [YJSendInnerViewController instanceWithOptions:(model.type != 2 ? YJDetailOrderOptionsAll : (YJDetailOrderOptionsAll ^ YJDetailOrderOptionsShrinkable)) ^ YJDetailOrderOptionsShowLastCost andModel:model withSpec:^(UITableView *tableView, YJBottomFloatView *floatView) {
			floatView.titleLabel.text = @"运费：";
			[floatView.rightButton setTitle:@"去支付" forState:UIControlStateNormal];
		}];
		sendInner.title = @"提交订单";
		[self.navigationController pushViewController:sendInner animated:YES];
	}
	else if (model.status == 3) {
		YJSendInnerViewController *sendInner = [YJSendInnerViewController instanceWithOptions:model.type != 2 ? (YJDetailOrderOptionsEditAddressable | YJDetailOrderOptionsShowLastCost | YJDetailOrderOptionsShrinkable) : (YJDetailOrderOptionsEditAddressable | YJDetailOrderOptionsShowLastCost) andModel:model withSpec:^(UITableView *tableView, YJBottomFloatView *floatView) {
			
		}];
		sendInner.title = @"订单详情";
		[self.navigationController pushViewController:sendInner animated:YES];
	}
	else if (model.status == 1) {
		YJShowPackageViewController *package = [YJShowPackageViewController instanceWithEditable:YES model:model];
		[self.navigationController pushViewController:package animated:YES];
	}
}

@end
