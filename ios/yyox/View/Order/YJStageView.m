//
//  YJStageView.m
//  yyox
//
//  Created by ddn on 2016/12/28.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJStageView.h"
//#import "YJTextInImage.h"
#import "YJBottomTextBtn.h"

@interface YJStageView()

@property (strong, nonatomic) NSMutableArray<YJBottomTextBtn *> *stages;

@property (copy, nonatomic) NSArray *indicators;

@property (copy, nonatomic) void(^clickOn)(NSString *text, NSInteger idx);

@end

@implementation YJStageView

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		
		_stages = [NSMutableArray array];
		
		NSArray *texts = @[@"待入库", @"待出库", @"已出库", @"清关中", @"国内配送"];
		NSArray *images = @[@"wait_enter_btn", @"wait_out_btn", @"outed_btn", @"going_btn", @"inner_going_btn"];
		for (int i=0; i<5; i++) {
			YJBottomTextBtn *btn = [YJBottomTextBtn buttonWithText:texts[i] image:[UIImage imageNamed:images[i]] selectedImage:nil];
			btn.bottomToTop = 4./7.;
			btn.spaceScale = 15./78.;
			[btn setTitleColor:[UIColor colorWithRGB:0x333333] forState:UIControlStateNormal];
			btn.fontSize = 12;
			[btn addTarget:self action:@selector(clickOn:) forControlEvents:UIControlEventTouchUpInside];
			[self addSubview:btn];
			[_stages addObject:btn];
		}
		
		NSMutableArray *arr = [NSMutableArray arrayWithCapacity:4];
		for (int i=0; i<4; i++) {
			UIImageView *imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"cell_disclosure"]];
			imageView.contentMode = UIViewContentModeCenter;
			[arr addObject:imageView];
			[self addSubview:imageView];
		}
		
		self.indicators = arr;
		
		[self lineLayout:self.stages withSetting:^(NSInteger idx, UIView *view) {
			
		} withInset:UIEdgeInsetsMake(0, 10, 0, 10) andSpace:10];
		
		for (int i=0; i<self.indicators.count; i++) {
			UIImageView *imageView = self.indicators[i];
			[imageView mas_makeConstraints:^(MASConstraintMaker *make) {
				make.left.equalTo(self.stages[i].mas_right);
				make.right.equalTo(self.stages[i+1].mas_left);
				make.top.mas_equalTo(20);
			}];
		}
		
	}
	return self;
}

- (void)updateConstraints
{
	[super updateConstraints];
}

- (void)clickOn:(UIButton *)btn
{
	if (self.clickOn) {
		self.clickOn(btn.titleLabel.text, [self.stages indexOfObject: (YJBottomTextBtn *)btn]);
	}
}

- (void)updateTexts:(NSInteger (^)(NSString *, NSInteger))update
{
	[self.stages enumerateObjectsUsingBlock:^(YJBottomTextBtn *obj, NSUInteger idx, BOOL * stop) {
		NSInteger text = update(obj.titleLabel.text, idx);
		[obj setBadgeValue:text];
	}];
}

- (void)setClickOn:(void (^)(NSString *, NSInteger))clickOn
{
	_clickOn = clickOn;
}

@end
