//
//  YJEditGoodsView.h
//  yyox
//
//  Created by ddn on 2017/2/28.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJMerchandiseModel.h"
#import "YJGoodsModel.h"

@interface YJEditGoodsView : UIView

@property (strong, nonatomic) YJMerchandiseModel *merchandise;
@property (strong, nonatomic) YJGoodsModel *goods;

+ (instancetype)editGoodsView;

@property (copy, nonatomic) void(^clickOnDone)(YJGoodsModel *, NSDictionary *, YJGoodsModel *);

- (void)reloadData;

@end
