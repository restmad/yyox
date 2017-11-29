//
//  YJLargeImageViewController.m
//  yyox
//
//  Created by ddn on 2017/3/15.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJLargeImageViewController.h"

@interface YJLargeImageViewController ()

@end

@implementation YJLargeImageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	
}

- (void)setEditable:(BOOL)editable
{
	if (editable) {
		UIBarButtonItem *dele = [[UIBarButtonItem alloc] initWithTitle:@"删除" style:UIBarButtonItemStyleDone target:self action:@selector(clickOnDeleItem)];
		self.navigationItem.rightBarButtonItem = dele;
	}
}

- (void)clickOnDeleItem
{
	[self removeModelAtIndex:self.currentIndexPath.item];
}

- (void)collectionView:(UICollectionView *)collectionView willDisplayCell:(UICollectionViewCell *)cell forItemAtIndexPath:(NSIndexPath *)indexPath
{
	[super collectionView:collectionView willDisplayCell:cell forItemAtIndexPath:indexPath];
	self.title = [NSString stringWithFormat:@"%zd/%zd", indexPath.item+1, self.models.count];
}

@end
