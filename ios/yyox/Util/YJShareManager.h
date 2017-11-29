//
//  YJShareManager.h
//  yyox
//
//  Created by ddn on 2017/5/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>
@interface YJShareView : UIView

- (void)dismiss;

@end
@interface YJShareModel : NSObject

@property (strong, nonatomic) NSURL *url;
@property (copy, nonatomic) NSArray *images;
@property (copy, nonatomic) NSString *title;
@property (copy, nonatomic) NSString *content;

@end

@interface YJShareManager : NSObject

/**
 是否包含要分享到的应用

 @return bool
 */
+ (BOOL)shareable;

/**
 初始化
 */
+ (void)initialShare;

/**
 展示分享选择视图

 @param shareModel 分享信息
 @param callback 回调
 @return 分享视图
 */
+ (id)showShareView:(YJShareModel *)shareModel callback:(BOOL(^)(BOOL success))callback;

@end
