//
//  YJCommonRefreshViewController.h
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJRefreshViewModel.h"

@interface YJCommonRefreshViewController : UIViewController <UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout>

@property (strong, nonatomic) YJRefreshViewModel *refreshViewModel;

@property (strong, nonatomic) UICollectionView *refreshView;

@property (strong, nonatomic) Class refreshViewModelClass;

@property (assign, nonatomic) UIEdgeInsets contentInset;

@property (assign, nonatomic) BOOL autoSize;

@property (assign, nonatomic) CGFloat lineSpace;

- (void)defaultDealWhenViewDidAppear;

@end
