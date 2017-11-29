//
//  YJiCarouselViewModel.m
//  yyox
//
//  Created by ddn on 2016/12/27.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJiCarouselViewModel.h"

@interface YJiCarouselViewModel() <iCarouselDelegate, iCarouselDataSource>

@property (strong, nonatomic) NSTimer *timer;

@end

@implementation YJiCarouselViewModel

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.autoScroll = YES;
		self.type = iCarouselTypeLinear;
	}
	return self;
}

- (void)setCarousel:(iCarousel *)carousel
{
	if (!_carousel) {
		_carousel = carousel;
		_carousel.dataSource = self;
		_carousel.delegate = self;
		_carousel.pagingEnabled = YES;
		_carousel.type = self.type;
	}
}

- (void)setDatas:(NSArray<YJiCarouselModel *> *)datas
{
	_datas = datas;
	if (datas.count == 1) {
		self.autoScroll = NO;
		self.carousel.scrollEnabled = NO;
	}
	if (_carousel) {
		[_carousel reloadData];
		
		[self startTimer];
	}
}

- (void)startTimer
{
	if ((!_timer || _timer.isValid) && self.autoScroll) {
		@weakify(self)
		_timer = [NSTimer scheduledTimerWithTimeInterval:4. block:^(NSTimer * _Nonnull timer) {
			@strongify(self)
			if (self.carousel && self.datas.count > 1)
				[self.carousel scrollToItemAtIndex:self.carousel.currentItemIndex + 1 animated:YES];
			else [self.timer invalidate];
		} repeats:YES];
	}
}

- (void)dealloc
{
	_timer.isValid ?: [_timer invalidate];
}

#pragma mark - iCarousel delegate && dataSource
- (NSInteger)numberOfItemsInCarousel:(iCarousel *)carousel
{
	return _datas.count;
}

- (UIView *)carousel:(iCarousel *)carousel viewForItemAtIndex:(NSInteger)index reusingView:(UIView *)view
{
	if (self.delegate && [self.delegate respondsToSelector:@selector(carousel:viewForItemAtIndex:reusingView:)])
		return [self.delegate carousel:carousel viewForItemAtIndex:index reusingView:view];
	
	if (!view) {
		view = [[UIImageView alloc]init];
		view.frame = carousel.bounds;
	}
	UIImageView *imageView = (UIImageView *)view;
	if ([_datas[index] localImage]) {
		imageView.image = [_datas[index] localImage];
	} else {
		[imageView yj_setBase64Image:[_datas[index] image] placeHolder:[UIImage imageNamed:@"order_placeholder"]];
	}
	return view;
}

- (CGFloat)carousel:(iCarousel *)carousel valueForOption:(iCarouselOption)option withDefault:(CGFloat)value
{
	if (option == iCarouselOptionWrap) {
		return YES;
	}
	return value;
}

- (void)carouselCurrentItemIndexDidChange:(iCarousel *)carousel
{
	if (self.delegate && [self.delegate respondsToSelector:@selector(carousel:currentItemIndexDidChange:)]) {
		[self.delegate carousel:carousel currentItemIndexDidChange:carousel.currentItemIndex];
	}
}

- (void)carousel:(iCarousel *)carousel didSelectItemAtIndex:(NSInteger)index
{
	
	if (self.delegate && [self.delegate respondsToSelector:@selector(carousel:didSelectItemAtIndex:)]) {
		[self.delegate carousel:carousel didSelectItemAtIndex:index];
	} else {
		
	}
}


@end
