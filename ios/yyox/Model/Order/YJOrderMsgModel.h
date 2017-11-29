//
//  YJOrderMsgModel.h
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJOrderMsgModel : NSObject <NSCoding>

@property (copy, nonatomic) NSString *icon;
@property (copy, nonatomic) NSString *title;
@property (copy, nonatomic) NSString *createTime;
@property (copy, nonatomic) NSString *desc;
@property (copy, nonatomic) NSString *info;

@end
