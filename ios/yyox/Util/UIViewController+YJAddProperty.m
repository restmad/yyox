//
//  UIViewController+YJAddProperty.m
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "UIViewController+YJAddProperty.h"

@implementation UIViewController (YJAddProperty)

YYSYNTH_DYNAMIC_PROPERTY_OBJECT(storyboardIdentify, setStoryboardIdentify, COPY_NONATOMIC, NSString *)

- (BOOL)autoRefresh
{
	NSNumber *num = objc_getAssociatedObject(self, @selector(setAutoRefresh:));
	if (!num) {
		[self setAutoRefresh:YES];
		num = @YES;
	}
	return num.boolValue;
}

- (void)setAutoRefresh:(BOOL)autoRefresh
{
	[self willChangeValueForKey:@"autoRefresh"];
	objc_setAssociatedObject(self, _cmd, @(autoRefresh), OBJC_ASSOCIATION_RETAIN);
	[self didChangeValueForKey:@"autoRefresh"];
}

- (void)setCancelTasksWhenViewDisappeared:(BOOL)cancelTasksWhenViewDisappeared
{
	[self willChangeValueForKey:@"cancelTasksWhenViewDisappeared"];
	objc_setAssociatedObject(self, _cmd, @(cancelTasksWhenViewDisappeared), OBJC_ASSOCIATION_RETAIN);
	[self didChangeValueForKey:@"cancelTasksWhenViewDisappeared"];
}

- (BOOL)cancelTasksWhenViewDisappeared
{
	NSNumber *num = objc_getAssociatedObject(self, @selector(setCancelTasksWhenViewDisappeared:));
	if (!num) {
		[self setCancelTasksWhenViewDisappeared:YES];
		num = @YES;
	}
	return num.boolValue;
}

+ (void)load
{
	[self swizzleInstanceMethod:@selector(viewDidDisappear:) with:@selector(yj_viewDidDisappear:)];
	
	[self swizzleInstanceMethod:@selector(preferredStatusBarStyle) with:@selector(yj_preferredStatusBarStyle)];
	
//	[self swizzleInstanceMethod:@selector(setTitle:) with:@selector(yj_setTitle:)];
}

- (void)yj_setTitle:(NSString *)title
{
	UIView *titleView = [UIView new];
	UILabel *label = nil;
	if (self.navigationItem.titleView) {
		label = objc_getAssociatedObject(titleView, "titleLabel");
	}
	
	if (!label) {
		label = [UILabel new];
		label.textColor = [UIColor whiteColor];
		label.textAlignment = NSTextAlignmentCenter;
		label.font = [UIFont boldSystemFontOfSize:14];
		
		[titleView addSubview:label];
		
		objc_setAssociatedObject(titleView, "titleLabel", label, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
	}
	
	label.text = title;
	titleView.height = 44;
	
	CGSize textSize = [title boundingRectWithSize:CGSizeMake(999, label.font.lineHeight) options:0 attributes:@{NSFontAttributeName: label.font} context:nil].size;
	
	label.width = textSize.width;
	label.height = 44;
	label.left = 0;
	
	titleView.width = textSize.width;
	
	self.navigationItem.titleView = titleView;
}

- (void)startLoading
{
	if (!self.navigationItem.titleView) return;
	UIActivityIndicatorView *indicatorView = objc_getAssociatedObject(self.navigationItem.titleView, "indicatorView");
	if (!indicatorView) {
		indicatorView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhite];
		indicatorView.hidesWhenStopped = YES;
		[self.navigationItem.titleView addSubview:indicatorView];
		
		indicatorView.size = CGSizeMake(44, 44);
		indicatorView.origin = CGPointMake(0, 0);
		
		objc_setAssociatedObject(self.navigationItem.titleView, "indicatorView", indicatorView, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
	}
	if (indicatorView.animating) return;
	[indicatorView startAnimating];
	
	UILabel *label = objc_getAssociatedObject(self.navigationItem.titleView, "titleLabel");
	label.left = CGRectGetMaxX(indicatorView.frame);
	
	self.navigationItem.titleView.width = 44 + label.width;
}

- (void)stopLoading
{
	if (!self.navigationItem.titleView) return;
	UIActivityIndicatorView *indicatorView = objc_getAssociatedObject(self.navigationItem.titleView, "indicatorView");
	if (!indicatorView) return;
	if (!indicatorView.animating) return;
	[indicatorView stopAnimating];
	
	UILabel *label = objc_getAssociatedObject(self.navigationItem.titleView, "titleLabel");
	label.left = 0;
	
	self.navigationItem.titleView.width = label.width;
}

- (void)yj_viewDidDisappear:(BOOL)animate
{
	[self yj_viewDidDisappear:animate];
	if (self.cancelTasksWhenViewDisappeared) {
		[self cancelTasks];
	}
}

- (UIStatusBarStyle)yj_preferredStatusBarStyle
{
	UIStatusBarStyle style = [self yj_preferredStatusBarStyle];
//	if ([self isKindOfClass:NSClassFromString(@"YJOrderViewController")]) {
//		style = UIStatusBarStyleDefault;
//	} else {
		style = UIStatusBarStyleLightContent;
//	}
	return style;
}

@end
