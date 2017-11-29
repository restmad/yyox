//
//  YJRegexte.m
//  yyox
//
//  Created by ddn on 2017/1/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJRegexte.h"

@implementation YJRegexte

+ (BOOL)isTelNumber:(NSString *)tel
{
	if (!tel) return NO;
	
	NSString *MOBILE = @"^1(3[0-9]|5[0-35-9]|8[025-9])\\d{8}$";
	NSString *CM = @"^1(34[0-8]|(3[5-9]|5[017-9]|8[278])\\d)\\d{7}$";
	NSString *CU = @"^1(3[0-2]|5[256]|8[56])\\d{8}$";
	NSString *CT = @"^1((33|53|8[019])[0-9]|349)\\d{7}$";
	
	NSString *tmp = @"^[1][3,4,5,7,8][0-9]{9}$";
	
	NSPredicate *regextestmobile = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", MOBILE];
	NSPredicate *regextestcm = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", CM];
	NSPredicate *regextestcu = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", CU];
	NSPredicate *regextestct = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", CT];
	NSPredicate *regextmp = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", tmp];
	
	BOOL res1 = [regextestmobile evaluateWithObject:tel];
	BOOL res2 = [regextestcm evaluateWithObject:tel];
	BOOL res3 = [regextestcu evaluateWithObject:tel];
	BOOL res4 = [regextestct evaluateWithObject:tel];
	BOOL res5 = [regextmp evaluateWithObject:tel];
	
	return res1 || res2 || res3 || res4 || res5;
}

+ (BOOL)isInviteNumber:(NSString *)invite
{
	if (!invite) return NO;
	
	NSString *invitable = @"^[0-9]{6}$";
	
	NSPredicate *invitePredicate = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", invitable];
	
	return [invitePredicate evaluateWithObject:invite];
}

+ (BOOL)isOnlyNumber:(NSString *)num
{
	if (!num) return NO;
	NSString *numRegex = @"^[0-9]+$";
	NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", numRegex];
	return [predicate evaluateWithObject:num];
}

+ (BOOL)isAuthCode:(NSString *)code
{
	if (!code) return NO;
	
	NSString *authCode = @"^[0-9]{5}$";
	NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", authCode];
	
	return [predicate evaluateWithObject:code];
}

+ (BOOL)isPassword:(NSString *)password
{
	if (!password) return NO;
	
	NSString *passwordable = @"^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$";
	NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", passwordable];
	
	return [predicate evaluateWithObject:password];
}

+ (BOOL)isLoginPassword:(NSString *)password
{
	if (!password) return NO;
	
	
	return password.length >= 6 && password.length <= 12;
}

+ (BOOL)isEmail:(NSString *)email
{
	NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
	NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
	return [emailTest evaluateWithObject:email];
}

+ (BOOL)isCardID:(NSString *)sPaperId
{
	sPaperId = [sPaperId stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
	if ([sPaperId length] != 18)
		{
			return NO;
		}
	NSString *mmdd = @"(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8])))";
	NSString *leapMmdd = @"0229";
	NSString *year = @"(19|20)[0-9]{2}";
	NSString *leapYear = @"(19|20)(0[48]|[2468][048]|[13579][26])";
	NSString *yearMmdd = [NSString stringWithFormat:@"%@%@", year, mmdd];
	NSString *leapyearMmdd = [NSString stringWithFormat:@"%@%@", leapYear, leapMmdd];
	NSString *yyyyMmdd = [NSString stringWithFormat:@"((%@)|(%@)|(%@))", yearMmdd, leapyearMmdd, @"20000229"];
	NSString *area = @"(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|82|[7-9]1)[0-9]{4}";
	NSString *regex = [NSString stringWithFormat:@"%@%@%@", area, yyyyMmdd , @"[0-9]{3}[0-9Xx]"];
	
	NSPredicate *regexTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regex];
	if (![regexTest evaluateWithObject:sPaperId])
		{
		return NO;
		}
	int summary = ([sPaperId substringWithRange:NSMakeRange(0,1)].intValue + [sPaperId substringWithRange:NSMakeRange(10,1)].intValue) *7
	+ ([sPaperId substringWithRange:NSMakeRange(1,1)].intValue + [sPaperId substringWithRange:NSMakeRange(11,1)].intValue) *9
	+ ([sPaperId substringWithRange:NSMakeRange(2,1)].intValue + [sPaperId substringWithRange:NSMakeRange(12,1)].intValue) *10
	+ ([sPaperId substringWithRange:NSMakeRange(3,1)].intValue + [sPaperId substringWithRange:NSMakeRange(13,1)].intValue) *5
	+ ([sPaperId substringWithRange:NSMakeRange(4,1)].intValue + [sPaperId substringWithRange:NSMakeRange(14,1)].intValue) *8
	+ ([sPaperId substringWithRange:NSMakeRange(5,1)].intValue + [sPaperId substringWithRange:NSMakeRange(15,1)].intValue) *4
	+ ([sPaperId substringWithRange:NSMakeRange(6,1)].intValue + [sPaperId substringWithRange:NSMakeRange(16,1)].intValue) *2
	+ [sPaperId substringWithRange:NSMakeRange(7,1)].intValue *1 + [sPaperId substringWithRange:NSMakeRange(8,1)].intValue *6
	+ [sPaperId substringWithRange:NSMakeRange(9,1)].intValue *3;
	NSInteger remainder = summary % 11;
	NSString *checkBit = @"";
	NSString *checkString = @"10X98765432";
	checkBit = [checkString substringWithRange:NSMakeRange(remainder,1)];// 判断校验位
	return [checkBit isEqualToString:[[sPaperId substringWithRange:NSMakeRange(17,1)] uppercaseString]];
}

+ (BOOL)isZipcode:(NSString *)zipcode
{
	NSString *zipcodeRegex = @"[0-9]{6}";
	//NSString *zipcodeRegex = @"[1-9]\\d{5}(?!\\d)";
	NSPredicate *zipcodeTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", zipcodeRegex];
	return [zipcodeTest evaluateWithObject:zipcode];
}

+ (BOOL)isChineseName:(NSString *)name
{
	NSString *chineseRegex = @"[\u4e00-\u9fa5]{2,10}";
	NSPredicate *chineseTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", chineseRegex];
	return [chineseTest evaluateWithObject:name];
}

+ (BOOL)isOnlyChinese:(NSString *)str
{
	NSString *chineseRegex = @"[\u4e00-\u9fa5]+";
	NSPredicate *chineseTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", chineseRegex];
	BOOL result = [chineseTest evaluateWithObject:str];
	return result;
}

+ (BOOL)hasChinese:(NSString *)str
{
	NSString *pattern = @"[\u4e00-\u9fa5]";
	NSRegularExpression *regular = [[NSRegularExpression alloc] initWithPattern:pattern options:0 error:nil];
	NSTextCheckingResult *result = [regular firstMatchInString:str options:0 range:NSMakeRange(0, str.length)];
	return result != nil;
}

+ (BOOL)isMoney:(NSString *)money
{
	NSString *moneyRegex = @"^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$";
	NSPredicate *moneyTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", moneyRegex];
	BOOL result = [moneyTest evaluateWithObject:money];
	return result;
}

@end
