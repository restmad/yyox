//
//  YJWKWebViewConfiguration+Extension.m
//  Transfer
//
//  Created by ddn on 16/12/15.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "WKWebViewConfiguration+YJExtension.h"

@implementation WKWebViewConfiguration (YJExtension)
@dynamic messageNames;

YYSYNTH_DYNAMIC_PROPERTY_OBJECT(messageNames, setMessageNames, RETAIN_NONATOMIC, NSMutableArray *)

- (WKUserScript *)jsObjFromJs:(NSString *)js
{
    if (![js isNotBlank]) return nil;
    
    WKUserScript *scriptObj = [[WKUserScript alloc]initWithSource:js injectionTime:WKUserScriptInjectionTimeAtDocumentEnd forMainFrameOnly:NO];
    return scriptObj;
}

- (WKUserScript *)jsObjFromJsFile:(NSString *)filePath
{
    if (![filePath isNotBlank] || ![NSFM fileExistsAtPath:filePath]) return nil;
    
    NSString *js = [NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:nil];
    
    return [self jsObjFromJs:js];
}

- (void)addJs:(NSString *)js
{
    WKUserScript *s = [self jsObjFromJs:js];
    if (s) {
        [self.userContentController addUserScript:s];
    }
}

- (void)addJsFromJsFile:(NSString *)filePath
{
    WKUserScript *s = [self jsObjFromJsFile:filePath];
    if (s) {
        [self.userContentController addUserScript:s];
    }
}

- (void)addMessageName:(NSString *)name
{
    [self.messageNames addObject:name];
}

- (void)addJs:(NSString *)js withMessageName:(NSString *)name
{
    if (![js isNotBlank]) return;
    
    [self addJs:js];
    [self addMessageName:name];
}

- (void)addJsFromJsFile:(NSString *)filePath withMessageName:(NSString *)name
{
    if (![filePath isNotBlank] || ![NSFM fileExistsAtPath:filePath]) return;
    
    [self addJsFromJsFile:filePath];
    [self addMessageName:name];
}

@end
