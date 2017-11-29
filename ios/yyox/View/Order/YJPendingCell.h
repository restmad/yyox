//
//  YJPendingCell.h
//  yyox
//
//  Created by ddn on 2017/1/3.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJPendingCellModel.h"

@interface YJPendingCell : UICollectionViewCell

@property (strong, nonatomic) YJPendingCellModel *cellModel;

@property (copy, nonatomic) void(^clickOn)(NSString *title);

@end
