//
//  UICollectionView+YJExtension.m
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "UICollectionView+YJExtension.h"
#import "NSIndexPath+YJArray.h"

@implementation UICollectionView (YJExtension)

- (void)yj_insertItemsAtIndexPaths:(NSArray *)indexPaths
{
	self.userInteractionEnabled = NO;
	dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.01 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
		[self performBatchUpdates:^{
			[self insertItemsAtIndexPaths:indexPaths];
		} completion:^(BOOL finished) {
			if (finished) {
				NSArray *visiableIndexPaths = [self indexPathsForVisibleItems];
				[self reloadItemsAtIndexPaths:visiableIndexPaths];
			}
			self.userInteractionEnabled = YES;
		}];
	});
}

- (void)yj_insertItemsAtIndexSet:(NSIndexSet *)indexSet inSection:(NSInteger)section
{
	NSArray *indexPaths = [NSIndexPath indexPathsForIndextSet:indexSet inSection:section];
	[self yj_insertItemsAtIndexPaths:indexPaths];
}

- (void)yj_removeItemsAtIndexPaths:(NSArray *)indexPaths
{
	dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.01 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
		[self performBatchUpdates:^{
			[self deleteItemsAtIndexPaths:indexPaths];
		} completion:^(BOOL finished) {
			if (finished) {
				NSArray *visiableIndexPaths = [self indexPathsForVisibleItems];
				[self reloadItemsAtIndexPaths:visiableIndexPaths];
			}
		}];
	});
}

- (void)yj_removeItemsAtIndexSet:(NSIndexSet *)indexSet inSection:(NSInteger)section
{
	NSArray *indexPaths = [NSIndexPath indexPathsForIndextSet:indexSet inSection:section];
	[self yj_removeItemsAtIndexPaths:indexPaths];
}

@end
