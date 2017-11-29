//
//  CookiesManager.h
//  Walker
//
//  Created by zhangyj on 16/1/12.
//  Copyright © 2016年 xitong. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CookiesManager : NSObject

+ (void)saveCookieWithTask:(NSURLSessionDataTask *)task;
+ (BOOL)setCookie;
+ (void)deleteCookie;

@end
