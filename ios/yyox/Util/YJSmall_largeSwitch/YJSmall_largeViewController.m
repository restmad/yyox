//
//  YJSmall_largeViewController.m
//  YJSmall_largeSwitchDemo
//
//  Created by ddn on 2017/3/15.
//  Copyright © 2017年 张永俊. All rights reserved.
//

#import "YJSmall_largeViewController.h"
#import "YJSmall_largeCell.h"

@interface YJSmall_largeViewController () 

@property (strong, nonatomic) UICollectionView *collectionView;

@property (assign, nonatomic) Class cellClass;

@property (copy, nonatomic) NSArray<NSObject<YJSmall_LargeModel> *> *models;


@end

@implementation YJSmall_largeViewController

- (instancetype)init
{
	self = [super init];
	if (self) {
		[self setup];
	}
	return self;
}

- (void)setup
{
	self.cellClass = [YJSmall_largeCell class];
}

+ (instancetype)small_largeViewControllerWithModels:(NSArray<NSObject<YJSmall_LargeModel> *> *)models cellClass:(Class)cellClass
{
	YJSmall_largeViewController *small_largeVc = [self new];
	small_largeVc.models = models;
	if (cellClass && [cellClass isSubclassOfClass:[YJSmall_largeCell class]]) {
		small_largeVc.cellClass = cellClass;
	}
	return small_largeVc;
}

- (UICollectionView *)collectionView
{
	if (!_collectionView) {
		UICollectionViewFlowLayout *layout = [UICollectionViewFlowLayout new];
		layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
		_collectionView = [[UICollectionView alloc] initWithFrame:self.view.bounds collectionViewLayout:layout];
		_collectionView.pagingEnabled = YES;
		[_collectionView registerClass:self.cellClass forCellWithReuseIdentifier:@"small_largeCell"];
		[self.view addSubview:_collectionView];
		_collectionView.dataSource = self;
		_collectionView.delegate = self;
		_collectionView.backgroundColor = [UIColor clearColor];
	}
	return _collectionView;
}

- (void)viewDidLoad {
    [super viewDidLoad];
	self.view.backgroundColor = [UIColor blackColor];
	[self.collectionView reloadData];
}

- (void)removeModelAtIndex:(NSInteger)index
{
	if (self.models.count > index) {
		if (self.delegate && [self.delegate respondsToSelector:@selector(small_largeViewController:shouldRemoveIndex:)]) {
			BOOL canDo = [self.delegate small_largeViewController:self shouldRemoveIndex:index];
			if (canDo) {
				NSMutableArray *arr = self.models.mutableCopy;
				[arr removeObjectAtIndex:index];
				self.models = arr;
				[self.collectionView deleteItemsAtIndexPaths:@[[NSIndexPath indexPathForItem:index inSection:0]]];
			}
			return;
		}
		NSMutableArray *arr = self.models.mutableCopy;
		[arr removeObjectAtIndex:index];
		self.models = arr;
		[self.collectionView deleteItemsAtIndexPaths:@[[NSIndexPath indexPathForItem:index inSection:0]]];
	}
}

- (void)viewDidLayoutSubviews
{
	[super viewDidLayoutSubviews];
	_collectionView.frame = self.view.bounds;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
	return self.models.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
	UICollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"small_largeCell" forIndexPath:indexPath];
	NSObject *model = [self modelForIndexPath:indexPath];
	[cell setValue:model forKey:@"model"];
	return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
	return CGSizeMake(collectionView.bounds.size.width, collectionView.bounds.size.height);
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
	return 0;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section
{
	return 0;
}

- (void)collectionView:(UICollectionView *)collectionView willDisplayCell:(UICollectionViewCell *)cell forItemAtIndexPath:(NSIndexPath *)indexPath
{
	self.currentIndexPath = indexPath;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
	[self dismissViewControllerAnimated:YES completion:^{
		
	}];
}

- (NSObject<YJSmall_LargeModel> *)modelForIndexPath:(NSIndexPath *)indexPath
{
	if (self.models.count > indexPath.item) {
		return self.models[indexPath.item];
	}
	return nil;
}

@end





