//
//  YJWKWebViewConfiguration+Extension.h
//  Transfer
//
//  Created by ddn on 16/12/15.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import <WebKit/WebKit.h>


//在添加到webview之前设置
@interface WKWebViewConfiguration (YJExtension)

@property (strong, nonatomic) NSMutableArray *messageNames;

- (void)addJs:(NSString *)js;

- (void)addJsFromJsFile:(NSString *)filePath;

- (void)addMessageName:(NSString *)name;

- (void)addJs:(NSString *)js withMessageName:(NSString *)name;

- (void)addJsFromJsFile:(NSString *)filePath withMessageName:(NSString *)name;

@end
