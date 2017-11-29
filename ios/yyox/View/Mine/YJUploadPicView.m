//
//  YJUploadPicView.m
//  yyox
//
//  Created by ddn on 2017/1/12.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJUploadPicView.h"
#import "YJBottomTextBtn.h"

@interface YJUploadPicView()

@property (strong, nonatomic) UIView *topLineView;

@property (strong, nonatomic) UILabel *titleLabel;
@property (strong, nonatomic) UILabel *desLabel;

@property (strong, nonatomic) UIImageView *frontImageView;
@property (strong, nonatomic) UIImageView *backImageView;

@property (strong, nonatomic) YJBottomTextBtn *frontButton;
@property (strong, nonatomic) YJBottomTextBtn *backButton;

@property (strong, nonatomic) UIButton *beDefaultButton;

@end

@implementation YJUploadPicView

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		self.backgroundColor = [UIColor whiteColor];
		[self setup];
	}
	return self;
}

- (void)setup
{
	
	self.titleLabel = [UILabel new];
	self.titleLabel.font = [UIFont systemFontOfSize:13];
	self.titleLabel.textColor = [UIColor colorWithRGB:0x666666];
	self.titleLabel.text = @"上传身份证照片";
	
	self.desLabel = [UILabel new];
	self.desLabel.numberOfLines = 0;
	NSString *text = @"必须是清晰原件电子版，仅支持jpg .jpeg .png .bmp格式，大小不超过2M";
	NSMutableAttributedString *attrStr = [[NSMutableAttributedString alloc] initWithString:text];
	attrStr.yy_font = [UIFont systemFontOfSize:10];
	attrStr.yy_color = [UIColor colorWithRGB:0x999999];
	attrStr.yy_lineSpacing = 5;
	self.desLabel.attributedText = attrStr;
	
	self.frontImageView = [UIImageView new];
	self.backImageView = [UIImageView new];
	
	self.frontImageView.userInteractionEnabled = self.backImageView.userInteractionEnabled = NO;
	self.frontImageView.layer.cornerRadius = self.backImageView.layer.cornerRadius = 8;
	self.frontImageView.layer.borderColor = self.backImageView.layer.borderColor = [UIColor colorWithRGB:0xcccccc].CGColor;
	self.frontImageView.layer.borderWidth = self.backImageView.layer.borderWidth = 1;
	self.frontImageView.layer.masksToBounds = self.backImageView.layer.masksToBounds = YES;
	
	self.frontButton = [YJBottomTextBtn buttonWithText:@"点击上传身份证正面照" image:[UIImage imageNamed:@"address_add_card"] selectedImage:nil];
	self.backButton = [YJBottomTextBtn buttonWithText:@"点击上传身份证背面照" image:[UIImage imageNamed:@"address_add_card"] selectedImage:nil];
	self.frontButton.titleLabel.font = self.backButton.titleLabel.font = [UIFont systemFontOfSize:10];
	[self.frontButton setTitleColor:[UIColor colorWithRGB:0x999999] forState:UIControlStateNormal];
	[self.backButton setTitleColor:[UIColor colorWithRGB:0x999999] forState:UIControlStateNormal];
	
	
	self.beDefaultButton = [UIButton new];
	[self.beDefaultButton setTitle:@"设为默认地址" forState:UIControlStateNormal];
	[self.beDefaultButton setImage:[UIImage imageNamed:@"focus_button_normal"] forState:UIControlStateNormal];
	[self.beDefaultButton setImage:[UIImage imageNamed:@"focus_button_selected"] forState:UIControlStateSelected];
	[self.beDefaultButton setTitleColor:[UIColor colorWithRGB:0x666666] forState:UIControlStateNormal];
	self.beDefaultButton.titleLabel.font = [UIFont systemFontOfSize:13];
	[self.beDefaultButton setTitleEdgeInsets:UIEdgeInsetsMake(0, 5, 0, -5)];
	[self addSubview:self.beDefaultButton];
	
	self.topLineView = [UIView new];
	self.topLineView.backgroundColor = [UIColor colorWithRGB:0xcccccc];
	[self addSubview:self.topLineView];
	
	
	[self.frontButton addTarget:self action:@selector(click:) forControlEvents:UIControlEventTouchUpInside];
	[self.backButton addTarget:self action:@selector(click:) forControlEvents:UIControlEventTouchUpInside];
	[self.beDefaultButton addTarget:self action:@selector(click:) forControlEvents:UIControlEventTouchUpInside];
	
	
	
	[self addSubview:self.titleLabel];
	[self addSubview:self.desLabel];
	[self addSubview:self.frontButton];
	[self addSubview:self.backButton];
	[self.frontButton addSubview:self.frontImageView];
	[self.backButton addSubview:self.backImageView];
	
	self.hasInfo = YES;
}

