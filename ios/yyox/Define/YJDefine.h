//
//  YJDefine.h
//  Transfer
//
//  Created by ddn on 16/12/15.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#ifndef YJDefine_h
#define YJDefine_h

#import "YJSingleton.h"
#import "YJNotificationConst.h"
#import "YJApiDefine.h"


#ifdef DEBUG
#define YJLog(...) NSLog(__VA_ARGS__)
#else
#define YJLog(...)
#endif

#define Color(r, g, b) [UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:1]
#define RandomColor Color(arc4random_uniform(256), arc4random_uniform(256),arc4random_uniform(256))

#define kGlobalBackgroundColor [UIColor colorWithRGB: 0xe5eae7]
#define kTopicColor [UIColor colorWithRGB: 0x2683ce]

#define kiOSLater(version) (kSystemVersion >= version)

#define kOnePx (1./YYScreenScale())

#define NSDC [NSNotificationCenter defaultCenter]
#define NSUD [NSUserDefaults standardUserDefaults]
#define NSFM [NSFileManager defaultManager]
#define UIAP [UIApplication sharedApplication]

#define kRemoveCellSeparator \
- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{\
cell.separatorInset = UIEdgeInsetsZero;\
cell.layoutMargins = UIEdgeInsetsZero; \
cell.preservesSuperviewLayoutMargins = NO; \
}

#endif /* YJDefine_h */
