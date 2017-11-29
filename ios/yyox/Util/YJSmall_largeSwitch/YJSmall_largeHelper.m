//
//  YJSmall_largeHelper.m
//  yyox
//
//  Created by ddn on 2017/3/15.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJSmall_largeHelper.h"

@implementation YJSmall_largeHelper

+ (CGPoint)calculateMaskCenterWithView:(UIImageView *)view inView:(UIView *)container
{
	CGRect absFrame = [view convertRect:view.bounds toView:container];
	return CGPointMake(CGRectGetMidX(absFrame), CGRectGetMidY(absFrame));
}

+ (CGSize)calculateMaskSizeWithView:(UIImageView *)view inView:(UIView *)container
{
	CGSize seeImageSize = CGSizeZero;
	UIImage *image = view.image;
	CGFloat imageScale = image.size.height/image.size.width;
	if (imageScale > view.height/view.width) {
		seeImageSize = CGSizeMake(view.height/imageScale, view.height);
	} else {
		seeImageSize = CGSizeMake(view.width, view.width*imageScale);
	}
	
	
	CGSize seeLargeImageSize = CGSizeZero;
	if (imageScale > (container.height-20)/(container.width-20)) {
		seeLargeImageSize = CGSizeMake((container.height-20)/imageScale, container.height-20);
	} else {
		seeLargeImageSize = CGSizeMake(container.width-20, (container.width-20)*imageScale);
	}

	return CGSizeMake(container.width*(seeImageSize.width/seeLargeImageSize.width), container.height*(seeImageSize.height/seeLargeImageSize.height));
}

@end
