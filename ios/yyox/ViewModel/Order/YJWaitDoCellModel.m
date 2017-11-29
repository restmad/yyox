//
//  YJWaitDoCellModel.m
//  yyox
//
//  Created by ddn on 2017/2/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJWaitDoCellModel.h"
#import "YJToDoModel.h"

@interface YJWaitDoCellModel()

@property (assign, nonatomic) CGRect timeFrame;
@property (assign, nonatomic) CGRect statusFrame;
@property (assign, nonatomic) CGRect warehouseFrame;
@property (assign, nonatomic) CGRect cateFrame;
@property (assign, nonatomic) CGRect sbFrame;

@property (strong, nonatomic) YYTextLayout *timeTextLayout;
@property (strong, nonatomic) YYTextLayout *statusTextLayout;
@property (strong, nonatomic) YYTextLayout *warehouseTextLayout;
@property (strong, nonatomic) YYTextLayout *cateTextLayout;
@property (strong, nonatomic) YYTextLayout *sbTextLayout;

@end

@implementation YJWaitDoCellModel

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.infoH = 40;
		self.infoLeft = 0;
		
		self.timePaddingTop = 20;
		self.statusLeft = 10;
		self.statusPaddingTop = 10;
		
		self.iconW = 20;
		self.cellInsets = UIEdgeInsetsMake(10, 20, 20, 20);
		
		self.subTitlePaddingTop = 15;
		self.titlePaddingTop = 20;
	}
	return self;
}

- (CGRect)infoFrame
{
	return CGRectMake(0, self.cellH-self.infoH, self.cellW, self.infoH);
}

- (CGRect)cateFrame
{
	if (CGRectEqualToRect(CGRectZero, _cateFrame) || CGRectEqualToRect(CGRectNull, _cateFrame)) {
		_cateFrame = CGRectMake(95./2., (40 - self.cateTextLayout.textBoundingSize.height) / 2, self.cateTextLayout.textBoundingSize.width, self.cateTextLayout.textBoundingSize.height);
	}
	return _cateFrame;
}

- (CGRect)titleFrame
{
	return CGRectMake(self.cellInsets.left, 40 + self.titlePaddingTop, self.titleTextLayout.textBoundingSize.width, self.titleTextLayout.textBoundingSize.height);
}

- (CGRect)sbFrame
{
	if (!self.sbTextLayout) {
		return CGRectZero;
	}
	return (CGRect){CGPointMake(CGRectGetMaxX(self.subTitleFrame), CGRectGetMinY(self.subTitleFrame)), self.sbTextLayout.textBoundingSize};
}

- (CGRect)subTitleFrame
{
	if (!self.subTitleTextLayout) {
		return CGRectZero;
	}
	return (CGRect){CGPointMake(20, CGRectGetMaxY(self.titleFrame) + self.subTitlePaddingTop), self.subTitleTextLayout.textBoundingSize};
}

- (CGRect)timeFrame
{
	CGFloat left = 0;
	if (self.warehouseTextLayout) {
		left = CGRectGetMaxX(self.warehouseFrame) + 10;
	} else {
		left = 20;
	}
	_timeFrame = CGRectMake(left, ((CGRectGetMaxY(self.subTitleFrame) ?: CGRectGetMaxY(self.titleFrame)) + self.timePaddingTop) - (self.timeTextLayout.textBoundingSize.height > 12), self.timeTextLayout.textBoundingSize.width, self.timeTextLayout.textBoundingSize.height);
	return _timeFrame;
}

- (CGRect)warehouseFrame
{
	if (!self.warehouseTextLayout) return CGRectZero;
	if (CGRectEqualToRect(CGRectZero, _warehouseFrame) || CGRectEqualToRect(CGRectNull, _warehouseFrame)) {
		_warehouseFrame = CGRectMake(20, ((CGRectGetMaxY(self.subTitleFrame) ?: CGRectGetMaxY(self.titleFrame)) + self.timePaddingTop) - (self.warehouseTextLayout.textBoundingSize.height > 12), self.warehouseTextLayout.textBoundingSize.width, self.warehouseTextLayout.textBoundingSize.height);
	}
	return _warehouseFrame;
}