- (void)updateConstraints
{
	[self.titleLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(15);
		make.left.mas_equalTo(40);
	}];
	
	[self.desLabel mas_updateConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(10);
		make.left.mas_equalTo(40);
		make.right.mas_equalTo(-20);
	}];
	
	[self.frontButton mas_remakeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		if (self.hasInfo) {
			make.top.mas_equalTo(self.desLabel.mas_bottom).offset(10);
		} else {
			make.top.mas_equalTo(15);
		}
		make.height.mas_equalTo(self.frontImageView.mas_width).multipliedBy(190./326.);
	}];
	
	[self.backButton mas_updateConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(self.frontImageView.mas_top);
		make.right.mas_equalTo(-20);
		make.left.mas_equalTo(self.frontImageView.mas_right).offset(10);
		make.width.mas_equalTo(self.frontImageView.mas_width);
		make.height.mas_equalTo(self.frontImageView.mas_height);
	}];
	
	[self.frontImageView mas_updateConstraints:^(MASConstraintMaker *make) {
		make.edges.mas_equalTo(0);
	}];
	
	[self.backImageView mas_updateConstraints:^(MASConstraintMaker *make) {
		make.edges.mas_equalTo(0);
	}];
	
	[self.beDefaultButton mas_updateConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(self.frontImageView.mas_bottom).offset(15);
		make.left.mas_equalTo(20);
		make.bottom.mas_equalTo(0);
	}];
	
	[self.topLineView mas_updateConstraints:^(MASConstraintMaker *make) {
		make.top.left.right.mas_equalTo(0);
		make.height.mas_equalTo(0.3);
	}];
	
	[super updateConstraints];
}

- (void)setHasInfo:(BOOL)hasInfo
{
	_hasInfo = hasInfo;
	self.titleLabel.hidden = !hasInfo;
	self.desLabel.hidden = !hasInfo;
	[self setNeedsUpdateConstraints];
	[self updateConstraintsIfNeeded];
}

- (void)setHasDefaultBtn:(BOOL)hasDefaultBtn
{
	_hasDefaultBtn = hasDefaultBtn;
	self.beDefaultButton.hidden = !hasDefaultBtn;
}

- (void)setIsDefault:(BOOL)isDefault
{
	_isDefault = isDefault;
	self.beDefaultButton.selected = isDefault;
}

- (void)click:(UIButton *)sender
{
	if (!self.clickOn) return;
	
	YJUploadPicViewButton type = 10;
	if ([sender isEqual:self.frontButton]) {
		type = YJUploadPicViewFrontButton;
	} else if ([sender isEqual:self.backButton]) {
		type = YJUploadPicViewBackButton;
	} else if ([sender isEqual:self.beDefaultButton]) {
		type = YJUploadPicViewBeDefaultButton;
	}
	
	self.clickOn(type);
}

- (void)beginLoadImage:(id (^)(UIImageView *, UIImageView *))beginLoadImage
{
	if (!beginLoadImage) return;
	
	id re = beginLoadImage(self.frontImageView, self.backImageView);
	
	if ([re isKindOfClass:[NSArray class]]) {
		
	} else if ([re isEqual:self.frontImageView]) {
		
	} else if ([re isEqual:self.backImageView]) {
		
	}
	
}

- (void)endLoadImage:(id (^)(UIImageView *, UIImageView *))endLoadImage
{
	if (!endLoadImage) return;
	
	id re = endLoadImage(self.frontImageView, self.backImageView);
	
	if ([re isKindOfClass:[NSArray class]]) {
		
	} else if ([re isEqual:self.frontImageView]) {
		
	} else if ([re isEqual:self.backImageView]) {
		
	}
}

- (UIImage *)frontImage
{
	return self.frontImageView.image;
}

- (UIImage *)backImage
{
	return self.backImageView.image;
}

@end







