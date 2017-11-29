//
//  UIView+YJTopBottomLine.m
//  yyox
//
//  Created by ddn on 2017/2/4.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "UIView+YJTopBottomLine.h"

@implementation UIView (YJTopBottomLine)

- (void)setEdgeLineViews:(NSMutableArray *)edgeLineViews
{
	objc_setAssociatedObject(self, _cmd, edgeLineViews, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (NSMutableArray *)edgeLineViews
{
	NSMutableArray *views = objc_getAssociatedObject(self, @selector(setEdgeLineViews:));
	if (!views) {
		views = [NSMutableArray array];
		[self setEdgeLineViews:views];
	}
	return views;
}

- (void)setEdgeLines:(UIEdgeInsets)edgeLines
{
	[self willChangeValueForKey:@"edgeLines"];
	NSValue *value = [NSValue value:&edgeLines withObjCType:@encode(UIEdgeInsets)];
	objc_setAssociatedObject(self, _cmd, value, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
	[self didChangeValueForKey:@"edgeLines"];
	
	[self.edgeLineViews enumerateObjectsUsingBlock:^(UIView * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
		[obj removeFromSuperview];
	}];
	[self.edgeLineViews removeAllObjects];
	
	if (edgeLines.left > 0.2) {
		UIView *view = [UIView new];
		view.backgroundColor = [UIColor colorWithRGB:0xcccccc];
		[self addSubview:view];
		[view mas_makeConstraints:^(MASConstraintMaker *make) {
			make.top.left.bottom.mas_equalTo(0);
			make.width.mas_equalTo(edgeLines.left);
		}];
		[self.edgeLineViews addObject:view];
	}
	if (edgeLines.right > 0.2) {
		UIView *view = [UIView new];
		view.backgroundColor = [UIColor colorWithRGB:0xcccccc];
		[self addSubview:view];
		[view mas_makeConstraints:^(MASConstraintMaker *make) {
			make.top.right.bottom.mas_equalTo(0);
			make.width.mas_equalTo(edgeLines.right);
		}];
		[self.edgeLineViews addObject:view];
	}
	if (edgeLines.top > 0.2) {
		UIView *view = [UIView new];
		view.backgroundColor = [UIColor colorWithRGB:0xcccccc];
		[self addSubview:view];
		[view mas_makeConstraints:^(MASConstraintMaker *make) {
			make.top.right.left.mas_equalTo(0);
			make.height.mas_equalTo(edgeLines.top);
		}];
		[self.edgeLineViews addObject:view];
	}
	if (edgeLines.bottom > 0.2) {
		UIView *view = [UIView new];
		view.backgroundColor = [UIColor colorWithRGB:0xcccccc];
		[self addSubview:view];
		[view mas_makeConstraints:^(MASConstraintMaker *make) {
			make.bottom.right.left.mas_equalTo(0);
			make.height.mas_equalTo(edgeLines.bottom);
		}];
		[self.edgeLineViews addObject:view];
	}
}

- (UIEdgeInsets)edgeLines
{
	UIEdgeInsets edgeLines = UIEdgeInsetsZero;
	NSValue *value = objc_getAssociatedObject(self, @selector(setEdgeLines:));
	if (!value) {
		[self setEdgeLines:UIEdgeInsetsZero];
	}
	[value getValue:&edgeLines];
	return edgeLines;
}

@end