- (CGRect)statusFrame
{
	if (!_statusTextLayout) return CGRectZero;
	if (CGRectEqualToRect(CGRectZero, _statusFrame) || CGRectEqualToRect(CGRectNull, _statusFrame)) {
		_statusFrame = (CGRect){CGPointMake(CGRectGetMaxX(self.timeFrame) + 10, ((CGRectGetMaxY(self.subTitleFrame) ?: CGRectGetMaxY(self.titleFrame)) + self.timePaddingTop) - (self.statusTextLayout.textBoundingSize.height > 12)), self.statusTextLayout.textBoundingSize};
	}
	return _statusFrame;
}

- (CGRect)descFrame
{
	if (!self.descTextLayout) return CGRectZero;
	CGRect frame = (CGRect){CGPointMake(self.cellW - 20 - self.descTextLayout.textBoundingSize.width, ((CGRectGetMaxY(self.subTitleFrame) ?: CGRectGetMaxY(self.titleFrame)) + self.timePaddingTop) - (self.descTextLayout.textBoundingSize.height > 12)), self.descTextLayout.textBoundingSize};
	return frame;
}

- (CGFloat)cellH
{
	return CGRectGetMaxY(self.timeFrame) + self.cellInsets.bottom + self.infoH;
}

- (void)reset
{
	[super reset];
	_statusFrame = CGRectZero;
	_timeFrame = CGRectZero;
	_cateFrame = CGRectZero;
	_warehouseFrame = CGRectZero;
	_sbFrame = CGRectZero;
	
	_statusTextLayout = nil;
	_timeTextLayout = nil;
	_sbTextLayout = nil;
	_warehouseTextLayout = nil;
	_cateTextLayout = nil;
}

- (YYTextLayout *)timeTextLayout
{
	_timeTextLayout = [self generateTimeLayout:[self generateTime]];
	return _timeTextLayout;
}

- (void)layout
{
	[super layout];
	
	NSString *cate = [self generateCate];
	_cateTextLayout = [self generateCateLayout:cate];
	
	NSString *warehouse = [self generateWarehouse];
	_warehouseTextLayout = [self generateWarehouseLayout:warehouse];
	
	NSString *status = [self generateStatus];
	_statusTextLayout = [self generateStatusLayout: status];
	
	if (self.info && self.info.length > 0) {
		self.infoH = 40;
	} else {
		self.infoH = 0;
	}
}

- (NSString *)generateInfo
{
	YJToDoModel *model = (YJToDoModel *)self.model;
	if (model.status == 2) {
		return @"去支付";
	} else if (model.status == 4) {
		return @"缴税金";
	} else if (model.status == 3) {
		return @"上传身份证";
	}
	return @"";
}

- (NSString *)generateIconName
{
	YJToDoModel *model = (YJToDoModel *)self.model;
	switch (model.type) {
		case 1:
			return @"package_category";
		case 2:
			return @"order_category";
		case 3:
			return @"together_category";
		default:
			return @"";
	}
}

- (NSString *)generateStatus
{
	if ([self generateWarehouse]) return nil;
	YJToDoModel *model = (YJToDoModel *)self.model;
	if (model.status == 3) {
		return model.statement;
	}
	return model.orderStatus;
}

- (NSString *)generateTime
{
	YJToDoModel *model = (YJToDoModel *)self.model;
	if ([self generateWarehouse]) {
		return [NSString stringWithFormat:@"%@ %@", model.orderStatus, [NSString timeDistanceTo:model.date]];
	}
	return [NSString timeDistanceTo:model.date];
}

- (NSString *)generateDesc
{
	YJToDoModel *model = (YJToDoModel *)self.model;
	if (model.status == 3) return nil;
	return model.statement;
}

- (NSString *)generateTitle
{
	YJToDoModel *model = (YJToDoModel *)self.model;
	return model.nickname;
}

- (NSString *)generateSubTitle
{
	YJToDoModel *model = (YJToDoModel *)self.model;
	switch (model.type) {
		case 1:
			return nil;
		case 2:
			return nil;
		case 3:
			return [NSString stringWithFormat:@"包裹数量：%zd件", model.packageNum.integerValue];
			
		default:
			break;
	}
	return model.productName;
}

