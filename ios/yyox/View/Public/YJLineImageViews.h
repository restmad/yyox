//
//  YJLineImageViews.h
//  yyox
//
//  Created by ddn on 2017/1/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
@class YJLineImageViews;

@protocol YJLineImageViewsDelegate <NSObject>

@optional
- (void)lineImageViews:(YJLineImageViews *)lineImageViews clickOnIdx:(NSInteger)idx;

- (void)lineImageViews:(YJLineImageViews *)lineImageView didAddImageViewAtIdx:(NSInteger)idx;

- (void)lineImageViews:(YJLineImageViews *)lineImageView didRemoveImageViewAtIdx:(NSInteger)idx;

@end

@interface YJLineImageViews : UIView

@property (assign, nonatomic) NSInteger limit;
@property (copy, nonatomic, readonly) NSArray *images;

@property (weak, nonatomic) id<YJLineImageViewsDelegate> delegate;

- (UIImageView *)addImage:(UIImage *)image;
- (UIImageView *)imageViewAtIdx:(NSInteger)idx;
- (void)deleteImageAtIdx:(NSInteger)idx;
- (void)reset;

@end
