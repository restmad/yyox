//
//  YJBottomTextBtn.m
//  FishToMan
//
//  Created by zhangyj on 15/12/2.
//  Copyright © 2015年 xitong. All rights reserved.
//

#import "YJBottomTextBtn.h"

@interface YJBottomTextBtn ()

@property (assign, nonatomic) CGFloat currentImageHeight;
//@property (assign, nonatomic) CGFloat currentFontSize;

@property (assign, nonatomic) CGFloat oriImageWidth;

@property (strong, nonatomic) UIImageView *badgeImageView;
@property (strong, nonatomic) UILabel *badgeLabel;

@end

@implementation YJBottomTextBtn

- (instancetype)initWithFrame:(CGRect)frame
{
		self = [super initWithFrame:frame];
		if (self) {
				self.fontSize = 13;
				self.badgeInset = UIEdgeInsetsMake(1.5, 4, 1.5, 4);
				self.badgeFontSize = 9;
				self.spaceScale = 30./194.;
				self.bottomToTop = 5./8.;
		}
		return self;
}

+ (instancetype)buttonWithText:(NSString *)text image:(UIImage *)image selectedImage:(UIImage *)selectedImage {
    YJBottomTextBtn *btn = [[self alloc]init];
    [btn setImage:image forState:UIControlStateNormal];
    if (selectedImage) {
        [btn setImage:selectedImage forState:UIControlStateSelected];
    }
    btn.currentImageHeight = btn.currentImage.size.height;
    btn.imageView.contentMode = UIViewContentModeCenter;
    //    btn.currentFontSize = btn.currentImageHeight * 0.5;
    //    btn.titleLabel.font = [UIFont systemFontOfSize:btn.currentFontSize];
    btn.titleLabel.font = [UIFont systemFontOfSize:btn.fontSize];
    [btn setTitle:text forState:UIControlStateNormal];
    [btn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [btn setTitleColor:[UIColor grayColor] forState:UIControlStateHighlighted];
    
    
    return btn;
}

//- (void)setHighlighted:(BOOL)highlighted {}

- (CGRect)imageRectForContentRect:(CGRect)contentRect {
    CGFloat totalH = (self.currentImageHeight + self.fontSize);
    CGFloat space = self.height * self.spaceScale;
    CGFloat y = (self.height - totalH - space) * self.bottomToTop;
    return CGRectMake(0, y, contentRect.size.width, self.currentImageHeight);
}

- (CGRect)titleRectForContentRect:(CGRect)contentRect {
    CGFloat totalH = (self.currentImageHeight + self.fontSize);
    CGFloat space = self.height * self.spaceScale;
    CGFloat y = (self.height - totalH - space) * self.bottomToTop + space + self.currentImageHeight;
    return CGRectMake(0, y, self.width, self.fontSize);
}

- (void)setTitle:(NSString *)title forState:(UIControlState)state {
    [super setTitle:title forState:state];
	self.titleLabel.textAlignment = NSTextAlignmentCenter;
	self.titleLabel.font = [UIFont systemFontOfSize:self.fontSize];
    [self sizeToFit];
}

- (void)setImage:(UIImage *)image forState:(UIControlState)state {
    [super setImage:image forState:state];
    self.currentImageHeight = image.size.height;
    self.imageView.contentMode = UIViewContentModeCenter;
    [self.imageView sizeToFit];
    [self sizeToFit];
}

- (void)setFontSize:(CGFloat)fontSize
{
	_fontSize = fontSize;
	if (self.titleLabel) {
		self.titleLabel.font = [UIFont systemFontOfSize:fontSize];
	}
}

- (void)layoutSubviews {
    [super layoutSubviews];
	
	if (_badgeImageView) {
		
		CGSize size = [self.badgeLabel.text boundingRectWithSize:CGSizeMake(999, _badgeLabel.font.lineHeight) options:NSStringDrawingUsesFontLeading attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:self.badgeFontSize]} context:nil].size;
		self.badgeLabel.size = size;
		UIEdgeInsets inset = self.badgeInset;
		self.badgeImageView.size = CGSizeMake(size.width + inset.left + inset.right, size.height + inset.top + inset.bottom);
		self.badgeLabel.left = self.badgeInset.left;
		self.badgeLabel.top = self.badgeInset.top;
		
		CGFloat w = self.imageView.image.size.width;
		
		CGFloat width = self.width;
		CGFloat height = self.imageView.top;
		
		_badgeImageView.left = (width - w) / 2 + w - 8;
		
		_badgeImageView.top = height - _badgeImageView.height + 5;
		
		_badgeImageView.layer.masksToBounds = YES;
		_badgeImageView.layer.cornerRadius = _badgeImageView.height / 2;
	}
}

- (UIImageView *)badgeImageView
{
	if (!_badgeImageView) {
		_badgeImageView = [UIImageView new];
		_badgeImageView.image = [UIImage imageNamed:self.badgeImage];
		[self addSubview:_badgeImageView];
		_badgeImageView.backgroundColor = [UIColor redColor];
	}
	return _badgeImageView;
}

- (UILabel *)badgeLabel
{
	if (!_badgeLabel) {
		_badgeLabel = [UILabel new];
		[self.badgeImageView addSubview:_badgeLabel];
		_badgeLabel.textColor = [UIColor whiteColor];
		_badgeLabel.font = [UIFont systemFontOfSize:self.badgeFontSize];
	}
	return _badgeLabel;
}

- (void)setBadgeValue:(NSInteger)badgeValue
{
	_badgeValue = badgeValue;
	if (badgeValue <= 0) {
		_badgeImageView.hidden = YES;
		return;
	}
	_badgeImageView.hidden = NO;
	NSString *value;
	if (badgeValue > 99) {
		value = @"99+";
	} else {
		value = [NSString stringWithFormat:@"%zd", badgeValue];
	}
	
	self.badgeLabel.text = value;
	
	[self setNeedsLayout];
	[self layoutIfNeeded];
}


@end
