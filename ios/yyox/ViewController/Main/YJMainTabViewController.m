//
//  YJMainTableViewController.m
//  yyox
//
//  Created by ddn on 2016/12/26.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJMainTabViewController.h"
#import "YJNavViewController.h"
#import "yyox-Swift.h"
#import "YJMineViewController.h"

@interface YJMainTabViewController ()

@property (assign, nonatomic, readonly) UIImage *image;
@property (assign, nonatomic, readonly) UIImage *selectedImage;

@end

@implementation YJMainTabViewController

- (UIImage *)image
{
	return [UIImage imageWithColor:[UIColor whiteColor] size:CGSizeMake((kScreenWidth)/4, 50)];
}

- (UIImage *)selectedImage
{
	return [UIImage imageWithColor:[UIColor colorWithHexString:@"#f2f2f2"] size:CGSizeMake((kScreenWidth)/4, 50)];
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
	[self addChildViewControllers];
	
	[self globalNavSetting];
	[self gloablTabSetting];
	
	[self regisiterNofity];
}

- (void)regisiterNofity
{
	[NSDC addObserver:self selector:@selector(changeBarItem:) name:ChangeTabItemNotification object:nil];
}

- (void)changeBarItem:(NSNotification *)notify
{
	NSString *itemTitle = notify.userInfo[ChangeTabItemNotificationKey];
	[self.tabBar.items enumerateObjectsUsingBlock:^(UITabBarItem * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
		if ([obj.title isEqualToString:itemTitle]) {
			self.selectedIndex = idx;
			self.tabBar.hidden = NO;
			*stop = YES;
		}
	}];
	
}

- (void)addChildViewControllers
{
	NSString *filePath = [UIAP.documentsPath stringByAppendingPathComponent:@"vcs.json"];
	BOOL exists = [NSFM fileExistsAtPath:filePath];
	NSArray *vcs;
	NSError *error = nil;
	if (exists) {
		vcs = [NSJSONSerialization JSONObjectWithData:[NSData dataWithContentsOfFile:filePath] options:NSJSONReadingMutableContainers error:&error];
	}
	if (!error && exists) {
		for (NSDictionary *vc in vcs) {
			[self addChildViewController:vc[@"name"] tabTitle:vc[@"tabTitle"] navTitle:vc[@"navTitle"] imageName:vc[@"imageName"]];
		}
	} else {
		
		YJLog(@"error: %@", error);
		[self addChildViewController:@"YJOrder" tabTitle:@"运单" navTitle:@"邮客全球转运" imageName:@"tabbar_order"];
		[self addChildViewController:@"YJShop" tabTitle:@"海淘" navTitle:@"海淘全球购" imageName:@"tabbar_shop"];
		[self addChildViewController:@"YJService" tabTitle:@"服务" navTitle:@"邮客服务" imageName:@"tabbar_service"];
		[self addChildViewController:@"YJMine" tabTitle:@"我的" navTitle:@"我的" imageName:@"tabbar_mine"];
	}
}

- (void)addChildViewController:(NSString *)name tabTitle:(NSString *)tabTitle navTitle:(NSString *)navTitle imageName:(NSString *)imageName
{
	Class class = NSClassFromString(name);
	if (!class) class = NSClassFromString([name stringByAppendingString:@"ViewController"]);
	if (!class) {
		NSString *nameSpace = [NSBundle mainBundle].infoDictionary[@"CFBundleExecutable"];
		NSString *swiftName = [NSString stringWithFormat:@"%@.%@",nameSpace, name];
		class = NSClassFromString(swiftName);
		if (!class) {
			class = NSClassFromString([swiftName stringByAppendingString:@"ViewController"]);
		}
	}
	UIViewController *vc;
	if (!class) {
		YJLog(@"class is null: %@", name);
		if ([name containsString:@"YJShop"]) {
			YJShopViewController *shopVc = [YJShopViewController new];
			if ([BaseUrl hasSuffix:@"/"]) {
				shopVc.baseUrl = [BaseUrl substringToIndex:BaseUrl.length - 1];
			} else {
				shopVc.baseUrl = BaseUrl;
			}
			vc = shopVc;
		} else {
			return;
		}
	}
	if (!vc) {
		if ([navTitle isEqualToString:@"我的"]) {
			vc = [YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyMine];
		} else {
			vc = [class new];
		}
	}
	
	//if ([name isEqualToString:@"YJService"]) {
		//[self addChildViewController:vc];
	//} else {
		YJNavViewController *nav = [[YJNavViewController alloc] initWithRootViewController:vc];
		
		[self addChildViewController:nav];
		
		vc.title = navTitle;
	//}
	
	[self setItemImage:imageName title:tabTitle forVC:vc atIndex:self.childViewControllers.count-1];
}

