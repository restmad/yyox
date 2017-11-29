//
//  YJWaitDoViewModel.m
//  yyox
//
//  Created by ddn on 2017/2/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJWaitDoViewModel.h"
#import "YJToDoModel.h"
#import "YJWaitDoCellModel.h"
#import "YJWaitDoCell.h"

@implementation YJWaitDoViewModel

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.cellClass = [YJWaitDoCell class];
		self.modelClass = [YJWaitDoCellModel class];
		self.netUrl = WaitDoListApi;
		self.footerRefreshCount = 10;
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
	return [YJRouter commonGetRefresh:self.netUrl currentCount:start pageSize:self.footerRefreshCount otherParmas:self.baseParams modelClass:[YJToDoModel class] callback:^(YJRouterResponseModel *result) {
		@strongify(self)
		
		[self.refreshView.mj_footer endRefreshing];
		[self.refreshView.mj_header endRefreshing];
		if (result.code == 0 || result.code == 999) {
			if (result.data) {
				NSMutableArray *arr = [NSMutableArray array];
				for (YJToDoModel *model in result.data) {
					YJWaitDoCellModel *cellModel = [YJWaitDoCellModel new];
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
	
	return nil;
}

@end






