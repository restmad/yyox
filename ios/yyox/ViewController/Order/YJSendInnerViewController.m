//
//  YJSendInnerViewController.m
//  yyox
//
//  Created by ddn on 2017/2/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJSendInnerViewController.h"
#import "YJCommonAddressTableViewCell.h"
#import "YJCommonTableViewCell.h"
#import "YJCommonGoodsTableViewCell.h"
#import "YJAdressViewController.h"
#import "YJPayViewController.h"
#import "YJShowPackageViewController.h"
#import "YJEditAddressViewController.h"
#import "YJTransferViewController.h"
#import "YJCouponViewController.h"
#import "YJBaseWevViewController.h"
#import "UIBarButtonItem+YJExtension.h"

@interface YJSendInnerViewController () <UITableViewDataSource, UITableViewDelegate>

@property (strong, nonatomic) YJToDoModel *model;

@property (strong, nonatomic) UITableView *tableView;
@property (strong, nonatomic) YJBottomFloatView *bottomView;

@property (strong, nonatomic) UIView *tableHeader;

@property (strong, nonatomic) NSMutableDictionary *sectionRowCount;

@property (strong, nonatomic) NSMutableDictionary *params;

@property (strong, nonatomic) UIButton *acceptButton;

@property (assign, nonatomic) YJDetailOrderOptions options;

@end

@implementation YJSendInnerViewController
{
	struct {
		unsigned int popToList : 1;
		unsigned int editable : 1;
		unsigned int shrinkable : 1;
		unsigned int editAddressable : 1;
		unsigned int onlyEditAddressable : 1;
		unsigned int showLastCost : 1;
	} _bools;
}


/**
 根据opts初始化如何展示

 @param options options
 @param model 待处理模型
 @param specSetting 个性化设置
 @return 控制器对象
 */
+ (instancetype)instanceWithOptions:(YJDetailOrderOptions)options andModel:(YJToDoModel *)model withSpec:(void (^)(UITableView *, YJBottomFloatView *))specSetting
{
	YJSendInnerViewController *vc = [self new];
	vc.options = options;
	vc->_bools.editable = (options & YJDetailOrderOptionsEditable) > 0;
	vc->_bools.shrinkable = (options & YJDetailOrderOptionsShrinkable) > 0;
	vc->_bools.onlyEditAddressable = ((options == YJDetailOrderOptionsEditAddressable) || (options ^ YJDetailOrderOptionsShowLastCost) == YJDetailOrderOptionsEditAddressable) > 0;
	vc->_bools.editAddressable = ((options & YJDetailOrderOptionsEditAddressable) | (options & YJDetailOrderOptionsEditable) || vc->_bools.onlyEditAddressable) > 0;
	vc->_bools.showLastCost = (options & YJDetailOrderOptionsShowLastCost) > 0;
	vc.model = model;
	[vc baseUI];
	
	(options & YJDetailOrderOptionsHasFooter) ? [vc footerUI] : NULL;
	(options & YJDetailOrderOptionsCommitable) ? [vc commitUI] : NULL;
	
	if (specSetting) {
		specSetting(vc.tableView, vc.bottomView);
	}
	
	vc.sectionRowCount = [NSMutableDictionary dictionary];
	
	if ((model.type == 3 || model.type == 2) && vc->_bools.editable) {
		UIBarButtonItem *cancel = [[UIBarButtonItem alloc] initWithTitle:model.type == 3 ? @"取消合箱" : @"取消订单" style:UIBarButtonItemStyleDone target:vc action:@selector(clickOnCancel)];
		vc.navigationItem.rightBarButtonItem = cancel;
	}
	vc.title = @"提交订单";
	
	return vc;
}

