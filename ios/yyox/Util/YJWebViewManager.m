//
//  YJWebViewManager.m
//  Transfer
//
//  Created by ddn on 16/12/15.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "YJWebViewManager.h"

@interface YJWebViewManager() <WKNavigationDelegate, WKScriptMessageHandler, WKUIDelegate>

@property (copy, nonatomic) WKWebViewConfiguration *config;
@property (strong, nonatomic) WKWebView *webView;

@end

@implementation YJWebViewManager
{
	struct {
		unsigned int delegateValueChanged : 1;
		unsigned int delegateDidReceivedMessage : 1;
		unsigned int delegateDecidePolicyForNavigationAction : 1;
		unsigned int delegateDidFinishWithError : 1;
		unsigned int delegateWebViewDidStartLoad : 1;
	} _delegateFlags;
}

+ (instancetype)managerWithConfig:(WKWebViewConfiguration *)config
{
    YJWebViewManager *manager = [self new];
    manager.config = config;
    
    if (config.messageNames) {
        for (NSString *name in config.messageNames) {
            [config.userContentController addScriptMessageHandler:manager name:name];
        }
        [config.messageNames removeAllObjects];
    }
    
    manager.webView = [[WKWebView alloc] initWithFrame:CGRectZero configuration:config];
    manager.webView.navigationDelegate = manager;
    manager.webView.UIDelegate = manager;
    manager.webView.allowsBackForwardNavigationGestures = YES;
    
    [manager.webView addObserver:manager forKeyPath:@"loading" options:0x01 context:nil];
    [manager.webView addObserver:manager forKeyPath:@"title" options:0x01 context:nil];
    [manager.webView addObserver:manager forKeyPath:@"estimatedProgress" options:0x01 context:nil];
    
    return manager;
}

- (void)loadURL:(NSURL *)url
{
	if (!url) return;
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    [self.webView loadRequest:request];
}

- (BOOL)goBack
{
    if (self.webView.canGoBack) {
        [self.webView goBack];
    }
    return self.webView.canGoBack;
}

- (BOOL)goForward
{
    if (self.webView.canGoForward) {
        [self.webView goForward];
    }
    return self.webView.canGoForward;
}

- (void)stopLoading
{
    [self.webView stopLoading];
}

- (void)reload
{
    [self.webView reload];
}

- (void)setDelegate:(UIViewController<YJWebViewManagerDelegate> *)delegate
{
	_delegate = delegate;
	_delegateFlags.delegateValueChanged = [delegate respondsToSelector:@selector(webView:valueChanged:forKeyPath:)];
	_delegateFlags.delegateDidFinishWithError = [delegate respondsToSelector:@selector(webView:didFinishWithError:)];
	_delegateFlags.delegateDidReceivedMessage = [delegate respondsToSelector:@selector(webView:didReceiveMessage:)];
	_delegateFlags.delegateWebViewDidStartLoad = [delegate respondsToSelector:@selector(webViewDidStartLoad:)];
	_delegateFlags.delegateDecidePolicyForNavigationAction = [delegate respondsToSelector:@selector(webView:decidePolicyForNavigationAction:decisionHandler:)];
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSString *,id> *)change context:(void *)context
{
    if (_delegateFlags.delegateValueChanged) {
        [self.delegate webView:self.webView valueChanged:change forKeyPath:[self transformKeyPathToKeypathType:keyPath]];
    }
}

- (YJWebViewKeypath)transformKeyPathToKeypathType:(NSString *)keyPath
{
	if ([keyPath isEqualToString:@"loading"]) {
		return YJWebViewKeypathLoading;
	} else if ([keyPath isEqualToString:@"title"]) {
		return YJWebViewKeypathTitle;
	} else if ([keyPath isEqualToString:@"estimatedProgress"]) {
		return YJWebViewKeypathProgress;
	}
	return YJWebViewKeypathNone;
}

- (NSString *)transformKeypathTypeToKeyPath:(YJWebViewKeypath)keypathType
{
	switch (keypathType) {
		case 0:
			return nil;
		case 1:
			return @"loading";
		case 2:
			return @"title";
		case 3:
			return @"estimatedProgress";
		default:
			return nil;
	}
}

- (void)dealloc
{
    [self.webView removeObserver:self forKeyPath:@"loading"];
    [self.webView removeObserver:self forKeyPath:@"title"];
    [self.webView removeObserver:self forKeyPath:@"estimatedProgress"];
}

#pragma mark - WKUserScriptMessageHandler
- (void)userContentController:(WKUserContentController *)userContentController didReceiveScriptMessage:(WKScriptMessage *)message
{
    if (_delegateFlags.delegateDidReceivedMessage) {
        [self.delegate webView:self.webView didReceiveMessage:message];
    }
}

