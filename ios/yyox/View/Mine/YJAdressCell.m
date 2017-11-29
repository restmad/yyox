//
//  YJAdressCell.m
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJAdressCell.h"

@interface YJAdressCell()

@property (strong, nonatomic) UIButton *topViewContainer;

@property (strong, nonatomic) YYLabel *nameLabel;
@property (strong, nonatomic) YYLabel *phoneLabel;
@property (strong, nonatomic) YYLabel *addressLabel;
@property (strong, nonatomic) UIView *lineView;
@property (strong, nonatomic) UIButton *beDefaultButton;
@property (strong, nonatomic) UIButton *editButton;
@property (strong, nonatomic) UIButton *deleteButton;
@property (strong, nonatomic) UIButton *selectedButton;

@property (strong, nonatomic) UIImageView *disclauseImageView;

@end

@implementation YJAdressCell

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		self.backgroundColor = [UIColor whiteColor];
		[self setup];
		self.edgeLines = UIEdgeInsetsMake(0.3, 0, 0.3, 0);
	}
	return self;
}

- (void)prepareForReuse
{
	[super prepareForReuse];
}

- (void)setup
{
	UIButton *topViewContainer = [UIButton new];
	self.topViewContainer = topViewContainer;
	[self.contentView addSubview:topViewContainer];
	[topViewContainer addTarget:self action:@selector(clickOn:) forControlEvents:UIControlEventTouchUpInside];
	
	YYLabel *nameLabel = [YYLabel new];
	self.nameLabel = nameLabel;
	[self.contentView addSubview:nameLabel];
	_nameLabel.ignoreCommonProperties = YES;
	nameLabel.userInteractionEnabled = NO;
	
	
	YYLabel *phoneLabel = [YYLabel new];
	self.phoneLabel = phoneLabel;
	[self.contentView addSubview:phoneLabel];
	_phoneLabel.ignoreCommonProperties = YES;
	phoneLabel.userInteractionEnabled = NO;
	
	
	YYLabel *addressLabel = [YYLabel new];
	self.addressLabel = addressLabel;
	[self.contentView addSubview:addressLabel];
	_addressLabel.ignoreCommonProperties = YES;
	addressLabel.userInteractionEnabled = NO;
	
	UIView *lineView = [UIView new];
	self.lineView = lineView;
	[self.topViewContainer addSubview:lineView];
	lineView.backgroundColor = [UIColor colorWithRGB:0xcccccc];
	
//	UIView *bottomLineView = [UIView new];
//	self.bottomLineView = bottomLineView;
//	[self.contentView addSubview:bottomLineView];
//	bottomLineView.backgroundColor = [UIColor colorWithRGB:0xcccccc];
	
	UIButton *beDefaultButton = [UIButton new];
	self.beDefaultButton = beDefaultButton;
	[self.contentView addSubview:beDefaultButton];
	[beDefaultButton setImage:[UIImage imageNamed:@"focus_button_normal"] forState:UIControlStateNormal];
	[beDefaultButton setImage:[UIImage imageNamed:@"focus_button_selected"] forState:UIControlStateSelected];
	[beDefaultButton setTitle:@"设为默认地址" forState:UIControlStateNormal];
	[beDefaultButton setTitleEdgeInsets:UIEdgeInsetsMake(0, 7.5, 0, -7.5)];
	[beDefaultButton setTitleColor:[UIColor colorWithRGB:0x666666] forState:UIControlStateNormal];
	beDefaultButton.titleLabel.font = [UIFont systemFontOfSize:12];
	[beDefaultButton addTarget:self action:@selector(clickOnBeDefault:) forControlEvents:UIControlEventTouchUpInside];
	[beDefaultButton sizeToFit];
	
	UIButton *editButton = [UIButton new];
	self.editButton = editButton;
	[self.contentView addSubview:editButton];
	[editButton setImage:[UIImage imageNamed:@"address_edit"] forState:UIControlStateNormal];
	[editButton setTitle:@"编辑" forState:UIControlStateNormal];
	[editButton setTitleEdgeInsets:UIEdgeInsetsMake(0, 5, 0, -5)];
	editButton.titleLabel.font = [UIFont systemFontOfSize:12];
	[editButton setTitleColor:[UIColor colorWithRGB:0x666666] forState:UIControlStateNormal];
	[editButton addTarget:self action:@selector(clickOnEdit:) forControlEvents:UIControlEventTouchUpInside];
	[editButton sizeToFit];
	
	UIButton *deleteButton = [UIButton new];
	self.deleteButton = deleteButton;
	[self.contentView addSubview:deleteButton];
	[deleteButton setImage:[UIImage imageNamed:@"address_delete"] forState:UIControlStateNormal];
	[deleteButton setTitle:@"删除" forState:UIControlStateNormal];
	[deleteButton setTitleEdgeInsets:UIEdgeInsetsMake(0, 5, 0, -5)];
	deleteButton.titleLabel.font = [UIFont systemFontOfSize:12];
	[deleteButton setTitleColor:[UIColor colorWithRGB:0x666666] forState:UIControlStateNormal];
	[deleteButton addTarget:self action:@selector(clickOnDelete:) forControlEvents:UIControlEventTouchUpInside];
	[deleteButton sizeToFit];
	
	UIButton *selectedButton = [UIButton new];
	self.selectedButton = selectedButton;
	[self.contentView addSubview:self.selectedButton];
	[selectedButton setImage:[UIImage imageNamed:@"focus_button_normal"] forState:UIControlStateNormal];
	[selectedButton setImage:[UIImage imageNamed:@"focus_button_selected"] forState:UIControlStateSelected];
	[selectedButton sizeToFit];
	[selectedButton addTarget:self action:@selector(clickOnSelect:) forControlEvents:UIControlEventTouchUpInside];
	
	UIImageView *disclauseImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"cell_disclosure"]];
	disclauseImageView.contentMode = UIViewContentModeCenter;
	self.disclauseImageView = disclauseImageView;
	[self.contentView addSubview:disclauseImageView];
