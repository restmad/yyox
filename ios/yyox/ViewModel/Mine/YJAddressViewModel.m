//
//  YJAddressViewModel.m
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJAddressViewModel.h"
#import "YJAdressCell.h"
#import "YJAddressCellModel.h"

@implementation YJAddressViewModel

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.cellClass = [YJAdressCell class];
		self.modelClass = [YJAddressCellModel class];
		self.netUrl = AddressListApi;
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

- (NSIndexPath *)indexPathForDefautCell
{
	__block NSInteger index = -1;
    [self.models indexOfObjectPassingTest:^BOOL(YJAddressCellModel * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
		if (obj.isBeDefault) {
			*stop = YES;
			index = idx;
			return YES;
		}
		return NO;
	}];
	return index == -1 ? nil : [NSIndexPath indexPathForRow:index inSection:0];
}

/**
 获取数据

 @param start 起始个数
 @return task
 */
- (NSURLSessionDataTask *)refresh:(NSInteger)start
{
	if (!self.netUrl) {
		if (!start) {
			[SVProgressHUD dismissFromView:self.refreshView.superview];
		}
		return nil;
	}
	
	@weakify(self)
	return [YJRouter commonGetRefresh:self.netUrl currentCount:start pageSize:self.footerRefreshCount otherParmas:nil modelClass:[YJAddressModel class] callback:^(YJRouterResponseModel *result) {
		@strongify(self)
		[self.refreshView.mj_footer endRefreshing];
		[self.refreshView.mj_header endRefreshing];
		if (result.code == 0 || result.code == 999) {
			if (result.data) {
				NSMutableArray *arr = [NSMutableArray array];
				for (YJAddressModel *model in result.data) {
					YJAddressCellModel *cellModel = [YJAddressCellModel new];
					[cellModel bindingModel:model];
					[arr addObject:cellModel];
				}
				[self doWithNetResult:arr start:start];
			}
			[SVProgressHUD dismissFromView:self.refreshView.superview];
		}
		else {
			
			[SVProgressHUD showErrorInView:self.refreshView.superview withStatus:result.msg];
		}
	}];
}

- (void)reloadItemAtIndexPath:(NSIndexPath *)indexPath
{
	YJAddressCellModel *cellModel = self.models[indexPath.item];
	[cellModel bindingModel:cellModel.model];
	[self postRefreshNotify];
	[self.refreshView performSelector:@selector(reloadItemsAtIndexPaths:) withObject:@[indexPath]];
}

/**
 删除地址

 @param idx 所处位置
 @param success 成功回调
 @return task
 */
- (NSURLSessionDataTask *)removeModelAtIdx:(NSInteger)idx success:(void (^)())success
{
	YJAddressCellModel *cellModel = self.models[idx];
	YJAddressModel *model = cellModel.model;
	[SVProgressHUD showInView:self.refreshView.superview];
	@weakify(self)
	return [YJRouter commonDelete:DeleteAddressApi params:@{@"id": model.id} callback:^(YJRouterResponseModel *result) {
		if (!weak_self) return ;
		
		if (result.code == 0) {
			if (model.isdefault) {
				[self refresh:0];
			} else {
				[SVProgressHUD showSuccessInView:self.refreshView.superview withStatus:result.msg];
				[super removeModelAtIdx:idx success:nil];
			}
			if (success) {
				success();
			}
		} else {
			[SVProgressHUD showErrorInView:self.refreshView.superview withStatus:result.msg];
		}
	}];
}

@end
