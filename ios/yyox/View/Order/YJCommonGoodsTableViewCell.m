//
//  YJCommonGoodsTableViewCell.m
//  yyox
//
//  Created by ddn on 2017/2/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCommonGoodsTableViewCell.h"

@interface YJCommonGoodsTableViewCell()


@end

@implementation YJCommonGoodsTableViewCell

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
	_leftSubnameLabel = [UILabel new];
	_rightSubnameLabel = [UILabel new];
	_deleteBtn = [UIButton new];
	
	_nameLabel.font = [UIFont systemFontOfSize:13];
	_leftSubnameLabel.font = [UIFont systemFontOfSize:12];
	_rightSubnameLabel.font = [UIFont systemFontOfSize:12];
	
	_nameLabel.textColor = [UIColor colorWithRGB:0x666666];
	_leftSubnameLabel.textColor = [UIColor colorWithRGB:0x999999];
	_rightSubnameLabel.textColor = [UIColor colorWithRGB:0x999999];
	
	[_deleteBtn setImage:[UIImage imageNamed:@"delete"] forState:UIControlStateNormal];
	[_deleteBtn addTarget:self action:@selector(clickOnDeleteBtn) forControlEvents:UIControlEventTouchUpInside];
	
	[self.contentView addSubview:_nameLabel];
	[self.contentView addSubview:_leftSubnameLabel];
	[self.contentView addSubview:_rightSubnameLabel];
	[self.contentView addSubview:_deleteBtn];
	
	[_nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(15);
		make.right.mas_equalTo(-60);
	}];
	[_leftSubnameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(_nameLabel.mas_bottom).offset(10);
		make.bottom.mas_equalTo(-15);
	}];
	[_rightSubnameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(_leftSubnameLabel.mas_right).offset(20);
		make.right.mas_lessThanOrEqualTo(-60);
		make.top.mas_equalTo(_nameLabel.mas_bottom).offset(10);
	}];
	[_deleteBtn mas_makeConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(0);
		make.centerY.mas_equalTo(0);
	}];
	[_deleteBtn setContentEdgeInsets:UIEdgeInsetsMake(20, 20, 20, 20)];
}

- (void)clickOnDeleteBtn
{
	if (self.clickOnDelete) {
		self.clickOnDelete();
	}
}

@end




















