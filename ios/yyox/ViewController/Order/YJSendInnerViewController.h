//
//  YJSendInnerViewController.h
//  yyox
//
//  Created by ddn on 2017/2/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJToDoModel.h"
#import "YJBottomFloatView.h"
#import "YJDetailOrderModel.h"

typedef NS_OPTIONS(NSUInteger, YJDetailOrderOptions) {
	YJDetailOrderOptionsNone = 1 << 0,
	YJDetailOrderOptionsCommitable = 1 << 1,
	YJDetailOrderOptionsEditable = 1 << 2,
	YJDetailOrderOptionsShrinkable = 1 << 3,
	YJDetailOrderOptionsHasHeader = 1 << 4,
	YJDetailOrderOptionsHasFooter = 1 << 5,
	YJDetailOrderOptionsEditAddressable = 1 << 6,
	YJDetailOrderOptionsShowLastCost = 1 << 7,
	YJDetailOrderOptionsAll = (1 << 1) | (1 << 2) | (1 << 3) | (1 << 4) | (1 << 5) | (1 << 6) | (1 << 7)
};

@interface YJSendInnerViewController : UIViewController

+ (instancetype)instanceWithOptions:(YJDetailOrderOptions)options andModel:(YJToDoModel *)model withSpec:(void(^)(UITableView *tableView, YJBottomFloatView *floatView))specSetting;

@property (strong, nonatomic) YJDetailOrderModel *detailOrderModel;

@end
