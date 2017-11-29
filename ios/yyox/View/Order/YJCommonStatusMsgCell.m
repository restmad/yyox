//
//  YJCommonStatusMsgCell.m
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonStatusMsgCell.h"

@interface YJCommonStatusMsgCell()

@property (strong, nonatomic) UILabel *msgLabel;

@property (strong, nonatomic) UILabel *timeLabel;

@end

@implementation YJCommonStatusMsgCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
	self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
	if (self) {
		self.backgroundColor = [UIColor whiteColor];
		
		[self setup];
	}
	return self;
}

- (void)setup
{
	_msgLabel = [UILabel new];
	_timeLabel = [UILabel new];
	
	[self.contentView addSubview:_msgLabel];
	[self.contentView addSubview:_timeLabel];
	
	_msgLabel.textColor = [UIColor colorWithRGB:0x999999];
	_timeLabel.textColor = [UIColor colorWithRGB:0x999999];
	
	_msgLabel.font = [UIFont systemFontOfSize:12];
	_timeLabel.font = [UIFont systemFontOfSize:12];
	
	_msgLabel.numberOfLines = 0;
	_timeLabel.numberOfLines = 1;
	
	[self setNeedsUpdateConstraints];
	[self updateConstraintsIfNeeded];
}

- (void)updateConstraints
{
	[_msgLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(15);
		make.left.mas_equalTo(20);
		make.right.mas_equalTo(-20);
	}];
	
	[_timeLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(_msgLabel.mas_bottom).offset(10);
		make.bottom.mas_equalTo(-15);
	}];
	
	[super updateConstraints];
}

- (void)setModel:(YJCommonStatusDetailMsgModel *)model
{
	_model = model;
	
	self.msgLabel.text = (!model.history || model.history.length == 0) ? @" " : model.history;
	self.timeLabel.text = (!model.actionDateWithFormat || model.actionDateWithFormat.length == 0) ? @" " : model.actionDateWithFormat;
}

@end
