//
//  YJPaymentViewController.m
//  yyox
//
//  Created by ddn on 2017/3/27.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPaymentViewController.h"
#import "YJExpenseModel.h"
#import "YJExpenseCell.h"

@interface YJPaymentViewController () <UITableViewDelegate, UITableViewDataSource>

@property (strong, nonatomic) UITableView *refreshView;

@property (strong, nonatomic) YJRefreshViewModel *refreshViewModel;

@end

@implementation YJPaymentViewController

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.refreshViewModel = [YJRefreshViewModel new];
		self.refreshViewModel.modelClass = [YJExpenseModel class];
		self.refreshViewModel.cellClass = [YJExpenseCell class];
		self.refreshViewModel.netUrl = PayHistoryApi;
		self.refreshViewModel.footerRefreshCount = 10;
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
		self.refreshViewModel.netMethod = 1;
		
		self.title = @"消费记录";
	}
	return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	self.refreshView.backgroundColor = kGlobalBackgroundColor;
	self.refreshView.separatorStyle = UITableViewCellSeparatorStyleNone;
}


/**
 视图加载后获取优惠券列表

 @param animated 动画
 */
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
	return (UITableViewCell *)[self.refreshViewModel cellForIndexPath:indexPath];
}

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	return NO;
}

@end
