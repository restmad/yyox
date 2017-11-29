//
//  YJPendingCellModel.m
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPendingCellModel.h"

@interface YJPendingCellModel()

@property (strong, nonatomic) NSObject *model;

@property (strong, nonatomic) YYTextLayout *titleTextLayout;

@property (strong, nonatomic) YYTextLayout *descTextLayout;

@property (strong, nonatomic) YYTextLayout *subTitleTextLayout;

@end

@implementation YJPendingCellModel

MJCodingImplementation

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.cellW = kScreenWidth;
		self.cellInsets = UIEdgeInsetsMake(15, 20, 15, 20);
		self.iconW = 60;
		
		self.titleLeft = 20;
		self.titlePaddingTop = 10;
		
		self.subTitleLeft = 20;
		self.subTitlePaddingTop = 10;
		
		self.descLeft = self.cellInsets.left;
		self.descPaddingTop = 10;
		
		self.infoH = 43;
		self.infoLeft = self.cellInsets.left;
	}
	return self;
}

- (CGRect)iconFrame
{
	if (CGRectEqualToRect(CGRectZero, _iconFrame) || CGRectEqualToRect(CGRectNull, _iconFrame)) {
		_iconFrame = CGRectMake(self.cellInsets.left, self.cellInsets.top, self.iconW, self.iconW);
	}
	return _iconFrame;
}

- (CGFloat)cellH
{
	return CGRectGetMaxY(self.infoFrame);
}

- (CGRect)titleFrame
{
	if (!CGRectIsEmpty(_titleFrame) && !CGRectIsNull(_titleFrame)) return _titleFrame;
	_titleFrame = (CGRect){CGPointMake(CGRectGetMaxX(self.iconFrame) + self.titleLeft, CGRectGetMinY(self.iconFrame)+self.titlePaddingTop), self.titleTextLayout.textBoundingSize};
	return _titleFrame;
}

- (CGRect)subTitleFrame
{
	if (!CGRectIsEmpty(_subTitleFrame) && !CGRectIsNull(_subTitleFrame)) return _subTitleFrame;
	_subTitleFrame = (CGRect){CGPointMake(CGRectGetMaxX(self.iconFrame) + self.subTitleLeft, CGRectGetMaxY(self.titleFrame) + self.subTitlePaddingTop), self.subTitleTextLayout.textBoundingSize};
	return _subTitleFrame;
}

- (CGRect)descFrame
{
	if (!self.descTextLayout) return CGRectZero;
	
	if (!CGRectIsEmpty(_descFrame) && !CGRectIsNull(_descFrame)) return _descFrame;
	
	_descFrame = (CGRect){CGPointMake(self.descLeft, CGRectGetMaxY(self.iconFrame) + self.descPaddingTop), self.descTextLayout.textBoundingSize};
	return _descFrame;
}

- (CGRect)infoFrame
{
	
	if (!CGRectIsEmpty(_infoFrame) && !CGRectIsNull(_infoFrame)) return _infoFrame;
	CGFloat y = CGRectGetMaxY(self.descFrame) ?: CGRectGetMaxY(self.iconFrame) + self.cellInsets.bottom;
	_infoFrame = (CGRect){CGPointMake(self.infoLeft, y), CGSizeMake(kScreenWidth - self.infoLeft*2 - 130, self.infoH)};
	return _infoFrame;
}

- (void)bindingModel:(NSObject *)model
{
	_model = model;
	
	[self reset];
	
	[self layout];
}

- (void)reset
{
	_titleTextLayout = nil;
	_descTextLayout = nil;
	_subTitleTextLayout = nil;
	
	_titleFrame = CGRectZero;
	_subTitleFrame = CGRectZero;
	_infoFrame = CGRectZero;
	_descFrame = CGRectZero;
	_cellH = 0;
	_icon = nil;
	_info = nil;
}

