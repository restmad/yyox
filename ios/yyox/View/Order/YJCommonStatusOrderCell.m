//
//  YJCommonStatusOrderCell.m
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonStatusOrderCell.h"

@interface YJCommonStatusOrderCell()

@property (strong, nonatomic) UILabel *titleLabel;
@property (strong, nonatomic) UILabel *subTitleLabel;
@property (strong, nonatomic) UILabel *timeLabel;
@property (strong, nonatomic) UILabel *statusLabel;
@property (strong, nonatomic) UIImageView *indicatorImageView;

@end

@implementation YJCommonStatusOrderCell

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		[self setup];
		self.backgroundColor = [UIColor whiteColor];
		self.edgeLines = UIEdgeInsetsMake(0.3, 0, 0.3, 0);
	}
	return self;
}

- (void)setup
{
	_titleLabel = [UILabel new];
	_subTitleLabel = [UILabel new];
	_timeLabel = [UILabel new];
	_statusLabel = [UILabel new];
	
	_titleLabel.font = [UIFont systemFontOfSize:14];
	_subTitleLabel.font = _timeLabel.font = _statusLabel.font = [UIFont systemFontOfSize:12];
	
	_titleLabel.textColor = [UIColor colorWithRGB:0x323131];
	_subTitleLabel.textColor = _timeLabel.textColor = _statusLabel.textColor = [UIColor colorWithRGB:0x999999];
	
	_titleLabel.numberOfLines = _subTitleLabel.numberOfLines = _timeLabel.numberOfLines = _statusLabel.numberOfLines = 1;
	
	_titleLabel.lineBreakMode = _subTitleLabel.lineBreakMode = _timeLabel.lineBreakMode = _statusLabel.lineBreakMode = NSLineBreakByTruncatingTail;
	
	_indicatorImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"cell_disclosure"]];
	
	[self.contentView addSubview:_titleLabel];
	[self.contentView addSubview:_subTitleLabel];
	[self.contentView addSubview:_timeLabel];
	[self.contentView addSubview:_statusLabel];
	[self.contentView addSubview:_indicatorImageView];
	
	[self setNeedsUpdateConstraints];
	[self updateConstraintsIfNeeded];
}

- (void)updateConstraints
{
	[_titleLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(15);
		make.right.mas_equalTo(-20);
	}];
	
	[_subTitleLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(_titleLabel.mas_bottom).offset(13);
		make.left.mas_equalTo(20);
		make.right.mas_equalTo(-20);
	}];
	
	[_timeLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(_subTitleLabel.mas_bottom).offset(13);
	}];
	
	[_statusLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(_timeLabel.mas_right).offset(13);
		make.top.mas_equalTo(_subTitleLabel.mas_bottom).offset(13);
	}];
	
	[_indicatorImageView mas_updateConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(-20);
		make.centerY.mas_equalTo(0);
	}];
	
	[super updateConstraints];
}

- (void)setModel:(YJCommonStatusOrderModel *)model
{
	_model = model;
	NSString *name = @" ";
	if (model.type == 3) {
		name = @"合箱发货";
	}
	else if (model.nickname && model.nickname.length > 0) {
		name = model.nickname;
	}
	self.titleLabel.text = name;
	self.subTitleLabel.text = [NSString stringWithFormat:@"邮客单号：%@", model.orderNo];
	self.timeLabel.text = [NSString timeDistanceTo:model.orStatusDate];
	self.statusLabel.text = model.orderStatus;
}

@end
