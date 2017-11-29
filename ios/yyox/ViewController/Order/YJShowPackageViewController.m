//
//  YJShowPackageViewController.m
//  yyox
//
//  Created by ddn on 2017/2/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJShowPackageViewController.h"
#import "YJLineImageViews.h"
#import "YJCommonWithFooterTableViewCell.h"
#import "YJCommonGoodsTableViewCell.h"
#import "YJAdressViewController.h"
#import "UIWindow+YJExtension.h"
#import "YJEditGoodsView.h"
#import "YJLargeImageViewController.h"
#import "YJSwitchModel.h"

@interface YJShowPackageViewController () <UITableViewDelegate, UITableViewDataSource, YJLineImageViewsDelegate>

@property (strong, nonatomic) UITableView *tableView;

@property (strong, nonatomic) YJLineImageViews *currentImageViews;

@property (strong, nonatomic) NSMutableArray *largeModels;

@property (strong, nonatomic) NSMutableDictionary *sectionRowCount;//goods个数

@property (assign, nonatomic) BOOL editable;
@property (strong, nonatomic) YJToDoModel *model;

@property (strong, nonatomic) UIButton *doneBtn;

@property (strong, nonatomic) YJEditGoodsView *editView;

@property (strong, nonatomic) NSIndexPath *currentEditIndexPath;

@end

@implementation YJShowPackageViewController
@synthesize detailOrderModel;


+ (YJShowPackageViewController *)instanceWithEditable:(BOOL)editable model:(YJToDoModel *)model
{
	YJShowPackageViewController *vc = [self new];
	vc.editable = editable;
	vc.model = model;
	[vc setupUI];
	
	vc.title = @"待处理";
	return vc;
}

- (YJEditGoodsView *)editView
{
	if (!_editView) {
		_editView = [YJEditGoodsView editGoodsView];
		_editView.layer.anchorPoint = CGPointMake(0.5, 1);
		_editView.layer.position = CGPointMake(kScreenWidth/2, kScreenHeight);
		_editView.layer.bounds = CGRectMake(0, 0, kScreenWidth, 441);
		@weakify(self)
		[_editView setClickOnDone:^(YJGoodsModel *preGoods, NSDictionary *params, YJGoodsModel *selectGoods){
			@strongify(self)
			[self editGoodsWithPreGoods:preGoods selectGoods:selectGoods params:params];
		}];
		[NSDC addObserver:self selector:@selector(dismissEditView) name:UIWindowClickOnAnimationContainer object:nil];
	}
	return _editView;
}

- (YJDetailOrderModel *)detailOrderModel
{
	if (!detailOrderModel) {
		detailOrderModel = [YJDetailOrderModel new];
	}
	return detailOrderModel;
}

- (NSMutableDictionary *)sectionRowCount
{
	if (!_sectionRowCount) {
		_sectionRowCount = [NSMutableDictionary dictionary];
	}
	return _sectionRowCount;
}

- (NSMutableArray *)largeModels
{
	if (!_largeModels) {
		_largeModels = [NSMutableArray array];
	}
	return _largeModels;
}

- (void)setupUI
{
	_tableView = [[UITableView alloc] initWithFrame:self.view.bounds style:UITableViewStyleGrouped];
	[self.view addSubview:_tableView];
	[_tableView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.edges.mas_equalTo(0);
	}];
	_tableView.dataSource = self;
	_tableView.delegate = self;
	_tableView.rowHeight = UITableViewAutomaticDimension;
	_tableView.estimatedRowHeight = 44;
	
	[_tableView registerClass:[YJCommonWithFooterTableViewCell class] forCellReuseIdentifier:@"nick"];
	[_tableView registerClass:[YJCommonGoodsTableViewCell class] forCellReuseIdentifier:@"goods"];
	[_tableView setContentInset:UIEdgeInsetsMake(0, 0, self.editable ? 43 : 0, 0)];
	
	if (self.editable) {
		_doneBtn = [UIButton new];
		[self.view addSubview:_doneBtn];
		[_doneBtn addTarget:self action:@selector(clickOnDone) forControlEvents:UIControlEventTouchUpInside];
		[_doneBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
		[_doneBtn setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_normal"] forState:UIControlStateNormal];
		[_doneBtn setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_highlighted"] forState:UIControlStateHighlighted];
		[_doneBtn setTitle:self.model.type == 1 ? @"创建订单" : @"保存" forState:UIControlStateNormal];
		_doneBtn.titleLabel.font = [UIFont systemFontOfSize:14];
		[_doneBtn mas_makeConstraints:^(MASConstraintMaker *make) {
			make.left.bottom.right.mas_equalTo(0);
			make.height.mas_equalTo(49);
		}];
	}
}

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)showBottom
{
	_doneBtn.hidden = NO;
}

- (void)hideBottom
{
	_doneBtn.hidden = YES;
}

- (void)addCancelOpt
{
	self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"取消合箱" style:UIBarButtonItemStyleDone target:self action:@selector(clickOnCancel)];
}