- (void)layout
{
	self.icon = [self generateIconName];
	//title
	NSString *title = [self generateTitle];
	_titleTextLayout = [self generateTitleLayout:title];
	
	//subtitle
	NSString *subTitle = [self generateSubTitle];
	_subTitleTextLayout = [self generateSubTitleLayout:subTitle];
	
	//desc
	NSString *desc = [self generateDesc];
	_descTextLayout = [self generateDescLayout:desc];
	
	self.info = [self generateInfo];
}

- (NSString *)generateIconName
{
	return [(YJMerchandiseModel *)self.model icon];
}

- (NSString *)generateInfo
{
	return [(YJMerchandiseModel *)self.model warehouseName];
}

- (NSString *)generateTitle
{
	NSString *title = [(YJMerchandiseModel *)self.model nickname];
	if (!title || title.length == 0) {
		title = @" ";
	}
	return title;
}

- (YYTextLayout *)generateTitleLayout:(NSString *)title
{
	UIFont *titleFont = [UIFont systemFontOfSize:14];
	NSMutableAttributedString *titleText = [[NSMutableAttributedString alloc] initWithString:title];
	titleText.yy_font = titleFont;
	titleText.yy_color = [UIColor colorWithRGB:0x323131];
	titleText.yy_lineBreakMode = NSLineBreakByTruncatingTail;
	
	YYTextContainer *titleContainer = [YYTextContainer containerWithSize:CGSizeMake(kScreenWidth - self.cellInsets.left - self.cellInsets.right - self.iconW - self.titleLeft, titleFont.lineHeight * 2)];
	titleContainer.maximumNumberOfRows = 1;
	YYTextLayout *titleTextLayout = [YYTextLayout layoutWithContainer:titleContainer text:titleText];
	return titleTextLayout;
}

- (NSString *)generateSubTitle
{
	NSString *subTitle = [(YJMerchandiseModel *)self.model carrierNo];
	if (!subTitle || subTitle.length == 0) {
		subTitle = @" ";
	}
	return subTitle;
}

- (YYTextLayout *)generateSubTitleLayout:(NSString *)subTitle
{
	UIFont *subTitleFont = [UIFont systemFontOfSize:12];
	NSMutableAttributedString *subTitleText = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"运单号：%@", subTitle]];
	subTitleText.yy_font = subTitleFont;
	subTitleText.yy_color = [UIColor colorWithRGB:0x999999];
	subTitleText.yy_lineBreakMode = NSLineBreakByTruncatingTail;
	
	YYTextContainer *subTitleContainer = [YYTextContainer containerWithSize:CGSizeMake(kScreenWidth - self.cellInsets.left - self.cellInsets.right - self.iconW - self.subTitleLeft, subTitleFont.lineHeight * 2)];
	subTitleContainer.maximumNumberOfRows = 1;
	YYTextLayout *subTitleTextLayout = [YYTextLayout layoutWithContainer:subTitleContainer text:subTitleText];
	return subTitleTextLayout;
}

- (NSString *)generateDesc
{
	return [(YJMerchandiseModel *)self.model productName];
}

- (YYTextLayout *)generateDescLayout:(NSString *)desc
{
//	if (desc) {
//		UIFont *descFont = [UIFont systemFontOfSize:12];
//		NSMutableAttributedString *descText = [[NSMutableAttributedString alloc] initWithString:desc];
//		descText.yy_font = descFont;
//		descText.yy_color = [UIColor colorWithRGB:0x323131];
//		descText.yy_lineSpacing = descFont.lineHeight * 0.5;
//		
//		YYTextContainer *descContainer = [YYTextContainer containerWithSize:CGSizeMake(kScreenWidth - self.cellInsets.left - self.cellInsets.right, descFont.lineHeight * 2)];
//		descContainer.maximumNumberOfRows = 1;
//		
//		YYTextLayout *descTextLayout = [YYTextLayout layoutWithContainer:descContainer text:descText];
//		return descTextLayout;
//	}
	return nil;
}

@end
