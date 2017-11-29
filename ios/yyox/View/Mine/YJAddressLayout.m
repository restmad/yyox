//
//  YJAddressLayout.m
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJAddressLayout.h"

@implementation YJAddressLayout

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.minimumLineSpacing = 10;
		self.minimumInteritemSpacing = 10;
	}
	return self;
}

- (void)prepareLayout
{
//	self.estimatedItemSize = CGSizeMake(self.collectionView.size.width-self.collectionView.contentInset.left-self.collectionView.contentInset.right, 200);
	self.footerReferenceSize = CGSizeMake(self.collectionView.size.width-self.collectionView.contentInset.left-self.collectionView.contentInset.right, 60);
	[super prepareLayout];
}

@end