- (void)setItemImage:(NSString *)imageName title:(NSString *)title forVC:(UIViewController *)vc atIndex:(NSInteger)idx
{
	UIImage *image;
	UIImage *selectedImage;
	UIEdgeInsets insets = UIEdgeInsetsZero;
	
	if (!imageName || [imageName isEqualToString:@""]) {
		image = self.tabBar.items[0].image ?: self.image;
		selectedImage = self.tabBar.items[0].selectedImage ?: self.selectedImage;
		insets = UIEdgeInsetsMake(6., 0, -6., idx%2 ? 0 : 0.5);
		
		[vc.tabBarItem setTitleTextAttributes:@{
												NSFontAttributeName: [UIFont systemFontOfSize:16],
												NSForegroundColorAttributeName: [UIColor blackColor]
												}
									 forState:UIControlStateNormal];
		[vc.tabBarItem setTitleTextAttributes:@{
												NSForegroundColorAttributeName: [UIColor colorWithHexString:@"#1b82d2"]
												}
									 forState:UIControlStateSelected];
		[vc.tabBarItem setTitlePositionAdjustment:UIOffsetMake(0, -13)];
		
	} else {
		image = [UIImage imageNamed:imageName];
		selectedImage = [UIImage imageNamed:[imageName stringByAppendingString:@"_selected"]];
	}
	
	[vc.tabBarItem setTitle:title];
	
	vc.tabBarItem.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
	vc.tabBarItem.selectedImage = [selectedImage imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
	
	vc.tabBarItem.imageInsets = insets;
}

- (void)globalNavSetting
{
	UINavigationBar *apperanceNavBar = [UINavigationBar appearanceWhenContainedIn:[YJNavViewController class], nil];
	[apperanceNavBar setTintColor:[UIColor whiteColor]];//item
	[apperanceNavBar setTitleTextAttributes:@{NSForegroundColorAttributeName : [UIColor whiteColor]}];//title
	[apperanceNavBar setBarTintColor:[UIColor colorWithRGB:0x1b82d2]];//background
	apperanceNavBar.translucent = NO;
	UIBarButtonItem *apperanceItem = [UIBarButtonItem appearanceWhenContainedIn:[YJNavViewController class], nil];
	[apperanceItem setTitleTextAttributes:@{NSFontAttributeName: [UIFont systemFontOfSize:14], NSForegroundColorAttributeName: [UIColor whiteColor]} forState:UIControlStateNormal];
}

- (void)gloablTabSetting
{
	UITabBar *apperanceTabbar = [UITabBar appearanceWhenContainedIn:[YJMainTabViewController class], nil];
	[apperanceTabbar setTintColor:[UIColor colorWithRGB:0x248df5]];//item
	[apperanceTabbar setBarTintColor:[UIColor whiteColor]];
	
}

- (void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item
{
	tabBar.hidden = [item.title isEqualToString:@"服务"];
	if ([item.title isEqualToString:@"服务"]) {
		static dispatch_once_t onceToken;
		dispatch_once(&onceToken, ^{
			item.image = [UIImage imageNamed:@"tabbar_service_unredcircle"];
		});
	}
}

- (void)dealloc
{
	[NSDC removeObserver:self];
}

@end
