//
//  YJAddressCellModel.h
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJAddressModel.h"

@interface YJAddressCellModel : NSObject <NSCoding>

@property (assign, nonatomic) UIEdgeInsets cellInsets;
@property (assign, nonatomic) CGFloat cellW;

@property (assign, nonatomic) CGFloat descPaddingTop;

@property (assign, nonatomic) CGFloat phoneLeft;

@property (assign, nonatomic) CGFloat infoH;

@property (strong, nonatomic, readonly) YJAddressModel *model;
- (void)bindingModel:(YJAddressModel *)model;

@property (assign, nonatomic, readonly) CGRect nameFrame;
@property (assign, nonatomic, readonly) CGRect descFrame;
@property (assign, nonatomic, readonly) CGRect phoneFrame;


@property (assign, nonatomic, readonly) CGFloat cellH;

@property (assign, nonatomic, getter=isBeDefault) BOOL beDefault;

@property (strong, nonatomic, readonly) YYTextLayout *nameTextLayout;

@property (strong, nonatomic, readonly) YYTextLayout *descTextLayout;

@property (strong, nonatomic, readonly) YYTextLayout *phoneTextLayout;

@end
