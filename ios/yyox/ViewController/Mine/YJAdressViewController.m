//
//  YJAdressViewController.m
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJAdressViewController.h"
#import "YJAddressViewModel.h"
#import "YJAdressCell.h"

#import "YJEditAddressViewController.h"
#import "YJSendInnerViewController.h"
#import "YJTransferViewController.h"

#import "YJDetailOrderModel.h"

@interface YJAddressFooter : UICollectionReusableView

@property (copy, nonatomic) void(^callback)();

@end

@implementation YJAddressFooter

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		[self setup];
	}
	return self;
}

- (void)setup
{
	UIButton *backgroundButton = [UIButton new];
	backgroundButton.backgroundColor = [UIColor whiteColor];
	[backgroundButton setImage:[UIImage imageNamed:@"address_add"] forState:UIControlStateNormal];
	[backgroundButton setTitle:@"新增地址" forState:UIControlStateNormal];
	[backgroundButton setTitleColor:[UIColor colorWithRGB:0x666666] forState:UIControlStateNormal];
	backgroundButton.titleLabel.font = [UIFont systemFontOfSize:14];
	backgroundButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
	[backgroundButton setContentEdgeInsets:UIEdgeInsetsMake(0, 20, 0, 20)];
	[backgroundButton setTitleEdgeInsets:UIEdgeInsetsMake(0, 10, 0, -10)];
	[self addSubview:backgroundButton];
	[backgroundButton addTarget:self action:@selector(clickOn) forControlEvents:UIControlEventTouchUpInside];
	
	UIImageView *imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"cell_disclosure"]];
	[backgroundButton addSubview:imageView];
	imageView.contentMode = UIViewContentModeCenter;
	
	[backgroundButton mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.right.bottom.mas_equalTo(0);
		make.top.mas_equalTo(10);
	}];
	
	[imageView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.bottom.mas_equalTo(0);
		make.right.mas_equalTo(-20);
	}];
	
	UIView *topLineView = [UIView new];
	topLineView.backgroundColor = [UIColor colorWithRGB:0xcccccc];
	[self addSubview:topLineView];
	[topLineView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.right.mas_equalTo(0);
		make.height.mas_equalTo(0.3);
		make.top.mas_equalTo(10);
	}];
	
	UIView *bottomLineView = [UIView new];
	bottomLineView.backgroundColor = [UIColor colorWithRGB:0xcccccc];
	[self addSubview:bottomLineView];
	[bottomLineView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.bottom.left.right.mas_equalTo(0);
		make.height.mas_equalTo(0.3);
	}];
}

- (void)clickOn
{
	if (self.callback) {
		self.callback();
	}
}

@end


@interface YJAdressViewController () <UINavigationControllerDelegate>

@property (strong, nonatomic) NSIndexPath *currentDefaultIndexPath;

@property (strong, nonatomic) UIButton *doneButton;

@end

@implementation YJAdressViewController

