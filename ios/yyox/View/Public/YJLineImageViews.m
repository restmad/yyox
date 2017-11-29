//
//  YJLineImageViews.m
//  yyox
//
//  Created by ddn on 2017/1/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJLineImageViews.h"

@interface YJLineImageViews()

@property (strong, nonatomic) NSMutableArray *imageViews;

@end

@implementation YJLineImageViews

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		self.limit = 3;
	}
	return self;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
	self = [super initWithCoder:aDecoder];
	if (self) {
		self.limit = 3;
	}
	return self;
}

- (NSMutableArray *)imageViews
{
	if (!_imageViews) {
		_imageViews = [NSMutableArray array];
	}
	return _imageViews;
}

- (NSArray *)images
{
	return [self.imageViews valueForKeyPath:@"image"];
}

- (UIImageView *)addImage:(UIImage *)image
{
	if (self.imageViews.count == self.limit) {
		return nil;
	}
	UIImageView *imageView = [UIImageView new];
	imageView.contentMode = UIViewContentModeScaleAspectFit;
	imageView.image = image;
	imageView.userInteractionEnabled = YES;
	@weakify(self)
	UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithActionBlock:^(UITapGestureRecognizer * _Nonnull sender) {
		@strongify(self)
		if (self.delegate && [self.delegate respondsToSelector:@selector(lineImageViews:clickOnIdx:)]) {
			[self.delegate lineImageViews:self clickOnIdx:[self.imageViews indexOfObject:sender.view]];
		}
	}];
	[imageView addGestureRecognizer:tap];
	[self.imageViews addObject:imageView];
	[self addSubview:imageView];
	if ([_delegate respondsToSelector:@selector(lineImageViews:didAddImageViewAtIdx:)]) {
		[_delegate lineImageViews:self didAddImageViewAtIdx:self.imageViews.count-1];
	}
	return imageView;
}

- (void)reset
{
	[self.imageViews enumerateObjectsUsingBlock:^(UIView * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
		[obj removeFromSuperview];
	}];
	NSInteger count = self.imageViews.count;
	[self.imageViews removeAllObjects];
	for (NSInteger i=0; i<count; i++) {
		if ([_delegate respondsToSelector:@selector(lineImageViews:didRemoveImageViewAtIdx:)]) {
			[_delegate lineImageViews:self didRemoveImageViewAtIdx:i];
		}
	}
}

- (void)deleteImageAtIdx:(NSInteger)idx
{
	if (self.imageViews.count <= idx) {
		return;
	}
	[(UIView *)self.imageViews[idx] removeFromSuperview];
	[self.imageViews removeObjectAtIndex:idx];
	if ([_delegate respondsToSelector:@selector(lineImageViews:didRemoveImageViewAtIdx:)]) {
		[_delegate lineImageViews:self didRemoveImageViewAtIdx:idx];
	}
}

- (UIImageView *)imageViewAtIdx:(NSInteger)idx
{
	if (self.imageViews.count <= idx) {
		return nil;
	}
	return self.imageViews[idx];
}

- (void)layoutSubviews
{
	[super layoutSubviews];
	for (NSInteger i=0; i<self.imageViews.count; i++) {
		UIImageView *imageView = self.imageViews[i];
		if (i == 0) {
			imageView.frame = CGRectMake(0, 0, self.height, self.height);
		} else {
			imageView.frame = CGRectMake(i * self.height + 8 * i, 0, self.height, self.height);
		}
	}
}

@end
