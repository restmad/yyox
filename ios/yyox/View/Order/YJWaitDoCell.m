//
//  YJWaitDoCell.m
//  yyox
//
//  Created by ddn on 2017/2/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJWaitDoCell.h"

@interface YJWaitDoCell()

@property (strong, nonatomic) UIImageView *cateImageView;
@property (strong, nonatomic) YYLabel *cateLabel;

@property (strong, nonatomic) YYLabel *nameLabel;

@property (strong, nonatomic) YYLabel *subNameLabel;

@property (strong, nonatomic) YYLabel *timeLabel;
@property (strong, nonatomic) YYLabel *statusLabel;
@property (strong, nonatomic) UIImageView *indicatorImageView;

@property (strong, nonatomic) YYLabel *warehouseLabel;

@property (strong, nonatomic) YYLabel *toDoLabel;

@property (strong, nonatomic) YYLabel *sbLabel;

@property (strong, nonatomic) UIView *middleLine;
@property (strong, nonatomic) UIButton *infoButton;

@end

@implementation YJWaitDoCell

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
	_cateImageView = [UIImageView new];
	_cateImageView.contentMode = UIViewContentModeCenter;
	
	_cateLabel = [YYLabel new];
	_cateLabel.ignoreCommonProperties = YES;
	_cateLabel.userInteractionEnabled = NO;
	
	_nameLabel = [YYLabel new];
	_nameLabel.ignoreCommonProperties = YES;
	_nameLabel.userInteractionEnabled = NO;
	
	_warehouseLabel = [YYLabel new];
	_warehouseLabel.ignoreCommonProperties = YES;
	_warehouseLabel.userInteractionEnabled = NO;
	
	_subNameLabel = [YYLabel new];
	_subNameLabel.ignoreCommonProperties = YES;
	_subNameLabel.userInteractionEnabled = NO;
	
	_timeLabel = [YYLabel new];
	_timeLabel.ignoreCommonProperties = YES;
	_timeLabel.userInteractionEnabled = NO;
	
	_statusLabel = [YYLabel new];
	_statusLabel.ignoreCommonProperties = YES;
	_statusLabel.userInteractionEnabled = NO;
	
	_toDoLabel = [YYLabel new];
	_toDoLabel.ignoreCommonProperties = YES;
	_toDoLabel.userInteractionEnabled = NO;
	
	_sbLabel = [YYLabel new];
	_sbLabel.ignoreCommonProperties = YES;
	_sbLabel.userInteractionEnabled = NO;
	
	_indicatorImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"cell_disclosure"]];
	_indicatorImageView.contentMode = UIViewContentModeCenter;
	
	_middleLine = [UIView new];
	_middleLine.backgroundColor = [UIColor colorWithRGB:0xcccccc];
	
	_infoButton = [UIButton new];
	[_infoButton setTitleColor:[UIColor colorWithRGB:0x1b82d2] forState:UIControlStateNormal];
	[_infoButton addTarget:self action:@selector(clickOnInfo:) forControlEvents:UIControlEventTouchUpInside];
	_infoButton.titleLabel.font = [UIFont systemFontOfSize:14];
	
	[self.contentView addSubview:_cateImageView];
	[self.contentView addSubview:_cateLabel];
	[self.contentView addSubview:_nameLabel];
	[self.contentView addSubview:_warehouseLabel];
	[self.contentView addSubview:_subNameLabel];
	[self.contentView addSubview:_timeLabel];
	[self.contentView addSubview:_statusLabel];
	[self.contentView addSubview:_indicatorImageView];
	[self.contentView addSubview:_warehouseLabel];
	[self.contentView addSubview:_sbLabel];
	[self.contentView addSubview:_middleLine];
	[self.contentView addSubview:_infoButton];
	[self.contentView addSubview:_toDoLabel];
}

- (void)setCellModel:(YJWaitDoCellModel *)cellModel
{
	_cellModel = cellModel;
	
	self.nameLabel.textLayout = cellModel.titleTextLayout;
	self.nameLabel.frame = cellModel.titleFrame;
	
	self.warehouseLabel.textLayout = cellModel.warehouseTextLayout;
	self.warehouseLabel.frame = cellModel.warehouseFrame;
	
	self.subNameLabel.textLayout = cellModel.subTitleTextLayout;
	self.subNameLabel.frame = cellModel.subTitleFrame;
	
	self.sbLabel.textLayout = cellModel.sbTextLayout;
	self.sbLabel.frame = cellModel.sbFrame;
	
	self.timeLabel.frame = cellModel.timeFrame;
	self.timeLabel.textLayout = cellModel.timeTextLayout;
	
	self.statusLabel.frame = cellModel.statusFrame;
	self.statusLabel.textLayout = cellModel.statusTextLayout;
	
	self.toDoLabel.textLayout = cellModel.descTextLayout;
	self.toDoLabel.frame = cellModel.descFrame;
	
	self.cateImageView.frame = cellModel.iconFrame;
	
	if ([cellModel.icon hasPrefix:@"http://"]) {
		[self.cateImageView yy_setImageWithURL:[NSURL URLWithString:cellModel.icon] options:YYWebImageOptionSetImageWithFadeAnimation];
	}else {
		self.cateImageView.image = [UIImage imageNamed:cellModel.icon];
	}
	
	self.cateLabel.textLayout = cellModel.cateTextLayout;
	self.cateLabel.frame = cellModel.cateFrame;
	
	self.middleLine.frame = CGRectMake(0, 40, cellModel.cellW, 0.3);
	
	[self.infoButton setTitle:cellModel.info forState:UIControlStateNormal];
	self.infoButton.frame = cellModel.infoFrame;
	if (cellModel.infoH > 1) {
		if (self.infoButton.edgeLineViews.count == 0) {
			_infoButton.edgeLines = UIEdgeInsetsMake(0.3, 0, 0, 0);
		}
	}
	
	self.indicatorImageView.centerY = (cellModel.cellH - cellModel.infoH) / 2;
	self.indicatorImageView.left = cellModel.cellW - self.indicatorImageView.width - cellModel.cellInsets.right;
}

- (void)clickOnInfo:(UIButton *)sender
{
	if (self.clickOnInfo) {
		self.clickOnInfo(sender.titleLabel.text);
	}
}

@end
