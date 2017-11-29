//
//  YJExpenseCell.m
//  yyox
//
//  Created by ddn on 2017/1/13.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJExpenseCell.h"

@interface YJExpenseCell()

@property (strong, nonatomic) UILabel *titleLabel;
@property (strong, nonatomic) UILabel *timeLabel;
@property (strong, nonatomic) UILabel *numberLabel;

@end

@implementation YJExpenseCell

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		[self setup];
	}
	return self;
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
	self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
	if (self) {
		[self setup];
	}
	return self;
}

- (void)setup
{
	_titleLabel = [UILabel new];
	_titleLabel.font = [UIFont systemFontOfSize:13];
	_titleLabel.textColor = [UIColor colorWithRGB:0x666666];
	
	_timeLabel = [UILabel new];
	_timeLabel.font = [UIFont systemFontOfSize:13];
	_timeLabel.textColor = [UIColor colorWithRGB:0x666666];
	
	_numberLabel = [UILabel new];
	_numberLabel.font = [UIFont systemFontOfSize:11];
	
	[self.contentView addSubview:_titleLabel];
	[self.contentView addSubview:_timeLabel];
	[self.contentView addSubview:_numberLabel];
	
	self.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
	
	[_titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(20);
	}];
	[_timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(_titleLabel.mas_bottom).offset(10);
		make.left.mas_equalTo(20);
		make.bottom.mas_equalTo(-20);
	}];
	[_numberLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(20);
		make.right.mas_equalTo(-20);
	}];
}

- (void)setModel:(YJExpenseModel *)model
{
	_model = model;
	_titleLabel.text = model.STATEMENT_TYPE_NAME;
	_timeLabel.text = model.FREEZE_MONEY_DATE;
	_numberLabel.text = model.AMOUNT;
	if ([model.AMOUNT containsString:@"+"]) {
		_numberLabel.textColor = [UIColor colorWithRGB:0xfc9b31];
	} else {
		_numberLabel.textColor = [UIColor colorWithRGB:0xb1cb2a];
	}
}

@end



