- (void)baseUI
{
	_tableView = [[UITableView alloc] initWithFrame:self.view.bounds style:UITableViewStyleGrouped];
	_tableView.dataSource = self;
	_tableView.delegate = self;
	_tableView.estimatedRowHeight = 44;
	_tableView.rowHeight = UITableViewAutomaticDimension;
	[_tableView registerClass:[YJCommonAddressTableViewCell class] forCellReuseIdentifier:@"address"];
	[_tableView registerClass:[YJCommonTableViewCell class] forCellReuseIdentifier:@"nick"];
	[_tableView registerClass:[YJCommonGoodsTableViewCell class] forCellReuseIdentifier:@"goods"];
	[_tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"common"];
	_tableView.backgroundColor = kGlobalBackgroundColor;
	_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
	[self.view addSubview:_tableView];
	[_tableView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.edges.mas_equalTo(0);
	}];
}

- (UIView *)tableHeader
{
	if (!_tableHeader) {
		[self headerUI];
	}
	return _tableHeader;
}


/**
 顶部warning
 */
- (void)headerUI
{
	_tableHeader = [UIView new];
	_tableHeader.height = 30 + 10;
	_tableHeader.backgroundColor = kGlobalBackgroundColor;
	
	YYLabel *warningLabel = [YYLabel new];
	warningLabel.backgroundColor = [UIColor whiteColor];
	NSMutableAttributedString *warningText = [[NSMutableAttributedString alloc] initWithString:@"  !  合箱发货最终运费以出库称重为准，多收部分会退至账号余额"];
	YYTextBorder *border = [YYTextBorder new];
	border.strokeColor = [UIColor colorWithRGB:0xfc9b31];
	border.strokeWidth = 0.5;
	border.cornerRadius = 5;
	border.lineStyle = YYTextLineStyleSingle;
	border.insets = UIEdgeInsetsMake(1, 2.5, 1, 2.5);
	
	warningText.yy_font = [UIFont systemFontOfSize:10];
	warningText.yy_color = [UIColor colorWithRGB:0xfc9b31];
	warningText.yy_alignment = NSTextAlignmentCenter;
	warningText.yy_maximumLineHeight = 30;
	
	[warningText yy_setTextBorder:border range:NSMakeRange(0, 5)];
	warningLabel.frame = CGRectMake(0, 0, kScreenWidth, 30);
	
	YYTextLayout *layout = [YYTextLayout layoutWithContainerSize:CGSizeMake(kScreenWidth, 30) text:warningText];
	warningLabel.textLayout = layout;
	
	[_tableHeader addSubview:warningLabel];
}

- (void)resetHeader
{
	if (self.detailOrderModel.merchandiseList.count > 1 && (self.options & YJDetailOrderOptionsHasHeader)) {
		self.tableView.tableHeaderView = self.tableHeader;
	}
}

- (void)resetCommitUIContent
{
	self.bottomView.valueLabel.text = [NSString stringWithFormat:@"%.2f", self.model.status == 4 ? self.detailOrderModel.taxRepay.doubleValue : self.detailOrderModel.realcost.doubleValue];
	self.bottomView.subValueLabel.text = [NSString stringWithFormat:@"%.2fkg", self.detailOrderModel.realWeight.doubleValue];
}

- (void)footerUI
{
	UIButton *acceptImageBtn = [UIButton new];
	[acceptImageBtn setImage:[UIImage imageNamed:@"focus_button_normal"] forState:UIControlStateNormal];
	[acceptImageBtn setImage:[UIImage imageNamed:@"focus_button_selected"] forState:UIControlStateSelected];
	[acceptImageBtn sizeToFit];
	[acceptImageBtn addTarget:self action:@selector(clickOnAcceptImageBtn:) forControlEvents:UIControlEventTouchUpInside];
	acceptImageBtn.selected = YES;
	_acceptButton = acceptImageBtn;
	
	UIButton *acceptBtn = [UIButton new];
	[acceptBtn setTitle:@"同意邮客服务条例" forState:UIControlStateNormal];
	acceptBtn.titleLabel.font = [UIFont systemFontOfSize:11];
	[acceptBtn setTitleColor:[UIColor colorWithRGB:0x666666] forState:UIControlStateNormal];
	[acceptBtn addTarget:self action:@selector(clickOnAcceptBtn:) forControlEvents:UIControlEventTouchUpInside];
	
	UIView *footerView = [UIView new];
	[footerView addSubview:acceptBtn];
	[footerView addSubview:acceptImageBtn];
	[acceptImageBtn mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(0);
		make.bottom.mas_equalTo(-10);
	}];
	[acceptBtn mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(acceptImageBtn.mas_right).offset(5);
		make.height.mas_equalTo(acceptImageBtn.mas_height);
		make.centerY.mas_equalTo(acceptImageBtn.mas_centerY);
	}];
	footerView.height = acceptImageBtn.height + 10;
	
	_tableView.tableFooterView = footerView;
}

