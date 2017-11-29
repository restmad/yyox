//
//  YJCombineCell.m
//  yyox
//
//  Created by ddn on 2017/2/28.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCombineCell.h"

@interface YJCombineCell()

@property (strong, nonatomic) UIImageView *iconImageView;
@property (strong, nonatomic) UILabel *nameLabel;
//@property (strong, nonatomic) UILabel *subNameLabel;
@property (strong, nonatomic) UILabel *descLabel;
@property (strong, nonatomic) UILabel *leftSubLabel;
@property (strong, nonatomic) UILabel *rightSubLabel;
@property (strong, nonatomic) UIButton *selectBtn;

@end

@implementation YJCombineCell

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
	_nameLabel = [UILabel new];
//	_subNameLabel = [UILabel new];
	_descLabel = [UILabel new];
	_leftSubLabel = [UILabel new];
	_rightSubLabel = [UILabel new];
	_selectBtn = [UIButton new];
	UIView *topView = [UIView new];
	
	[self.contentView addSubview:topView];
	[self.contentView addSubview:_iconImageView];
	[self.contentView addSubview:_nameLabel];
//	[self.contentView addSubview:_subNameLabel];
	[self.contentView addSubview:_descLabel];
	[self.contentView addSubview:_leftSubLabel];
	[self.contentView addSubview:_rightSubLabel];
	[self.contentView addSubview:_selectBtn];
	
	_iconImageView.image = [UIImage imageNamed:@"package_category"];
	
	_nameLabel.font = [UIFont systemFontOfSize:13];
	_nameLabel.textColor = [UIColor colorWithRGB:0x333333];
	
	_descLabel.font = [UIFont systemFontOfSize:15];
	_descLabel.textColor = [UIColor colorWithRGB:0x323131];

//	_subNameLabel.font = _rightSubLabel.font = _leftSubLabel.font = [UIFont systemFontOfSize:12];
//	_subNameLabel.textColor = _rightSubLabel.textColor = _leftSubLabel.textColor = [UIColor colorWithRGB:0x999999];
	
	_rightSubLabel.font = _leftSubLabel.font = [UIFont systemFontOfSize:12];
	_rightSubLabel.textColor = _leftSubLabel.textColor = [UIColor colorWithRGB:0x999999];
	
	[_selectBtn setImage:[UIImage imageNamed:@"focus_button_normal"] forState:UIControlStateNormal];
	[_selectBtn setImage:[UIImage imageNamed:@"focus_button_selected"] forState:UIControlStateSelected];
	[_selectBtn addTarget:self action:@selector(clickOnSelect:) forControlEvents:UIControlEventTouchUpInside];
	
	[topView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.left.right.mas_equalTo(0);
		make.height.mas_equalTo(40);
	}];
	topView.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
	
	[_iconImageView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(10);
		make.size.mas_equalTo(CGSizeMake(20, 20));
	}];
	[_nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(_iconImageView.mas_right).offset(8);
//		make.top.mas_equalTo(10);
		make.centerY.mas_equalTo(_iconImageView);
	}];
//	[_subNameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
//		make.left.mas_equalTo(_iconImageView.mas_right).offset(20);
//		make.top.mas_equalTo(_nameLabel.mas_bottom).offset(10);
//		make.right.mas_equalTo(-20);
//	}];
	[_descLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.right.mas_equalTo(-20);
		make.top.mas_equalTo(60);
	}];
	[_leftSubLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.top.mas_equalTo(_descLabel.mas_bottom).offset(20);
		make.bottom.mas_equalTo(-20);
	}];
	[_rightSubLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(_leftSubLabel.mas_right).offset(10);
		make.top.mas_equalTo(_descLabel.mas_bottom).offset(20);
		make.bottom.mas_equalTo(-20);
	}];
	[_selectBtn setContentEdgeInsets:UIEdgeInsetsMake(10, 20, 20, 20)];
	[_selectBtn mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.right.mas_equalTo(0);
	}];
}

- (void)setModel:(YJMerchandiseModel *)model
{
	_model = model;
	
	NSString *name;
	if (!model.nickname || model.nickname.length == 0) {
		name = [NSString stringWithFormat:@"%@%@", @"运单号：", model.carrierNo ?: model.bikeUPS];
	} else {
		name = [NSString stringWithFormat:@"包裹昵称：%@", model.nickname];
	}
	self.nameLabel.text = @"包裹";
//	self.subNameLabel.text = [NSString stringWithFormat:@"运单号：%@", model.carrierNo];
	self.descLabel.text = name;
	self.leftSubLabel.text = model.warehouseName;
	self.rightSubLabel.text = [NSString stringWithFormat:@"%@ %@", @"已入库", [NSString timeDistanceTo:model.actionDate]];
}

- (void)clickOnSelect:(UIButton *)sender
{
	if (self.clickOnSelect) {
		self.clickOnSelect();
	}
}

- (void)setIsWant:(BOOL)isWant
{
	_isWant = isWant;
	self.selectBtn.selected = isWant;
}

- (void)setHighlighted:(BOOL)highlighted animated:(BOOL)animated
{}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{}

@end








