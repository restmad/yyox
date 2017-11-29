//
//  YJWarehouseCell.m
//  yyox
//
//  Created by ddn on 2017/5/17.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJWarehouseCell.h"

@interface YJWarehouseCell()

@property (strong, nonatomic) UIImageView *iconImageView;
@property (strong, nonatomic) UILabel *largeTitleLabel;
@property (strong, nonatomic) UILabel *contentLabel;
@property (strong, nonatomic) UIButton *toCopyButton;

@end

@implementation YJWarehouseCell

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
	_iconImageView = [UIImageView new];
	_largeTitleLabel = [UILabel new];
	_contentLabel = [UILabel new];
	_toCopyButton = [UIButton buttonWithType:UIButtonTypeSystem];
	
	[self.contentView addSubview:_iconImageView];
	[self.contentView addSubview:_largeTitleLabel];
	[self.contentView addSubview:_contentLabel];
	[self.contentView addSubview:_toCopyButton];
	
	_largeTitleLabel.font = [UIFont systemFontOfSize:14];
	_toCopyButton.titleLabel.font = [UIFont systemFontOfSize:13];
	
	_largeTitleLabel.textColor = [UIColor colorWithRGB:0x333333];
	[_toCopyButton setTitle:@"复制" forState:UIControlStateNormal];
	[_toCopyButton addTarget:self action:@selector(clickOnCopyBtn:) forControlEvents:UIControlEventTouchUpInside];
	
	_largeTitleLabel.text = @"仓库";
	
	_contentLabel.numberOfLines = 0;
	
	[_iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(15);
		make.size.mas_equalTo(CGSizeMake(22.5, 22.5));
	}];
	[_largeTitleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(_iconImageView.mas_right).offset(10);
		make.right.mas_equalTo(-80);
		make.centerY.mas_equalTo(_iconImageView.mas_centerY);
	}];
	[_contentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(_iconImageView.mas_bottom).offset(8);
		make.left.mas_equalTo(20);
		make.right.mas_equalTo(-80);
		make.bottom.mas_equalTo(-15);
	}];
	[_toCopyButton mas_makeConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(-20);
		make.centerY.mas_equalTo(_largeTitleLabel.mas_centerY);
	}];
	
	self.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
}

- (void)setModel:(YJWarehouseModel *)model
{
	_model = model;
	_largeTitleLabel.text = model.name;
	NSAttributedString *attrStr = [self generateAttributeContent:model.value];
	_contentLabel.attributedText = attrStr;
	if (model.type == YJWarehouseCountryAmerica) {
		self.iconImageView.image = [UIImage imageNamed:@"America"];
	} else if (model.type == YJWarehouseCountryAustralia) {
		self.iconImageView.image = [UIImage imageNamed:@"Australia"];
	} else if (model.type == YJWarehouseCountryGermany) {
		self.iconImageView.image = [UIImage imageNamed:@"Germany"];
	} else if (model.type == YJWarehouseCountryJapan) {
		self.iconImageView.image = [UIImage imageNamed:@"Japan"];
	} else {
		self.iconImageView.image = nil;
	}
}

- (NSAttributedString *)generateAttributeContent:(NSString *)string
{
	if (!string) return nil;
	NSMutableAttributedString *mAttrStr = [[NSMutableAttributedString alloc] initWithString:string];
	mAttrStr.yy_font = [UIFont systemFontOfSize:12];
	mAttrStr.yy_color = [UIColor colorWithRGB:0x666666];
	mAttrStr.yy_lineSpacing = 5;
	return mAttrStr;
}

- (void)clickOnCopyBtn:(UIButton *)sender
{
	UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
	pasteboard.string = [NSString stringWithFormat:@"%@\n%@\n", self.largeTitleLabel.text, self.contentLabel.attributedText.string];
	if (self.copyCallback) {
		self.copyCallback();
	}
}

@end
















