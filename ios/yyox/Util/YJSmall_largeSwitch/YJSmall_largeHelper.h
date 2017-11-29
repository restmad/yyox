//
//  YJSmall_largeHelper.h
//  yyox
//
//  Created by ddn on 2017/3/15.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJSmall_largeHelper : NSObject

+ (CGPoint)calculateMaskCenterWithView:(UIImageView *)view inView:(UIView *)container;
+ (CGSize)calculateMaskSizeWithView:(UIImageView *)view inView:(UIView *)container;

@end