- (instancetype)init
{
	self = [super init];
	if (self) {
		
		self.refreshViewModelClass = [YJAddressViewModel class];
		
		_doneButton = [UIButton new];
		[_doneButton setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_normal"] forState:UIControlStateNormal];
		[_doneButton setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_highlighted"] forState:UIControlStateHighlighted];
		
		[_doneButton setTitle:@"保存" forState:UIControlStateNormal];
		_doneButton.titleLabel.font = [UIFont systemFontOfSize:15];
		
		[_doneButton addTarget:self action:@selector(clickOnDone) forControlEvents:UIControlEventTouchUpInside];
		
		self.showType = 0;
		self.selectedCallback = nil;
		
		self.title = @"地址管理";
	}
	return self;
}

+ (YJAdressViewController *)addressWithShowType:(NSInteger)showType
{
	YJAdressViewController *vc = [self new];
	vc.showType = showType;
	return vc;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
	UIBarButtonItem *add = [[UIBarButtonItem alloc] initWithTitle:@"新增" style:UIBarButtonItemStyleDone target:self action:@selector(clickOnAdd)];
	self.navigationItem.rightBarButtonItem = add;
	
	[self.refreshView registerClass:[YJAddressFooter class] forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"footer"];
	self.refreshView.contentInset = UIEdgeInsetsMake(0, 0, 49, 0);
	
	[NSDC addObserver:self selector:@selector(refreshViewWillRefresh:) name:YJRefreshViewWillRefreshByRefreshViewModel object:nil];
	
	[self.view addSubview:_doneButton];
	[_doneButton mas_makeConstraints:^(MASConstraintMaker *make) {
		make.bottom.left.right.mas_equalTo(0);
		make.height.mas_equalTo(49);
	}];
}

- (void)viewDidAppear:(BOOL)animated
{
	self.autoRefresh = YES;
	[super viewDidAppear:animated];
	self.navigationController.delegate = self;
}


/**
 监听界面刷新

 @param notify 通知
 */
- (void)refreshViewWillRefresh:(NSNotification *)notify
{
	if (!self.selectId) {
		for (NSInteger i=0; i<self.refreshViewModel.models.count; i++) {
			YJAddressCellModel *cellModel = self.refreshViewModel.models[i];
			if (cellModel.isBeDefault) {
				self.selectId = cellModel.model.id;
				break;
			}
		}
	}
}


/**
 查找当前选中的cellModel

 @param callback 查询结果的回调
 */
- (void)findSelectCellModel:(void(^)(YJAddressCellModel *cellModel))callback
{
	for (NSInteger i=0; i<self.refreshViewModel.models.count; i++) {
		YJAddressCellModel *cellModel = self.refreshViewModel.models[i];
		if ([cellModel.model.id isEqualToNumber:self.selectId]) {
			callback(cellModel);
			break;
		}
	}
}


/**
 监听导航控制器跳转
 */
- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
	if (self.showType != 1 && ![viewController isEqual:self] && self.selectedCallback && self.selectId) {
		if ([viewController isKindOfClass:[YJSendInnerViewController class]]) {
			[self findSelectCellModel:^(YJAddressCellModel *cellModel) {
				self.selectedCallback(cellModel.model);
			}];
		}
	}
	if (![viewController isEqual:self]) {
		if ([self.navigationController.delegate isEqual:self]) {
			self.navigationController.delegate = nil;
		}
	}
}

- (void)dealloc
{
	[NSDC removeObserver:self];
}


/**
 根据不同的showType设置底部按钮

 @param showType 0.隐藏  1.确定   2.下一步
 */
- (void)setShowType:(NSInteger)showType
{
	_showType = showType;
	if (showType == 1) {
		[self.doneButton setTitle:@"确定" forState:UIControlStateNormal];
		self.doneButton.hidden = NO;
	} else if (showType == 2) {
		[self.doneButton setTitle:@"下一步" forState:UIControlStateNormal];
		self.doneButton.hidden = NO;
	} else {
		self.doneButton.hidden = YES;
	}
}


/**
 点击底部按钮根据showType触发动作，保存／下一步
 */
- (void)clickOnDone
{
	if (self.showType == 1) {
		if (!self.selectId) {
			[SVProgressHUD showInfoInView:self.view withStatus:@"请选择地址"];
			return;
		}
		[self findSelectCellModel:^(YJAddressCellModel *cellModel) {
			self.selectedCallback(cellModel.model);
		}];
		[self.navigationController popViewControllerAnimated:YES];
	} else if (self.showType == 2) {
		if (!self.selectId) {
			[SVProgressHUD showInfoInView:self.view withStatus:@"请选择地址"];
			return;
		}
		[YJGlobalWeakHoldManager yj_findValue:^BOOL(id obj) {
			YJDetailOrderModel *detailModel = (YJDetailOrderModel *)obj;
			if (detailModel.merchandiseList.count == 0 || !detailModel.merchandiseList[0].warehouseId) {
				[SVProgressHUD showErrorInView:self.view withStatus:@"信息有误"];
				return YES;
			}
			[self findSelectCellModel:^(YJAddressCellModel *cellModel) {
				detailModel.address = cellModel.model;
			}];
			detailModel.realWeight = @([[detailModel.merchandiseList valueForKeyPath:@"@sum.weight"] doubleValue]);
			YJTransferViewController *transfer = [YJTransferViewController instanceWithWarehouseId:detailModel.merchandiseList[0].warehouseId weight:detailModel.realWeight model:nil];
			[transfer setTextForBottom:@"下一步"];
			[self.navigationController pushViewController:transfer animated:YES];
			return YES;
		}];
	}
}


/**
 推出新增地址界面
 */
- (void)clickOnAdd
{
	YJEditAddressViewController *editAddressVc = [YJEditAddressViewController new];
	editAddressVc.title = @"新增收货地址";
	editAddressVc.type = YJEditAddressEditAll;
	@weakify(self)
	[editAddressVc setCallback:^(BOOL changeDefault){
		@strongify(self)
		NSURLSessionDataTask *task = [self.refreshViewModel refresh:0];
		[self weakHoldTask:task];
	}];
	[self.navigationController pushViewController:editAddressVc animated:YES];
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
	YJAdressCell *cell = (YJAdressCell *)[super collectionView:collectionView cellForItemAtIndexPath:indexPath];
	if (self.showType == 1 || self.showType == 2) {
		cell.type = YJAddressCellTypeSelect;
		if (!self.selectId) {
			[self refreshViewWillRefresh:nil];
		}
		if (self.selectId) {
			cell.isWant = [cell.cellModel.model.id isEqualToNumber:self.selectId];
		} else {
			cell.isWant = NO;
		}
	} else {
		cell.type = YJAddressCellTypeDisclosure;
	}
	@weakify(self)
	@weakify(cell)
	[cell setClickOn:^(NSString *text) {
		@strongify(self)
		@strongify(cell)
		if (!text) {
			[self clickOnCell:cell atIndexPath:indexPath];
		} else if ([text isEqualToString:@"设为默认地址"]) {
			
			[self clickOnBeDefaultInCell:cell atIndexPath:indexPath];
			
		} else if ([text isEqualToString:@"编辑"]) {
			
			[self clickOnEditInCell:cell atIndexPath:indexPath];
			
		} else if ([text isEqualToString:@"删除"]) {
			
			[self clickOnDeleteInCell:cell atIndexPath:indexPath];
			
		} else if ([text isEqualToString:@"选中"]) {
			[self clickOnSelectInCell:cell atIndexPath:indexPath];
		}
	}];
	return cell;
}


/**
 选中cell跳转详情页

 @param cell 选中cell
 @param indexPath 选中indexPath
 */
- (void)clickOnCell:(YJAdressCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
	if (self.showType == 1 || self.showType == 2) {
		return;
	}
	YJEditAddressViewController *detailVc = [YJEditAddressViewController new];
	detailVc.title = @"地址详情";
	detailVc.type = YJEditAddressNone;
	detailVc.addressModel = [(YJAddressCellModel *)[self.refreshViewModel modelForIndexPath:indexPath] model];
	@weakify(self)
	[detailVc setCallback:^(BOOL changeDefault){
		@strongify(self)
		if (changeDefault) {
			[self resetDefaultForCell:cell atIndexPath:indexPath];
		}
		//else {
			YJAddressCellModel *cellModel = (YJAddressCellModel *)[self.refreshViewModel modelForIndexPath:indexPath];
			[cellModel bindingModel:cellModel.model];
		//}
		[self.refreshView reloadItemsAtIndexPaths:self.refreshView.indexPathsForVisibleItems];
	}];
	[self.navigationController pushViewController:detailVc animated:YES];
}


/**
 点击设为默认按钮

 @param cell 所处cell
 @param indexPath 所处indexPath
 */
- (void)clickOnBeDefaultInCell:(YJAdressCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
	YJAddressCellModel *cellModel = (YJAddressCellModel *)[self.refreshViewModel modelForIndexPath:indexPath];
	
	NSMutableDictionary *params = @{}.mutableCopy;
	params[@"id"] = cellModel.model.id;
	
	[SVProgressHUD showInView:self.view withStatus:nil];
	NSURLSessionDataTask *task = [YJRouter commonPUT:setDefaultAddressApi params:params.mutableCopy callback:^(YJRouterResponseModel *result) {
		if (result.code == 0) {
			[SVProgressHUD dismissFromView:self.view];
			[self resetDefaultForCell:cell atIndexPath:indexPath];
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
}


/**
 更新默认

 @param cell 当前cell
 @param indexPath 当前indexPath
 */
- (void)resetDefaultForCell:(YJAdressCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
	YJAddressCellModel *cellModel = (YJAddressCellModel *)[self.refreshViewModel modelForIndexPath:indexPath];
	
	if (!self.currentDefaultIndexPath) {
		self.currentDefaultIndexPath = [(YJAddressViewModel *)self.refreshViewModel indexPathForDefautCell];
	}
	if (self.currentDefaultIndexPath) {
		if (self.currentDefaultIndexPath.section == indexPath.section && self.currentDefaultIndexPath.row == indexPath.row) {
			cellModel.beDefault = !cellModel.beDefault;
		} else {
			YJAddressCellModel *preModel = (YJAddressCellModel *)[self.refreshViewModel modelForIndexPath:self.currentDefaultIndexPath];
			YJAdressCell *preCell = (YJAdressCell *)[self.refreshView cellForItemAtIndexPath:self.currentDefaultIndexPath];
			preModel.beDefault = NO;
			preCell.cellModel = preModel;
			
			cellModel.beDefault = YES;
			
			self.currentDefaultIndexPath = indexPath;
		}
	} else {
		self.currentDefaultIndexPath = indexPath;
		cellModel.beDefault = !cellModel.beDefault;
	}
	cell.cellModel = cellModel;
}


/**
 点击删除

 @param cell 所处cell
 @param indexPath 所处indexPath
 */
- (void)clickOnDeleteInCell:(YJAdressCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
	[UIAlertController showByController:self callback:^{
		@weakify(self)
		[self.refreshViewModel removeModelAtIdx:indexPath.row success:^{
			@strongify(self)
			if (cell.isWant) {
				self.selectId = nil;
			}
		}];
	}];
}


/**
 点击编辑

 @param cell 所处cell
 @param indexPath 所处indexPath
 */
- (void)clickOnEditInCell:(YJAdressCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
	YJEditAddressViewController *editAddressVc = [YJEditAddressViewController new];
	editAddressVc.title = @"编辑收货地址";
	editAddressVc.addressModel = [(YJAddressCellModel *)[self.refreshViewModel modelForIndexPath:indexPath] model];
	editAddressVc.type = YJEditAddressEditAll;
	@weakify(self)
	[editAddressVc setCallback:^(BOOL changeDefault){
		@strongify(self)
		if (changeDefault) {
			[self resetDefaultForCell:cell atIndexPath:indexPath];
		}
		//else {
			YJAddressCellModel *cellModel = (YJAddressCellModel *)[self.refreshViewModel modelForIndexPath:indexPath];
			[cellModel bindingModel:cellModel.model];
		//}
		[self.refreshView reloadItemsAtIndexPaths:self.refreshView.indexPathsForVisibleItems];
	}];
	[self.navigationController pushViewController:editAddressVc animated:YES];
}


/**
 点击选中

 @param cell 所处cell
 @param indexPath 所处indexPath
 */
- (void)clickOnSelectInCell:(YJAdressCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
	NSArray *cells = [self.refreshView visibleCells];
	for (YJAdressCell *cc in cells) {
		if (cc.isWant) {
			cc.isWant = NO;
			break;
		}
	}
	cell.isWant = YES;
	self.selectId = cell.cellModel.model.id;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForFooterInSection:(NSInteger)section
{
	return CGSizeMake(collectionView.width - collectionView.contentInset.left - collectionView.contentInset.right, 50);
}

- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
{
	if ([kind isEqualToString:UICollectionElementKindSectionFooter]) {
		YJAddressFooter *footer = (YJAddressFooter *)[collectionView dequeueReusableSupplementaryViewOfKind:kind withReuseIdentifier:@"footer" forIndexPath:indexPath];
		if (!footer.callback) {
			@weakify(self)
			[footer setCallback:^{
				@strongify(self)
			    [self clickOnAdd];
			}];
		}
		return footer;
	}
	return [UICollectionReusableView new];
}

@end













