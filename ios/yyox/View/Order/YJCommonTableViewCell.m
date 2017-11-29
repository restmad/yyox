//
//  YJCommonTableViewCell.m
//  yyox
//
//  Created by ddn on 2017/2/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonTableViewCell.h"

@interface YJCommonTableViewCell()

@end

@implementation YJCommonTableViewCell

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
	_iconImageView = [UIImageView new];
	_nameLabel = [UILabel new];
	_subnameLabel = [UILabel new];
	_editButton = [UIButton new];
	_moreInfoButton = [UIButton new];
	_ssubnameLabel = [UILabel new];
	
	_iconImageView.contentMode = UIViewContentModeCenter;
	
	_nameLabel.font = [UIFont systemFontOfSize:15];
	_subnameLabel.font = [UIFont systemFontOfSize:13];
	_ssubnameLabel.font = [UIFont systemFontOfSize:13];
	
	_nameLabel.textColor = [UIColor colorWithRGB:0x333333];
	_subnameLabel.textColor = [UIColor colorWithRGB:0x999999];
	_ssubnameLabel.textColor = [UIColor colorWithRGB:0x999999];
	
	[_editButton setTitle:@"编辑" forState:UIControlStateNormal];
	[_editButton setTitleColor:[UIColor colorWithRGB:0x999999] forState:UIControlStateNormal];
	[_editButton addTarget:self action:@selector(clickOnEdit:) forControlEvents:UIControlEventTouchUpInside];
	[_editButton setImage:[UIImage imageNamed:@"address_edit"] forState:UIControlStateNormal];
	[_editButton setTitleEdgeInsets:UIEdgeInsetsMake(0, 5, 0, -5)];
	_editButton.titleLabel.font = [UIFont systemFontOfSize:12];
	[_editButton sizeToFit];
	[_moreInfoButton setImage:[[UIImage imageNamed:@"cell_disclosure"] yy_imageByRotateRight90] forState:UIControlStateNormal];
	[_moreInfoButton addTarget:self action:@selector(clickOnShowMore:) forControlEvents:UIControlEventTouchUpInside];
	
	[self.contentView addSubview:_iconImageView];
	[self.contentView addSubview:_nameLabel];
	[self.contentView addSubview:_subnameLabel];
	[self.contentView addSubview:_editButton];
	[self.contentView addSubview:_moreInfoButton];
	[self.contentView addSubview:_ssubnameLabel];
	
	[_editButton mas_makeConstraints:^(MASConstraintMaker *make) {
		make.bottom.mas_equalTo(_iconImageView.mas_bottom);
		make.right.mas_equalTo(-20);
	}];
	[_iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.height.mas_equalTo(20);
		make.width.mas_equalTo(_iconImageView.mas_height);
		make.top.mas_equalTo(15);
	}];
	[_nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.bottom.mas_equalTo(_iconImageView.mas_bottom);
		make.left.mas_equalTo(_iconImageView.mas_right).offset(15);
		make.right.mas_lessThanOrEqualTo(_editButton.mas_left).offset(-10);
	}];
	[_subnameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(_iconImageView.mas_bottom).offset(15);
		make.left.mas_equalTo(_iconImageView.mas_right).offset(15);
		make.right.mas_lessThanOrEqualTo(_editButton.mas_left).offset(-10);
	}];
	[_moreInfoButton mas_makeConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(_editButton.mas_right);
		make.top.mas_equalTo(_editButton.mas_bottom).offset(15);
	}];
	[_ssubnameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(_subnameLabel.mas_left);
		make.top.mas_equalTo(_subnameLabel.mas_bottom).offset(10);
		make.bottom.mas_equalTo(-10);
	}];
}

- (void)clickOnEdit:(UIButton *)sender
{
	if (self.clickOnEdit) {
		self.clickOnEdit(self.doing);
	}
}

- (void)clickOnShowMore:(UIButton *)sender
{
	self.showing = !self.showing;
	if (self.clickOnShowMore) {
		self.clickOnShowMore(self.showing);
	}
}

- (void)setShowing:(BOOL)showing
{
	if (showing) {
		[self.moreInfoButton setImage:[[UIImage imageNamed:@"cell_disclosure"] yy_imageByRotateLeft90] forState:UIControlStateNormal];
	} else {
		[self.moreInfoButton setImage:[[UIImage imageNamed:@"cell_disclosure"] yy_imageByRotateRight90] forState:UIControlStateNormal];
	}
}

@end
