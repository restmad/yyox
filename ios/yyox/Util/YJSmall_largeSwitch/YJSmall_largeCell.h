//
//  YJSmall_largeCell.h
//  YJSmall_largeSwitchDemo
//
//  Created by ddn on 2017/3/15.
//  Copyright © 2017年 张永俊. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MJExtension.h"
#import "YYWebImage.h"
#import "YJSmall_LargeModel.h"

@interface YJSmall_largeCell : UICollectionViewCell

@property (strong, nonatomic) NSObject<YJSmall_LargeModel> *model;
@property (strong, nonatomic, readonly) UIImageView *imageView;
@property (assign, nonatomic) BOOL hasLargeImage;
@property (copy, nonatomic) void(^longPressAction)();

@end
