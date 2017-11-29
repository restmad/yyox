//
//  YJOrderCell.m
//  yyox
//
//  Created by ddn on 2016/12/30.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJOrderCell.h"

@interface YJOrderCell()

@property (strong, nonatomic) UIImageView *orderImageView;

@property (strong, nonatomic) YYLabel *nameLabel;

@property (strong, nonatomic) YYLabel *timeLabel;

@property (strong, nonatomic) YYLabel *desLabel;

@property (strong, nonatomic) YYLabel *infoLabel;

@property (strong, nonatomic) UIView *lineView;

@end

@implementation YJOrderCell

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		[self setup];
		self.backgroundColor = [UIColor whiteColor];
	}
	return self;
}

- (void)setup
{
	_orderImageView = [UIImageView new];
	_orderImageView.contentMode = UIViewContentModeCenter;
	
	_nameLabel = [YYLabel new];
	_nameLabel.displaysAsynchronously = YES;
	_nameLabel.ignoreCommonProperties = YES;
	
	_desLabel = [YYLabel new];
	_desLabel.displaysAsynchronously = YES;
	_desLabel.ignoreCommonProperties = YES;
	
	_timeLabel = [YYLabel new];
	_timeLabel.displaysAsynchronously = YES;
	_timeLabel.ignoreCommonProperties = YES;
	
	_infoLabel = [YYLabel new];
	_infoLabel.displaysAsynchronously = YES;
	_infoLabel.ignoreCommonProperties = YES;
	
	[self.contentView addSubview:_orderImageView];
	[self.contentView addSubview:_nameLabel];
	[self.contentView addSubview:_desLabel];
	[self.contentView addSubview:_timeLabel];
	[self.contentView addSubview:_infoLabel];
	
	_lineView = [UIView new];
	_lineView.backgroundColor = [UIColor colorWithRGB:0xcccccc];
	[self.contentView addSubview:_lineView];
}

- (void)setHighlighted:(BOOL)highlighted animated:(BOOL)animated{}

- (void)setCellModel:(YJOrderCellModel *)cellModel
{
	_cellModel = cellModel;
	
	self.nameLabel.textLayout = cellModel.titleTextLayout;
	self.nameLabel.frame = cellModel.titleFrame;
	
	self.timeLabel.textLayout = cellModel.timeTextLayout;
	self.timeLabel.frame = cellModel.timeFrame;
	
	self.desLabel.textLayout = cellModel.descTextLayout;
	self.desLabel.frame = cellModel.descFrame;
	
	self.infoLabel.textLayout = cellModel.infoTextLayout;
	self.infoLabel.frame = cellModel.infoFrame;
	
	self.orderImageView.frame = cellModel.iconFrame;
	
	if ([cellModel.icon hasPrefix:@"http://"]) {
		[self.orderImageView yy_setImageWithURL:[NSURL URLWithString:cellModel.icon] options:YYWebImageOptionSetImageWithFadeAnimation];
	}else {
		self.orderImageView.image = [UIImage imageNamed:cellModel.icon];
	}
}

- (void)layoutSubviews
{
	[super layoutSubviews];
	
	_lineView.frame = CGRectMake(0, self.height-0.5, self.width, 0.5);
}

@end