- (void)commitUI
{
	_bottomView = [YJBottomFloatView new];
	_bottomView.edgeLines = UIEdgeInsetsMake(0.3, 0, 0, 0);
	_bottomView.backgroundColor = [UIColor whiteColor];
	[_bottomView.rightButton setTitle:@"提交订单" forState:UIControlStateNormal];
	_bottomView.titleLabel.text = @"运费：";
	_bottomView.unitsLabel.text = @"¥";
	_bottomView.valueLabel.text = @"0.00";
	_bottomView.subTitleLabel.text = @"总重量：";
	_bottomView.subValueLabel.text = @"0.00kg";
	[self.view addSubview:_bottomView];
	[_bottomView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.bottom.right.left.mas_equalTo(0);
		make.height.mas_equalTo(50);
	}];
	@weakify(self)
	[_bottomView setClickOnButton:^{
		@strongify(self)
		[self clickOnDone];
	}];
	
	_tableView.contentInset = UIEdgeInsetsMake(0, 0, 50, 0);
	[_bottomView setShowSub:self->_bools.editable];
}

/**
 检测参数

 @param params 参数
 @return 是否有误
 */
- (BOOL)checkParamsBeforeCommint:(NSMutableDictionary * __autoreleasing*)params
{
	if (!self.acceptButton) return YES;
	
	NSString *msg = nil;
	for (NSInteger i=0; i<self.detailOrderModel.merchandiseList.count; i++) {
		YJMerchandiseModel *merchandise = self.detailOrderModel.merchandiseList[i];
		if (merchandise.unreadyGoodsList.count > 0) {
			msg = @"您还有未完成申报的商品";
			break;
		}
	}
	
	NSArray *packageIds = self.detailOrderModel.merchandiseList.count > 0 ? [self.detailOrderModel.merchandiseList valueForKeyPath:@"id"] : nil;
	if (!self.acceptButton.selected) msg = @"您还没有同意邮客服务条例";
	else if (!self.detailOrderModel.address.id) msg = @"缺少地址信息";
	else if (!packageIds || packageIds.count == 0) msg = @"缺少包裹信息";
	else if (!self.detailOrderModel.orderchannel.orderTypeCode) msg = @"运输服务信息不完整";
	else if (!self.detailOrderModel.realcost) msg = @"信息不完整";
	else if (!self.detailOrderModel.realWeight) msg = @"信息不完整";
	
	if (msg) {
		[SVProgressHUD showErrorInView:self.view withStatus:msg];
		return NO;
	} else {
		[*params setValue:self.detailOrderModel.address.id forKey:@"customerAddressId"];
		[*params setValue:[packageIds componentsJoinedByString:@","] forKey:@"inventoryIds"];
		[*params setValue:self.detailOrderModel.orderchannel.orderTypeCode forKey:@"orderType"];
		[*params setValue:self.detailOrderModel.realcost forKey:@"money"];
		[*params setValue:self.detailOrderModel.realWeight forKey:@"weight"];
		[*params setValue:self.detailOrderModel.couponList.code ?: @"removeCoupon" forKey:@"couponcode"];
		[*params setValue:self.detailOrderModel.valueAddedlist ? [self.detailOrderModel.valueAddedlist componentsJoinedByString:@","] : @"" forKey:@"list"];
		[*params setValue:self.detailOrderModel.orderNo ?: @"" forKey:@"orderNo"];
		return YES;
	}
}


/**
 提交
 */
