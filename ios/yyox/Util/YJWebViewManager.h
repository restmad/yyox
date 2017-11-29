//
//  YJWebViewManager.h
//  Transfer
//
//  Created by ddn on 16/12/15.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <WebKit/WebKit.h>
#import "WKWebViewConfiguration+YJExtension.h"

typedef enum : NSUInteger {
	YJWebViewKeypathNone = 0,
	YJWebViewKeypathLoading,
	YJWebViewKeypathTitle,
	YJWebViewKeypathProgress,
} YJWebViewKeypath;

@protocol YJWebViewManagerDelegate <NSObject>

@optional
- (void)webView:(WKWebView *)webView valueChanged:(NSDictionary *)change forKeyPath:(YJWebViewKeypath)keyPath;

- (void)webView:(WKWebView *)webView didReceiveMessage:(WKScriptMessage *)message;

- (void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler;

- (void)webView:(WKWebView *)webView didFinishWithError:(NSError *)error;

- (void)webViewDidStartLoad:(WKWebView *)webView;

@end

@interface YJWebViewManager : NSObject

@property (nonatomic, readonly, copy) WKWebViewConfiguration *config;
@property (strong, nonatomic, readonly) WKWebView *webView;

@property (weak, nonatomic) UIViewController<YJWebViewManagerDelegate> *delegate;

+ (instancetype)managerWithConfig:(WKWebViewConfiguration *)config;

- (void)loadURL:(NSURL *)url;
- (BOOL)goBack;
- (BOOL)goForward;
- (void)stopLoading;
- (void)reload;

@end