//	self.disclauseImageView.hidden = YES;
}

- (void)setType:(YJAddressCellType)type
{
	_type = type;
	if (type == YJAddressCellTypeNone) {
		self.disclauseImageView.hidden = YES;
		self.selectedButton.hidden = YES;
	} else if (type == YJAddressCellTypeDisclosure) {
		self.disclauseImageView.hidden = NO;
		self.selectedButton.hidden = YES;
	} else if (type == YJAddressCellTypeSelect) {
		self.disclauseImageView.hidden = YES;
		self.selectedButton.hidden = NO;
	}
}

- (void)clickOnBeDefault:(UIButton *)sender
{
	if (self.beDefaultButton.selected) return;
	if (self.clickOn) {
		self.clickOn(sender.titleLabel.text);
	}
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

- (void)clickOnSelect:(UIButton *)sender
{
	if (self.selectedButton.selected) return;
	if (self.clickOn) {
		self.clickOn(@"选中");
	}
}

- (void)clickOn:(UIButton *)sender
{
	if (self.clickOn) {
		self.clickOn(nil);
	}
}

- (void)setIsWant:(BOOL)isWant
{
	_isWant = isWant;
	self.selectedButton.selected = isWant;
}

- (void)setCellModel:(YJAddressCellModel *)cellModel
{
	_cellModel = cellModel;
	
	self.beDefaultButton.selected = cellModel.beDefault;
	
	self.nameLabel.textLayout = cellModel.nameTextLayout;
	self.nameLabel.frame = cellModel.nameFrame;
	
	self.phoneLabel.textLayout = cellModel.phoneTextLayout;
	self.phoneLabel.frame = cellModel.phoneFrame;
	
	self.addressLabel.textLayout = cellModel.descTextLayout;
	self.addressLabel.frame = cellModel.descFrame;
	
	self.lineView.frame = CGRectMake(0, CGRectGetMaxY(self.addressLabel.frame) + cellModel.cellInsets.bottom - 0.5, self.width, 0.5);
	
	self.deleteButton.frame = CGRectMake(self.width - 20 - self.deleteButton.width, CGRectGetMaxY(self.lineView.frame), self.deleteButton.width, cellModel.infoH);
	
	self.editButton.frame = CGRectMake(self.deleteButton.left - self.editButton.width - 17, CGRectGetMaxY(self.lineView.frame), self.editButton.width, cellModel.infoH);
	
	self.beDefaultButton.frame = CGRectMake(cellModel.cellInsets.left, CGRectGetMaxY(self.lineView.frame), self.beDefaultButton.width, cellModel.infoH);
	
//	self.bottomLineView.frame = CGRectMake(0, CGRectGetMaxY(self.beDefaultButton.frame)-0.3, self.width, 0.3);
	
	self.topViewContainer.frame = CGRectMake(0, 0, self.width, CGRectGetMaxY(self.lineView.frame));
	
	self.disclauseImageView.frame = CGRectMake(self.topViewContainer.width - 20 - self.disclauseImageView.width, 0, self.disclauseImageView.width, self.topViewContainer.height);
	
	self.selectedButton.frame = CGRectMake(self.topViewContainer.width - 20 - self.selectedButton.width, cellModel.cellInsets.top, self.selectedButton.width, self.selectedButton.height);
}

@end







