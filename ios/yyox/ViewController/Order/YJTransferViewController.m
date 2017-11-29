//
//  YJTransferViewController.m
//  yyox
//
//  Created by ddn on 2017/2/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJTransferViewController.h"
#import "YJTransferCell.h"

#import "YJSendInnerViewController.h"

#import "YJDetailOrderModel.h"

@interface YJTransferViewController () <UITableViewDelegate, UITableViewDataSource>

@property (strong, nonatomic) NSIndexPath *selectedIndexPath;

@property (strong, nonatomic) UIButton *doneBtn;

@property (strong, nonatomic) UITableView *refreshView;

@property (strong, nonatomic) YJRefreshViewModel *refreshViewModel;

@property (strong, nonatomic) YJTransferModel *model;

@end

@implementation YJTransferViewController

+ (YJTransferViewController *)instanceWithWarehouseId:(id)warehouseId weight:(NSNumber *)weight model:(YJTransferModel *)model
{
	YJTransferViewController *vc = [self new];
	vc.model = model;
	vc.refreshViewModel.baseParams = @{@"warehouseId": warehouseId ?: model.warehouseId, @"weight": weight};
	return vc;
}

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.refreshViewModel = [YJRefreshViewModel new];
		self.refreshViewModel.modelClass = [YJTransferModel class];
		self.refreshViewModel.cellClass = [YJTransferCell class];
		self.refreshViewModel.netUrl = TransformListApi;
		self.refreshViewModel.autoRefreshVisiableCells = NO;
		self.refreshViewModel.footerRefreshCount = 0;
		
		self.refreshView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStyleGrouped];
		[self.view addSubview:self.refreshView];
		[self.refreshView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.edges.mas_equalTo(0);
		}];
		self.refreshView.dataSource = self;
		self.refreshView.delegate = self;
		self.refreshView.rowHeight = UITableViewAutomaticDimension;
		self.refreshView.estimatedRowHeight = 44;
		
		self.refreshViewModel.refreshView = self.refreshView;
		self.refreshViewModel.netMethod = 0;
		self.refreshView.showType = YJTableViewShowTypeSections;
		@weakify(self)
		[self.refreshViewModel setModelForIndexPath:^NSObject *(NSIndexPath *indexPath) {
			@strongify(self)
			if (self.refreshViewModel.models.count > indexPath.section) {
				return self.refreshViewModel.models[indexPath.section];
			}
			return nil;
		}];
	}
	return self;
}

- (UIButton *)doneBtn
{
	if (!_doneBtn) {
		_doneBtn = [UIButton new];
		[_doneBtn setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_normal"] forState:UIControlStateNormal];
		[_doneBtn setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_highlighted"] forState:UIControlStateHighlighted];
		[self.view addSubview:_doneBtn];
		[_doneBtn mas_makeConstraints:^(MASConstraintMaker *make) {
			make.bottom.left.right.mas_equalTo(0);
			make.height.mas_equalTo(49);
		}];
		[_doneBtn setTitle:@"下一步" forState:UIControlStateNormal];
		[_doneBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
		_doneBtn.titleLabel.font = [UIFont systemFontOfSize:14];
		[_doneBtn addTarget:self action:@selector(clickOnDone) forControlEvents:UIControlEventTouchUpInside];
	}
	return _doneBtn;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	self.title = @"选择运输服务";
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	if (self.autoRefresh) {
		self.autoRefresh = NO;
		[SVProgressHUD showInView:self.refreshView.superview withStatus:nil userEnable:NO];
		NSURLSessionDataTask *task = [self.refreshViewModel headerRefresh];
		[self weakHoldTask:task];
	}
}

- (void)setTextForBottom:(NSString *)text
{
	if (text && text.length > 0) {
		[self.doneBtn setTitle:text forState:UIControlStateNormal];
	}
}

/**
 跳转提交页
 */
- (void)clickOnDone
{
	if (!self.selectedIndexPath) {
		[SVProgressHUD showInfoInView:self.view withStatus:@"请选择运输服务"];
		return;
	}
	if (self.model) {
		if (self.callback) {
			YJTransferModel *model = (YJTransferModel *)[self.refreshViewModel modelForIndexPath:self.selectedIndexPath];
			self.callback(model);
			self.callback = nil;
		}
		[self.navigationController popViewControllerAnimated:YES];
		return;
	}
	[YJGlobalWeakHoldManager yj_findValue:^BOOL(id obj) {
		YJDetailOrderModel *detailModel = (YJDetailOrderModel *)obj;
		YJTransferModel *model = (YJTransferModel *)[self.refreshViewModel modelForIndexPath:self.selectedIndexPath];
		detailModel.orderchannel = model;
		YJToDoModel *todoModel = nil;
		if (detailModel.merchandiseList.count > 1) {
			todoModel = [YJToDoModel new];
			todoModel.type = 3;
		}
		YJDetailOrderOptions options = detailModel.merchandiseList.count > 1 ? (YJDetailOrderOptionsAll ^ YJDetailOrderOptionsShowLastCost) : ((YJDetailOrderOptionsAll ^ YJDetailOrderOptionsShrinkable) ^ YJDetailOrderOptionsShowLastCost);
		YJSendInnerViewController *sendInner = [YJSendInnerViewController instanceWithOptions:options andModel:todoModel withSpec:^(UITableView *tableView, YJBottomFloatView *floatView) {
			
		}];
		sendInner.detailOrderModel = detailModel;
		[self.navigationController pushViewController:sendInner animated:YES];
		return YES;
	}];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	return self.refreshViewModel.modelsCount;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	YJTransferCell *cell = (YJTransferCell *)[self.refreshViewModel cellForIndexPath:indexPath];
	if (!self.selectedIndexPath) {
		if (self.model) {
			if ([cell.model.orderTypeId isEqualToNumber:self.model.orderTypeId]) {
				self.selectedIndexPath = indexPath;
			}
		} else {
			self.selectedIndexPath = indexPath;
		}
	}
	if (self.selectedIndexPath) {
		cell.isWant = self.selectedIndexPath.section == indexPath.section;
	}
	@weakify(self)
	@weakify(cell)
	[cell setClickOnSelected:^{
		@strongify(self)
		@strongify(cell)
		[self clickOnSelectInCell:cell atIndexPath:indexPath];
	}];
	return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
	return 9.9;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
	return 0.1;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
	[self clickOnSelectInCell:(YJTransferCell *)cell atIndexPath:indexPath];
}

- (void)clickOnSelectInCell:(YJTransferCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
	if (self.selectedIndexPath) {
		YJTransferCell *preCell = (YJTransferCell *)[self.refreshView cellForRowAtIndexPath:self.selectedIndexPath];
		preCell.isWant = NO;
	}
	cell.isWant = YES;
	self.selectedIndexPath = indexPath;
}

@end
