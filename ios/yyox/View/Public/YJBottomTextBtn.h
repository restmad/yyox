//
//  YJBottomTextBtn.h
//  FishToMan
//
//  Created by zhangyj on 15/12/2.
//  Copyright © 2015年 xitong. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YJBottomTextBtn : UIButton

@property (copy, nonatomic) NSString *badgeImage;
@property (assign, nonatomic) NSInteger badgeValue;

@property (assign, nonatomic) CGFloat fontSize;
@property (assign, nonatomic) UIEdgeInsets badgeInset;
@property (assign, nonatomic) CGFloat badgeFontSize;
@property (assign, nonatomic) CGFloat spaceScale;
@property (assign, nonatomic) CGFloat bottomToTop;

+ (instancetype)buttonWithText:(NSString *)text image:(UIImage *)image selectedImage:(UIImage *)selectedImage;

@end
