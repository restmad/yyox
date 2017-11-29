//
//  UITableView+YJExtension.m
//  yyox
//
//  Created by ddn on 2017/5/11.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "UITableView+YJExtension.h"

@implementation UITableView (YJExtension)

- (void)setShowType:(YJTableViewShowType)showType
{
	objc_setAssociatedObject(self, _cmd, @(showType), OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (YJTableViewShowType)showType
{
	NSNumber *showType = objc_getAssociatedObject(self, @selector(setShowType:));
	if (!showType) {
		showType = @0;
		[self setShowType:0];
	}
	return showType.integerValue;
}

@end
