//
//  YJTabBar.m
//  yyox
//
//  Created by ddn on 2016/12/28.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJTabBar.h"
#import "YJBottomTextBtn.h"

@interface YJTabBar()

@property (strong, nonatomic) NSMutableArray *items;

@property (copy, nonatomic) void(^clickOn)(NSString *text, NSInteger idx);

@property (strong, nonatomic) UIView *lineView;

@end

@implementation YJTabBar

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		_items = [NSMutableArray array];
		YJBottomTextBtn *pre = [YJBottomTextBtn buttonWithText:@"我要预报"
													 image:[UIImage imageNamed:@"prediction_btn"]
											 selectedImage:nil];
		[pre addTarget:self action:@selector(clickOn:) forControlEvents:UIControlEventTouchUpInside];
		[pre setTitleColor:[UIColor colorWithRGB:0x333333] forState:UIControlStateNormal];
		
		YJBottomTextBtn *wait = [YJBottomTextBtn buttonWithText:@"待处理"
													  image:[UIImage imageNamed:@"wait_done_btn"]
											  selectedImage:nil];
		[wait addTarget:self action:@selector(clickOn:) forControlEvents:UIControlEventTouchUpInside];
		[wait setTitleColor:[UIColor colorWithRGB:0x333333] forState:UIControlStateNormal];
		
		YJBottomTextBtn *done = [YJBottomTextBtn buttonWithText:@"已完成"
													  image:[UIImage imageNamed:@"finish_btn"]
											  selectedImage:nil];
		[done addTarget:self action:@selector(clickOn:) forControlEvents:UIControlEventTouchUpInside];
		[done setTitleColor:[UIColor colorWithRGB:0x333333] forState:UIControlStateNormal];
		
		[self.items addObject:pre];
		[self.items addObject:wait];
		[self.items addObject:done];
		
		[self addSubview:pre];
		[self addSubview:wait];
		[self addSubview:done];
		
		self.backgroundColor = [UIColor colorWithRGB:0xffffff];
		
		UIView *lineView = [UIView new];
		_lineView = lineView;
		lineView.backgroundColor = [UIColor colorWithRGB:0xcccccc];
		[self addSubview:lineView];
	}
	return self;
}

- (void)clickOn:(UIButton *)btn
{
	if (self.clickOn) {
		self.clickOn(btn.titleLabel.text, [self.items indexOfObject:btn]);
	}
}

- (void)updateConstraints
{
	[super updateConstraints];
	
	[self lineLayout:self.items withSetting:^(NSInteger idx, UIView *view) {
		
	} withInset:UIEdgeInsetsZero andSpace:0];
}

- (void)layoutSubviews
{
	[super layoutSubviews];
	
	self.lineView.frame = CGRectMake(0, self.height-0.5, self.width, 0.5);
}

- (void)setClickOn:(void (^)(NSString *, NSInteger))clickOn
{
	_clickOn = clickOn;
}

- (void)updateTexts:(NSInteger (^)(NSString *, NSInteger))update
{
	[self.items enumerateObjectsUsingBlock:^(YJBottomTextBtn *obj, NSUInteger idx, BOOL * stop) {
		NSInteger text = update(obj.titleLabel.text, idx);
		text >= 0 ? [obj setBadgeValue:text] : NULL;
	}];
}

- (NSInteger)bageValueForIdx:(NSInteger)idx
{
	YJBottomTextBtn *done = [self.items objectOrNilAtIndex:idx];
	if (done) {
		return done.badgeValue;
	}
	return 0;
}

@end
