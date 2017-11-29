//
//  YJCouponViewController.h
//  yyox
//
//  Created by ddn on 2017/3/27.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonRefreshViewController.h"
@class YJCouponModel;

@interface YJCouponViewController : YJCommonRefreshViewController

@property (copy, nonatomic) void(^callback)(YJCouponModel *);
@property (copy, nonatomic) void(^cancelCallback)();

- (void)appendParams:(NSString *)money warehouse:(NSNumber *)warehouseRange transport:(NSNumber *)ordertypeRange;

@end
