//
//  YJRegexte.h
//  yyox
//
//  Created by ddn on 2017/1/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJRegexte : NSObject

+ (BOOL)isOnlyNumber:(NSString *)num;

+ (BOOL)isTelNumber:(NSString *)tel;

+ (BOOL)isInviteNumber:(NSString *)invite;

+ (BOOL)isAuthCode:(NSString *)code;

+ (BOOL)isPassword:(NSString *)password;

+ (BOOL)isLoginPassword:(NSString *)password;

+ (BOOL)isEmail:(NSString *)email;

+ (BOOL)isCardID:(NSString *)sPaperId;

+ (BOOL)isZipcode:(NSString *)zipcode;

+ (BOOL)isChineseName:(NSString *)name;

+ (BOOL)isOnlyChinese:(NSString *)str;

+ (BOOL)hasChinese:(NSString *)str;

+ (BOOL)isMoney:(NSString *)money;

@end