#pragma mark - WKNavigationDelegate
- (void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler
{
    if (_delegateFlags.delegateDecidePolicyForNavigationAction) {
        [self.delegate webView:self.webView decidePolicyForNavigationAction:navigationAction decisionHandler:decisionHandler];
    } else {
        //如果请求的动作是跨域的
//        if (navigationAction.navigationType == WKNavigationTypeLinkActivated) {
//            [webView loadRequest:navigationAction.request];
//            decisionHandler(WKNavigationActionPolicyCancel);
//        }else {
		NSLog(@"navAct:%@", navigationAction.request.URL);
//		if ([navigationAction.request.URL.absoluteString isEqualToString:@"https://www.amazon.com/a/addresses/add?ref=ya_address_book_add_button"]) {
//			[webView.configuration addJsFromJsFile:[[NSBundle mainBundle] pathForResource:@"re.js" ofType:nil]];
//		}
            decisionHandler(WKNavigationActionPolicyAllow);
//        }
    }
}

- (void)webView:(WKWebView *)webView didStartProvisionalNavigation:(WKNavigation *)navigation
{
    if (_delegateFlags.delegateWebViewDidStartLoad) {
        [self.delegate webViewDidStartLoad:self.webView];
    }
}

- (void)webView:(WKWebView *)webView didFinishNavigation:(WKNavigation *)navigation
{
//	    NSString *meta = @"document.documentElement.innerHTML";
//	    [self.webView evaluateJavaScript:meta completionHandler:^(id _Nullable returnObj, NSError * _Nullable error) {
//	        NSLog(@"网页源码:%@,错误:%@",returnObj, error);
//	    }];
    if (_delegateFlags.delegateDidFinishWithError) {
        [self.delegate webView:self.webView didFinishWithError:nil];
    }
}

- (void)webView:(WKWebView *)webView didFailNavigation:(WKNavigation *)navigation withError:(NSError *)error
{
    if (_delegateFlags.delegateDidFinishWithError) {
        [self.delegate webView:self.webView didFinishWithError:error];
    }
}

#pragma mark - WKUIDelegate
- (WKWebView *)webView:(WKWebView *)webView createWebViewWithConfiguration:(nonnull WKWebViewConfiguration *)configuration forNavigationAction:(nonnull WKNavigationAction *)navigationAction windowFeatures:(nonnull WKWindowFeatures *)windowFeatures
{
    //接口的作用是打开新窗口委托
    return nil;
}
//弹出警告框
- (void)webView:(WKWebView *)webView runJavaScriptAlertPanelWithMessage:(NSString *)message initiatedByFrame:(WKFrameInfo *)frame completionHandler:(void (^)(void))completionHandler
{
    
    if (!self.webView.viewController) return;
    
    // js 里面的alert实现，如果不实现，网页的alert函数无效
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:message
                                                                             message:nil
                                                                      preferredStyle:UIAlertControllerStyleAlert];
    [alertController addAction:[UIAlertAction actionWithTitle:@"确定"
                                                        style:UIAlertActionStyleCancel
                                                      handler:^(UIAlertAction *action) {
                                                          completionHandler();
                                                      }]];
    
    [self.webView.viewController presentViewController:alertController animated:YES completion:^{}];
}


//弹出确认框
- (void)webView:(WKWebView *)webView runJavaScriptConfirmPanelWithMessage:(nonnull NSString *)message initiatedByFrame:(nonnull WKFrameInfo *)frame completionHandler:(nonnull void (^)(BOOL))completionHandler
{
    
    if (!self.webView.viewController) return;
    
    // js 里面的alert实现，如果不实现，网页的alert函数无效
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:message
                                                                             message:nil
                                                                      preferredStyle:UIAlertControllerStyleAlert];
    [alertController addAction:[UIAlertAction actionWithTitle:@"确定"
                                                        style:UIAlertActionStyleDefault
                                                      handler:^(UIAlertAction *action) {
                                                          completionHandler(YES);
                                                      }]];
    [alertController addAction:[UIAlertAction actionWithTitle:@"取消"
                                                        style:UIAlertActionStyleCancel
                                                      handler:^(UIAlertAction *action){
                                                          completionHandler(NO);
                                                      }]];
    
    [self.webView.viewController presentViewController:alertController animated:YES completion:^{}];
}

//弹出输入框
- (void)webView:(WKWebView *)webView runJavaScriptTextInputPanelWithPrompt:(nonnull NSString *)prompt defaultText:(nullable NSString *)defaultText initiatedByFrame:(nonnull WKFrameInfo *)frame completionHandler:(nonnull void (^)(NSString * _Nullable))completionHandler
{
    // js 里面的alert实现，如果不实现，网页的alert函数无效
    completionHandler(@"Client Not handler");
}


@end
