//
//  YJSmall_largeCell.m
//  YJSmall_largeSwitchDemo
//
//  Created by ddn on 2017/3/15.
//  Copyright © 2017年 张永俊. All rights reserved.
//

#import "YJSmall_largeCell.h"
#import <objc/message.h>

@interface YJSmall_largeCell() <UIScrollViewDelegate>

@property (strong, nonatomic) UIImageView *imageView;
@property (weak, nonatomic) UIScrollView *containView;

@end

@implementation YJSmall_largeCell

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		[self initial];
	}
	return self;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
	self = [super initWithCoder:aDecoder];
	if (self) {
		[self initial];
	}
	return self;
}

- (void)initial
{
	UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapOn:)];
	[self addGestureRecognizer:tap];
	UILongPressGestureRecognizer *longPress = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(longPressOn:)];
	[self addGestureRecognizer:longPress];
}

- (void)tapOn:(UITapGestureRecognizer *)tap
{
	[self touchIn];
}

- (void)longPressOn:(UILongPressGestureRecognizer *)longPress
{
	if (self.longPressAction) {
		self.longPressAction();
	}
}

- (UIView *)viewForZoomingInScrollView:(UIScrollView *)scrollView
{
	return self.imageView;
}

- (void)touchIn
{
	UICollectionView *view = [self myCollectionView];
	SEL sel = @selector(collectionView:didSelectItemAtIndexPath:);
	if (view && view.delegate && [view.delegate respondsToSelector:sel]) {
		NSIndexPath *indexPath = [view indexPathForCell:self];
		if (indexPath) {
			((void (*)(void *, SEL,UIView *view, NSIndexPath *))objc_msgSend)((__bridge void *)(view.delegate), sel, view, indexPath);
		}
	}
}

- (UIImageView *)imageView {
	if (!_imageView) {
		
		UIScrollView *containView = [UIScrollView new];
		[self.contentView addSubview:containView];
		_containView = containView;
		containView.delegate = self;
		containView.maximumZoomScale = 2;
		
		UIImageView *imageView = [[UIImageView alloc]init];
		_imageView = imageView;
		[containView addSubview:imageView];
		
		imageView.contentMode = UIViewContentModeScaleAspectFit;
		
	}
	return _imageView;
}

- (UICollectionView *)myCollectionView
{
	UIResponder *responder = self;
	UICollectionView *view;
	while (responder) {
		if ([responder isKindOfClass:[UICollectionView class]]) {
			view = (UICollectionView *)responder;
			break;
		}
		else {
			responder = responder.nextResponder;
		}
	}
	return view;
}

- (void)layoutSubviews
{
	[super layoutSubviews];
	_containView.frame = CGRectMake(10, 10, self.bounds.size.width-20, self.bounds.size.height-20);
	_imageView.frame = _containView.bounds;
	_containView.contentSize = _imageView.bounds.size;
}

- (void)prepareForReuse
{
	[super prepareForReuse];
	[_containView setZoomScale:1];
}

- (void)setModel:(NSObject<YJSmall_LargeModel> *)model
{
	_model = model;
	if (!model.largeImage) {
		self.hasLargeImage = YES;
		self.imageView.image = model.image;
	} else {
		self.hasLargeImage = NO;
		__weak typeof(self) ws = self;
		[self.imageView yj_setBase64Image:model.largeImage placeHolder:model.image completion:^(UIImage *image) {
			ws.hasLargeImage = image != nil ? YES : NO;
		}];
	}
}

@end
