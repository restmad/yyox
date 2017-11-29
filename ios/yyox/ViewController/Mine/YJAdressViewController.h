//
//  YJAdressViewController.h
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJCommonRefreshViewController.h"
#import "YJAddressModel.h"

@interface YJAdressViewController : YJCommonRefreshViewController

@property (copy, nonatomic) void((^selectedCallback)(YJAddressModel *));

@property (assign, nonatomic) NSInteger showType;//0. 1.选择保存 2.选择下一步

+ (YJAdressViewController *)addressWithShowType:(NSInteger)showType;

@property (strong, nonatomic) NSNumber *selectId;

@end
