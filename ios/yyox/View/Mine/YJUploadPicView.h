//
//  YJUploadPicView.h
//  yyox
//
//  Created by ddn on 2017/1/12.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum : NSUInteger {
	YJUploadPicViewFrontButton = 0,
	YJUploadPicViewBackButton,
	YJUploadPicViewBeDefaultButton,
} YJUploadPicViewButton;

@interface YJUploadPicView : UIView

@property (copy, nonatomic) void(^clickOn)(YJUploadPicViewButton button);

@property (assign, nonatomic) BOOL isDefault;
@property (assign, nonatomic) BOOL hasDefaultBtn;

@property (strong, nonatomic, readonly) UIImage *frontImage;
@property (strong, nonatomic, readonly) UIImage *backImage;

@property (assign, nonatomic) BOOL hasInfo;

- (void)beginLoadImage:(id(^)(UIImageView *frontImageView, UIImageView *backImageView))beginLoadImage;

- (void)endLoadImage:(id(^)(UIImageView *frontImageView, UIImageView *backImageView))endLoadImage;

@end
