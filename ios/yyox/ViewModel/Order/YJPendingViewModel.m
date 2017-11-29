//
//  YJPendingViewModel.m
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPendingViewModel.h"
#import "YJPendingCell.h"
#import "YJPendingCellModel.h"
#import "YJMerchandiseModel.h"

@implementation YJPendingViewModel

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.cellClass = [YJPendingCell class];
		self.modelClass = [YJPendingCellModel class];
		self.netUrl = PendingOrderApi;
		self.baseParams = @{@"statusCodeC": explainOrderStatus(YJOrderStatusWaitIn)};
		self.footerRefreshCount = 20;
	}
	return self;
}

- (CGFloat)cellHeightForIndexPath:(NSIndexPath *)indexPath
{
	if (indexPath.row >= self.models.count) {
		return 0;
	}
	return [self.models[indexPath.row] cellH];
}

- (NSURLSessionDataTask *)refresh:(NSInteger)start
{
	if (!self.netUrl) {
		if (!start) {
			[SVProgressHUD dismissFromView:self.refreshView.superview];
		}
		return nil;
	}
	@weakify(self)
	return [YJRouter commonPostRefresh:self.netUrl currentCount:start pageSize:self.footerRefreshCount otherParmas:self.baseParams modelClass:[YJMerchandiseModel class] callback:^(YJRouterResponseModel *result) {
		@strongify(self)
		
		[self.refreshView.mj_footer endRefreshing];
		[self.refreshView.mj_header endRefreshing];
		if (result.code == 0 || result.code == 999) {
			if (result.data) {
				NSMutableArray *arr = [NSMutableArray array];
				for (YJMerchandiseModel *model in result.data) {
					YJPendingCellModel *cellModel = [YJPendingCellModel new];
					[cellModel bindingModel:model];
					[arr addObject:cellModel];
				}
				[self doWithNetResult:arr start:start];
			}
			[SVProgressHUD dismissFromView:self.refreshView.superview];
			self.refreshView.mj_footer.hidden = result.code == 999;
		}
		else {
			
			[SVProgressHUD showErrorInView:self.refreshView.superview withStatus:result.msg];
		}
	}];
}

- (NSURLSessionDataTask *)removeModelAtIdx:(NSInteger)idx success:(void (^)())success
{
	YJPendingCellModel *cellModel = self.models[idx];
	YJMerchandiseModel *model = (YJMerchandiseModel *)cellModel.model;
	[SVProgressHUD showInView:self.refreshView.superview];
	NSURLSessionDataTask *task = [YJRouter commonDelete:[NSString stringWithFormat:@"%@/%@", DeletePendingOrderApi, model.id] params:nil callback:^(YJRouterResponseModel *result) {
		if (result.code == 0) {
			[SVProgressHUD showSuccessInView:self.refreshView.superview withStatus:result.msg];
			[super removeModelAtIdx:idx success:success];
		} else {
			[SVProgressHUD showErrorInView:self.refreshView.superview withStatus:result.msg];
		}
	}];
	return task;
}

- (void)reloadItemAtIndexPath:(NSIndexPath *)indexPath
{
	YJPendingCellModel *cellModel = self.models[indexPath.item];
	[cellModel bindingModel:cellModel.model];
	[self postRefreshNotify];
	[self.refreshView performSelector:@selector(reloadItemsAtIndexPaths:) withObject:@[indexPath]];
}

@end
