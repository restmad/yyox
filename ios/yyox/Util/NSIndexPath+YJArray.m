//
//  NSIndexPath+YJArray.m
//  yyox
//
//  Created by ddn on 2017/1/3.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "NSIndexPath+YJArray.h"

@implementation NSIndexPath (YJArray)

+ (NSArray *)indexPathsForCells:(NSRange)range inSection:(NSInteger)section
{
	NSMutableArray *indexPaths = [NSMutableArray array];
	for (NSUInteger i=range.location; i<range.length; i++) {
		NSIndexPath *indexPath = [NSIndexPath indexPathForItem:i inSection:section];
		[indexPaths addObject:indexPath];
	}
	return indexPaths;
}

+ (NSArray *)indexPathsForIndextSet:(NSIndexSet *)indexSet inSection:(NSInteger)section
{
	__block NSMutableArray *indexPaths = [NSMutableArray array];
	[indexSet enumerateIndexesUsingBlock:^(NSUInteger idx, BOOL * _Nonnull stop) {
		NSIndexPath *indexPath = [NSIndexPath indexPathForItem:idx inSection:section];
		[indexPaths addObject:indexPath];
	}];
	return indexPaths;
}

@end
