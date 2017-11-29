//
//  YJPayManager.m
//  yyox
//
//  Created by ddn on 2017/2/15.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPayManager.h"
#import <AlipaySDK/AlipaySDK.h>

/*
 9000	订单支付成功
 8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
 4000	订单支付失败
 5000	重复请求
 6001	用户中途取消
 6002	网络连接出错
 6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
 其它	其它支付错误
 */
@implementation YJPayManager

+ (void)applicationOpenUrl:(NSURL *)url callback:(void(^)(YJRouterResponseModel *))callback
{
	if ([url.host isEqualToString:@"safepay"]) {
		//跳转支付宝钱包进行支付，处理支付结果
		[[AlipaySDK defaultService] processOrderWithPaymentResult:url standbyCallback:^(NSDictionary *resultDic) {
			[self dealWithPayResult:resultDic callback:callback];
		}];
	} else {
		callback = nil;
		[YJPayModel giveup];
	}
}

+ (void)recharge:(YJPayModel *)pay orderString:(NSString *)orderString callback:(void (^)(YJRouterResponseModel *))callback
{
	if (pay) [pay holdon];
	// NOTE: 调用支付结果开始支付
	[[AlipaySDK defaultService] payOrder:orderString fromScheme:@"yyox" callback:^(NSDictionary *resultDic) {
		[self dealWithPayResult:resultDic callback:callback];
	}];
}

+ (void)payOrder:(YJPayModel *)pay orderString:(NSString *)orderString callback:(void(^)(YJRouterResponseModel *))callback
{
	if (pay) [pay holdon];
	// NOTE: 调用支付结果开始支付
	[[AlipaySDK defaultService] payOrder:orderString fromScheme:@"yyox" callback:^(NSDictionary *resultDic) {
		[self dealWithPayResult:resultDic callback:callback];
	}];
}

+ (void)dealWithPayResult:(NSDictionary *)resultDic callback:(void(^)(YJRouterResponseModel *))callback
{
	YJPayModel *pay = [YJPayModel pickup];
	NSMutableDictionary *params = @{}.mutableCopy;
	NSString *api = nil;
	if (![pay.payTypeComments isEqualToString:@"充值"]) {
		NSString *orderNo = pay.orderNo;
		NSString *ss = resultDic[@"result"];
		params[@"orderNo"] = orderNo;
		if (!ss || ss.length == 0 || !orderNo || orderNo.length == 0) {
			YJRouterResponseModel *payResult = [YJRouterResponseModel new];
			payResult.code = [resultDic[@"resultStatus"] integerValue];
			payResult.msg = resultDic[@"memo"];
			payResult.data = orderNo;
			if (callback) {
				callback(payResult);
			}
			[YJPayModel giveup];
			return;
		}
		api = PaymentStatusApi;
	} else {
		NSString *ss = resultDic[@"result"];
		if (!ss || ss.length == 0) {
			YJRouterResponseModel *payResult = [YJRouterResponseModel new];
			payResult.code = [resultDic[@"resultStatus"] integerValue];
			payResult.msg = resultDic[@"memo"];
			if (callback) {
				callback(payResult);
			}
			[YJPayModel giveup];
			return;
		}
		api = RechargeVerifyApi;
	}
	
	[self checkPayResult:resultDic baseParams:params api:api callback:callback];
}

+ (void)checkPayResult:(NSDictionary *)resultDic baseParams:(NSDictionary *)p api:(NSString *)api callback:(void(^)(YJRouterResponseModel *))callback
{
	NSArray *keyValues = [(NSString *)resultDic[@"result"] componentsSeparatedByString:@"&"];
	NSMutableDictionary *dic = @{}.mutableCopy;
	for (NSString *keyValue in keyValues) {
		NSArray *kvs = [keyValue componentsSeparatedByString:@"="];
		if (kvs && kvs.count == 2) {
			dic[kvs[0]] = kvs[1];
		} else {
			NSRange range = [keyValue rangeOfString:@"="];
			dic[[keyValue substringToIndex:range.location]] = [keyValue substringFromIndex:range.location + 1];
		}
	}
	
	NSMutableDictionary *params = p.mutableCopy;
	//	params[@"alipayReturnPayment"] = dic;
	[params addEntriesFromDictionary:dic];
	
	[SVProgressHUD show];
	[YJRouter commonPostModel:api params:params modelClass:[YJPayModel class] callback:^(YJRouterResponseModel *result) {
		[SVProgressHUD dismiss];
		YJRouterResponseModel *payResult = [YJRouterResponseModel new];
		if (result.code != 0) {
			payResult.code = [resultDic[@"resultStatus"] integerValue];
			payResult.msg = resultDic[@"memo"];
		} else {
			payResult.code = [[result.data payStatus] integerValue];
			payResult.msg = [result.data msgs];
		}
		
		if (callback) {
			callback(payResult);
		}
		[YJPayModel giveup];
	}];
}

+ (NSString *)getOrderNoFromResult:(NSString *)result
{
	if (!result) return nil;
	NSString *pattern = @"out_trade_no=[(\")|(\\d)]*";
	NSRegularExpression *regex = [[NSRegularExpression alloc] initWithPattern:pattern options:0 error:nil];
	NSTextCheckingResult *regexResult = [regex firstMatchInString:result options:0 range:NSMakeRange(0, result.length)];
	if (!regexResult || regexResult.range.length == 0) return nil;
	NSString *rr = [[result substringWithRange:regexResult.range] stringByReplacingOccurrencesOfString:@"\"" withString:@""];
	if (!rr || rr.length == 0) return nil;
	return [rr substringWithRange:NSMakeRange(13, rr.length-13)];
}

@end
