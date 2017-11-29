//
//  YJPendingViewController.m
//  yyox
//
//  Created by ddn on 2017/1/14.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPendingViewController.h"
#import "YJPendingViewModel.h"
#import "YJPendingCell.h"

#import "YJPredicteViewController.h"

@interface YJPendingViewController () <UICollectionViewDelegateFlowLayout, UICollectionViewDelegate, UICollectionViewDataSource>

@end

@implementation YJPendingViewController

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.refreshViewModelClass = [YJPendingViewModel class];
	}
	return self;
}

- (void)viewDidLoad {
	[super viewDidLoad];
	
	[self.refreshView reloadData];
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
	YJPendingCell *cell = (YJPendingCell *)[super collectionView:collectionView cellForItemAtIndexPath:indexPath];
	
	@weakify(self)
	[cell setClickOn:^(NSString *text) {
		@strongify(self)
		if ([text isEqualToString:@"编辑"]) {
			YJPredicteViewController *predicteVc = (YJPredicteViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyPredicte];
			[predicteVc initialWithOrder:(YJMerchandiseModel *)[[self.refreshViewModel modelForIndexPath:indexPath] valueForKeyPath:@"model"]];
			@weakify(self)
			[predicteVc setCallback:^(YJMerchandiseModel *model) {
				@strongify(self)
				[(YJPendingViewModel *)self.refreshViewModel reloadItemAtIndexPath:indexPath];
			}];
			[self.navigationController pushViewController:predicteVc animated:YES];
		} else if ([text isEqualToString:@"删除"]) {
			[UIAlertController showByController:self callback:^{
				[self.refreshViewModel removeModelAtIdx:indexPath.row success:nil];
			}];
		}
	}];
	
	return cell;
}

@end
