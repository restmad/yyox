//
//  YJPayResultViewController.m
//  yyox
//
//  Created by ddn on 2017/2/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPayResultViewController.h"
#import "UIBarButtonItem+YJExtension.h"

#import "YJEditAddressViewController.h"

#import "YJCommonStatusMsgCell.h"
#import "YJCommonStatusMsgModel.h"
#import "YJStatusProcessView.h"

@interface YJPayResultViewController () <UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UIImageView *resultImageView;
@property (weak, nonatomic) IBOutlet UILabel *orderLabel;
@property (weak, nonatomic) IBOutlet UILabel *resultDescLabel;
@property (weak, nonatomic) IBOutlet UIView *needUploadPicView;

@property (strong, nonatomic) NSNumber *addressId;

@property (weak, nonatomic) UITableView *tableView;

@end

@implementation YJPayResultViewController

- (UITableView *)tableView
{
	if (!_tableView) {
		UITableView *tableView = [UITableView new];
		_tableView = tableView;
		[self.view addSubview:tableView];
		[tableView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.top.mas_equalTo(self.needUploadPicView.mas_top).offset(-30);
			make.left.right.bottom.mas_equalTo(0);
		}];
		
		UIView *headerView = [UIView new];
		headerView.height = 95;
		headerView.backgroundColor = kGlobalBackgroundColor;
		tableView.tableHeaderView = headerView;
		
		YJStatusProcessView *processView = [YJStatusProcessView new];
		processView.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
		processView.frame = CGRectMake(0, 0, kScreenWidth, 95);
		processView.orderStatus = YJOrderStatusWaitOut;
		tableView.tableHeaderView = processView;
		
		tableView.estimatedRowHeight = 44;
		tableView.rowHeight = UITableViewAutomaticDimension;
		tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
		[tableView registerClass:[YJCommonStatusMsgCell class] forCellReuseIdentifier:@"msgCell"];
		tableView.dataSource = self;
		tableView.delegate = self;
		tableView.scrollEnabled = NO;
	}
	return _tableView;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	self.title = @"支付结果";
	[self.navigationItem.leftBarButtonItem changeTarget:self action:@selector(clickOnBack)];
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	if (!self.model || !self.model.orderNo) {
		return;
	} else {
		self.orderLabel.text = [NSString stringWithFormat:@"邮客单号：%@", self.model.orderNo];
	}
	if (![self.model.payType isEqualToString:@"SHIPPINGCOST"]) {
		self.resultDescLabel.text = @"您的税金已经支付成功，清关完成后我们将尽快安排配送，请关注订单状态。";
		return;
	};
	[SVProgressHUD showInView:self.view];
	NSURLSessionDataTask *task = [YJRouter commonGetModel:CheckNeedUploadCardInfoApi params:@{@"orderNo" : self.model.orderNo} modelClass:nil callback:^(YJRouterResponseModel *result) {
		if (result.code != 0) {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		} else {
			[SVProgressHUD dismissFromView:self.view];
			if ([self.model.payType isEqualToString:@"SHIPPINGCOST"]) {
				BOOL needUpload = [result.data[@"checkCard"] boolValue];
				if (!needUpload) {
					self.resultDescLabel.text = @"您的订单已经支付成功，我们将尽快安排出库，发往国内，请关注订单状态。";
					[self.tableView reloadData];
				} else {
					self.resultDescLabel.text = @"您的订单已经完成支付，请尽快上传身份证照片，以便仓库安排出库。";
					[_tableView removeFromSuperview];
				}
				self.needUploadPicView.hidden = !needUpload;
				self.addressId = result.data[@"addressId"];
			}
		}
	}];
	[self weakHoldTask:task];
}

- (void)clickOnBack
{
	[self.navigationController popToViewController:self.navigationController.viewControllers[1] animated:YES];
}


/**
 上传照片

 @param sender 按钮
 */
- (IBAction)clickOnUpload:(id)sender {
	YJEditAddressViewController *editAddress = [YJEditAddressViewController new];
	YJAddressModel *address = [YJAddressModel new];
	address.id = self.addressId;
	editAddress.addressModel = address;
	editAddress.type = YJEditAddressCard;
	editAddress.title = @"上传身份证";
	[self.navigationController pushViewController:editAddress animated:YES];
}

- (IBAction)clickOnNotUpload:(id)sender {
	[self clickOnBack];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	YJCommonStatusMsgCell *cell = [tableView dequeueReusableCellWithIdentifier:@"msgCell" forIndexPath:indexPath];
	YJCommonStatusDetailMsgModel *msg = [YJCommonStatusDetailMsgModel new];
	msg.actionDateWithFormat = [[NSDate date] stringWithFormat:@"yyyy-MM-dd HH:mm:ss"];
	msg.history = [NSString stringWithFormat:@"您的订单(%@)已经创建", self.model.orderNo];
	cell.model = msg;
	return cell;
}

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	return NO;
}

@end
