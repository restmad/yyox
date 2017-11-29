//
//  YJOrderViewController.m
//  yyox
//
//  Created by ddn on 2016/12/26.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJOrderViewController.h"
#import "YJTabBar.h"
#import "YJStageView.h"
#import "YJOrderViewModel.h"

#import "YJPendingViewController.h"

#import "YJCommonStatusOrderViewController.h"

#import "YJWaitDoViewController.h"

#import "YJOrderCellModel.h"
#import "YJOrderCell.h"
#import "YJOrderMainModel.h"

#import "YJCommonStatusMsgViewController.h"

//#import "YJExpenseViewController.h"
#import "YJPaymentViewController.h"
#import "YJBaseWevViewController.h"

#import "YJUserModel.h"

#define kLoopViewH (kScreenWidth * 410. / 750.)
#define kTabbarH (194. / 2.)
#define kStageViewH (156./2.)

@interface YJOrderViewController () <UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout>

@property (strong, nonatomic) iCarousel *loopView;

@property (strong, nonatomic) YJTabBar *tabbar;

@property (strong, nonatomic) YJStageView *stageView;

@property (strong, nonatomic) UIView *headerView;

@property (strong, nonatomic) UIView *statusBar;

@end

@implementation YJOrderViewController


/**
 懒加载轮播图

 @return 轮播图
 */
- (iCarousel *)loopView
{
	if (!_loopView) {
		iCarousel *loopView = [iCarousel new];
		loopView.frame = CGRectMake(0, 0, kScreenWidth, kLoopViewH);
		_loopView = loopView;
	}
	return _loopView;
}


/**
 懒加载第一排按钮

 @return tabbar
 */
- (YJTabBar *)tabbar
{
	if (!_tabbar) {
		YJTabBar *tabbar = [YJTabBar new];
		
		_tabbar = tabbar;
		@weakify(self)
		[tabbar setClickOn:^(NSString *text, NSInteger idx) {
			@strongify(self)
			
			[self switchBarVC:text idx:idx];
			
		}];
	}
	return _tabbar;
}


/**
 点击第一排按钮

 @param text 按钮文字
 @param idx 按钮位置
 */
- (void)switchBarVC:(NSString *)text idx:(NSInteger)idx
{
	if ([text isEqualToString:@"待处理"]) {
		
		YJWaitDoViewController *pendingvc = [YJWaitDoViewController new];
		pendingvc.title = @"待处理";
		[self.navigationController pushViewController:pendingvc animated:YES];
		
	} else if ([text isEqualToString:@"已完成"]) {
		
		YJCommonStatusOrderViewController *donevc = [YJCommonStatusOrderViewController new];
		donevc.checkCount = [self.tabbar bageValueForIdx:idx];
		donevc.orderStatus = YJOrderStatusDone;
		donevc.title = @"已完成";
		[self.navigationController pushViewController:donevc animated:YES];
	} else if ([text isEqualToString:@"我要预报"]) {
		UIViewController *pre = [YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyPredicte];
		[self.navigationController pushViewController:pre animated:YES];
	}
}


/**
 懒加载第二排按钮

 @return stageView
 */
- (YJStageView *)stageView
{
	if (!_stageView) {
		YJStageView *stageView = [YJStageView new];
		stageView.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
		_stageView = stageView;
		stageView.backgroundColor = [UIColor whiteColor];
		@weakify(self)
		[stageView setClickOn:^(NSString *text, NSInteger idx) {
			
			@strongify(self)
			
			[self switchStageVC:text idx:idx];
		}];
	}
	return _stageView;
}


/**
 点击第二排按钮

 @param text 按钮文字
 @param idx 按钮位置
 */
- (void)switchStageVC:(NSString *)text idx:(NSInteger)idx
{
	YJCommonStatusOrderViewController *vc;
	switch (idx) {
		case 0:
		{
			vc = [YJPendingViewController new];
			vc.title = @"已预报待入库";
		}
			break;
		case 1:
		{
			vc = [YJCommonStatusOrderViewController new];
			vc.orderStatus = YJOrderStatusWaitOut;
			vc.title = @"待出库";
		}
			break;
		case 2:
		{
			vc = [YJCommonStatusOrderViewController new];
			vc.orderStatus = YJOrderStatusDidOut;
			vc.title = @"已出库";
		}
			break;
		case 3:
		{
			vc = [YJCommonStatusOrderViewController new];
			vc.orderStatus = YJOrderStatusRunning;
			vc.title = @"清关中";
		}
			break;
		case 4:
		{
			vc = [YJCommonStatusOrderViewController new];
			vc.orderStatus = YJOrderStatusInner;
			vc.title = @"国内配送";
		}
			break;
		default:
			return;
	}
	[self.navigationController pushViewController:vc animated:YES];
}


/**
 懒加载头视图

 @return headerView
 */
