//
//  YJCommonStatusMsgViewController.m
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonStatusMsgViewController.h"
#import "YJCommonStatusMsgCell.h"
#import "YJCommonStatusMsgModel.h"
#import "YJStatusProcessView.h"

#import "YJSendInnerViewController.h"
#import "YJShareManager.h"

@interface YJCommonStatusMsgViewController () <UITableViewDelegate, UITableViewDataSource>

@property (strong, nonatomic) UITableView *refreshView;

@property (strong, nonatomic) YJRefreshViewModel *refreshViewModel;

@property (strong, nonatomic) UILabel *moreInfoLabel;

@property (strong, nonatomic) YJStatusProcessView *processView;

@property (copy, nonatomic) NSString *url;
@property (copy, nonatomic) NSString *summary;
@property (copy, nonatomic) NSString *companyCode;
@property (copy, nonatomic) NSString *webTitle;
@property (copy, nonatomic) NSString *customerId;

@property (strong, nonatomic) id shareVc;

@end

@implementation YJCommonStatusMsgViewController


/**
 懒加载列表

 @return 列表
 */
- (UITableView *)refreshView
{
	if (!_refreshView) {
		_refreshView = [UITableView new];
		_refreshView.dataSource = self;
		_refreshView.delegate = self;
		_refreshView.backgroundColor = kGlobalBackgroundColor;
		_refreshView.separatorStyle = UITableViewCellSeparatorStyleNone;
		_refreshView.estimatedRowHeight = 44;
		_refreshView.rowHeight = UITableViewAutomaticDimension;
		[self.view addSubview:_refreshView];
		[_refreshView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.edges.mas_equalTo(0);
		}];
		
		UIView *headerView = [UIView new];
		headerView.height = 158;
		headerView.backgroundColor = kGlobalBackgroundColor;
		_refreshView.tableHeaderView = headerView;
		
		UIView *moreInfoView = [UIView new];
		moreInfoView.backgroundColor = [UIColor whiteColor];
		[headerView addSubview:moreInfoView];
		[moreInfoView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.top.mas_equalTo(10);
			make.width.mas_equalTo(self.view.mas_width);
			make.height.mas_equalTo(43);
		}];
		
		UILabel *titleLabel = [UILabel new];
		titleLabel.text = @"邮客单号";
		titleLabel.textColor = [UIColor colorWithRGB:0x666666];
		titleLabel.font = [UIFont systemFontOfSize:14];
		[moreInfoView addSubview:titleLabel];
		[titleLabel setContentHuggingPriority:UILayoutPriorityRequired forAxis:UILayoutConstraintAxisHorizontal];
		[titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
			make.left.mas_equalTo(20);
			make.top.bottom.mas_equalTo(0);
		}];
		
		UILabel *moreInfoLabel = [UILabel new];
		moreInfoLabel.numberOfLines = 0;
		moreInfoLabel.lineBreakMode = NSLineBreakByTruncatingTail;
		moreInfoLabel.text = [NSString stringWithFormat:@"%@",_searchNo];
		moreInfoLabel.textColor = [UIColor colorWithRGB:0x999999];
		moreInfoLabel.font = [UIFont systemFontOfSize:14];
		_moreInfoLabel = moreInfoLabel;
		[moreInfoView addSubview:moreInfoLabel];
		[moreInfoLabel mas_makeConstraints:^(MASConstraintMaker *make) {
			make.left.mas_equalTo(titleLabel.mas_right).offset(20);
			make.top.bottom.mas_equalTo(0);
			make.right.mas_equalTo(-40);
		}];
		
		UIImageView *disImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"cell_disclosure"]];
		disImageView.contentMode = UIViewContentModeCenter;
		[moreInfoView addSubview:disImageView];
		[disImageView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.right.mas_equalTo(-20);
			make.top.bottom.mas_equalTo(0);
		}];
		
		moreInfoView.edgeLines = UIEdgeInsetsMake(0.3, 0, 0.3, 0);
		
		UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapOnTopView:)];
		[moreInfoView addGestureRecognizer:tap];
		
		YJStatusProcessView *processView = [YJStatusProcessView new];
		processView.edgeLines = UIEdgeInsetsMake(0.3, 0, 0.3, 0);
		_processView = processView;
		processView.frame = CGRectMake(0, headerView.height-95, kScreenWidth, 95);
		processView.orderStatus = self.orderStatus;
		[headerView addSubview:processView];
	}
	return _refreshView;
}


/**
 懒加载viewModel

 @return viewModel
 */
