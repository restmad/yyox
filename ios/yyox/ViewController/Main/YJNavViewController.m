//
//  YJNavViewController.m
//  yyox
//
//  Created by ddn on 2016/12/26.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJNavViewController.h"
#import "UIBarButtonItem+YJExtension.h"
#import "yyox-Swift.h"
#import "YJLoginViewController.h"

@interface YJNavViewController () <UIGestureRecognizerDelegate>

@end

@implementation YJNavViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
	
	//全屏pop
//	id target = self.interactivePopGestureRecognizer.delegate;
//	SEL handleNavigationTransition = NSSelectorFromString(@"handleNavigationTransition:");
//	UIPanGestureRecognizer *popGesture = [[UIPanGestureRecognizer alloc]initWithTarget:target action:handleNavigationTransition];
//	popGesture.delegate = self;
//	[self.view addGestureRecognizer:popGesture];
//	self.interactivePopGestureRecognizer.enabled = NO;
	
	//边缘pop
//	self.interactivePopGestureRecognizer.delegate = self;
//	
//	self.delegate = self;
}

//- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
//{
//	return self.viewControllers.count - 1;
//}

- (BOOL)requestLoginToVc:(UIViewController *)vc
{
	NSInteger count = self.viewControllers.count;
	if (count != 1) return NO;
	BOOL login = [YJEnterControllerManager isLogin];
	if (login) return NO;
	UIViewController *topVc = self.topViewController;
	if ([topVc isKindOfClass:[YJShopViewController class]]) return NO;
	if ([vc isKindOfClass:[YJLoginViewController class]]) return NO;
	if ([vc isKindOfClass:NSClassFromString(@"KFChatViewController")]) return NO;
	if ([vc isKindOfClass:NSClassFromString(@"YJBaseWevViewController")]) return NO;
	return YES;
}

- (void)pushViewController:(UIViewController *)viewController animated:(BOOL)animated
{
	if (!viewController) return;
	
	NSInteger count = self.viewControllers.count;
	viewController.hidesBottomBarWhenPushed = count > 0;
	
	if ([self requestLoginToVc:viewController]) {
		[UIAlertController showTitle:@"提示" message:@"您还未登录，请先登录" cancel:@"下次" sure:@"登录" byController:self callback:^{
			YJLoginViewController *loginVc = (YJLoginViewController *)[YJStoryboard storyboardInstanceWithIdentify:YJStoryboardIdentifyLogin];
			[self pushViewController:loginVc animated:YES];
		}];
		return;
	}
	
	[super pushViewController:viewController animated:animated];

	if (count == 0 && ([viewController isKindOfClass:NSClassFromString(@"YJServiceViewController")])) {
		[self setNavigationBarHidden:YES animated:YES];
	} else {
		[self setNavigationBarHidden:NO animated:YES];
	}
	if (count != 0) {
		UIBarButtonItem *backItem = [UIBarButtonItem customView:@"nav_back" title:@"返回" withTarget:self action:@selector(pop)];
		viewController.navigationItem.leftBarButtonItem = backItem;
	}
}

- (NSArray<UIViewController *> *)popToViewController:(UIViewController *)viewController animated:(BOOL)animated
{
	if (!viewController) return nil;
	
	NSArray *vcs = [super popToViewController:viewController animated:animated];
	
	[self switchHiddenNavBar];
	
	return vcs;
}

- (UIViewController *)popViewControllerAnimated:(BOOL)animated
{
	UIViewController *vc = [super popViewControllerAnimated:animated];
	
	[self switchHiddenNavBar];
	
	return vc;
}

- (NSArray<UIViewController *> *)popToRootViewControllerAnimated:(BOOL)animated
{
	NSArray *vcs = [super popToRootViewControllerAnimated:animated];
	
	[self switchHiddenNavBar];
	
	return vcs;
}

- (void)switchHiddenNavBar
{
	NSInteger count = self.viewControllers.count;
	if (count == 1 && ([self.topViewController isKindOfClass:NSClassFromString(@"YJServiceViewController")])) {
		[self setNavigationBarHidden:YES animated:YES];
	}
}

- (void)pop
{
	[self popViewControllerAnimated:YES];
}

- (UIViewController *)childViewControllerForStatusBarStyle
{
	return self.topViewController;
}

@end
