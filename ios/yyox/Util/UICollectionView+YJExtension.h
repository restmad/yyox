//
//  UICollectionView+YJExtension.h
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UICollectionView (YJExtension)

- (void)yj_insertItemsAtIndexPaths:(NSArray *)indexPaths;

- (void)yj_insertItemsAtIndexSet:(NSIndexSet *)indexSet inSection:(NSInteger)section;

- (void)yj_removeItemsAtIndexPaths:(NSArray *)indexPaths;

- (void)yj_removeItemsAtIndexSet:(NSIndexSet *)indexSet inSection:(NSInteger)section;

@end
