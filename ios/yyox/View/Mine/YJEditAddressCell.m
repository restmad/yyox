//
//  YJEditAddressCell.m
//  yyox
//
//  Created by ddn on 2017/1/11.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJEditAddressCell.h"
#import "YJTextView.h"

@interface YJEditAddressCell() <YYKeyboardObserver, YYTextViewDelegate>

@property (strong, nonatomic) UILabel *iconLabel;
@property (strong, nonatomic) UILabel *cateLabel;
@property (strong, nonatomic) YJTextView *contentLabel;

@property (strong, nonatomic) UILabel *infoLabel;

@property (assign, nonatomic) CGFloat rightMargin;

@property (strong, nonatomic) UIImageView *errorImageView;

@property (copy, nonatomic) BOOL((^regexte)(NSString *));

@end

@implementation YJEditAddressCell

- (void)showErrorImage
{
	[self.contentLabel showErrorImage];
}
- (void)hideErrorImage
{
	[self.contentLabel hideErrorImage];
}
- (BOOL)errorIsVisiable
{
	return [self.contentLabel errorIsVisiable];
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
	self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
	if (self) {
		self.rightMargin = 20;
		[self setup];
		
		[[YYKeyboardManager defaultManager] addObserver:self];
	}
	return self;
}

- (void)setup
{
	self.iconLabel = [UILabel new];
	self.cateLabel = [UILabel new];
	self.contentLabel = [YJTextView new];
	self.contentLabel.returnKeyType = UIReturnKeyDone;
	self.infoLabel = [UILabel new];
	
	[self.contentView addSubview:self.iconLabel];
	[self.contentView addSubview:self.cateLabel];
	[self.contentView addSubview:self.contentLabel];
	[self.contentView addSubview:self.infoLabel];
	
	self.iconLabel.text = @"＊";
	self.cateLabel.text = @"姓名";
	self.contentLabel.text = @"Kim";
	self.infoLabel.text = @"请选择";
	
	self.iconLabel.textColor = [UIColor colorWithRGB:0xf88000];
	self.cateLabel.textColor = [UIColor colorWithRGB:0x666666];
	self.contentLabel.textColor = [UIColor colorWithRGB:0x999999];
	self.infoLabel.textColor = [UIColor colorWithRGB:0x999999];
	
	self.iconLabel.font = [UIFont systemFontOfSize:13];
	self.cateLabel.font = [UIFont systemFontOfSize:13];
	self.contentLabel.font = [UIFont systemFontOfSize:13];
	self.infoLabel.font = [UIFont systemFontOfSize:13];
	
	self.contentLabel.tintColor = [UIColor colorWithRGB:0x999999];
	self.contentLabel.delegate = self;
	self.infoLabel.hidden = YES;
}

- (void)setHideRegionButton:(BOOL)hideRegionButton
{
	_hideRegionButton = hideRegionButton;
	if (hideRegionButton) {
		self.infoLabel.hidden = YES;
		self.accessoryType = UITableViewCellAccessoryNone;
		self.rightMargin = 20;
	} else {
		self.infoLabel.hidden = NO;
		self.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
		self.rightMargin = 80;
	}
}

- (void)setCanEditText:(BOOL)canEditText
{
	_canEditText = canEditText;
	self.contentLabel.userInteractionEnabled = canEditText;
}

- (void)layoutSubviews
{
	[super layoutSubviews];
	
	self.iconLabel.left = 20;
	self.iconLabel.top = 0;
	self.iconLabel.height = 50;
	self.iconLabel.width = 40 - 20;
	
	self.cateLabel.left = 40;
	self.cateLabel.height = 50;
	self.cateLabel.top = 0;
	self.cateLabel.width = 115 - 40;
	
	self.contentLabel.left = 115;
	self.contentLabel.width = self.width - 115 - self.rightMargin;
	self.contentLabel.height = 50;
	self.contentLabel.top = 0;
	
	self.infoLabel.top = 0;
	self.infoLabel.height = 50;
	self.infoLabel.width = 80 - 20 - 20;
	self.infoLabel.left = self.contentView.width - 35;
	
	CGFloat h = self.contentLabel.textLayout.textBoundingSize.height;
	if (h < 50 && h > 0) {
		self.contentLabel.contentInset = UIEdgeInsetsMake((50 - h)*0.5, 0, 0, 0);
	} else {
		self.contentLabel.contentInset = UIEdgeInsetsMake((50-self.contentLabel.font.lineHeight)*0.5, 0, 0, 0);
	}
	
}

