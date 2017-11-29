//
//  YJOrderCellModel.m
//  yyox
//
//  Created by ddn on 2017/1/14.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJOrderCellModel.h"

@interface YJOrderCellModel()

@property (strong, nonatomic) YJOrderMainMsgModel *model;

@property (assign, nonatomic) CGRect titleFrame;
@property (assign, nonatomic) CGRect descFrame;
@property (assign, nonatomic) CGRect infoFrame;
@property (assign, nonatomic) CGRect timeFrame;
@property (assign, nonatomic) CGRect iconFrame;

@property (assign, nonatomic) CGFloat timeMaxY;
@property (assign, nonatomic) CGFloat cellH;

@property (strong, nonatomic) YYTextLayout *titleTextLayout;

@property (strong, nonatomic) YYTextLayout *descTextLayout;

@property (strong, nonatomic) YYTextLayout *infoTextLayout;

@property (strong, nonatomic) YYTextLayout *timeTextLayout;

@end

@implementation YJOrderCellModel

MJCodingImplementation

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.cellW = kScreenWidth;
		self.cellInsets = UIEdgeInsetsMake(20, 30, 15, 30);
		self.iconW = 31.5;
		self.titleLeft = 15;
		self.descLeft = self.cellInsets.left;
		self.descPaddingTop = 15;
		self.infoLeft = self.cellInsets.left;
		self.infoPaddingTop = 15;
		self.timeLeft = 15;
		self.timePaddingTop = 10;
		
		_iconFrame = CGRectMake(self.cellInsets.left, self.cellInsets.top, self.iconW, self.iconW);
	}
	return self;
}

- (CGFloat)cellH
{
	if (self.infoTextLayout) {
		return CGRectGetMaxY(self.infoFrame) + self.cellInsets.bottom;
	}
	if (self.descTextLayout) {
		return CGRectGetMaxY(self.descFrame) + self.cellInsets.bottom;
	}
	return CGRectGetMaxY(self.iconFrame) + self.cellInsets.bottom;
}

- (CGRect)titleFrame
{
	if (!CGRectIsEmpty(_titleFrame) && !CGRectIsNull(_titleFrame)) return _titleFrame;
	_titleFrame = (CGRect){CGPointMake(CGRectGetMaxX(self.iconFrame) + self.titleLeft, self.cellInsets.top), self.titleTextLayout.textBoundingSize};
	return _titleFrame;
}

- (CGRect)timeFrame
{
	_timeFrame = (CGRect){CGPointMake(CGRectGetMaxX(self.iconFrame) + self.timeLeft, CGRectGetMaxY(self.titleFrame) + self.timePaddingTop), self.timeTextLayout.textBoundingSize};
	return _timeFrame;
}

- (CGFloat)timeMaxY
{
	if (_timeMaxY > 1) return _timeMaxY;
	
	_timeMaxY = CGRectGetMaxY(self.timeFrame);
	return _timeMaxY;
}

- (CGRect)descFrame
{
	if (!self.descTextLayout) return CGRectZero;
	
	if (!CGRectIsEmpty(_descFrame) && !CGRectIsNull(_descFrame)) return _descFrame;
	
	_descFrame = (CGRect){CGPointMake(self.descLeft, self.timeMaxY + self.descPaddingTop), self.descTextLayout.textBoundingSize};
	return _descFrame;
}

- (CGRect)infoFrame
{
	if (!self.infoTextLayout) return CGRectZero;
	
	if (!CGRectIsEmpty(_infoFrame) && !CGRectIsNull(_infoFrame)) return _infoFrame;
	
	_infoFrame = (CGRect){CGPointMake(self.infoLeft, CGRectGetMaxY(self.descFrame) + self.infoPaddingTop), self.infoTextLayout.textBoundingSize};
	return _infoFrame;
}

- (NSString *)icon
{
	return [self generateIcon];
}

- (NSString *)generateIcon
{
	if (self.model.state.integerValue == 1 || self.model.state.integerValue == 4) {
		return @"kehu";
	} else if (self.model.state.integerValue == 2 ) {
		
		if ([self.model.sourceType isEqualToNumber:@1]) {
			return @"zhuanyun";
		} else if ([self.model.sourceType isEqualToNumber:@2]) {
			return @"kehu";
		} else {
			return @"cangku";
		}
		
	} else if (self.model.state.integerValue == 3 || self.model.state.integerValue == 5) {
		return @"zhuanyun";
	}
	return @"kefu";
}

- (NSString *)generateTitle
{
	if (self.model.state.integerValue == 1 || self.model.state.integerValue == 4) {
		return self.model.source_name ?: @" ";
	} else if (self.model.state.integerValue == 2 ) {
		
		if ([self.model.sourceType isEqualToNumber:@1]) {
			return @"邮客转运";
		} else if ([self.model.sourceType isEqualToNumber:@2]) {
			return self.model.customerName ?: @" ";
		} else {
			return self.model.source_name ?: @" ";
		}
		
	} else if (self.model.state.integerValue == 3 || self.model.state.integerValue == 5) {
		return @"邮客转运";
	}
	return @"邮客客服";
}

