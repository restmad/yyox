//
//  YJBaseWevViewController.m
//  yyox
//
//  Created by ddn on 2016/12/26.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "YJBaseWevViewController.h"
#import "YJWebViewManager.h"

@interface YJBaseWevViewController () <YJWebViewManagerDelegate>

@property (strong, nonatomic) UIProgressView *progressView;

@property (strong, nonatomic) YJWebViewManager *webViewMgr;

@end

@implementation YJBaseWevViewController

#pragma lazy

- (YJWebViewManager *)webViewMgr
{
	if (!_webViewMgr) {
		WKWebViewConfiguration *config = [WKWebViewConfiguration new];
		_webViewMgr = [YJWebViewManager managerWithConfig:config];
		_webViewMgr.delegate = self;
		UIView *webView = _webViewMgr.webView;
		[self.view addSubview:webView];
		
		[webView mas_updateConstraints:^(MASConstraintMaker *make) {
			make.edges.mas_equalTo(0);
		}];
	}
	return _webViewMgr;
}

- (UIProgressView *)progressView
{
	if (!_progressView) {
		_progressView = [[UIProgressView alloc] initWithProgressViewStyle:UIProgressViewStyleDefault];
		[self.view addSubview:_progressView];
		[_progressView mas_makeConstraints:^(MASConstraintMaker *make) {
			make.top.left.right.mas_equalTo(0);
			make.height.mas_equalTo(3);
		}];
	}
	return _progressView;
}

- (void)viewDidLoad {
	[super viewDidLoad];
	
	[self.webViewMgr loadURL:self.startUrl];
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
}

#pragma mark - webViewManagerDelegate
- (void)webView:(WKWebView *)webView didFinishWithError:(NSError *)error
{
	NSLog(@"error:%@",error);
}

- (void)webView:(WKWebView *)webView valueChanged:(NSDictionary *)change forKeyPath:(YJWebViewKeypath)keyPath
{
	if (keyPath == YJWebViewKeypathProgress) {
		self.progressView.progress = [change[@"new"] floatValue];
	} else if (keyPath == YJWebViewKeypathLoading) {
		self.progressView.hidden = ![change[@"new"] boolValue];
	}
}

@end