- (void)setCate:(NSString *)cate
{
	_cate = cate;
	self.cateLabel.text = cate;
	BOOL editable = ![cate isEqualToString:@"所在地区"];
	self.contentLabel.editable = editable;
	if (!editable && !self.hideRegionButton) {
		self.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
		self.infoLabel.hidden = NO;
	} else {
		self.accessoryType = UITableViewCellAccessoryNone;
		self.infoLabel.hidden = YES;
	}
	self.rightMargin = 20;
}

- (void)setContent:(NSString *)content
{
	_content = content;
	self.contentLabel.text = content;
}

- (void)setHasIcon:(BOOL)hasIcon
{
	_hasIcon = hasIcon;
	self.iconLabel.hidden = !hasIcon;
}

- (void)setPlaceHolderText:(NSString *)placeHolderText
{
	_placeHolderText = placeHolderText;
	self.contentLabel.placeholderText = placeHolderText;
}

- (void)keyboardChangedWithTransition:(YYKeyboardTransition)transition
{
	if (!self.contentLabel.isFirstResponder) return;
	
	NSTimeInterval duration = transition.animationDuration;
	
	[self animateWithKeyboardFrame:transition.toFrame InDuration:duration];
}

- (void)animateWithKeyboardFrame:(CGRect)frame InDuration:(NSTimeInterval)duration
{
	YYKeyboardManager *mgr = [YYKeyboardManager defaultManager];
	
	CGRect toFrame = [mgr convertRect:frame toView:nil];
	
	CGRect textFieldFrame = [self.contentLabel convertRect:self.contentLabel.bounds toView:nil];
	
	if (toFrame.origin.y < CGRectGetMaxY(textFieldFrame)) {
		[UIView animateWithDuration:duration animations:^{
			self.tableView.transform = CGAffineTransformMakeTranslation(0, toFrame.origin.y - CGRectGetMaxY(textFieldFrame));
		}];
	} else {
		[UIView animateWithDuration:duration animations:^{
			self.tableView.transform = CGAffineTransformIdentity;
		}];
	}
}

- (BOOL)textViewShouldBeginEditing:(YYTextView *)textView
{
	YYKeyboardManager *mgr = [YYKeyboardManager defaultManager];
	[self animateWithKeyboardFrame:mgr.keyboardFrame InDuration:0.25];
	return YES;
}

- (void)textViewDidEndEditing:(YJTextView *)textView
{
	if (self.regexte) {
		if (self.regexte(textView.text)) {
			[self.contentLabel hideErrorImage];
			textView.errored = NO;
		} else {
			[self.contentLabel showErrorImage];
			textView.errored = YES;
		}
	}
	
	if (!self.textChanged) return;
	self.textChanged(self.cateLabel.text, self.contentLabel.text);
}

- (void)textViewDidChange:(YYTextView *)textView
{
	CGFloat h = self.contentLabel.textLayout.textBoundingSize.height;
	if (h < 50 && h > 0) {
		self.contentLabel.contentInset = UIEdgeInsetsMake((50 - h)*0.5, 0, 0, 0);
	} else {
		self.contentLabel.contentInset = UIEdgeInsetsMake((50-self.contentLabel.font.lineHeight)*0.5, 0, 0, 0);
	}
	
	if (self.regexte) {
		if (self.regexte(self.contentLabel.text)) {
			[self.contentLabel hideErrorImage];
		} else {
			if ([self.contentLabel errored]) {
				[self.contentLabel showErrorImage];
			}
		}
	}
	
	if (!self.textChanged) return;
	self.textChanged(self.cateLabel.text, self.contentLabel.text);
}

- (BOOL)textView:(YYTextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
	if ([text isEqualToString:@"\n"]) {
		[textView endEditing:YES];
		return NO;
	}
	return YES;
}

- (void)dealloc
{
	[[YYKeyboardManager defaultManager] removeObserver:self];
}

@end
