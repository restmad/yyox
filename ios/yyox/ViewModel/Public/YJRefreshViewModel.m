//
//  YJRefreshViewModel.m
//  yyox
//
//  Created by ddn on 2016/12/30.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJRefreshViewModel.h"
#import "NSIndexPath+YJArray.h"
#import "UICollectionView+YJExtension.h"

NSString *const YJRefreshViewWillRefreshByRefreshViewModel = @"YJRefreshViewWillRefreshByRefreshViewModel";
@interface YJRefreshViewModel()

@property (assign, nonatomic) BOOL registered;

@property (strong, nonatomic) NSMutableArray *models;

@end

@implementation YJRefreshViewModel

- (NSMutableArray *)models
{
	if (!_models) {
		_models = [NSMutableArray array];
		[self addObserver:self forKeyPath:@"models" options:0x03 context:nil];
	}
	return _models;
}

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.autoRefreshVisiableCells = YES;
		self.footerRefreshCount = 20;
		self.netMethod = 1;
		[NSDC addObserver:self selector:@selector(didLogout) name:DidLogOutNotification object:nil];
	}
	return self;
}

- (void)didLogout
{
	[self.models removeAllObjects];
	[self.refreshView performSelector:@selector(reloadData)];
	self.refreshView.mj_footer.hidden = YES;
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSKeyValueChangeKey,id> *)change context:(void *)context
{
	UICollectionView *collectionView = (UICollectionView *)self.refreshView;
	UITableView *tableView = (UITableView *)self.refreshView;
	if ([keyPath isEqualToString:@"models"]) {
		
		_dataChanged = YES;
		
		NSIndexSet *indexSet = change[@"indexes"];
		
		NSArray *indexPaths = [NSIndexPath indexPathsForIndextSet:indexSet inSection:0];
		
		if (indexPaths.count > 0) {
			[self postRefreshNotify];
		}
		
		if (change[@"old"]) {
			if ([self.refreshView isKindOfClass:[UICollectionView class]]) {
				[collectionView yj_removeItemsAtIndexPaths:indexPaths];
			}
			else if ([self.refreshView isKindOfClass:[UITableView class]]) {
				[tableView beginUpdates];
				if (tableView.showType == YJTableViewShowTypeSections) {
					[tableView deleteSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
				} else {
					[tableView deleteRowsAtIndexPaths:indexPaths withRowAnimation:UITableViewRowAnimationNone];
				}
				[tableView endUpdates];
				NSArray *visiableIndexPaths = [tableView indexPathsForVisibleRows];
				[tableView reloadRowsAtIndexPaths:visiableIndexPaths withRowAnimation:UITableViewRowAnimationNone];
			}
		} else if (change[@"new"]) {
			if ([self.refreshView isKindOfClass:[UICollectionView class]]) {
				self.autoRefreshVisiableCells ? [collectionView yj_insertItemsAtIndexPaths:indexPaths] : [collectionView insertItemsAtIndexPaths:indexPaths];
			}
			else if ([self.refreshView isKindOfClass:[UITableView class]]) {
//				[tableView beginUpdates];
				if (tableView.showType == YJTableViewShowTypeSections) {
					[tableView insertSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
				} else {
					[tableView insertRowsAtIndexPaths:indexPaths withRowAnimation:UITableViewRowAnimationNone];
				}
//				[tableView endUpdates];
//				NSArray *visiableIndexPaths = [tableView indexPathsForVisibleRows];
//				[tableView reloadRowsAtIndexPaths:visiableIndexPaths withRowAnimation:UITableViewRowAnimationNone];
			}
		}
	}
}

- (void)postRefreshNotify
{
	[NSDC postNotificationName:YJRefreshViewWillRefreshByRefreshViewModel object:self];
}

- (void)setRefreshView:(UIScrollView *)refreshView
{
	if (_refreshView) return;
	_refreshView = refreshView;
	if (refreshView) {
		[self registerCell];
		[self bindingRefresh];
	}
}

- (void)setCellClass:(Class)cellClass
{
	_cellClass = cellClass;
	if (cellClass) {
		[self registerCell];
	}
}

- (void)registerCell
{
	if (_registered) return;
	
	if (![self aviableForCellAndView]) return;
	
	if ([self.refreshView isKindOfClass:[UITableView class]]) {
		[self.refreshView performSelector:@selector(registerClass:forCellReuseIdentifier:) withObject:self.cellClass withObject:identify];
	} else if ([self.refreshView isKindOfClass:[UICollectionView class]]) {
		[self.refreshView performSelector:@selector(registerClass:forCellWithReuseIdentifier:) withObject:self.cellClass withObject:identify];
	}
	_registered = YES;
}

- (BOOL)aviableForCellAndView
{
	if (!self.cellClass || !self.refreshView) return NO;
	
	if ((!([self.refreshView isKindOfClass:[UITableView class]] && [self.cellClass isSubclassOfClass:[UITableViewCell class]])) && (!([self.refreshView isKindOfClass:[UICollectionView class]] && [self.cellClass isSubclassOfClass:[UICollectionViewCell class]]))) return NO;
	
	return YES;
}

- (void)bindingRefresh
{
	if (!self.refreshView) return;
	
	if (!self.refreshView.mj_header) {
		self.refreshView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
		[(MJRefreshNormalHeader *)self.refreshView.mj_header lastUpdatedTimeLabel].hidden = YES;
		self.refreshView.mj_header.ignoredScrollViewContentInsetTop = self.refreshView.contentInset.top;
	}
	if (!self.refreshView.mj_footer) {
		self.refreshView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
		[(MJRefreshAutoNormalFooter *)self.refreshView.mj_footer setAutomaticallyRefresh:YES];
		[(MJRefreshAutoNormalFooter *)self.refreshView.mj_footer setTriggerAutomaticallyRefreshPercent:-5];
		self.refreshView.mj_footer.hidden = YES;
	}
}

- (NSURLSessionDataTask *)headerRefresh
{
	return [self refresh:0];
}

- (NSURLSessionDataTask *)footerRefresh
{
	return [self refresh:self.modelsCount];
}

- (NSURLSessionDataTask *)refresh:(NSInteger)start
{
	if (!_netUrl) {
		if (!start) {
			self.refreshView.mj_footer.hidden = YES;
			[SVProgressHUD dismissFromView:self.refreshView.superview];
			[self.refreshView.mj_footer endRefreshing];
			[self.refreshView.mj_header endRefreshing];
		}
		return nil;
	}
	
	if (self.refresh) {
		return self.refresh(start);
	}
	
	void (^dealWithResult)(YJRouterResponseModel *) = ^(YJRouterResponseModel *result) {
		[self.refreshView.mj_footer endRefreshing];
		[self.refreshView.mj_header endRefreshing];
		
		if (result.code == 0 || result.code == 999) {
			
			[self doWithNetResult:result.data start:start];
			self.refreshView.mj_footer.hidden = result.code == 999;
			[SVProgressHUD dismissFromView:self.refreshView.superview];
		} else {
			[SVProgressHUD showErrorInView:self.refreshView.superview withStatus:result.msg];
		}
	};
	
	if (self.netMethod == 0) {
		return [YJRouter commonGetRefresh:_netUrl currentCount:start pageSize:self.footerRefreshCount otherParmas:_baseParams modelClass:_modelClass callback:^(YJRouterResponseModel *result) {
			dealWithResult(result);
		}];
	}
	return [YJRouter commonPostRefresh:_netUrl currentCount:start pageSize:self.footerRefreshCount otherParmas:_baseParams modelClass:_modelClass callback:^(YJRouterResponseModel *result) {
		dealWithResult(result);
	}];
}

- (void)doWithNetResult:(id)result start:(NSInteger)start
{
	if (![self aviableForCellAndView]) return;
	if (!start) {
		[self resetModels];
		[self.models addObjectsFromArray:result];
		[self postRefreshNotify];
		[self.refreshView performSelector:@selector(reloadData)];
		return;
	}
	self.autoRefreshVisiableCells = NO;
	[self appendModels:result];
}

- (void)resetModels
{
	[self.models removeAllObjects];
}

- (void)insertModels:(NSArray *)models atIndexs:(NSIndexSet *)indexSet
{
	[[self mutableArrayValueForKeyPath:@"models"] insertObjects:models atIndexes:indexSet];
}

- (void)appendModels:(NSArray *)models
{
	[[self mutableArrayValueForKeyPath:@"models"] addObjectsFromArray:models];
}

- (NSURLSessionDataTask *)removeModelAtIdx:(NSInteger)idx success:(void (^)())success
{
	self.autoRefreshVisiableCells = YES;
	[[self mutableArrayValueForKeyPath:@"models"] removeObjectAtIndex:idx];
	if (success) {
		success();
	}
	return nil;
}

- (NSInteger)modelsCount
{
	return _models.count;
}

- (UIView *)cellForIndexPath:(NSIndexPath *)indexPath
{
	UIView *cell;
	if ([self aviableForCellAndView]) {
		if ([self.refreshView isKindOfClass:[UITableView class]]) {
			cell = [self.refreshView performSelector:@selector(dequeueReusableCellWithIdentifier:forIndexPath:) withObject:identify withObject:indexPath];
		} else if ([self.refreshView isKindOfClass:[UICollectionView class]]) {
			cell = [self.refreshView performSelector:@selector(dequeueReusableCellWithReuseIdentifier:forIndexPath:) withObject:identify withObject:indexPath];
		}
		if (_models.count > 0) {
			[_cellClass mj_enumerateProperties:^(MJProperty *property, BOOL *stop) {
				NSObject *model = [self modelForIndexPath:indexPath];
				if ([property.type.typeClass isSubclassOfClass:[model class]]) {
					[cell setValue:model forKeyPath:property.name];
					*stop = YES;
				}
			}];
		}
	}
	return cell;
}

- (NSObject *)modelForIndexPath:(NSIndexPath *)indexPath
{
	if (self.modelForIndexPath) {
		return self.modelForIndexPath(indexPath);
	}
	if (_models.count > indexPath.row) {
		return _models[indexPath.row];
	}
	return nil;
}

- (CGFloat)cellHeightForIndexPath:(NSIndexPath *)indexPath
{
	if (self.cellHeightForIndexPath) {
		return self.cellHeightForIndexPath(indexPath);
	}
	return 44;
}

- (void)dealloc
{
	if (_models) {
		[self removeObserver:self forKeyPath:@"models"];
	}
	[NSDC removeObserver:self];
}

@end
