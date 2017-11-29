//
//  YJTransferCell.m
//  yyox
//
//  Created by ddn on 2017/2/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJTransferCell.h"

@interface YJTransferCell()

@property (strong, nonatomic) UILabel *nameLabel;
@property (strong, nonatomic) UILabel *subnameLabel;
//@property (strong, nonatomic) UILabel *descLabel;

@property (strong, nonatomic) UIButton *selectedBtn;

@end

@implementation YJTransferCell

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
	self.contentView.backgroundColor = [UIColor whiteColor];
	
	_nameLabel = [UILabel new];
	_subnameLabel = [UILabel new];
//	_descLabel = [UILabel new];
	_selectedBtn = [UIButton new];
	
	[self.contentView addSubview:_nameLabel];
	[self.contentView addSubview:_subnameLabel];
//	[self.contentView addSubview:_descLabel];
	[self.contentView addSubview:_selectedBtn];
	
	_nameLabel.font = [UIFont systemFontOfSize:14];
	_nameLabel.textColor = [UIColor colorWithRGB:0x333333];
	
	_subnameLabel.font = [UIFont systemFontOfSize:14];
	_subnameLabel.textColor = [UIColor colorWithRGB:0x666666];
	_subnameLabel.numberOfLines = 0;
	
//	_descLabel.font = [UIFont systemFontOfSize:14];
//	_descLabel.textColor = [UIColor colorWithRGB:0x999999];
//	_descLabel.numberOfLines = 0;
	
	[_selectedBtn setImage:[UIImage imageNamed:@"focus_button_normal"] forState:UIControlStateNormal];
	[_selectedBtn setImage:[UIImage imageNamed:@"focus_button_selected"] forState:UIControlStateSelected];
	[_selectedBtn sizeToFit];
	[_selectedBtn addTarget:self action:@selector(clickOnSelect:) forControlEvents:UIControlEventTouchUpInside];
	
	[_nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(15);
		make.right.mas_equalTo(-60);
	}];
	
	[_subnameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(_nameLabel.mas_bottom).offset(15);
		make.right.mas_equalTo(-20);
		make.bottom.mas_equalTo(-15);
	}];
	
//	[_descLabel mas_makeConstraints:^(MASConstraintMaker *make) {
//		make.left.mas_equalTo(20);
//		make.top.mas_equalTo(_subnameLabel.mas_bottom).offset(15);
//		make.right.mas_equalTo(-20);
//		make.bottom.mas_equalTo(-15);
//	}];
	
	[_selectedBtn mas_makeConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(-20);
		make.top.mas_equalTo(15);
	}];
}

- (void)setModel:(YJTransferModel *)model
{
	_model = model;
	self.nameLabel.text = model.code;
	NSMutableAttributedString *s = [[NSMutableAttributedString alloc] initWithString:model.priceWeight ?: @"" attributes:nil];
	s.yy_font = [UIFont systemFontOfSize:14];
	s.yy_color = [UIColor colorWithRGB:0x666666];
	if ([model.priceWeight containsString:@"\n"]) {
		s.yy_lineSpacing = s.yy_font.lineHeight * 0.5;
	}
	self.subnameLabel.attributedText = s;
//	self.descLabel.text = model.explain;
}

- (void)setIsWant:(BOOL)isWant
{
	_isWant = isWant;
	self.selectedBtn.selected = isWant;
}

- (void)clickOnSelect:(UIButton *)sender
{
	if (self.selectedBtn.selected) return;
	if (self.clickOnSelected) {
		self.clickOnSelected();
	}
}

- (void)setHighlighted:(BOOL)highlighted animated:(BOOL)animated
{}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{}

@end












