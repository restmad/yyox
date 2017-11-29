//
//  YJWaitDoCell.h
//  yyox
//
//  Created by ddn on 2017/2/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJWaitDoCellModel.h"

@interface YJWaitDoCell : UICollectionViewCell

@property (weak, nonatomic) YJWaitDoCellModel *cellModel;

@property (copy, nonatomic) void((^clickOnInfo)(NSString *));

@end