- (void)clickOnDone
{
	if (!self.acceptButton) {
		YJPayViewController *pay = (YJPayViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyPayment];
		YJPayModel *payModel = [YJPayModel new];
		payModel.orderNo = self.model.no;
		payModel.payType = @"TAXREPAY";
		payModel.payTypeComments = @"支付税金";
		pay.model = payModel;
		[self.navigationController pushViewController:pay animated:YES];
		return;
	}
	
	NSMutableDictionary *params = @{}.mutableCopy;
	BOOL able = [self checkParamsBeforeCommint:&params];
	if (!able) return;
	
	[SVProgressHUD showInView:self.view];
	NSURLSessionDataTask *task = [YJRouter commonPostForm:CommitOrderApi params:params modelClass:[YJPayModel class] callback:^(YJRouterResponseModel *result) {
		if (result.code != 0) {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		} else {
			[SVProgressHUD dismissFromView:self.view];
			YJPayViewController *payvc = (YJPayViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyPayment];
			payvc.model = result.data;
			self.detailOrderModel.orderNo = payvc.model.orderNo;
			
			if (!self.navigationItem.rightBarButtonItem && self.model.type == 3 && self->_bools.editable) {
				UIBarButtonItem *cancel = [[UIBarButtonItem alloc] initWithTitle:self.model.type == 3 ? @"取消合箱" : @"取消订单" style:UIBarButtonItemStyleDone target:self action:@selector(clickOnCancel)];
				self.navigationItem.rightBarButtonItem = cancel;
			}
			
			_bools.popToList = 1;
			[self.navigationController pushViewController:payvc animated:YES];
		}
	}];
	[self weakHoldTask:task];
}

- (void)clickOnAcceptImageBtn:(UIButton *)sender
{
	sender.selected = !sender.selected;
}

- (void)clickOnAcceptBtn:(UIButton *)sender
{
	YJBaseWevViewController *webvc = [YJBaseWevViewController new];
	webvc.title = @"邮客服务条例";
	webvc.startUrl = [NSURL yj_URLWithString:UseConditionUrl];
	[self.navigationController pushViewController:webvc animated:YES];
}

/**
 取消合箱/订单
 */
- (void)clickOnCancel
{
	NSString *no = self.detailOrderModel.orderNo;
	if (!no) {
		[self.navigationController popToViewController:self.navigationController.viewControllers[1] animated:YES];
		return;
	}
	[SVProgressHUD showInView:self.view];
	NSURLSessionDataTask *task = [YJRouter commonDelete:CancelPackageApi params:@{@"orderNo": no} callback:^(YJRouterResponseModel *result) {
		if (result.code != 0) {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		} else {
			[SVProgressHUD showSuccessInView:self.view withStatus:result.msg];
			dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
				[self.navigationController popToViewController:self.navigationController.viewControllers[1] animated:YES];
			});
		}
	}];
	[self weakHoldTask:task];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	
	
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	if (self.autoRefresh && !self.detailOrderModel) {
		self.autoRefresh = NO;
		[self sendNetwork];
	} else {
		if (_bools.editable) {
			self.detailOrderModel = _detailOrderModel;
			[self.tableView reloadData];
			if (self.detailOrderModel.orderchannel) {
				[self resetPrice];
			}
		}
	}
	[self.navigationItem.leftBarButtonItem changeTarget:self action:@selector(clickOnBack)];
}

- (void)setDetailOrderModel:(YJDetailOrderModel *)detailOrderModel
{
	_detailOrderModel = detailOrderModel;
	for (NSInteger i=0; i<detailOrderModel.merchandiseList.count; i++) {
		if (_bools.shrinkable) {
			self.sectionRowCount[@(i+1)] = @1;
		} else {
			self.sectionRowCount[@(i+1)] = @(detailOrderModel.merchandiseList[i].readyGoodsList.count + 1);
		}
	}
	[self resetHeader];
	[self resetCommitUIContent];
}

/**
 刷新价格
 */