- (UIView *)headerView
{
	if (!_headerView) {
		UIView *headerView = [UIView new];
		headerView.edgeLines = UIEdgeInsetsMake(0, 0, 0, 0);
		_headerView = headerView;
		
		[headerView addSubview:self.loopView];
		
		[headerView addSubview:self.tabbar];
		
		[headerView addSubview:self.stageView];
		
		[self.loopView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.width.mas_equalTo(headerView.mas_width);
			make.top.mas_equalTo(0);
			make.height.mas_equalTo(kLoopViewH);
			make.left.mas_equalTo(0);
		}];
		
		[self.tabbar mas_makeConstraints:^(MASConstraintMaker *make) {
			make.width.mas_equalTo(headerView.mas_width);
			make.top.mas_equalTo(self.loopView.mas_bottom).offset(2);
			make.left.mas_equalTo(0);
			make.height.mas_equalTo(kTabbarH);
		}];
		
		[self.stageView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.width.mas_equalTo(headerView.mas_width);
			make.top.mas_equalTo(self.tabbar.mas_bottom);
			make.left.mas_equalTo(0);
			make.bottom.mas_equalTo(-10);
		}];
		
		headerView.height = kTabbarH + kLoopViewH + kStageViewH + 10;
	}
	return _headerView;
}

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.lineSpace = 0;
		self.refreshViewModelClass = [YJOrderViewModel class];
		self.contentInset = UIEdgeInsetsMake(kTabbarH + kLoopViewH + kStageViewH + 10, 0.2, 49, 0.2);
	}
	return self;
}


/**
 初始化viewModel及相关视图
 */
- (void)viewDidLoad {
	[super viewDidLoad];
	self.automaticallyAdjustsScrollViewInsets = NO;
	
	YJOrderViewModel *orderViewModel = (YJOrderViewModel *)self.refreshViewModel;
	@weakify(self)
	[orderViewModel setRefreshHeaderView:^(YJOrderMainModel *mainModel) {
		YJOrderMainCountModel *model = mainModel.mainCount;
		
		@strongify(self)
		[self.stageView updateTexts:^NSInteger(NSString *text, NSInteger idx) {
			NSInteger badgeValue = 0;
			switch (idx) {
				case 0:
					badgeValue = model ? model.notputNo.integerValue : 0;
					break;
				case 1:
					badgeValue = model ? model.foroutboundNo.integerValue : 0;
					break;
				case 2:
					badgeValue = model ? model.haveoutboundNo.integerValue : 0;
					break;
				case 3:
					badgeValue = model ? model.clear.integerValue : 0;
					break;
				case 4:
					badgeValue = model ? model.delivering.integerValue : 0;
					break;
				default:
					break;
			}
			return badgeValue;
		}];
		[self.tabbar updateTexts:^NSInteger(NSString *text, NSInteger idx) {
			NSInteger badgeValue = 0;
			switch (idx) {
				case 0:
					break;
				case 1:
					badgeValue = model ? model.waitForDispose.integerValue : 0;
					break;
				case 2:
				{
					badgeValue = model ? model.finish.integerValue : 0;
					break;
				}
				default:
					break;
			}
			return badgeValue;
		}];
	}];
	orderViewModel.carousel = self.loopView;
	[orderViewModel setClickOnCarousel:^(NSInteger index) {
		@strongify(self)
		YJBaseWevViewController *vc = [YJBaseWevViewController new];
		vc.startUrl = [NSURL yj_URLWithString:[NSString stringWithFormat:@"app/index%zd.html", index + 1]];
		[self.navigationController pushViewController:vc animated:YES];
		vc.title = self.title;
	}];
	[self.refreshView addSubview:self.headerView];
	
	[self.headerView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(-self.headerView.height);
		make.left.right.mas_equalTo(self.view);
		make.height.mas_equalTo(self.refreshView.contentInset.top);
	}];
}

- (void)defaultDealWhenViewDidAppear
{
	self.autoRefresh = YES;
	[super defaultDealWhenViewDidAppear];
	[(YJOrderViewModel *)self.refreshViewModel initialCarousel];
}

- (CGFloat)verticalOffsetForEmptyDataSet:(UIScrollView *)scrollView
{
	return self.headerView.frame.size.height/2.0f - 44;
}


/**
 点击消息

 @param collectionView collectionView
 @param indexPath indexPath
 */
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
	YJOrderCellModel *cellModel = (YJOrderCellModel *)[self.refreshViewModel modelForIndexPath:indexPath];
	YJOrderMainMsgModel *model = cellModel.model;
	if (model.state.integerValue == 2) {
		YJOrderStatus status = putUpOrderStatus(model.orderStatus);
		if (status != YJOrderStatusNone && model.otherNo && model.otherNo.length > 0) {
			YJCommonStatusMsgViewController *msgVc = [YJCommonStatusMsgViewController new];
			msgVc.orderStatus = status;
			msgVc.searchNo = model.otherNo;
			msgVc.title = explainOrderStatus(status);
			[self.navigationController pushViewController:msgVc animated:YES];
		}
	} else if (model.state.integerValue == 3 || model.state.integerValue == 5) {
//		YJExpenseViewController *pageVc = [YJExpenseViewController expenseVc];
		YJPaymentViewController *pageVc = [YJPaymentViewController new];
		[self.navigationController pushViewController:pageVc animated:YES];
	}
}

@end
