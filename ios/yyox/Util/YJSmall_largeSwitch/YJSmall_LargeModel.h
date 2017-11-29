//
//  YJSmall_LargeModel.h
//  YJSmall_LargeSwitchManager
//
//  Created by 张永俊 on 2017/1/22.
//  Copyright © 2017年 张永俊. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol YJSmall_LargeModel <NSObject>

@property (nonatomic, assign) CGFloat w;
@property (nonatomic, assign) CGFloat h;
@property (nonatomic, strong) UIImage *image;

@property (copy, nonatomic) NSString *largeImage;

@end
