//
//  YJPendingCellModel.h
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJMerchandiseModel.h"

@interface YJPendingCellModel : NSObject <NSCoding>

@property (strong, nonatomic, readonly) NSObject *model;

@property (assign, nonatomic) CGFloat iconW;
@property (assign, nonatomic) UIEdgeInsets cellInsets;
@property (assign, nonatomic) CGFloat cellW;

@property (assign, nonatomic) CGFloat titleLeft;
@property (assign, nonatomic) CGFloat titlePaddingTop;
@property (assign, nonatomic) CGFloat subTitleLeft;
@property (assign, nonatomic) CGFloat subTitlePaddingTop;
@property (assign, nonatomic) CGFloat descLeft;
@property (assign, nonatomic) CGFloat descPaddingTop;
@property (assign, nonatomic) CGFloat infoLeft;
@property (assign, nonatomic) CGFloat infoH;


- (void)bindingModel:(NSObject *)model;

@property (assign, nonatomic) CGRect titleFrame;
@property (assign, nonatomic) CGRect descFrame;
@property (assign, nonatomic) CGRect infoFrame;
@property (assign, nonatomic) CGRect subTitleFrame;

@property (assign, nonatomic) CGRect iconFrame;


@property (assign, nonatomic) CGFloat cellH;

@property (copy, nonatomic) NSString *icon;
@property (copy, nonatomic) NSString *info;

@property (strong, nonatomic, readonly) YYTextLayout *titleTextLayout;

@property (strong, nonatomic, readonly) YYTextLayout *descTextLayout;

@property (strong, nonatomic, readonly) YYTextLayout *subTitleTextLayout;

- (void)reset;
- (void)layout;
- (NSString *)generateIconName;
- (NSString *)generateInfo;
- (NSString *)generateTitle;
- (YYTextLayout *)generateTitleLayout:(NSString *)title;
- (NSString *)generateSubTitle;
- (YYTextLayout *)generateSubTitleLayout:(NSString *)subTitle;
- (NSString *)generateDesc;
- (YYTextLayout *)generateDescLayout:(NSString *)desc;

@end