- (void)resetPrice
{
	NSMutableDictionary *params = @{}.mutableCopy;
	NSArray *ids = [self.detailOrderModel.merchandiseList valueForKeyPath:@"id"];
	if (!ids || ids.count == 0) return [SVProgressHUD showErrorInView:self.view withStatus:@"缺少包裹信息"];
	params[@"inventorys"] = ids;
	NSString *transform = self.detailOrderModel.orderchannel.orderTypeCode;
	if (!transform || transform.length == 0) return [SVProgressHUD showErrorInView:self.view withStatus:@"缺少运输服务信息"];
	params[@"ordertype"] = transform;
	NSNumber *warehouseId = self.detailOrderModel.orderchannel.warehouseId;
	if (!warehouseId) return [SVProgressHUD showErrorInView:self.view withStatus:@"缺少仓库信息"];
	params[@"warehouseId"] = warehouseId;
	params[@"couponCode"] = self.detailOrderModel.couponList.code ?: @"removeCoupon";
	NSMutableDictionary *orderDetail = @{}.mutableCopy;
	orderDetail[@"numberInventory"] = @(self.detailOrderModel.merchandiseList.count);
	orderDetail[@"useKD100"] = @NO;
	orderDetail[@"autoConfirm"] = @NO;
	orderDetail[@"estimatedWeight"] = self.detailOrderModel.realWeight;
	params[@"orderdetail"] = orderDetail;
	
	params[@"ordertypeId"] = self.detailOrderModel.orderchannel.orderTypeId ?: @"";
	params[@"leadId"] = self.detailOrderModel.orderchannel.leadId ?: @"";
	
	self.detailOrderModel.orderNo ? params[@"orderNo"] = self.detailOrderModel.orderNo : NULL;
	
	[SVProgressHUD showInView:self.view];
	NSURLSessionDataTask *task = [YJRouter commonPostModel:CalculateOrderPayApi params:params modelClass:nil callback:^(YJRouterResponseModel *result) {
		if (result.code != 0) {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		} else {
			[SVProgressHUD dismissFromView:self.view];
			self.detailOrderModel.realcost = result.data[@"money"][@"totalAmount"];
			self.detailOrderModel.couponNumber = [result.data[@"couponNumber"] intValue];
			self.detailOrderModel.valueAddedlist = result.data[@"valueAddedlist"];
			self.detailOrderModel.realWeight = result.data[@"weight"];
			[self resetCommitUIContent];
			[self.tableView reloadSection:[self.tableView numberOfSections]-1 withRowAnimation:UITableViewRowAnimationNone];
		}
	}];
	[self weakHoldTask:task];
}

/**
 获取详情
 */
