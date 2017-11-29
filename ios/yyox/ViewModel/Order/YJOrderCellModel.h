//
//  YJOrderCellModel.h
//  yyox
//
//  Created by ddn on 2017/1/14.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJOrderMainModel.h"

@interface YJOrderCellModel : NSObject <NSCoding>

@property (assign, nonatomic) CGFloat iconW;
@property (assign, nonatomic) UIEdgeInsets cellInsets;
@property (assign, nonatomic) CGFloat cellW;

@property (assign, nonatomic) CGFloat titleLeft;
@property (assign, nonatomic) CGFloat descLeft;
@property (assign, nonatomic) CGFloat descPaddingTop;
@property (assign, nonatomic) CGFloat infoLeft;
@property (assign, nonatomic) CGFloat infoPaddingTop;
@property (assign, nonatomic) CGFloat timeLeft;
@property (assign, nonatomic) CGFloat timePaddingTop;


- (void)bindingModel:(YJOrderMainMsgModel *)model;
@property (strong, nonatomic, readonly) YJOrderMainMsgModel *model;

@property (assign, nonatomic, readonly) CGRect titleFrame;
@property (assign, nonatomic, readonly) CGRect descFrame;
@property (assign, nonatomic, readonly) CGRect infoFrame;
@property (assign, nonatomic, readonly) CGRect timeFrame;

@property (assign, nonatomic, readonly) CGRect iconFrame;


@property (assign, nonatomic, readonly) CGFloat cellH;

@property (copy, nonatomic) NSString *icon;

@property (strong, nonatomic, readonly) YYTextLayout *titleTextLayout;

@property (strong, nonatomic, readonly) YYTextLayout *descTextLayout;

@property (strong, nonatomic, readonly) YYTextLayout *infoTextLayout;

@property (strong, nonatomic, readonly) YYTextLayout *timeTextLayout;

@end
