//
//  YJCommonAddressTableViewCell.m
//  yyox
//
//  Created by ddn on 2017/2/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonAddressTableViewCell.h"

@interface YJCommonAddressTableViewCell()

@property (strong, nonatomic) UILabel *nameLabel;
@property (strong, nonatomic) UILabel *telLabel;
@property (strong, nonatomic) UILabel *descLabel;
@property (strong, nonatomic) UILabel *defaultLabel;

@end

@implementation YJCommonAddressTableViewCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
	self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
	if (self) {
		[self setup];
	}
	return self;
}

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
	_nameLabel = [UILabel new];
	_telLabel = [UILabel new];
	_descLabel = [UILabel new];
	_defaultLabel = [UILabel new];
	
	_nameLabel.font = [UIFont systemFontOfSize:14];
	_nameLabel.textColor = [UIColor colorWithRGB:0x333333];
	_telLabel.font = [UIFont systemFontOfSize:14];
	_telLabel.textColor = [UIColor colorWithRGB:0x666666];
	_descLabel.font = [UIFont systemFontOfSize:14];
	_descLabel.textColor = [UIColor colorWithRGB:0x999999];
	_defaultLabel.font = [UIFont systemFontOfSize:14];
	_defaultLabel.textColor = [UIColor whiteColor];
	_defaultLabel.text = @"默认";
	_defaultLabel.textAlignment = NSTextAlignmentCenter;
	_defaultLabel.backgroundColor = [UIColor colorWithRGB:0x69a3d9];
	
	_descLabel.numberOfLines = 0;
	_defaultLabel.layer.cornerRadius = 10.5;
	_defaultLabel.layer.masksToBounds = YES;
	
	[self.contentView addSubview:_nameLabel];
	[self.contentView addSubview:_telLabel];
	[self.contentView addSubview:_descLabel];
	[self.contentView addSubview:_defaultLabel];
	
	[_nameLabel setContentHuggingPriority:UILayoutPriorityRequired forAxis:UILayoutConstraintAxisHorizontal];
	
	[_nameLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(15);
	}];
	[_telLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(15);
		make.left.mas_equalTo(_nameLabel.mas_right).offset(20);
		make.right.mas_equalTo(-10);
	}];
	[_defaultLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.centerX.mas_equalTo(_nameLabel.mas_centerX);
		make.top.mas_equalTo(_nameLabel.mas_bottom).offset(15);
		make.width.mas_equalTo(42);
		make.height.mas_equalTo(21);
	}];
	[_descLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(_telLabel.mas_bottom).offset(15);
		make.right.mas_equalTo(-10);
		make.left.mas_equalTo(_nameLabel.mas_right).offset(20);
		make.bottom.mas_equalTo(-15);
	}];
}

- (void)setModel:(YJAddressModel *)model
{
	_model = model;
	self.nameLabel.text = model.name;
	self.telLabel.text = model.mobile;
	
	NSMutableAttributedString *desc = [[NSMutableAttributedString alloc] initWithString:model.fullDetailaddress ?: [NSString stringWithFormat:@"%@%@%@%@", self.model.province ?: @"", self.model.city ?: @"", self.model.district ?: @"", self.model.detailaddress ?: @""]];
	desc.yy_font = self.descLabel.font;
	desc.yy_color = self.descLabel.textColor;
	desc.yy_lineSpacing = self.descLabel.font.lineHeight * 0.3;
	self.descLabel.attributedText = desc;
	self.defaultLabel.hidden = !model.isdefault;
}


@end