- (NSString *)generateCate
{
	YJToDoModel *model = (YJToDoModel *)self.model;
	switch (model.type) {
		case 1:
			return @"包裹";
		case 2:
			return @"订单";
		case 3:
			return @"合箱";
			
		default:
			return nil;
	}
}

- (NSString *)generateWarehouse
{
	YJToDoModel *model = (YJToDoModel *)self.model;
	if (model.status != 3 && [model.orderStatus containsString:@"已入库"]) {
		return model.warehouseName;
	}
	return nil;
}

- (YYTextLayout *)generateCateLayout:(NSString *)cate
{
	return [self generateLayoutWithStr:cate fontSize:13 fontColor:0x333333 maxW:self.cellW onlyLine:YES];
}

- (YYTextLayout *)generateTitleLayout:(NSString *)title
{
	return [self generateLayoutWithStr:title fontSize:15 fontColor:0x323131 maxW:self.cellW - 60 onlyLine:YES];
}

- (YYTextLayout *)generateSubTitleLayout:(NSString *)subTitle
{
	if (!subTitle || subTitle.length == 0) {
		return nil;
	}
	YYTextLayout *subLayout = nil;
	YJToDoModel *model = (YJToDoModel *)self.model;
	if (model.type == 3) {
		UIFont *font = [UIFont systemFontOfSize:13];
		NSMutableAttributedString *text = [[NSMutableAttributedString alloc] initWithString:subTitle];
		text.yy_font = font;
		text.yy_color = [UIColor colorWithRGB:0x666666];
		NSRegularExpression *regex = [[NSRegularExpression alloc] initWithPattern:@"\\d" options:0 error:nil];
		[text yy_setColor:[UIColor colorWithRGB:0xfc9b31] range:[regex rangeOfFirstMatchInString:text.string options:0 range:NSMakeRange(0, text.length)]];
		YYTextContainer *container = [YYTextContainer containerWithSize:CGSizeMake(self.cellW - 60, font.lineHeight * 2)];
		container.maximumNumberOfRows = 1;
		
		YYTextLayout *textLayout = [YYTextLayout layoutWithContainer:container text:text];
		subLayout = textLayout;
	} else {
		subLayout = [self generateLayoutWithStr:subTitle fontSize:13 fontColor:0x666666 maxW:self.cellW - 60 onlyLine:YES];
	}
	
	return subLayout;
}

- (YYTextLayout *)generateStatusLayout:(NSString *)status
{
	return [self generateLayoutWithStr:status fontSize:12 fontColor:0x999999 maxW:self.cellW - CGRectGetWidth(self.timeFrame) - 20 - 10 - 20 onlyLine:YES];
}

- (YYTextLayout *)generateTimeLayout:(NSString *)time
{
	return [self generateLayoutWithStr:time fontSize:12 fontColor:0x999999 maxW:self.cellW onlyLine:YES];
}

- (YYTextLayout *)generateWarehouseLayout:(NSString *)warehouse
{
	return [self generateLayoutWithStr:warehouse fontSize:12 fontColor:0x999999 maxW:self.cellW onlyLine:YES];
}

- (YYTextLayout *)generateDescLayout:(NSString *)desc
{
	return [self generateLayoutWithStr:desc fontSize:12 fontColor:0xfc9b31 maxW:self.cellW onlyLine:YES];
}

- (YYTextLayout *)generateLayoutWithStr:(NSString *)str fontSize:(CGFloat)fontSize fontColor:(uint32_t)rgbValue maxW:(CGFloat)maxW onlyLine:(BOOL)onlyLine
{
	if (!str || str.length == 0) {
		return nil;
	}
	UIFont *font = [UIFont systemFontOfSize:fontSize];
	NSMutableAttributedString *text = [[NSMutableAttributedString alloc] initWithString:str];
	text.yy_font = font;
	text.yy_color = [UIColor colorWithRGB:rgbValue];
	text.yy_lineBreakMode = NSLineBreakByTruncatingTail;
	
	YYTextContainer *container = [YYTextContainer containerWithSize:CGSizeMake(maxW, font.lineHeight * 2)];
	container.maximumNumberOfRows = onlyLine;
	
	YYTextLayout *textLayout = [YYTextLayout layoutWithContainer:container text:text];
	return textLayout;
}

@end
