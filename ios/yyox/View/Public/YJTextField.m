//
//  YJTextField.m
//  yyox
//
//  Created by ddn on 2017/2/15.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJTextField.h"

@interface YJTextField()

@property (strong, nonatomic) UIImageView *errorImageView;

@end

@implementation YJTextField

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		[self setup];
	}
	return self;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
	self = [super initWithCoder:aDecoder];
	if (self) {
		[self setup];
	}
	return self;
}

- (void)setup
{
	self.errorImageView = [UIImageView new];
	self.errorImageView.hidden = YES;
	self.errorImageView.contentMode = UIViewContentModeCenter;
	self.errorImage = [UIImage imageNamed:@"textFieldErrorIndicator"];
}

- (void)setErrorImage:(UIImage *)errorImage
{
	_errorImage = errorImage;
	self.errorImageView.image = errorImage;
	[self.errorImageView sizeToFit];
}

- (void)didMoveToSuperview
{
	[super didMoveToSuperview];
	if (self.superview) {
		[self.superview addSubview:self.errorImageView];
		[self.errorImageView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.left.mas_equalTo(self.mas_right).offset(10);
			make.top.bottom.mas_equalTo(self);
		}];
	} else {
		[self.errorImageView removeFromSuperview];
	}
}

- (void)showErrorImage
{
	self.errorImageView.hidden = NO;
}

- (void)hideErrorImage
{
	self.errorImageView.hidden = YES;
}

- (BOOL)errorIsVisiable
{
	return !self.errorImageView.hidden;
}

- (void)dealloc
{
	[self.errorImageView removeFromSuperview];
}

@end
