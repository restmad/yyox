//
//  YJCouponCell.m
//  yyox
//
//  Created by ddn on 2017/3/27.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCouponCell.h"

@interface YJCouponCell()

@property (strong, nonatomic) UILabel *nameLabel;
@property (strong, nonatomic) UILabel *descLabel;
@property (strong, nonatomic) UILabel *timeLabel;
@property (strong, nonatomic) UILabel *currencyLabel;
@property (strong, nonatomic) UILabel *priceLabel;
@property (strong, nonatomic) UIImageView *rightImageView;

@end

@implementation YJCouponCell

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
	self.backgroundColor = [UIColor whiteColor];
	_nameLabel = [UILabel new];
	_nameLabel.font = [UIFont systemFontOfSize:18];
	_nameLabel.textColor = [UIColor colorWithRGB:0x999999];
	_nameLabel.numberOfLines = 1;
	
	_descLabel = [UILabel new];
	_descLabel.font = [UIFont systemFontOfSize:10];
	_descLabel.textColor = [UIColor colorWithRGB:0x999999];
	_descLabel.numberOfLines = 1;
	
	_timeLabel = [UILabel new];
	_timeLabel.font = [UIFont systemFontOfSize:11];
	_timeLabel.textColor = [UIColor colorWithRGB:0xfc9b31];
	_timeLabel.numberOfLines = 1;
	
	_currencyLabel = [UILabel new];
	_currencyLabel.font = [UIFont systemFontOfSize:25];
	_currencyLabel.textColor = [UIColor whiteColor];
	
	_priceLabel = [UILabel new];
	_priceLabel.font = [UIFont systemFontOfSize:44];
	_priceLabel.textColor = [UIColor whiteColor];
	_priceLabel.numberOfLines = 1;
	
	_rightImageView = [UIImageView new];
	_rightImageView.image = [UIImage imageNamed:@"coupon_orange"];
	
	UIView *priceView = [UIView new];
	
	[self.contentView addSubview:_nameLabel];
	[self.contentView addSubview:_descLabel];
	[self.contentView addSubview:_timeLabel];
	[self.contentView addSubview:_rightImageView];
	[_rightImageView addSubview:priceView];
	[priceView addSubview:_currencyLabel];
	[priceView addSubview:_priceLabel];
	
	[_nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(15);
		make.left.mas_equalTo(20);
		make.right.mas_equalTo(priceView.mas_left).offset(-20);
	}];
	[_descLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.centerY.mas_equalTo(5);
		make.right.mas_equalTo(priceView.mas_left).offset(-20);
	}];
	[_timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.bottom.mas_equalTo(-10);
		make.left.mas_equalTo(20);
	}];
	[_rightImageView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.right.top.bottom.mas_equalTo(0);
		make.width.mas_equalTo(238/2);
	}];
	[priceView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.center.mas_equalTo(0);
	}];
	[_currencyLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(0);
		make.bottom.mas_equalTo(-5);
	}];
	[_priceLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.right.bottom.top.mas_equalTo(0);
		make.left.mas_equalTo(_currencyLabel.mas_right).offset(5);
	}];
	
	self.edgeLines = UIEdgeInsetsMake(0.3, 0, 0.3, 0);
}

- (void)setModel:(YJCouponModel *)model
{
	_model = model;
	self.nameLabel.text = model.coupon.name;
	self.descLabel.text = [NSString stringWithFormat:@"使用限制：%@", model.limitUse ?: @"无"];
	self.timeLabel.text = [NSString stringWithFormat:@"有效期：%@", model.validTo];
	self.currencyLabel.text = @"¥";
	self.priceLabel.text = model.coupon.discountAmount.stringValue;
	
	if ([model.status isEqualToString:@"OUT_OF_COMMISSION"] || [model.status isEqualToString:@"EXPIRED"]) {
		self.rightImageView.image = [UIImage imageNamed:@"coupon_gray"];
		self.timeLabel.textColor = [UIColor colorWithRGB:0x999999];
	} else {
		self.rightImageView.image = [UIImage imageNamed:@"coupon_orange"];
		self.timeLabel.textColor = [UIColor colorWithRGB:0xfc9b31];
	}
}

@end