- (void)clickOnCancel
{
	[self.navigationController popToViewController:self.navigationController.viewControllers[1] animated:YES];
}

/**
 创建订单
 */
- (void)clickOnDone
{
	if ([self.doneBtn.titleLabel.text isEqualToString:@"创建订单"]) {
		if (!self.detailOrderModel.merchandiseList || self.detailOrderModel.merchandiseList.count == 0) return;
		NSArray *merchandises = self.detailOrderModel.merchandiseList;
		if (!merchandises || merchandises.count == 0) {
			return [SVProgressHUD showInfoInView:self.view withStatus:@"缺少包裹信息"];
		}
		CGFloat weight = 0;
		for (YJMerchandiseModel *merchandise in merchandises) {
			if (merchandise.goodList.count == 0) {
				return [SVProgressHUD showInfoInView:self.view withStatus:@"缺少商品信息"];
			}
			if (merchandise.unreadyGoodsList.count > 0) {
				[SVProgressHUD showInfoInView:self.view withStatus:@"您还有待完善的商品申报信息"];
				return;
			}
			weight += merchandise.weight.doubleValue;
		}
		YJAdressViewController *address = [YJAdressViewController addressWithShowType: 2];
		address.selectId = nil;
		
		self.detailOrderModel.realWeight = @(weight);
		[YJGlobalWeakHoldManager yj_addValue:self.detailOrderModel];
		[self.navigationController pushViewController:address animated:YES];
		address.title = @"选择地址";
	}
}

- (void)setDetailOrderModel:(YJDetailOrderModel *)model
{
	detailOrderModel = model;
	if (self.editable || !self.shrinkable) {
		[self unfold:model];
	}
}

- (void)unfold:(YJDetailOrderModel *)detailModel
{
	for (NSInteger i=0; i<detailOrderModel.merchandiseList.count; i++) {
		if (self.shrinkable) {
			self.sectionRowCount[@(i)] = @0;
		} else {
			self.sectionRowCount[@(i)] = @(detailOrderModel.merchandiseList[i].readyGoodsList.count);
		}
	}
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	if (detailOrderModel.merchandiseList.count > 0) return;
	
	if (self.autoRefresh && self.model) {
		self.autoRefresh = NO;
		[SVProgressHUD showInView:self.view];
		if ((!self.model.inventorys || self.model.inventorys.count == 0) && self.model.type != 1 && self.model.no) {
			[self getOrderDetail];
		}
		else {
			[self getPacketDetail];
		}
	}
}

/**
 获取订单详情
 */
