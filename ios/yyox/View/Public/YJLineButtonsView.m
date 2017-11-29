//
//  YJLineButtonsView.m
//  yyox
//
//  Created by ddn on 2017/2/24.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJLineButtonsView.h"

@interface YJLineButtonsView()

@property (copy, nonatomic) void(^callback)(NSString *);

@property (strong, nonatomic) NSMutableArray *btns;

@end

@implementation YJLineButtonsView

- (instancetype)initWithFrame:(CGRect)frame titles:(NSArray *)titles callback:(void (^)(NSString *))callback
{
	self = [super initWithFrame:frame];
	if (self) {
		self.layoutInsets = UIEdgeInsetsZero;
		self.callback = callback;
		self.btns = [NSMutableArray array];
		for (NSInteger i=0; i<titles.count; i++) {
			UIButton *btn = [UIButton new];
			[btn setTitle:titles[i] forState:UIControlStateNormal];
			[btn addTarget:self action:@selector(clickOn:) forControlEvents:UIControlEventTouchUpInside];
			[btn setBackgroundImage:[UIImage imageWithColor:[UIColor colorWithRGB:0xe7e7e7]] forState:UIControlStateHighlighted];
			[self addSubview:btn];
			[self.btns addObject:btn];
			if (i != titles.count - 1) {
				btn.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
			}
		}
	}
	return self;
}

- (void)clickOn:(UIButton *)sender
{
	if (self.callback) {
		self.callback(sender.titleLabel.text);
	}
}

- (void)setBackgroundImage:(UIImage *)image
{
	UIImageView *imageView = nil;
	if ([self.subviews[0] isKindOfClass:[UIImageView class]]) {
		imageView = self.subviews[0];
	} else {
		imageView = [UIImageView new];
		[self addSubview:imageView];
		[self sendSubviewToBack:imageView];
		[imageView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.edges.mas_equalTo(0);
		}];
	}
	imageView.image = image;
}

- (void)layoutSubviews
{
	[super layoutSubviews];
	for (NSInteger i=0; i<self.btns.count; i++) {
		UIButton *btn = self.btns[i];
		CGFloat h = (self.frame.size.height - self.layoutInsets.top - self.layoutInsets.bottom) / self.btns.count;
		btn.frame = CGRectMake(self.layoutInsets.left, self.layoutInsets.top + h * i, self.frame.size.width - self.layoutInsets.left - self.layoutInsets.right, h);
	}
}

- (void)setupFont:(UIFont *)font color:(UIColor *)color
{
	for (UIButton *btn in self.btns) {
		btn.titleLabel.font = font;
		[btn setTitleColor:color forState:UIControlStateNormal];
	}
}

@end
