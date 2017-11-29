//
//  YJCouponViewController.m
//  yyox
//
//  Created by ddn on 2017/3/27.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJCouponViewController.h"
#import "YJCouponModel.h"
#import "YJCouponCell.h"

@interface YJCouponViewController ()

@end

@implementation YJCouponViewController

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.contentInset = UIEdgeInsetsMake(10, 0, 0, 0);
		self.refreshViewModel.modelClass = [YJCouponModel class];
		self.refreshViewModel.cellClass = [YJCouponCell class];
		self.refreshViewModel.netUrl = CouponApi;
		self.refreshViewModel.netMethod = 0;
		self.refreshViewModel.footerRefreshCount = 0;
		[self.refreshViewModel setCellHeightForIndexPath:^CGFloat(NSIndexPath *indexPath) {
			return 90;
		}];
		self.autoSize = NO;
		self.title = @"优惠券";
	}
	return self;
}


/**
 拼接网络请求参数

 @param money 金额
 @param warehouseRange 仓库
 @param ordertypeRange 运输
 */
- (void)appendParams:(NSString *)money warehouse:(NSNumber *)warehouseRange transport:(NSNumber *)ordertypeRange
{
	NSDictionary *appendParams = @{@"money": money, @"warehouseRange": warehouseRange, @"ordertypeRange": ordertypeRange};
	self.refreshViewModel.baseParams = appendParams;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
	if (self.callback) {
		YJCouponModel *coupon = (YJCouponModel *)[self.refreshViewModel modelForIndexPath:indexPath];
		if ([coupon.status isEqualToString:@"CAN_USE"]) {
			self.callback((YJCouponModel *)[self.refreshViewModel modelForIndexPath:indexPath]);
			[self.navigationController popViewControllerAnimated:YES];
		}
	}
}

- (void)setCancelCallback:(void (^)())cancelCallback
{
	_cancelCallback = cancelCallback;
	self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"不使用" style:UIBarButtonItemStyleDone target:self action:@selector(clickOnCancel:)];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)clickOnCancel:(UIBarButtonItem *)sender
{
	if (self.cancelCallback) {
		self.cancelCallback();
	}
	[self.navigationController popViewControllerAnimated:YES];
}


@end