- (void)getOrderDetail
{
	NSURLSessionDataTask *task = [YJRouter commonGetModel:[OrderDetailApi stringByAppendingPathComponent:self.model.no] params:nil modelClass:[YJDetailOrderModel class] callback:^(YJRouterResponseModel *result) {
		if (result.code == 0) {
			[SVProgressHUD dismissFromView:self.view];
			YJDetailOrderModel *model = (YJDetailOrderModel *)result.data;
			if (self.shrinkable) {
				self.shrinkable = model.merchandiseList.count > 1;
			}
			[self.sectionRowCount removeAllObjects];
			self.detailOrderModel = model;
			[self unfold:self.detailOrderModel];
			[self.tableView reloadData];
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
}

/**
 获取包裹详情
 */
- (void)getPacketDetail
{
	NSURLSessionDataTask *task = [YJRouter commonPostRefresh:PackageDetailApi currentCount:0 pageSize:0 otherParmas:@{@"inventorys": self.model.inventorys.count == 0 ? @[self.model.inventoryId ?: @""] : self.model.inventorys} modelClass:[YJMerchandiseModel class] callback:^(YJRouterResponseModel *result) {
		if (result.code == 0 || result.code == 999) {
			[SVProgressHUD dismissFromView:self.view];
			self.detailOrderModel.merchandiseList = result.data;
			[self unfold:self.detailOrderModel];
			[self.tableView reloadData];
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
}

/**
 检测参数

 @param params 参数
 @return 是否有误
 */
- (BOOL)checkEditGoodsParams:(NSDictionary *)params
{
	NSString *brandCNY = params[@"brandCNY"];
	NSString *productNameCNY = params[@"productNameCNY"];
	NSNumber *amount = params[@"amount"];
	NSString *stock = params[@"stock"];
	
	NSString *msg = nil;
	if (!brandCNY || brandCNY.length == 0 || ![YJRegexte isOnlyChinese:brandCNY]) {
		msg = @"请填写汉字组成的品牌";
	} else if (!productNameCNY || productNameCNY.length == 0 || ![YJRegexte isOnlyChinese:productNameCNY]) {
		msg = @"请填写汉字组成的商品名称";
	} else if (!amount || ![amount doubleValue]) {
		msg = @"总价不正确";
	} else if ([amount integerValue] > 99999999) {
		msg = @"商品总价超过限制";
	} else if (!stock) {
		msg = @"请选择包裹内件";
	}
	if (msg) {
		[SVProgressHUD showInfoInView:self.editView withStatus:msg];
		return NO;
	}
	return YES;
}


/**
 编辑包裹

 @param preGoods 之前的商品
 @param selectGoods 当前商品
 @param params 参数
 */
- (void)editGoodsWithPreGoods:(YJGoodsModel *)preGoods selectGoods:(YJGoodsModel *)selectGoods params:(NSDictionary *)params
{
	BOOL checkResult = [self checkEditGoodsParams:params];
	if (!checkResult) return;
	
	[SVProgressHUD showInView:self.editView];
	NSURLSessionDataTask *task = [YJRouter commonGetModel:UpdatePackageApi params:params modelClass:nil callback:^(YJRouterResponseModel *result) {
		if (result.code != 0) {
			[SVProgressHUD showErrorInView:self.editView withStatus:result.msg];
		} else {
			[SVProgressHUD dismissFromView:self.editView];
			if (preGoods) {//修改
				if ([preGoods.id isEqualToNumber:params[@"id"]]) {
					//没有更改upc
				} else {
					preGoods.goolsType = 0;
					selectGoods.goolsType = 1;
				}
			} else {//新增
				selectGoods.goolsType = 1;
				self.sectionRowCount[@(self.currentEditIndexPath.section)] = @([self.sectionRowCount[@(self.currentEditIndexPath.section)] integerValue] + 1);
			}
			[selectGoods setValuesForKeysWithDictionary:params];
			
			[self dismissEditView];
			
			[self.tableView reloadSection:self.currentEditIndexPath.section withRowAnimation:UITableViewRowAnimationAutomatic];
		}
	}];
	[self weakHoldTask:task];
}

- (void)dismissEditView
{
	@weakify(self)
	[self.view.window dismissWithAnimation:^(UIView *container, dispatch_block_t finishAnimate) {
		@strongify(self)
		[UIView animateWithDuration:0.25 animations:^{
			self.editView.transform = CGAffineTransformMakeScale(1, 0.01);
		} completion:^(BOOL finished) {
			[self.editView removeFromSuperview];
			finishAnimate();
		}];
	}];
}

- (void)showEditView
{
	self.editView.merchandise = self.detailOrderModel.merchandiseList[self.currentEditIndexPath.section];
	[self.editView reloadData];
	@weakify(self)
	[self.view.window showWithAnimation:^(UIView *container, dispatch_block_t finishAnimate) {
		@strongify(self)
		[container addSubview:self.editView];
		self.editView.transform = CGAffineTransformMakeScale(1, 0.01);
		[UIView animateWithDuration:0.25 animations:^{
			self.editView.transform = CGAffineTransformIdentity;
		} completion:^(BOOL finished) {
			finishAnimate();
		}];
	}];
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	cell.separatorInset = UIEdgeInsetsZero;
	cell.layoutMargins = UIEdgeInsetsZero;
	cell.preservesSuperviewLayoutMargins = NO;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	return self.detailOrderModel.merchandiseList.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	NSInteger count = [_sectionRowCount[@(section)] integerValue] + 1 + self.editable + 1;//goods + 标题 + 添加 + 包裹
	return count;
}

- (UITableViewCell *)getPackageCellForIndexPath:(NSIndexPath *)indexPath
{
	YJMerchandiseModel *merchandise = self.detailOrderModel.merchandiseList[indexPath.section];
	UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:@"nick" forIndexPath:indexPath];
	YJCommonWithFooterTableViewCell *fcell = (YJCommonWithFooterTableViewCell *)cell;
	fcell.hasShowOpt = self.shrinkable;
	fcell.subNameLabel.text = [@"运单号：" stringByAppendingString: merchandise.carrierNo ?: merchandise.bikeUPS];
	fcell.ssubNameLabel.text = [NSString stringWithFormat:@"包裹重量：%.2fkg", merchandise.weight.doubleValue];
	NSString *name;
	if (!merchandise.nickname || merchandise.nickname.length == 0) {
		name = [NSString stringWithFormat:@"包裹昵称：未命名"];
	} else {
		name = [NSString stringWithFormat:@"包裹昵称：%@", merchandise.nickname];
	}
	fcell.nameLabel.text = name;
	fcell.leftLabel.text = @"订单截图";
	fcell.editButton.hidden = !self.editable;
	[fcell.imageViews reset];
	for (NSInteger i=0; i<merchandise.orderSreenshot.count; i++) {
		UIImageView *imageView = [fcell.imageViews addImage:[UIImage imageNamed:@"order_placeholder"]];
		
		[imageView yj_setBase64Image:merchandise.orderSreenshot[i] thumbnail:YES placeHolder:[UIImage imageNamed:@"order_placeholder"]];
	}
	if (merchandise.orderSreenshot.count == 0) {
		[fcell hideFooter];
	} else {
		[fcell showFooter];
	}
	
	fcell.imageViews.delegate = self;
	fcell.showing = [self.sectionRowCount[@(indexPath.section)] integerValue];
	@weakify(self)
	[fcell setClickOnShowMore:^(BOOL showing) {
		@strongify(self)
		if ([self.sectionRowCount[@(indexPath.section)] integerValue] == 0) {
			self.sectionRowCount[@(indexPath.section)] = @(merchandise.readyGoodsList.count);
		} else {
			self.sectionRowCount[@(indexPath.section)] = @0;
		}
		[self.tableView reloadSection:indexPath.section withRowAnimation:UITableViewRowAnimationAutomatic];
	}];
	
	[fcell setClickOnEdit:^{
		@strongify(self)
		UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"修改昵称" message:nil preferredStyle:UIAlertControllerStyleAlert];
		[alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
			textField.placeholder = @"输入名称";
			textField.tintColor = [UIColor colorWithRGB:0x323131];
			textField.textColor = [UIColor colorWithRGB:0x323131];
		}];
		@weakify(alertController)
		UIAlertAction *sure = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
			@strongify(alertController)
			NSString *text = alertController.textFields.firstObject.text;
			[SVProgressHUD showInView:self.view];
			NSURLSessionDataTask *task = [YJRouter commonPostModel:UpdateNickNameApi params:@{@"id": merchandise.id ?: @"", @"nickName": text ?: @""} modelClass:nil callback:^(YJRouterResponseModel *result) {
				if (result.code == 0) {
					merchandise.nickname = text;
					[self.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
					[SVProgressHUD showSuccessInView:self.view withStatus:result.msg];
				} else {
					[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
				}
			}];
			[self weakHoldTask:task];
		}];
		UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
			
		}];
		[alertController addAction:sure];
		[alertController addAction:cancel];
		
		[self presentViewController:alertController animated:YES completion:^{
			
		}];
	}];
	return cell;
}

- (UITableViewCell *)getGoodsCellForIndexPath:(NSIndexPath *)indexPath
{
	YJMerchandiseModel *merchandise = self.detailOrderModel.merchandiseList[indexPath.section];
	YJGoodsModel *goods = [merchandise.readyGoodsList objectOrNilAtIndex:indexPath.row-1-1];
	UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:@"goods" forIndexPath:indexPath];
	YJCommonGoodsTableViewCell *gcell = (YJCommonGoodsTableViewCell *)cell;
	gcell.deleteBtn.hidden = !self.editable;
	gcell.nameLabel.text = goods.productNameCNY;
	gcell.leftSubnameLabel.text = [NSString stringWithFormat:@"总价：%.2f%@", goods.amount.doubleValue, [goods.currencyName transformToCurrency] ?: @""];
	gcell.rightSubnameLabel.text = [NSString stringWithFormat:@"数量：%zd%@", goods.stock.integerValue, goods.units];
	@weakify(self)
	[gcell setClickOnDelete:^{
		@strongify(self)
		[SVProgressHUD showInView:self.view];
		NSURLSessionDataTask *task = [YJRouter commonGetModel:DeleteGoodsApi params:@{@"goodsId": goods.id} modelClass:nil callback:^(YJRouterResponseModel *result) {
			if (result.code != 0) {
				[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
			} else {
				[SVProgressHUD dismissFromView:self.view];
				goods.goolsType = 0;
				self.sectionRowCount[@(indexPath.section)] = @(merchandise.readyGoodsList.count);
				[self.tableView reloadSection:indexPath.section withRowAnimation:UITableViewRowAnimationAutomatic];
			}
		}];
		[self weakHoldTask:task];
	}];
	return cell;
}

- (UITableViewCell *)getGoodsPlaceholderCellFroIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:@"commonCell"];
	if (!cell) {
		cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"commonCell"];
		cell.textLabel.textAlignment = NSTextAlignmentLeft;
		[cell.textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
			make.top.mas_equalTo(10);
			make.bottom.mas_equalTo(-10);
			make.left.mas_equalTo(15);
		}];
		cell.textLabel.font = [UIFont systemFontOfSize:14];
		cell.textLabel.textColor = [UIColor colorWithRGB:0x666666];
	}
	cell.textLabel.text = @"商品信息";
	return cell;
}

