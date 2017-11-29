//
//  YJCombineSendViewController.m
//  yyox
//
//  Created by ddn on 2017/2/28.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCombineSendViewController.h"
#import "YJCombineCell.h"
#import "YJMerchandiseModel.h"
#import "YJRefreshViewModel.h"
#import "YJShowPackageViewController.h"

@interface YJCombineSendViewController () <UITableViewDelegate, UITableViewDataSource>

@property (strong, nonatomic) NSMutableArray *selectedSections;

@property (strong, nonatomic) UIButton *doneBtn;

@property (strong, nonatomic) UITableView *refreshView;

@property (strong, nonatomic) YJRefreshViewModel *refreshViewModel;

@end

@implementation YJCombineSendViewController

- (NSMutableArray *)selectedSections
{
	if (!_selectedSections) {
		_selectedSections = [NSMutableArray array];
	}
	return _selectedSections;
}

- (instancetype)init
{
	self = [super init];
	if (self) {
		
		self.refreshViewModel = [YJRefreshViewModel new];
		self.refreshViewModel.modelClass = [YJMerchandiseModel class];
		self.refreshViewModel.cellClass = [YJCombineCell class];
		self.refreshViewModel.netUrl = PendingOrderApi;
		self.refreshViewModel.baseParams = @{@"statusCodeC": @"已入库"};
		self.refreshViewModel.autoRefreshVisiableCells = NO;
		@weakify(self)
		[self.refreshViewModel setModelForIndexPath:^NSObject *(NSIndexPath *indexPath) {
			@strongify(self);
			if (self.refreshViewModel.models.count > indexPath.section) {
				return self.refreshViewModel.models[indexPath.section];
			}
			return nil;
		}];
		
		self.refreshView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStyleGrouped];
		[self.view addSubview:self.refreshView];
		[self.refreshView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.edges.mas_equalTo(0);
		}];
		self.refreshView.dataSource = self;
		self.refreshView.delegate = self;
		self.refreshView.rowHeight = UITableViewAutomaticDimension;
		self.refreshView.estimatedRowHeight = 44;
		self.refreshView.contentInset = UIEdgeInsetsMake(0, 0, 80, 0);
		self.refreshViewModel.refreshView = self.refreshView;
	}
	return self;
}


/**
 懒加载确认按钮

 @return 确认按钮
 */
- (UIButton *)doneBtn
{
	if (!_doneBtn) {
		UIView *bottomView = [UIView new];
		[self.view addSubview:bottomView];
		[bottomView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.left.right.bottom.mas_equalTo(0);
			make.height.mas_equalTo(80);
		}];
		
		UILabel *titleLabel = [UILabel new];
		titleLabel.backgroundColor = [UIColor whiteColor];
		[bottomView addSubview:titleLabel];
		[titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
			make.top.left.right.mas_equalTo(0);
			make.height.mas_equalTo(31);
		}];
		titleLabel.text = @"一个包裹，发往同一地址";
		titleLabel.textColor = [UIColor colorWithRGB:0x666666];
		titleLabel.font = [UIFont systemFontOfSize:11];
		titleLabel.textAlignment = NSTextAlignmentCenter;
		
		_doneBtn = [UIButton new];
		[_doneBtn setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_normal"] forState:UIControlStateNormal];
		[_doneBtn setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_highlighted"] forState:UIControlStateHighlighted];
		[bottomView addSubview:_doneBtn];
		[_doneBtn mas_makeConstraints:^(MASConstraintMaker *make) {
			make.bottom.left.right.mas_equalTo(0);
			make.height.mas_equalTo(49);
		}];
		[_doneBtn setTitle:@"创建订单" forState:UIControlStateNormal];
		[_doneBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
		_doneBtn.titleLabel.font = [UIFont systemFontOfSize:14];
		[_doneBtn addTarget:self action:@selector(clickOnDone) forControlEvents:UIControlEventTouchUpInside];
		bottomView.edgeLines = UIEdgeInsetsMake(0.3, 0, 0, 0);
	}
	return _doneBtn;
}

- (void)viewDidLoad {
	[super viewDidLoad];
	self.title = @"待处理";
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	[self defaultDealWhenViewDidAppear];
}


/**
 界面加载完毕后获取数据
 */
- (void)defaultDealWhenViewDidAppear
{
	self.doneBtn.hidden = NO;
	[SVProgressHUD showInView:self.refreshView.superview withStatus:nil userEnable:NO];
	NSURLSessionDataTask *task = [self.refreshViewModel headerRefresh];
	[self weakHoldTask:task];
}


/**
 点击完成
 */
- (void)clickOnDone
{
	if (self.selectedSections.count < 2) {
		[SVProgressHUD showInfoInView:self.view withStatus:@"请选择至少两个包裹"];
		return;
	}
	NSArray *merchandises = [self.refreshViewModel.models selectIndexs:self.selectedSections];
	if ([[merchandises valueForKeyPath:@"@distinctUnionOfObjects.warehouseId"] count] != 1) {
		[SVProgressHUD showInfoInView:self.view withStatus:@"选择的包裹必须在同一个仓库！"];
		return;
	}
	YJDetailOrderModel *detaiModel = [YJDetailOrderModel new];
	
	YJToDoModel *model = [YJToDoModel new];
	model.type = 1;
	model.inventorys = [merchandises valueForKeyPath:@"id"];
	YJShowPackageViewController *sp = [YJShowPackageViewController instanceWithEditable:YES model:model];
	sp.detailOrderModel = detaiModel;
	[sp addCancelOpt];
	[self.navigationController pushViewController:sp animated:YES];
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
	YJCombineCell *cell = (YJCombineCell *)[self.refreshViewModel cellForIndexPath:indexPath];
	cell.isWant = [self.selectedSections containsObject:@(indexPath.section)];
	@weakify(self)
	@weakify(cell)
	[cell setClickOnSelect:^{
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


/**
 选中／取消选中

 @param cell 所处cell
 @param indexPath 所处indexPath
 */
- (void)clickOnSelectInCell:(YJCombineCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
	if ([self.selectedSections containsObject:@(indexPath.section)]) {
		[self.selectedSections removeObject:@(indexPath.section)];
		cell.isWant = NO;
	} else {
		[self.selectedSections addObject:@(indexPath.section)];
		cell.isWant = YES;
	}
}

@end
