//
//  YJWaitDoCellModel.h
//  yyox
//
//  Created by ddn on 2017/2/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPendingCellModel.h"

@interface YJWaitDoCellModel : YJPendingCellModel

@property (assign, nonatomic) CGFloat timePaddingTop;

@property (assign, nonatomic) CGFloat statusLeft;
@property (assign, nonatomic) CGFloat statusPaddingTop;

@property (assign, nonatomic, readonly) CGRect timeFrame;
@property (assign, nonatomic, readonly) CGRect statusFrame;
@property (assign, nonatomic, readonly) CGRect warehouseFrame;
@property (assign, nonatomic, readonly) CGRect cateFrame;
@property (assign, nonatomic, readonly) CGRect sbFrame;

@property (copy, nonatomic, readonly) NSString *infoText;

@property (strong, nonatomic, readonly) YYTextLayout *timeTextLayout;
@property (strong, nonatomic, readonly) YYTextLayout *statusTextLayout;
@property (strong, nonatomic, readonly) YYTextLayout *warehouseTextLayout;
@property (strong, nonatomic, readonly) YYTextLayout *cateTextLayout;

@property (strong, nonatomic, readonly) YYTextLayout *sbTextLayout;

@end
