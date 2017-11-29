//
//  UIBarButtonItem+YJExtension.m
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "UIBarButtonItem+YJExtension.h"

@implementation UIBarButtonItem (YJExtension)

+ (UIBarButtonItem *)customView:(NSString *)imageName title:(NSString *)title withTarget:(id)target action:(SEL)action
{
	UIButton *btn = [UIButton new];
	[btn setImage:[[UIImage imageNamed:imageName] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] forState:UIControlStateNormal];
	[btn setTitle:title forState:UIControlStateNormal];
	[btn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
	[btn setTitleColor:[UIColor grayColor] forState:UIControlStateHighlighted];
	btn.titleLabel.font = [UIFont systemFontOfSize:14];
	[btn addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
	[btn setTitleEdgeInsets:UIEdgeInsetsMake(0, 6, 0, -6)];
	[btn setContentEdgeInsets:UIEdgeInsetsMake(0, -6, 0, 6)];
	[btn sizeToFit];
	UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithCustomView:btn];
	return item;
}

- (void)changeTarget:(id)target action:(SEL)action
{
	if ([self.customView isKindOfClass:[UIButton class]]) {
		UIButton *btn = (UIButton *)self.customView;
		[btn removeAllTargets];
		[btn addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
	}
}

@end
