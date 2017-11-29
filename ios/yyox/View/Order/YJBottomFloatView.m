//
//  YJBottomFloatView.m
//  yyox
//
//  Created by ddn on 2017/2/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJBottomFloatView.h"

@interface YJBottomFloatView()



@end

@implementation YJBottomFloatView

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
	_rightButton = [UIButton new];
	_titleLabel = [UILabel new];
	_valueLabel = [UILabel new];
	_unitsLabel = [UILabel new];
	
	_subTitleLabel = [UILabel new];
	_subValueLabel = [UILabel new];
	
	[_rightButton setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_normal"] forState:UIControlStateNormal];
	[_rightButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
	[_rightButton addTarget:self action:@selector(clickOnButton:) forControlEvents:UIControlEventTouchUpInside];
	_rightButton.titleLabel.font = [UIFont systemFontOfSize:14];
	
	_titleLabel.font = [UIFont systemFontOfSize:13];
	_unitsLabel.font = [UIFont systemFontOfSize:12];
	_valueLabel.font = [UIFont systemFontOfSize:14];
	
	_subTitleLabel.font = [UIFont systemFontOfSize:11];
	_subValueLabel.font = [UIFont systemFontOfSize:9];
	
	_titleLabel.textColor = [UIColor colorWithRGB:0x323131];
	_unitsLabel.textColor = _valueLabel.textColor = [UIColor colorWithRGB:0xfc9b31];
	
	_subTitleLabel.textColor = [UIColor colorWithRGB:0x666666];
	_subValueLabel.textColor = [UIColor colorWithRGB:0x666666];
	
	[self addSubview:_rightButton];
	[self addSubview:_titleLabel];
	[self addSubview:_valueLabel];
	[self addSubview:_unitsLabel];
	
	[self addSubview:_subTitleLabel];
	[self addSubview:_subValueLabel];
	
	self.showSub = YES;
}

- (void)setupConstraints
{
	[_rightButton mas_updateConstraints:^(MASConstraintMaker *make) {
		make.right.top.bottom.mas_equalTo(0);
		make.width.mas_equalTo(120);
	}];
	[_valueLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(_rightButton.mas_left).offset(-10);
		make.centerY.mas_equalTo(_showSub ? -8 : 0);
	}];
	[_unitsLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(_valueLabel.mas_left);
		make.bottom.mas_equalTo(_valueLabel.mas_bottom);
	}];
	[_titleLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(_unitsLabel.mas_left);
		make.bottom.mas_equalTo(_unitsLabel.mas_bottom);
	}];
	
	[_subValueLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(_rightButton.mas_left).offset(-10);
		make.centerY.mas_equalTo(10);
	}];
	[_subTitleLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(_subValueLabel.mas_left);
		make.bottom.mas_equalTo(_subValueLabel.mas_bottom);
	}];
}

- (void)setShowSub:(BOOL)showSub
{
	_showSub = showSub;
	_subTitleLabel.hidden = _subValueLabel.hidden = !showSub;
	[self setupConstraints];
}

- (void)clickOnButton:(UIButton *)sender
{
	if (self.clickOnButton) {
		self.clickOnButton();
	}
}

@end