- (void)sendNetwork
{
	[SVProgressHUD showInView:self.view];
	NSURLSessionDataTask *task = [YJRouter commonGetModel:[OrderDetailApi stringByAppendingPathComponent:self.model.no] params:nil modelClass:[YJDetailOrderModel class] callback:^(YJRouterResponseModel *result) {
		if (result.code == 0) {
			[SVProgressHUD dismissFromView:self.view];
			YJDetailOrderModel *model = (YJDetailOrderModel *)result.data;
			[self.sectionRowCount removeAllObjects];
			
			if (self->_bools.shrinkable && self.model.type != 3) self->_bools.shrinkable = model.merchandiseList.count > 1;
			
			self.detailOrderModel = model;
			[self.tableView reloadData];
			if (_bools.editable) {
				[self resetPrice];
			}
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
}

- (void)clickOnBack
{
	if (_bools.popToList) {
		[self.navigationController popToViewController:self.navigationController.viewControllers[1] animated:YES];
	} else {
		[self.navigationController popViewControllerAnimated:YES];
	}
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	return 2 + self.sectionRowCount.count + !self->_bools.editable;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	if (section == 0) {
		return self.detailOrderModel.address != nil || (_bools.editable && self.detailOrderModel);
	}
	else if (section == [tableView numberOfSections] - 1) {
		if (self.detailOrderModel.merchandiseList) {
			return 2 + _bools.showLastCost;
		} else {
			return 0;
		}
	}
	else if (!self->_bools.editable && section == [tableView numberOfSections] - 2) {
		return self.detailOrderModel ? 2 : 0;
	}
	else if (section != [tableView numberOfSections] - 1) {
		return [_sectionRowCount[@(section)] integerValue];
	}
	return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell;
	if (indexPath.section == 0) {
		cell = [self generateAddressCellForIndexPath:indexPath];
	}
	else if (!self->_bools.editable && indexPath.section == [tableView numberOfSections] - 2) {
		cell = [self setupCommonCellForIndexPath:indexPath];
		UILabel *descLabel = objc_getAssociatedObject(cell, "descLabel");
		if (indexPath.row == 0) {
			cell.textLabel.text = @"入库总重量";
			if (descLabel) {
				descLabel.text = [NSString stringWithFormat:@"%.2fkg", self.detailOrderModel.realWeight.doubleValue];
			}
			cell.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
		} else {
			cell.textLabel.text = @"出库总重量";
			if (descLabel) {
				descLabel.text = [NSString stringWithFormat:@"%@", self.detailOrderModel.inWeight ?: @"0.00kg"];
			}
		}
	}
	else if (indexPath.section != [tableView numberOfSections] - 1) {
		if (indexPath.row > 0) {
			cell = [self generateGoodsCellForIndexPath:indexPath];
		} else {
			cell = [self generatePackageCellForIndexPath:indexPath];
		}
	}
	else if (indexPath.row == 0) {
		cell = [self setupCommonCellForIndexPath:indexPath];
		cell.textLabel.text = self.detailOrderModel.orderchannel.code ?: @"渠道";
		UILabel *descLabel = objc_getAssociatedObject(cell, "descLabel");
		if (descLabel) {
			descLabel.text = self.detailOrderModel.orderchannel.priceWeight;
		}
		if (cell.edgeLineViews.count < 2) {
			cell.edgeLines = UIEdgeInsetsMake(0.3, 0, 0.3, 0);
		}
	} else if (indexPath.row == 1) {
		cell = [self setupCommonCellForIndexPath:indexPath];
		cell.textLabel.text = @"优惠券";
		UILabel *descLabel = objc_getAssociatedObject(cell, "descLabel");
		if (descLabel) {
			if (self.detailOrderModel.couponList && self.detailOrderModel.couponList.coupon) {
				descLabel.text = self.detailOrderModel.couponList.coupon.name;
			} else {
				if (self->_bools.editable) {
					descLabel.text = [NSString stringWithFormat:@"有%zd张可用", self.detailOrderModel.couponNumber];
				} else {
					descLabel.text = [NSString stringWithFormat:@"未使用"];
				}
			}
		}
	}
	else if (indexPath.row == 2) {
		cell = [self setupCommonCellForIndexPath:indexPath];
		cell.textLabel.text = @"运费";
		UILabel *descLabel = objc_getAssociatedObject(cell, "descLabel");
		if (descLabel) {
			descLabel.text = [NSString stringWithFormat:@"总运费%.2f元，实际支付%.2f元", self.detailOrderModel.originalCost.doubleValue, self.detailOrderModel.realcost.doubleValue];
		}
	}
	return cell;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	UIEdgeInsets margins = cell.layoutMargins;
	margins.left = 20;
	margins.right = 20;
	cell.layoutMargins = margins;
}

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (_bools.onlyEditAddressable && indexPath.section == 0) return YES;
	if (!_bools.editable) return NO;
	if (indexPath.section + indexPath.row == 0) return YES;
	if (indexPath.section == [tableView numberOfSections]-1) return YES;
	return NO;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	if (indexPath.section == 0 && indexPath.row == 0) {
		if (_bools.onlyEditAddressable) {
			YJEditAddressViewController *editAddress = [YJEditAddressViewController new];
			editAddress.title = @"上传身份证";
			editAddress.type = YJEditAddressCard;
			editAddress.addressModel = self.detailOrderModel.address;
			[self.navigationController pushViewController:editAddress animated:YES];
		} else {
			YJAdressViewController *address = [YJAdressViewController addressWithShowType: 1];
			address.selectId = self.detailOrderModel.address.id;
			address.title = @"选择地址";
			@weakify(self)
			[address setSelectedCallback:^(YJAddressModel *model) {
				@strongify(self)
				self.detailOrderModel.address = model;
				[tableView reloadSection:0 withRowAnimation:UITableViewRowAnimationNone];
			}];
			[self.navigationController pushViewController:address animated:YES];
		}
	} else if (indexPath.section == [tableView numberOfSections] - 1 ) {
		if (indexPath.row == 0) {
			YJTransferModel *transfer = self.detailOrderModel.orderchannel;
			if (!transfer || !transfer.warehouseId || !self.detailOrderModel.realWeight) {
				return [SVProgressHUD showErrorInView:self.view withStatus:@"订单信息有误"];
			}
			YJTransferViewController *transferVc = [YJTransferViewController instanceWithWarehouseId:transfer.warehouseId weight:self.detailOrderModel.realWeight ?: @0 model:self.detailOrderModel.orderchannel];
			[transferVc setTextForBottom:@"保存"];
			@weakify(self)
			[transferVc setCallback:^(YJTransferModel *model) {
				@strongify(self)
				self.detailOrderModel.orderchannel = model;
				self.detailOrderModel.couponList = nil;
			}];
			[self.navigationController pushViewController:transferVc animated:YES];
		} else if (indexPath.row == 1) {
			YJCouponViewController *couponVc = [YJCouponViewController new];
			CGFloat money = self.detailOrderModel.realcost.doubleValue + self.detailOrderModel.couponList.coupon.discountAmount.doubleValue;
			[couponVc appendParams:[NSString stringWithFormat:@"%f", money] warehouse:self.detailOrderModel.orderchannel.warehouseId transport:self.detailOrderModel.orderchannel.orderTypeId];
			[couponVc setCallback:^(YJCouponModel *model){
				self.detailOrderModel.couponList = model;
			}];
			[couponVc setCancelCallback:^{
				self.detailOrderModel.couponList = nil;
			}];
			[self.navigationController pushViewController:couponVc animated:YES];
		}
	}
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
	return 9.9;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
	return 0.1;
}

- (UITableViewCell *)setupCommonCellForIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:@"common" forIndexPath:indexPath];
	cell.textLabel.font = [UIFont systemFontOfSize:13];
	cell.textLabel.textColor = [UIColor colorWithRGB:0x666666];
	BOOL last = indexPath.row == 2;
	if (last || !_bools.editable) {
		cell.accessoryType = UITableViewCellAccessoryNone;
	} else {
		cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
	}
	
	UILabel *descLabel = objc_getAssociatedObject(cell, "descLabel");
	if (!descLabel) {
		descLabel = [UILabel new];
		[cell.contentView addSubview:descLabel];
		objc_setAssociatedObject(cell, "descLabel", descLabel, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
		descLabel.font = [UIFont systemFontOfSize:12];
		descLabel.textColor = [UIColor colorWithRGB:0x999999];
	}
	[descLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo((last || !_bools.editable) ? -20 : 0);
		make.height.mas_equalTo(43);
		make.top.bottom.mas_equalTo(0);
		make.left.mas_greaterThanOrEqualTo(80);
	}];
	if (cell.edgeLineViews.count == 0 || cell.edgeLineViews.count > 1) {
		cell.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
	}
	cell.imageView.image = nil;
	return cell;
}

