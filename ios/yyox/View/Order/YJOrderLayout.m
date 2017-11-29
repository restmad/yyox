//
//  YJOrderLayout.m
//  yyox
//
//  Created by ddn on 2016/12/30.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJOrderLayout.h"

@implementation YJOrderLayout

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.minimumLineSpacing = 1;
	}
	return self;
}

- (void)prepareLayout
{
	[super prepareLayout];
	self.itemSize = CGSizeMake(self.collectionView.width - self.collectionView.contentInset.left - self.collectionView.contentInset.right, 158.);
}

@end
