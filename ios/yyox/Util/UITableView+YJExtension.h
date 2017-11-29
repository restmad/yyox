//
//  UITableView+YJExtension.h
//  yyox
//
//  Created by ddn on 2017/5/11.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef enum : NSUInteger {
	YJTableViewShowTypeRows = 0,
	YJTableViewShowTypeSections,
} YJTableViewShowType;

@interface UITableView (YJExtension)

@property (assign, nonatomic) YJTableViewShowType showType;

@end
