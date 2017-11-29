//
//  YJWarehouseViewController.m
//  yyox
//
//  Created by ddn on 2017/5/17.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJWarehouseViewController.h"
#import "YJWarehouseModel.h"
#import "YJWarehouseCell.h"

@interface YJWarehouseViewController () <UITableViewDelegate, UITableViewDataSource>

@property (strong, nonatomic) UITableView *refreshView;

@property (strong, nonatomic) YJRefreshViewModel *refreshViewModel;

@end

@implementation YJWarehouseViewController

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.refreshViewModel = [YJRefreshViewModel new];
		self.refreshViewModel.modelClass = [YJWarehouseModel class];
		self.refreshViewModel.cellClass = [YJWarehouseCell class];
		self.refreshViewModel.netUrl = WarehoushListApi2;
		self.refreshViewModel.footerRefreshCount = 0;
		self.refreshViewModel.autoRefreshVisiableCells = NO;
		
		self.refreshView = [[UITableView alloc] init];
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
	}
	return self;
}

- (void)viewDidLoad {
	[super viewDidLoad];
	self.refreshView.backgroundColor = kGlobalBackgroundColor;
	self.refreshView.separatorStyle = UITableViewCellSeparatorStyleNone;
	self.title = @"仓库列表";
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

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return self.refreshViewModel.modelsCount;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	YJWarehouseCell *cell = (YJWarehouseCell *)[self.refreshViewModel cellForIndexPath:indexPath];
	@weakify(self)
	[cell setCopyCallback:^{
		@strongify(self)
		[SVProgressHUD showSuccessInView:self.view withStatus:@"复制成功"];
	}];
	return cell;
}

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	return NO;
}


@end
