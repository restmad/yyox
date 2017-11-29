//
//  CookiesManager.m
//  Walker
//
//  Created by zhangyj on 16/1/12.
//  Copyright © 2016年 xitong. All rights reserved.
//

#import "CookiesManager.h"

#define COOKIESKEY @"COOKIESKEY"
@implementation CookiesManager

+ (void)saveCookieWithTask:(NSURLSessionDataTask *)task {
//    [[NSHTTPCookieStorage sharedHTTPCookieStorage] getCookiesForTask:task completionHandler:^(NSArray<NSHTTPCookie *> * _Nullable cookies) {
//        NSData *data = [NSKeyedArchiver archivedDataWithRootObject:cookies];
//        [NSUD setObject:data forKey:COOKIESKEY];
//        [NSUD synchronize];
//    }];
    NSArray *cookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage]cookies];
    NSData *data = [NSKeyedArchiver archivedDataWithRootObject:cookies];
    [NSUD setObject:data forKey:COOKIESKEY];
    [NSUD synchronize];
}

+ (BOOL)setCookie{
    NSData *cookiesdata = [NSUD objectForKey:COOKIESKEY];
    if (cookiesdata && [cookiesdata length]) {
        NSArray *cookies = [NSKeyedUnarchiver unarchiveObjectWithData:cookiesdata];
        for (NSHTTPCookie *cookie in cookies) {
			[[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookie:cookie];
			return YES;
        }
	}
	return NO;
}

+ (void)deleteCookie{
    NSArray * cookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
    for (NSHTTPCookie *cookie in cookies){
        [[NSHTTPCookieStorage sharedHTTPCookieStorage] deleteCookie:cookie];
    }
    [NSUD removeObjectForKey:COOKIESKEY];
}

@end
