//
//  YJOrderViewModel.h
//  yyox
//
//  Created by ddn on 2016/12/28.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJRefreshViewModel.h"

#import "YJOrderMainModel.h"

@interface YJOrderViewModel : YJRefreshViewModel

@property (weak, nonatomic) iCarousel *carousel;

@property (copy, nonatomic) void(^refreshHeaderView)(YJOrderMainModel *model);

@property (copy, nonatomic) void(^clickOnCarousel)(NSInteger index);

- (void)initialCarousel;

@end
