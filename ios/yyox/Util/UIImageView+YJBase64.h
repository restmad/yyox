//
//  UIImageView+YJBase64.h
//  yyox
//
//  Created by ddn on 2017/7/11.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImageView (YJBase64)

- (void)yj_setBase64Image:(NSString *)urlStr placeHolder:(UIImage *)placeHolder;

- (void)yj_setBase64Image:(NSString *)urlStr thumbnail:(BOOL)thumbnail placeHolder:(UIImage *)placeHolder;

- (void)yj_setBase64Image:(NSString *)urlStr placeHolder:(UIImage *)placeHolder completion:(void(^)(UIImage *))completion;

- (void)yj_setBase64Image:(NSString *)urlStr thumbnail:(BOOL)thumbnail placeHolder:(UIImage *)placeHolder completion:(void(^)(UIImage *))completion;

@property (copy, nonatomic, readonly) NSString *urlStr;

@end
