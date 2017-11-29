//
//  YJRouterResponseModel.h
//  yyox
//
//  Created by ddn on 2017/1/9.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJRouterResponseModel : NSObject

@property (assign, nonatomic) NSInteger code;//999 没有更多数据   998 任务取消   500 网络错误

@property (copy, nonatomic) NSString *msg;

@property (strong, nonatomic) id data;

@end
