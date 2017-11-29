//
//  YJPendingCell.m
//  yyox
//
//  Created by ddn on 2017/1/3.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPendingCell.h"

@interface YJPendingCell()

@property (strong, nonatomic) UIImageView *orderImageView;

@property (strong, nonatomic) YYLabel *nameLabel;

@property (strong, nonatomic) YYLabel *subnameLabel;

@property (strong, nonatomic) YYLabel *desLabel;

@property (strong, nonatomic) UILabel *infoLabel;

@property (strong, nonatomic) UIView *lineView;

@property (strong, nonatomic) UIButton *editButton;
@property (strong, nonatomic) UIButton *deleteButton;

@end

@implementation YJPendingCell

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
	_lineView = [UIView new];
	_lineView.backgroundColor = [UIColor colorWithRGB:0xcccccc];
	
	_orderImageView = [UIImageView new];
	_orderImageView.contentMode = UIViewContentModeCenter;
	
	_nameLabel = [YYLabel new];
	_desLabel.ignoreCommonProperties = YES;
	
	_subnameLabel = [YYLabel new];
	_desLabel.ignoreCommonProperties = YES;
	
	_desLabel = [YYLabel new];
	_desLabel.ignoreCommonProperties = YES;
	
	[self.contentView addSubview:_orderImageView];
	[self.contentView addSubview:_nameLabel];
	[self.contentView addSubview:_subnameLabel];
	[self.contentView addSubview:_desLabel];
	[self.contentView addSubview:_lineView];
	
	_infoLabel = [UILabel new];
	_infoLabel.numberOfLines = 1;
	_infoLabel.font = [UIFont systemFontOfSize:13];
	_infoLabel.textColor = [UIColor colorWithRGB:0x999999];
	
	UIButton *editButton = [UIButton new];
	self.editButton = editButton;
	[editButton setImage:[UIImage imageNamed:@"address_edit"] forState:UIControlStateNormal];
	[editButton setTitle:@"编辑" forState:UIControlStateNormal];
	[editButton setTitleEdgeInsets:UIEdgeInsetsMake(0, 5, 0, -5)];
	editButton.titleLabel.font = [UIFont systemFontOfSize:12];
	[editButton setTitleColor:[UIColor colorWithRGB:0x666666] forState:UIControlStateNormal];
	[editButton addTarget:self action:@selector(clickOnEdit:) forControlEvents:UIControlEventTouchUpInside];
	[editButton sizeToFit];
	
	UIButton *deleteButton = [UIButton new];
	self.deleteButton = deleteButton;
	[deleteButton setImage:[UIImage imageNamed:@"address_delete"] forState:UIControlStateNormal];
	[deleteButton setTitle:@"删除" forState:UIControlStateNormal];
	[deleteButton setTitleEdgeInsets:UIEdgeInsetsMake(0, 5, 0, -5)];
	deleteButton.titleLabel.font = [UIFont systemFontOfSize:12];
	[deleteButton setTitleColor:[UIColor colorWithRGB:0x666666] forState:UIControlStateNormal];
	[deleteButton addTarget:self action:@selector(clickOnDelete:) forControlEvents:UIControlEventTouchUpInside];
	[deleteButton sizeToFit];
	
	[self.contentView addSubview:_infoLabel];
	[self.contentView addSubview:_editButton];
	[self.contentView addSubview:_deleteButton];
	
}

- (void)setHighlighted:(BOOL)highlighted animated:(BOOL)animated{}

- (void)setCellModel:(YJPendingCellModel *)cellModel
{
	_cellModel = cellModel;
	
	self.nameLabel.textLayout = cellModel.titleTextLayout;
	self.nameLabel.frame = cellModel.titleFrame;
	
	self.subnameLabel.textLayout = cellModel.subTitleTextLayout;
	self.subnameLabel.frame = cellModel.subTitleFrame;
	
	self.desLabel.textLayout = cellModel.descTextLayout;
	self.desLabel.frame = cellModel.descFrame;
	
	self.infoLabel.frame = cellModel.infoFrame;
	self.infoLabel.text = cellModel.info;
	
	self.orderImageView.frame = cellModel.iconFrame;
	
	if ([cellModel.icon hasPrefix:@"http://"]) {
		[self.orderImageView yy_setImageWithURL:[NSURL URLWithString:cellModel.icon] options:YYWebImageOptionSetImageWithFadeAnimation];
	}else {
		self.orderImageView.image = [UIImage imageNamed:@"package"];
	}
	
	self.deleteButton.frame = CGRectMake(self.width - cellModel.cellInsets.right - self.deleteButton.width, self.infoLabel.top, self.deleteButton.width, self.infoLabel.height);
	self.editButton.frame = CGRectMake(self.deleteButton.left - self.editButton.width - 17, self.infoLabel.top, self.editButton.width, self.infoLabel.height);
	self.lineView.frame = CGRectMake(0, CGRectGetMinY(self.deleteButton.frame) - 0.5, self.width, 0.5);
}

- (void)clickOnEdit:(UIButton *)sender
{
	if (self.clickOn) {
		self.clickOn(sender.titleLabel.text);
	}
}

- (void)clickOnDelete:(UIButton *)sender
{
	if (self.clickOn) {
		self.clickOn(sender.titleLabel.text);
	}
}

@end
