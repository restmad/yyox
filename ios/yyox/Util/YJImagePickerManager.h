//
//  YJImagePickerManager.h
//  yyox
//
//  Created by ddn on 2017/1/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJRouterResponseModel.h"

@interface YJImagePickerManager : NSObject

YJSingleton_h(ImagePickerManager)

+ (void)showImagePicker:(void(^)(YJRouterResponseModel *))callback fromController:(UIViewController *)vc;

+ (void)scaleImage:(UIImage *)image toSize:(CGFloat)size callback:(void(^)(YJRouterResponseModel *))callback;

@end
