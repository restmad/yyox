//
//  YJOrderViewModel.m
//  yyox
//
//  Created by ddn on 2016/12/28.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJOrderViewModel.h"
#import "YJiCarouselViewModel.h"

#import "YJOrderCell.h"
#import "YJOrderMsgModel.h"

#import "YJOrderCellModel.h"

@interface YJOrderViewModel() <YJiCarouselViewModelDelegate>

@property (strong, nonatomic) YJiCarouselViewModel *carouselViewModel;

@end

@implementation YJOrderViewModel

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.cellClass = [YJOrderCell class];
		self.modelClass = [YJOrderCellModel class];
		self.autoRefreshVisiableCells = NO;
		self.netUrl = OrderMainApi;
		self.footerRefreshCount = 10;
		
	}
	return self;
}

- (YJiCarouselViewModel *)carouselViewModel
{
	if (!_carouselViewModel) {
		_carouselViewModel = [YJiCarouselViewModel new];
		_carouselViewModel.carousel = self.carousel;
		_carouselViewModel.delegate = self;
	}
	return _carouselViewModel;
}

- (CGFloat)cellHeightForIndexPath:(NSIndexPath *)indexPath
{
	if (indexPath.row >= self.models.count) {
		return 0;
	}
	return [self.models[indexPath.row] cellH];
}


/**
 初始化轮播图
 */
- (void)initialCarousel
{
	if (!_carouselViewModel) {
		YJiCarouselModel *model = [YJiCarouselModel new];
		model.localImage = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"order_circle01" ofType:@"png"]];
		YJiCarouselModel *model2 = [YJiCarouselModel new];
		model2.localImage = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"order_circle02" ofType:@"png"]];
		self.carouselViewModel.datas = @[model, model2];
	}
}

/**
 发送网络请求

 @param start 起始位置
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
	NSMutableDictionary *params = @{}.mutableCopy;
	params[@"pageNumber"] = @(self.footerRefreshCount);
	if (start) {
		params[@"date"] = [(YJOrderMainMsgModel *)[[self.models lastObject] model] operateTime];
	}
	return [YJRouter commonGetModel:self.netUrl params:params modelClass:[YJOrderMainModel class] callback:^(YJRouterResponseModel *result) {
		[self.refreshView.mj_header endRefreshing];
		[self.refreshView.mj_footer endRefreshing];
		if (result.code == 0) {
			[SVProgressHUD dismissFromView:self.refreshView.superview];
			if (self.refreshHeaderView) {
				self.refreshHeaderView(result.data);
			}
			NSArray *arr = [result.data result];
			self.refreshView.mj_footer.hidden = arr.count <= self.footerRefreshCount;
			if (arr.count > 0) {
				[self doWithNetResult:arr start:start];
			}
		} else if (result.code == 2) {
			self.refreshView.mj_footer.hidden = YES;
			[SVProgressHUD dismissFromView:self.refreshView.superview];
			
			if (self.refreshHeaderView) {
				self.refreshHeaderView(nil);
			}
			[self.models removeAllObjects];
			[self.refreshView performSelector:@selector(reloadData)];
		} else {
			[SVProgressHUD showErrorInView:self.refreshView.superview withStatus:result.msg];
		}
	}];
}

- (void)doWithNetResult:(id)result start:(NSInteger)start
{
	NSMutableArray *arr = [NSMutableArray array];
	for (YJOrderMainMsgModel *model in result) {
		YJOrderCellModel *cellModel = [YJOrderCellModel new];
		[cellModel bindingModel:model];
		[arr addObject:cellModel];
	}
	
	[super doWithNetResult:arr start:start];
}

//- (void)doWithNetResult:(id)result start:(NSInteger)start
//{
//	if (![self aviableForCellAndView]) return;
//	if (!start) {
//		int r=0;
//		if (self.models.count > 0) {
//			YJOrderCellModel *firstCellModel = self.models[0];
//			NSString *firstTime = firstCellModel.model.operateTime;
//			
//			for (NSInteger i=[result count]-1; i>=0; i--) {
//				YJOrderMainMsgModel *model = result[i];
//				NSString *time = model.operateTime;
//				NSComparisonResult compareResult = [time compare:firstTime];
//				r++;
//				if (compareResult == NSOrderedDescending) {//新
//					[self resetModels];
//					break;
//				} else if (compareResult == NSOrderedAscending) {//旧
//					continue;
//				} else {
//					break;
//				}
//			}
//		} else {
//			if ([result count] > self.footerRefreshCount) r++;
//		}
//		
//		NSMutableArray *newResult = [result mutableCopy];
//		if (r > 0) {
//			[newResult removeObjectsAtIndexes:[NSIndexSet indexSetWithIndexesInRange:NSMakeRange([result count]-r, r)]];
//		}
//		
//		NSMutableArray *arr = [NSMutableArray array];
//		for (YJOrderMainMsgModel *model in newResult) {
//			YJOrderCellModel *cellModel = [YJOrderCellModel new];
//			[cellModel bindingModel:model];
//			[arr addObject:cellModel];
//		}
//		
//		if (self.models.count == 0) {
//			//触发列表占位显示
//			[self.models appendObjects:arr];
//			[self.refreshView reloadData];
//			return;
//		}
//		[self insertModels:arr atIndexs:[NSIndexSet indexSetWithIndexesInRange:NSMakeRange(0, [arr count])]];
//		return;
//	}
//	
//	NSMutableArray *arr = [NSMutableArray array];
//	for (YJOrderMainMsgModel *model in result) {
//		YJOrderCellModel *cellModel = [YJOrderCellModel new];
//		[cellModel bindingModel:model];
//		[arr addObject:cellModel];
//	}
//	[self appendModels:arr];
//}

/**
 点击轮播图的回调

 @param carousel 轮播图
 @param index 位置
 */
- (void)carousel:(iCarousel *)carousel didSelectItemAtIndex:(NSInteger)index
{
	if (self.clickOnCarousel) {
		self.clickOnCarousel(index);
	}
}

@end
