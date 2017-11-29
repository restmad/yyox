//
//  YJCommonStatusOrderViewController.m
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonStatusOrderViewController.h"
#import "YJCommonStatusOrderCell.h"
#import "YJCommonStatusOrderModel.h"

#import "YJCommonStatusMsgViewController.h"

@interface YJCommonStatusOrderViewController ()

@end

@implementation YJCommonStatusOrderViewController

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.lineSpace = 10;
		self.refreshViewModelClass = [YJRefreshViewModel class];
		self.checkCount = 0;
	}
	return self;
}


/**
 根据订单状态初始化viewModel

 @param orderStatus 订单状态
 */
- (void)setOrderStatus:(YJOrderStatus)orderStatus
{
	_orderStatus = orderStatus;
	
	self.refreshViewModel.baseParams = @{@"statusStr": explainOrderStatus(orderStatus), @"showCancleOrder": @"false", @"checkCount": @(self.checkCount)};
	self.refreshViewModel.netUrl = OrderStatusListApi;
	self.refreshViewModel.footerRefreshCount = 10;
	self.refreshViewModel.autoRefreshVisiableCells = NO;
	
	self.refreshViewModel.modelClass = [YJCommonStatusOrderModel class];
	self.refreshViewModel.cellClass = [YJCommonStatusOrderCell class];
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
	[self.refreshView reloadData];
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
	YJCommonStatusOrderCell *cell = (YJCommonStatusOrderCell *)[super collectionView:collectionView cellForItemAtIndexPath:indexPath];
//	cell.orderStatus = explainOrderStatus(self.orderStatus);
	return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
	return CGSizeMake(collectionView.width - collectionView.contentInset.left - collectionView.contentInset.right, 94);
}


/**
 跳转历史消息页

 @param collectionView collectionView
 @param indexPath indexPath
 */
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
	YJCommonStatusMsgViewController *msgVc = [YJCommonStatusMsgViewController new];
	msgVc.orderStatus = self.orderStatus;
	YJCommonStatusOrderModel *model = (YJCommonStatusOrderModel *)[self.refreshViewModel modelForIndexPath:indexPath];
	msgVc.searchNo = model.orderNo;
	msgVc.title = model.nickname;
	[self.navigationController pushViewController:msgVc animated:YES];
}


@end
