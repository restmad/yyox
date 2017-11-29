//
//  YJRefreshViewModel.h
//  yyox
//
//  Created by ddn on 2016/12/30.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>

UIKIT_EXTERN NSString *const YJRefreshViewWillRefreshByRefreshViewModel;
static NSString *identify = @"cell";
@interface YJRefreshViewModel : NSObject

@property (assign, nonatomic, readonly) BOOL dataChanged;

@property (strong, nonatomic, readonly) NSMutableArray *models;

@property (assign, nonatomic, readonly) NSInteger modelsCount;

@property (assign, nonatomic) NSInteger footerRefreshCount;
@property (copy, nonatomic) NSString *netUrl;
@property (copy, nonatomic) NSDictionary *baseParams;
@property (assign, nonatomic) int netMethod; //0. GET   1. POST

@property (weak, nonatomic) UIScrollView *refreshView;

@property (assign, nonatomic) BOOL autoRefreshVisiableCells;

@property (assign, nonatomic) Class cellClass;
@property (assign, nonatomic) Class modelClass;

- (UIView *)cellForIndexPath:(NSIndexPath *)indexPath;

@property (copy, nonatomic) NSObject *(^modelForIndexPath)(NSIndexPath *);
- (NSObject *)modelForIndexPath:(NSIndexPath *)indexPath;

@property (copy, nonatomic) NSURLSessionDataTask *(^refresh)(NSInteger);
- (NSURLSessionDataTask *)refresh:(NSInteger)start;

- (NSURLSessionDataTask *)headerRefresh;

- (BOOL)aviableForCellAndView;

- (void)appendModels:(NSArray *)models;

- (NSURLSessionDataTask *)removeModelAtIdx:(NSInteger)idx success:(void(^)())success;

- (void)insertModels:(NSArray *)models atIndexs:(NSIndexSet *)indexSet;

- (void)resetModels;

- (void)doWithNetResult:(id)result start:(NSInteger)start;

@property (copy, nonatomic) CGFloat (^cellHeightForIndexPath)(NSIndexPath *);
- (CGFloat)cellHeightForIndexPath:(NSIndexPath *)indexPath;

- (void)postRefreshNotify;

@end
