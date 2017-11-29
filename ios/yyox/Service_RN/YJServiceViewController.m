//
//  YJServiceViewController.m
//  yyox
//
//  Created by ddn on 2016/12/26.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJServiceViewController.h"
#import "RCTRootView.h"
#import "RCTBundleURLProvider.h"

#import "YJAppDelegate.h"


@interface YJServiceViewController ()

@end

@implementation YJServiceViewController

- (void)loadView
{
//	NSURL *jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index.ios" fallbackResource:nil];

	YJAppDelegate *appDelegate = (YJAppDelegate *)UIAP.delegate;
//	RCTRootView *rootView = [[RCTRootView alloc] initWithBundleURL:jsCodeLocation
//														moduleName:@"App"
//												 initialProperties:nil
//													 launchOptions:appDelegate.launchOptions];
	self.view = appDelegate.rnView;
}

- (void)viewDidLoad {
    [super viewDidLoad];
}



@end