- (UITableViewCell *)generateAddressCellForIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell = nil;
	if (self.detailOrderModel.address) {
		cell = [self.tableView dequeueReusableCellWithIdentifier:@"address" forIndexPath:indexPath];
		if (cell.edgeLineViews.count == 0) {
			cell.edgeLines = UIEdgeInsetsMake(0.3, 0, 5, 0);
			UIView *view = cell.edgeLineViews[1];
			view.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"edge"]];
		}
		[(YJCommonAddressTableViewCell *)cell setModel:self.detailOrderModel.address];
	} else if (_bools.editable || _bools.editAddressable) {
		cell = [self setupCommonCellForIndexPath:indexPath];
		UILabel *descLabel = objc_getAssociatedObject(cell, "descLabel");
		if (descLabel) {
			descLabel.text = @"";
		}
		cell.textLabel.text = @"添加地址";
		if (cell.edgeLineViews.count < 2) {
			cell.edgeLines = UIEdgeInsetsMake(0.3, 0, 0.3, 0);
		}
		cell.imageView.image = [UIImage imageNamed:@"address_add"];
	} else {
		cell = [UITableViewCell new];
	}
	
	cell.accessoryType = (_bools.editable || _bools.editAddressable) ? UITableViewCellAccessoryDisclosureIndicator : UITableViewCellAccessoryNone;
	
	return cell;
}

