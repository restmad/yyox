//
//  YJCommonRefreshViewController.m
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonRefreshViewController.h"

@interface YJCommonRefreshViewController () 


@end

@implementation YJCommonRefreshViewController

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.contentInset = UIEdgeInsetsZero;
		self.refreshViewModelClass = [YJRefreshViewModel class];
		self.lineSpace = 10;
		self.autoSize = NO;
	}
	return self;
}

- (UICollectionView *)refreshView
{
	if (!_refreshView) {
		UICollectionView *refreshView = [[UICollectionView alloc] initWithFrame:self.view.bounds collectionViewLayout:[UICollectionViewFlowLayout new]];
		_refreshView = refreshView;
		refreshView.dataSource = self;
		refreshView.delegate = self;
		
		[self.view addSubview:refreshView];
		
		refreshView.backgroundColor = kGlobalBackgroundColor;
		[refreshView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.edges.mas_equalTo(0);
		}];
		refreshView.contentInset = self.contentInset;
		refreshView.scrollIndicatorInsets = refreshView.contentInset;
		
		if (_refreshViewModel && !_refreshViewModel.refreshView) {
			_refreshViewModel.refreshView = _refreshView;
		}
	}
	return _refreshView;
}

- (YJRefreshViewModel *)refreshViewModel
{
	if (!_refreshViewModel) {
		_refreshViewModel = [self.refreshViewModelClass new];
		if (_refreshView) {
			_refreshViewModel.refreshView = _refreshView;
		}
	}
	return _refreshViewModel;
}

- (void)viewDidLoad {
	[super viewDidLoad];
	self.view.backgroundColor = kGlobalBackgroundColor;
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	
	[self defaultDealWhenViewDidAppear];
}

- (void)defaultDealWhenViewDidAppear
{
	if (self.autoRefresh && [YJEnterControllerManager isLogin]) {
		[SVProgressHUD showInView:self.refreshView.superview withStatus:nil userEnable:NO];
		NSURLSessionDataTask *task = [self.refreshViewModel headerRefresh];
		[self weakHoldTask:task];
		self.autoRefresh = NO;
	}
}

- (BOOL)respondsToSelector:(SEL)aSelector
{
	if (aSelector == @selector(collectionView:layout:sizeForItemAtIndexPath:)) {
		return !self.autoSize;
	}
	return [super respondsToSelector:aSelector];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
	return self.refreshViewModel.modelsCount;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
	return CGSizeMake(collectionView.width - collectionView.contentInset.left - collectionView.contentInset.right, [self.refreshViewModel cellHeightForIndexPath:indexPath]);
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
	return (UICollectionViewCell *)[self.refreshViewModel cellForIndexPath:indexPath];
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
	return self.lineSpace;
}

@end
