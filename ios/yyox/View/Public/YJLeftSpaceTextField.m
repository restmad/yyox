//
//  YJLeftSpaceTextField.m
//  yyox
//
//  Created by ddn on 2017/1/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJLeftSpaceTextField.h"

@interface YJLeftSpaceTextField()

@end

@implementation YJLeftSpaceTextField

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
	self = [super initWithCoder:aDecoder];
	if (self) {
		[self setup];
	}
	return self;
}

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		[self setup];
	}
	return self;
}

- (void)setup
{
	UIView *leftView = [UIView new];
	self.leftViewMode = UITextFieldViewModeAlways;
	self.leftView = leftView;
}

- (void)setLeftViewW:(CGFloat)leftViewW
{
	_leftViewW = leftViewW;
	[self setNeedsLayout];
	[self layoutIfNeeded];
}

- (void)layoutSubviews
{
	[super layoutSubviews];
	
	if (self.leftView) {
		CGFloat w = self.leftViewW ?: self.height;
		self.leftView.size = CGSizeMake(w, self.height);
	}
}

@end