- (NSString *)generateDesc
{
	if (self.model.state.integerValue != 2) {
		return self.model.information;
	}
	NSString *desc = nil;
	if ([self.model.information containsString:@") 已创建"]) {
		desc = [NSString stringWithFormat:@"您的订单(%@)已经创建", self.model.otherNo];
	} else if ([self.model.information containsString:@"仓库订单已分拣"]) {
		desc = [NSString stringWithFormat:@"您的订单已经打印完毕"];
	} else if ([self.model.information containsString:@"仓库订单已下架"]) {
		desc = [NSString stringWithFormat:@"您的订单已经完成捡货"];
	} else if ([self.model.information containsString:@"请及时上传身份证以便订单清关"]) {
		desc = [NSString stringWithFormat:@"您的订单(%@)已经创建，等待提供身份证信息", self.model.otherNo];
	} else if ([self.model.information containsString:@"仓库订单已出库"]) {
		desc = [NSString stringWithFormat:@"您的订单(%@)已经打包成功", self.model.otherNo];
	} else if ([self.model.information containsString:@"订单清关已完成，正在派送中"]) {
		NSArray *strs = [self.model.information componentsSeparatedByString:@" "];
		NSString *company = strs[1];
		NSString *no = strs[2];
		desc = [NSString stringWithFormat:@"您的订单海关已放行，已交往%@，单号%@", company, no];
	} else if ([self.model.information containsString:@"发往机场，预计起飞时间"]) {
		desc = [NSString stringWithFormat:@"您的订单已经离开%@，发往%@%@", self.model.source_name, self.model.destination, (self.model.flightNo && self.model.flightNo.length > 0) ? [NSString stringWithFormat:@"，航班号为%@", self.model.flightNo] : @""];
	} else if ([self.model.information containsString:@"机场，正在提货中"]) {
		desc = [NSString stringWithFormat:@"您的订单已抵达口岸机场"];
	} else if ([self.model.information containsString:@"海关清关中"]) {
		desc = [NSString stringWithFormat:@"您的订单已从机场提货，送交海关清关中"];
	} else if ([self.model.information containsString:@"海关查验中"]) {
		desc = [NSString stringWithFormat:@"您的订单正在海关查验中"];
	} else if ([self.model.information containsString:@"请尽快支付税金以便订单及时派送"]) {
		desc = [NSString stringWithFormat:@"您的订单等待补缴税款后放行"];
	} else if ([self.model.information hasPrefix:@"订单"] && [self.model.information hasSuffix:@"已取消"]) {
		desc = [NSString stringWithFormat:@"您的%@", self.model.information];
	} else if ([self.model.information hasPrefix:@"订单"] && [self.model.information hasSuffix:@"已完成"]) {
		desc = [NSString stringWithFormat:@"您的订单(%@)已经送达，感谢您使用我们的服务", self.model.otherNo];
	}
	else {
		desc = self.model.information;
	}
	return desc;
}

- (NSString *)generateInfo
{
	return nil;
}

- (void)bindingModel:(YJOrderMainMsgModel *)model
{
	if ([_model isEqual:model]) return;
	_model = model;
	
	[self reset];
	
	[self layout];
}

- (void)reset
{
	_titleTextLayout = nil;
	_descTextLayout = nil;
	_timeTextLayout = nil;
	_infoTextLayout = nil;
	
	_titleFrame = CGRectZero;
	_timeFrame = CGRectZero;
	_infoFrame = CGRectZero;
	_descFrame = CGRectZero;
	_cellH = 0;
	_icon = nil;
}

- (void)layout
{
	//title
	NSString *title = [self generateTitle];
	if (title) {
		UIFont *titleFont = [UIFont systemFontOfSize:13];
		NSMutableAttributedString *titleText = [[NSMutableAttributedString alloc] initWithString:title];
		titleText.yy_font = titleFont;
		titleText.yy_color = [UIColor colorWithRGB:0x333333];
		titleText.yy_lineBreakMode = NSLineBreakByTruncatingTail;
		
		YYTextContainer *titleContainer = [YYTextContainer containerWithSize:CGSizeMake(kScreenWidth - 70 - 30, titleFont.lineHeight * 2)];
		titleContainer.maximumNumberOfRows = 1;
		_titleTextLayout = [YYTextLayout layoutWithContainer:titleContainer text:titleText];
	}
	
	//desc
	NSString *desc = [self generateDesc];
	if (desc) {
		UIFont *descFont = [UIFont systemFontOfSize:12];
		NSMutableAttributedString *descText = [[NSMutableAttributedString alloc] initWithString:desc];
		descText.yy_font = descFont;
		descText.yy_color = [UIColor colorWithRGB:0x666666];
		descText.yy_lineSpacing = descFont.lineHeight * 0.5;
		_descTextLayout = [YYTextLayout layoutWithContainerSize:CGSizeMake(kScreenWidth - 60, MAXFLOAT) text:descText];
	}
	
	//info
	NSString *info = [self generateInfo];
	if (info) {
		UIFont *infoFont = [UIFont systemFontOfSize:11];
		NSMutableAttributedString *infoText = [[NSMutableAttributedString alloc] initWithString:info];
		infoText.yy_font = infoFont;
		infoText.yy_color = [UIColor colorWithRGB:0x999999];
		infoText.yy_lineSpacing = infoFont.lineHeight * 0.5;
		_infoTextLayout = [YYTextLayout layoutWithContainerSize:CGSizeMake(kScreenWidth - 60, MAXFLOAT) text:infoText];
	}
}

- (YYTextLayout *)timeTextLayout
{
	UIFont *timeFont = [UIFont systemFontOfSize:11];
	NSMutableAttributedString *timeText = [[NSMutableAttributedString alloc] initWithString:[NSString timeDistanceTo:self.model.operateTime] ?: @""];
	timeText.yy_font = timeFont;
	timeText.yy_color = [UIColor colorWithRGB:0x999999];
	
	YYTextContainer *timeContainer = [YYTextContainer containerWithSize:CGSizeMake(self.cellW - self.cellInsets.left * 2 - self.iconW - self.timeLeft, timeFont.lineHeight * 2)];
	timeContainer.maximumNumberOfRows = 1;
	
	_timeTextLayout = [YYTextLayout layoutWithContainer:timeContainer text:timeText];
	return _timeTextLayout;
}


@end











