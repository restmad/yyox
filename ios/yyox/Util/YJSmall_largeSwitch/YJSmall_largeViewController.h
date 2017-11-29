//
//  YJSmall_largeViewController.h
//  YJSmall_largeSwitchDemo
//
//  Created by ddn on 2017/3/15.
//  Copyright © 2017年 张永俊. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CustomTranstion.h"
#import "YJSmall_LargeModel.h"
#import "YJSmall_largeHelper.h"
@class YJSmall_largeViewController;

@protocol YJSmall_LargeDelegate <NSObject>

- (BOOL)small_largeViewController:(YJSmall_largeViewController *)small_largeVc shouldRemoveIndex:(NSInteger)index;

@end

@interface YJSmall_largeViewController : UIViewController <UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout>

@property (strong, nonatomic, readonly) UICollectionView *collectionView;
@property (copy, nonatomic, readonly) NSArray<NSObject<YJSmall_LargeModel> *> *models;
@property (strong, nonatomic) NSIndexPath *currentIndexPath;
@property (weak, nonatomic) id<YJSmall_LargeDelegate> delegate;

+ (instancetype)small_largeViewControllerWithModels:(NSArray<NSObject<YJSmall_LargeModel> *> *)models cellClass:(Class)cellClass;

- (void)removeModelAtIndex:(NSInteger)index;

@end