- (UITableViewCell *)getAddCellForIndexPath:(NSIndexPath *)indexPath
{
	YJMerchandiseModel *merchandise = self.detailOrderModel.merchandiseList[indexPath.section];
	UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:@"add"];
	if (!cell) {
		cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"add"];
		cell.textLabel.textAlignment = NSTextAlignmentCenter;
		cell.textLabel.font = [UIFont systemFontOfSize:14];
		cell.textLabel.textColor = [UIColor colorWithRGB:0x1b82d2];
	}
	if (merchandise.unreadyGoodsList.count > 0) {
		cell.textLabel.text = [NSString stringWithFormat:@"完善商品申报信息(%zd)", merchandise.unreadyGoodsList.count];
	} else {
		cell.textLabel.text = nil;
	}
	return cell;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell = nil;
	
	if (indexPath.row == 0) {
		cell = [self getPackageCellForIndexPath:indexPath];
	}
	else if (indexPath.row == 1) {
		cell = [self getGoodsPlaceholderCellFroIndexPath:indexPath];
	}
	else if ((self.editable && indexPath.row != [tableView numberOfRowsInSection:indexPath.section] - 1) || !self.editable) {
		YJMerchandiseModel *merchandise = self.detailOrderModel.merchandiseList[indexPath.section];
		YJGoodsModel *goods = [merchandise.readyGoodsList objectOrNilAtIndex:indexPath.row-1-1];
		if (goods) {
			cell = [self getGoodsCellForIndexPath:indexPath];
		}
	} else {
		cell = [self getAddCellForIndexPath:indexPath];
	}
	
	return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	YJMerchandiseModel *merchandise = self.detailOrderModel.merchandiseList[indexPath.section];
	if (indexPath.row == 0) {
		return merchandise.orderSreenshot.count == 0 ? 73.5 : 141.5;
	} else if (self.editable && merchandise.unreadyGoodsList.count == 0 && indexPath.row == [tableView numberOfRowsInSection:indexPath.section] - 1) {
		return 0;
	} else {
		return UITableViewAutomaticDimension;
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

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (!self.editable) return NO;
	
	return indexPath.row > 1;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	if (indexPath.row != [tableView numberOfRowsInSection:indexPath.section] - 1) {
		self.editView.goods = self.detailOrderModel.merchandiseList[indexPath.section].readyGoodsList[indexPath.row-1-1];
	} else {
		if (self.detailOrderModel.merchandiseList[indexPath.section].unreadyGoodsList.count == 0) {
			return [SVProgressHUD showInfoInView:self.view withStatus:@"没有待申报的商品"];
		}
		self.editView.goods = nil;
	}
	self.currentEditIndexPath = indexPath;
	[self showEditView];
}

- (void)lineImageViews:(YJLineImageViews *)lineImageViews clickOnIdx:(NSInteger)idx
{
	if (![self.currentImageViews isEqual:lineImageViews]) {
		[self.largeModels removeAllObjects];
		for (NSInteger i=0; i<lineImageViews.images.count; i++) {
			UIImageView *imageView = [lineImageViews imageViewAtIdx:i];
			YJSwitchModel *switchModel = [YJSwitchModel new];
			switchModel.w = imageView.image.size.width;
			switchModel.h = imageView.image.size.height;
			switchModel.image = imageView.image;
			switchModel.largeImage = imageView.urlStr;
			[self.largeModels addObject:switchModel];
		}
	}
	
	self.currentImageViews = lineImageViews;
	YJLargeImageViewController *largeVc = [YJLargeImageViewController small_largeViewControllerWithModels:self.largeModels cellClass:nil];
	largeVc.currentIndexPath = [NSIndexPath indexPathForItem:idx inSection:0];
	[self.navigationController pushViewController:largeVc animated:YES];
	
}

@end
