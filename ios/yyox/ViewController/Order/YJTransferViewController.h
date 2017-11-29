//
//  YJTransferViewController.h
//  yyox
//
//  Created by ddn on 2017/2/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonRefreshViewController.h"
#import "YJTransferModel.h"
#import "YJMerchandiseModel.h"

@interface YJTransferViewController : UIViewController

+ (YJTransferViewController *)instanceWithWarehouseId:(id)warehouseId weight:(NSNumber *)weight model:(YJTransferModel *)model;

- (void)setTextForBottom:(NSString *)text;

@property (copy, nonatomic) void(^callback)(YJTransferModel *);

@end
