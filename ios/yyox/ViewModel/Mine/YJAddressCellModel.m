//
//  YJAddressCellModel.m
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJAddressCellModel.h"

@interface YJAddressCellModel()

@property (strong, nonatomic) YJAddressModel *model;

@property (assign, nonatomic) CGRect nameFrame;
@property (assign, nonatomic) CGRect descFrame;
@property (assign, nonatomic) CGRect phoneFrame;

@property (assign, nonatomic) CGFloat cellH;

@property (strong, nonatomic) YYTextLayout *nameTextLayout;

@property (strong, nonatomic) YYTextLayout *descTextLayout;

@property (strong, nonatomic) YYTextLayout *phoneTextLayout;

@end

@implementation YJAddressCellModel

MJCodingImplementation

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.cellW = kScreenWidth;
		
		self.phoneLeft = 23;
		
		self.cellInsets = UIEdgeInsetsMake(15, 25, 15, 50);
		
		self.descPaddingTop = 10;
		
		self.infoH = 43;
	}
	return self;
}

- (CGFloat)cellH
{
	return CGRectGetMaxY(self.descFrame) + self.infoH + self.cellInsets.bottom;
}

- (CGRect)nameFrame
{
	if (!CGRectIsEmpty(_nameFrame) && !CGRectIsNull(_nameFrame)) return _nameFrame;
	_nameFrame = (CGRect){CGPointMake(self.cellInsets.left, self.cellInsets.top), self.nameTextLayout.textBoundingSize};
	return _nameFrame;
}

- (CGRect)phoneFrame
{
	if (!CGRectIsEmpty(_phoneFrame) && !CGRectIsNull(_phoneFrame)) return _phoneFrame;
	_phoneFrame = (CGRect){CGPointMake(CGRectGetMaxX(self.nameFrame) + self.phoneLeft, self.cellInsets.top), self.phoneTextLayout.textBoundingSize};
	return _phoneFrame;
}

- (CGRect)descFrame
{
	if (!self.descTextLayout) return CGRectZero;
	
	if (!CGRectIsEmpty(_descFrame) && !CGRectIsNull(_descFrame)) return _descFrame;
	
	_descFrame = (CGRect){CGPointMake(self.cellInsets.left, CGRectGetMaxY(self.nameFrame) + self.descPaddingTop), self.descTextLayout.textBoundingSize};
	return _descFrame;
}

- (void)bindingModel:(YJAddressModel *)model
{
	_model = model;
	
	[self reset];
	
	[self layout];
}

- (void)reset
{
	_nameTextLayout = nil;
	_descTextLayout = nil;
	_phoneTextLayout = nil;
	
	_nameFrame = CGRectZero;
	_descFrame = CGRectZero;
	_phoneFrame = CGRectZero;
	_cellH = 0;
}

- (void)layout
{
	//name
	if (self.model.name) {
		
		UIFont *nameFont = [UIFont systemFontOfSize:14];
		NSMutableAttributedString *nameText = [[NSMutableAttributedString alloc] initWithString:self.model.name];
		nameText.yy_font = nameFont;
		nameText.yy_color = [UIColor colorWithRGB:0x333333];
		nameText.yy_lineBreakMode = NSLineBreakByTruncatingTail;
		
		YYTextContainer *nameContainer = [YYTextContainer containerWithSize:CGSizeMake(kScreenWidth - self.cellInsets.left - self.cellInsets.right, nameFont.lineHeight * 2)];
		nameContainer.maximumNumberOfRows = 1;
		_nameTextLayout = [YYTextLayout layoutWithContainer:nameContainer text:nameText];
	}
	
	if (self.model.mobile) {
		UIFont *phoneFont = [UIFont systemFontOfSize:14];
		NSMutableAttributedString *phoneText = [[NSMutableAttributedString alloc] initWithString:self.model.mobile];
		phoneText.yy_font = phoneFont;
		phoneText.yy_color = [UIColor colorWithRGB:0x666666];
		phoneText.yy_lineBreakMode = NSLineBreakByTruncatingTail;
		
		YYTextContainer *phoneContainer = [YYTextContainer containerWithSize:CGSizeMake(kScreenWidth - self.cellInsets.right - _nameTextLayout.textBoundingSize.width - self.phoneLeft, phoneFont.lineHeight * 2)];
		phoneContainer.maximumNumberOfRows = 1;
		_phoneTextLayout = [YYTextLayout layoutWithContainer:phoneContainer text:phoneText];
	}
	
	//desc
	if (self.model.fullDetailaddress || self.model.detailaddress || self.model.province || self.model.city || self.model.district) {
		UIFont *descFont = [UIFont systemFontOfSize:14];
		NSMutableAttributedString *descText = [[NSMutableAttributedString alloc] initWithString:self.model.fullDetailaddress ?: [NSString stringWithFormat:@"%@%@%@%@", self.model.province, self.model.city, self.model.district, self.model.detailaddress]];
		descText.yy_font = descFont;
		descText.yy_lineSpacing = descFont.lineHeight * 0.5;
		descText.yy_color = [UIColor colorWithRGB:0x999999];
		_descTextLayout = [YYTextLayout layoutWithContainerSize:CGSizeMake(kScreenWidth - self.cellInsets.left - self.cellInsets.right, MAXFLOAT) text:descText];
	}
	
	self.beDefault = self.model.isdefault;
}

- (void)setBeDefault:(BOOL)beDefault
{
	_beDefault = beDefault;
	self.model.isdefault = beDefault;
}

@end
