//
//  YJTextInImage.m
//  yyox
//
//  Created by ddn on 2016/12/28.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJTextInImage.h"

@interface YJTextInImage()

@property (strong, nonatomic) UILabel *inLabel;

@end

@implementation YJTextInImage

- (UILabel *)inLabel
{
	if (!_inLabel) {
		_inLabel = [UILabel new];
		_inLabel.font = [UIFont systemFontOfSize:12];
		_inLabel.textColor = [UIColor whiteColor];
		[self.imageView addSubview:_inLabel];
		_inLabel.textAlignment = NSTextAlignmentCenter;
	}
	return _inLabel;
}

- (void)updateText:(NSString *)text
{
	self.inLabel.text = text;
}

- (void)layoutSubviews
{
	[super layoutSubviews];
	self.inLabel.frame = self.imageView.bounds;
}

@end
