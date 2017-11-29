//
//  YJCommonWithFooterTableViewCell.m
//  yyox
//
//  Created by ddn on 2017/2/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonWithFooterTableViewCell.h"

@interface YJCommonWithFooterTableViewCell()

@end

@implementation YJCommonWithFooterTableViewCell

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
	_subNameLabel = [UILabel new];
	_middleLineView = [UIView new];
	_leftLabel = [UILabel new];
	_imageViews = [YJLineImageViews new];
	_moreInfoButton = [UIButton new];
	_editButton = [UIButton new];
	_ssubNameLabel = [UILabel new];
	
	[self.contentView addSubview:_iconImageView];
	[self.contentView addSubview:_nameLabel];
	[self.contentView addSubview:_subNameLabel];
	[self.contentView addSubview:_middleLineView];
	[self.contentView addSubview:_leftLabel];
	[self.contentView addSubview:_imageViews];
	[self.contentView addSubview:_moreInfoButton];
	[self.contentView addSubview:_editButton];
	[self.contentView addSubview:_ssubNameLabel];
	
	[self setupConstraints];
	
	_iconImageView.contentMode = UIViewContentModeCenter;
	_iconImageView.image = [UIImage imageNamed:@"package"];
	
	_nameLabel.font = [UIFont systemFontOfSize:13];
	_nameLabel.textColor = [UIColor colorWithRGB:0x323131];
	
	_subNameLabel.font = [UIFont systemFontOfSize:11];
	_subNameLabel.textColor = [UIColor colorWithRGB:0x999999];
	
	_ssubNameLabel.font = [UIFont systemFontOfSize:11];
	_ssubNameLabel.textColor = [UIColor colorWithRGB:0x999999];
	
	_middleLineView.backgroundColor = [UIColor colorWithRGB:0xcccccc];
	
	_leftLabel.font = [UIFont systemFontOfSize:12];
	_leftLabel.textColor = [UIColor colorWithRGB:0x999999];
	
	_imageViews.limit = 3;
	
	[_moreInfoButton setImage:[[UIImage imageNamed:@"cell_disclosure"] yy_imageByRotateRight90] forState:UIControlStateNormal];
	[_moreInfoButton addTarget:self action:@selector(clickOnShowMore:) forControlEvents:UIControlEventTouchUpInside];
	
	[_editButton setImage:[UIImage imageNamed:@"edit_btn"] forState:UIControlStateNormal];
	[_editButton addTarget:self action:@selector(clickOnEdit:) forControlEvents:UIControlEventTouchUpInside];
	
	self.hasShowOpt = NO;
}

- (void)layoutSubviews
{
	[super layoutSubviews];
	_iconImageView.frame = CGRectMake(20, 15, (147.-60.)/2, (147.-60.)/2);
	_middleLineView.frame = CGRectMake(0, CGRectGetMaxY(_iconImageView.frame) + 15, self.width, 0.3);
	[_leftLabel sizeToFit];
	_leftLabel.left = 20;
	_leftLabel.centerY = CGRectGetMaxY(_middleLineView.frame) + (135.-60.)/4 + 15;
	_imageViews.frame = CGRectMake(CGRectGetMaxX(_leftLabel.frame) + 20, CGRectGetMaxY(_middleLineView.frame) + 15, self.width - (CGRectGetMaxX(_leftLabel.frame) + 20) - 20, (135.-60.)/2);
}

- (void)setupConstraints
{
	[_nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(15);
		make.left.mas_equalTo(_iconImageView.mas_right).offset(20);
		make.right.mas_lessThanOrEqualTo(-60);
	}];
	[_subNameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(_iconImageView.mas_right).offset(20);
		make.right.mas_lessThanOrEqualTo(-20);
		make.centerY.mas_equalTo(_iconImageView.mas_centerY).offset(1.5);
	}];
	[_ssubNameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(_iconImageView.mas_right).offset(20);
		make.bottom.mas_equalTo(_iconImageView.mas_bottom);
		make.right.mas_lessThanOrEqualTo(-20);
	}];
	[_moreInfoButton setContentEdgeInsets:UIEdgeInsetsMake(0, 20, 10, 20)];
	[_moreInfoButton mas_makeConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(0);
		make.bottom.mas_equalTo(0);
	}];
	[_editButton setContentEdgeInsets:UIEdgeInsetsMake(15, 20, 0, 20)];
	[_editButton mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.right.mas_equalTo(0);
	}];
}

- (void)setHasShowOpt:(BOOL)hasShowOpt
{
	_hasShowOpt = hasShowOpt;
	self.moreInfoButton.hidden = !hasShowOpt;
}

- (void)clickOnShowMore:(UIButton *)sender
{
	self.showing = !self.showing;
	if (self.clickOnShowMore) {
		self.clickOnShowMore(self.showing);
	}
}

- (void)clickOnEdit:(UIButton *)sender
{
	if (self.clickOnEdit) {
		self.clickOnEdit();
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

- (void)hideFooter
{
	self.leftLabel.hidden = YES;
	self.imageViews.hidden = YES;
	self.middleLineView.hidden = YES;
}

- (void)showFooter
{
	self.leftLabel.hidden = NO;
	self.imageViews.hidden = NO;
	self.middleLineView.hidden = NO;
}

@end