- (YJRefreshViewModel *)refreshViewModel
{
	if (!_refreshViewModel) {
		_refreshViewModel = [YJRefreshViewModel new];
		_refreshViewModel.netUrl = OrderStatusMsgListApi;
		_refreshViewModel.modelClass = [YJCommonStatusMsgModel class];
		_refreshViewModel.cellClass = [YJCommonStatusMsgCell class];
		@weakify(self)
		[_refreshViewModel setRefresh:^NSURLSessionDataTask *(NSInteger start) {
			@strongify(self)
			return [YJRouter commonGetModel:self.refreshViewModel.netUrl params:self.refreshViewModel.baseParams modelClass:[self.refreshViewModel.modelClass class] callback:^(YJRouterResponseModel *result) {
				[self.refreshView.mj_header endRefreshing];
				@strongify(self)
				if (result.code == 0 || result.code == 999) {
					self.url = [(YJCommonStatusMsgModel *)result.data url];
					self.summary = [(YJCommonStatusMsgModel *)result.data summary];
					self.companyCode = [(YJCommonStatusMsgModel *)result.data companyCode];
					self.webTitle = [(YJCommonStatusMsgModel *)result.data title];
					self.customerId = [(YJCommonStatusMsgModel *)result.data customerId];
					[self.refreshViewModel doWithNetResult:[result.data list] start:start];
					[SVProgressHUD dismissFromView:self.refreshView.superview];
					[self addShareBtn];
				} else {
					[SVProgressHUD showErrorInView:self.refreshView.superview withStatus:result.msg];
				}
			}];
		}];
		if (_refreshView) {
			_refreshViewModel.refreshView = _refreshView;
		}
		if (!_refreshViewModel.baseParams && self.searchNo) {
			_refreshViewModel.baseParams = @{@"searchArray": self.searchNo};
		}
	}
	return _refreshViewModel;
}


/**
 设置进度

 @param orderStatus 进度
 */
- (void)setOrderStatus:(YJOrderStatus)orderStatus
{
	_orderStatus = orderStatus;
	if (_processView) {
		_processView.orderStatus = orderStatus;
	}
}

- (void)setSearchNo:(NSString *)searchNo
{
	_searchNo = searchNo;
	if (_refreshViewModel) {
		_refreshViewModel.baseParams = @{@"searchArray": searchNo};
	}
	if (_moreInfoLabel) {
		_moreInfoLabel.text = [NSString stringWithFormat:@"%@",searchNo];
	}
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
	[self.refreshView reloadData];
}


/**
 添加分享按钮
 */
- (void)addShareBtn
{
	if ([YJShareManager shareable]) {
		self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"分享" style:UIBarButtonItemStyleDone target:self action:@selector(clickOnShare:)];
	}
}


/**
 分享

 @param sender 按钮
 */
- (void)clickOnShare:(UIBarButtonItem *)sender
{
	YJShareModel *model = [YJShareModel new];
	model.content = self.summary ?: @"邮客极速转运";
	model.title = self.webTitle ?: self.title;
	model.images = @[[UIImage imageNamed:@"icon"]];
	if (self.url) {
		model.url = [NSURL URLWithString:[[NSString stringWithFormat:@"%@?orderNo=%@&orderStatus=%zd&companyCode=%@&title=%@&customerId=%@", self.url, self.searchNo, self.orderStatus, self.companyCode, model.title, self.customerId] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
	}
	
	[YJShareManager showShareView:model callback:^BOOL(BOOL success) {
		if (success) {
			[SVProgressHUD showSuccessInView:self.view withStatus:@"分享成功"];
		}
		return YES;
	}];
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	
	[self defaultDealWhenViewDidAppear];
}

- (void)defaultDealWhenViewDidAppear
{
	if (self.autoRefresh) {
		[SVProgressHUD showInView:self.view];
		NSURLSessionDataTask *task = [self.refreshViewModel refresh:0];
		[self weakHoldTask:task];
		self.autoRefresh = NO;
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


/**
 跳转详情页

 @param tap tap
 */
- (void)tapOnTopView:(UITapGestureRecognizer *)tap
{
	YJToDoModel *model = [YJToDoModel new];
	model.no = self.searchNo;
	YJSendInnerViewController *vc = [YJSendInnerViewController instanceWithOptions:YJDetailOrderOptionsShowLastCost | YJDetailOrderOptionsShrinkable
																		  andModel:model
																		  withSpec:^(UITableView *tableView, YJBottomFloatView *floatView) {
																			  
																		  }];
	vc.title = @"订单详情";
	[self.navigationController pushViewController:vc animated:YES];
}

@end
