//
//  YJCommonStatusOrderViewController.h
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonRefreshViewController.h"



@interface YJCommonStatusOrderViewController : YJCommonRefreshViewController

@property (assign, nonatomic) YJOrderStatus orderStatus;

@property (assign, nonatomic) NSInteger checkCount; //首页数量，针对已完成，要在设置orderStauts之前设置

@end