- (UITableViewCell *)generatePackageCellForIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell = nil;
	cell = [self.tableView dequeueReusableCellWithIdentifier:@"nick" forIndexPath:indexPath];
	YJCommonTableViewCell *ccell = (YJCommonTableViewCell *)cell;
	
	YJMerchandiseModel *merchandise = (YJMerchandiseModel *)self.detailOrderModel.merchandiseList[indexPath.section-1];
	
	if (cell.edgeLineViews.count == 0) {
		cell.edgeLines = UIEdgeInsetsMake(0.3, 0, 0.3, 0);
	}
	NSString *name;
	if (!merchandise.nickname || merchandise.nickname.length == 0) {
		name = @"包裹昵称：未命名";
	} else {
		name = [NSString stringWithFormat:@"包裹昵称：%@", merchandise.nickname];
	}
	ccell.nameLabel.text = name;
	ccell.subnameLabel.text = [@"运单号：" stringByAppendingString:merchandise.bikeUPS ?: merchandise.carrierNo ?: @""];
	ccell.ssubnameLabel.text = [NSString stringWithFormat:@"包裹重量：%.2fkg", merchandise.weight.floatValue];
	ccell.editButton.hidden = !_bools.editable;
	ccell.iconImageView.image = [UIImage imageNamed:@"package_category"];
	ccell.moreInfoButton.hidden = !_bools.shrinkable;
	ccell.showing = [self.sectionRowCount[@(indexPath.section)] integerValue] - 1;
	@weakify(self)
	[ccell setClickOnShowMore:^(BOOL showing) {
		@strongify(self)
		if ([self.sectionRowCount[@(indexPath.section)] integerValue] == 1) {
			self.sectionRowCount[@(indexPath.section)] = @([merchandise.readyGoodsList count] + 1);
		} else {
			self.sectionRowCount[@(indexPath.section)] = @1;
		}
		[self.tableView reloadSection:indexPath.section withRowAnimation:UITableViewRowAnimationAutomatic];
	}];
	[ccell setClickOnEdit:^(BOOL editing) {
		@strongify(self)
		YJShowPackageViewController *vc = [YJShowPackageViewController instanceWithEditable:YES model:self.model];
		[vc hideBottom];
		vc.detailOrderModel = self.detailOrderModel;
		vc.title = @"编辑包裹";
		[self.navigationController pushViewController:vc animated:YES];
	}];
	
	return cell;
}

- (UITableViewCell *)generateGoodsCellForIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell = nil;
	cell = [self.tableView dequeueReusableCellWithIdentifier:@"goods" forIndexPath:indexPath];
	YJCommonGoodsTableViewCell *gcell = (YJCommonGoodsTableViewCell *)cell;
	YJMerchandiseModel *package = self.detailOrderModel.merchandiseList[indexPath.section - 1];
	YJGoodsModel *goods = package.readyGoodsList[indexPath.row - 1];
	gcell.nameLabel.text = goods.productNameCNY;
	gcell.leftSubnameLabel.text = [NSString stringWithFormat:@"总价：%.2f%@", goods.amount.doubleValue, [goods.currencyName transformToCurrency] ?: @""];
	gcell.rightSubnameLabel.text = [NSString stringWithFormat:@"数量：%zd%@", goods.stock.integerValue, goods.units ?: @""];
	gcell.deleteBtn.hidden = YES;
	if (gcell.edgeLineViews.count == 0) {
		gcell.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
	}
	
	return cell;
}

@end
