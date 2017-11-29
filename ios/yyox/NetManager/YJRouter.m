//
//  YJRouter.m
//  yyox
//
//  Created by ddn on 2017/1/3.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "YJRouter.h"

#import "YJBaseNetManager.h"

@implementation YJRouter

NSString *generateUrlStr(NSString *str)
{
	if (![str hasSuffix:RegisterApi] && ![str hasSuffix:ReRegisterApi] && ![str hasSuffix:OrderStatusListApi] && ![str hasSuffix:PendingOrderApi] && ![str hasSuffix:PutPendingOrderApi] && ![str hasSuffix:WaitDoListApi] && ![str hasSuffix:PackageDetailApi] && ![str hasSuffix:CalculateOrderPayApi] && ![str hasSuffix:EditUserInfoApi] && ![str hasSuffix:ResetPasswordApi]) {
		[YJBaseNetManager sharedNetManager].httpManager.requestSerializer = [AFHTTPRequestSerializer serializer];
	} else {
		[YJBaseNetManager sharedNetManager].httpManager.requestSerializer = [AFJSONRequestSerializer serializer];
	}
	
	if ([str hasPrefix:@"http://"]) {
		return str;
	}
	return [BaseUrl stringByAppendingString:str];
}

NSString *generateMsg(id response)
{
	NSString *msgs = response[@"msgs"];
	if (![msgs isKindOfClass:[NSString class]]) {
		msgs = nil;
	}
	return msgs;
}

YJRouterResponseModel *generateResponseModel(NSString *msg, NSInteger code, id data)
{
	YJRouterResponseModel *result = [YJRouterResponseModel new];
	result.msg = msg;
	result.code = code;
	result.data = data;
	return result;
}

void sendCallback(Callback callback, YJRouterResponseModel *response)
{
	if (callback) {
		callback(response);
	}
}

void yj_sendCallback(Callback callback, NSString *msg, NSInteger code, id data)
{
	if (callback) {
		YJRouterResponseModel *result = [YJRouterResponseModel new];
		result.msg = msg;
		result.code = code;
		result.data = data;
		callback(result);
	}
}

void netError(Callback callback)
{
	yj_sendCallback(callback, @"网络错误", 500, nil);
}

void cancelError(Callback callback)
{
	yj_sendCallback(callback, @"请求取消", 998, nil);
}

void sendError(Callback callback, NSURLSessionDataTask *task, NSError *error)
{
	if ([error.userInfo[@"NSLocalizedDescription"] isEqualToString:@"cancelled"]) {
		cancelError(callback);
	} else {
		netError(callback);
	}
}

void commonModelDeal(Class modelClass, id responseObject, Callback callback)
{
	NSInteger status = [responseObject[@"status"] integerValue];
	NSString *msg = generateMsg(responseObject);
	id data = nil;
	if (status == 0) {
		if (modelClass && responseObject) {
			data = [modelClass mj_objectWithKeyValues:responseObject[@"data"]];
		}
		if (!data) {
			data = [modelClass mj_objectWithKeyValues:responseObject];
		}
		if (!data) {
			data = responseObject[@"data"];
		}
	} else if (status == 2) {
		[YJEnterControllerManager updateLogin:NO];
		data = responseObject[@"data"];
	}
	yj_sendCallback(callback, msg, status, data);
}

+ (NSURLSessionDataTask *)commonGetModel:(NSString *)api params:(id)params modelClass:(Class)modelClass callback:(Callback)callback
{
	NSString *apiUrl = generateUrlStr(api);
	
	return [YJBaseNetManager GET:apiUrl parameters:params progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
		commonModelDeal(modelClass, responseObject, callback);
	} failure:^(NSURLSessionDataTask *task, NSError *error) {
		sendError(callback, task, error);
	}];
}

+ (NSURLSessionDataTask *)commonPostModel:(NSString *)api params:(id)params modelClass:(Class)modelClass callback:(Callback)callback
{
	NSString *apiUrl = generateUrlStr(api);
	return [YJBaseNetManager POST:apiUrl parameters:params progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
		commonModelDeal(modelClass, responseObject, callback);
	} failure:^(NSURLSessionDataTask *task, NSError *error) {
		sendError(callback, task, error);
	}];
}

+ (NSURLSessionDataTask *)commonDelete:(NSString *)api params:(id)params callback:(Callback)callback
{
	NSString *apiUrl = generateUrlStr(api);
	return [YJBaseNetManager DELETE:apiUrl parameters:params success:^(NSURLSessionDataTask *task, id responseObject) {
		commonModelDeal(nil, responseObject, callback);
	} failure:^(NSURLSessionDataTask *task, NSError *error) {
		sendError(callback, task, error);
	}];
}

+ (NSURLSessionDataTask *)commonPUT:(NSString *)api params:(id)params callback:(Callback)callback
{
	NSString *apiUrl = generateUrlStr(api);
	return [YJBaseNetManager PUT:apiUrl parameters:params success:^(NSURLSessionDataTask *task, id responseObject) {
		commonModelDeal(nil, responseObject, callback);
	} failure:^(NSURLSessionDataTask *task, NSError *error) {
		sendError(callback, task, error);
	}];
}

NSDictionary *generateRefreshParams(NSInteger currentCount, NSInteger pageSize, NSDictionary *otherParams)
{
	if (pageSize == 0) {
		return otherParams;
	}
	NSMutableDictionary *dic;
	if (otherParams) {
		dic = otherParams.mutableCopy;
	} else {
		dic = [NSMutableDictionary dictionary];
	}
	dic[@"pageNo"] = @(currentCount / pageSize + 1);
	dic[@"pageNumber"] = @(pageSize);
	return dic;
}

void commonRefreshDeal(NSInteger currentCount, NSInteger pageSize, Class modelClass, id responseObj, Callback callback)
{
	NSInteger status = [responseObj[@"status"] integerValue];
	NSString *msg = generateMsg(responseObj);
	id data = nil;
	if (status == 0) {
		if (modelClass && responseObj) {
			if ([responseObj[@"data"] isKindOfClass:[NSArray class]]) {
				data = [modelClass mj_objectArrayWithKeyValuesArray:responseObj[@"data"]];
			} else {
				data = [modelClass mj_objectWithKeyValues:responseObj[@"data"]];
			}
		}
		
		if (pageSize == 0 || !responseObj[@"other"] || !responseObj[@"other"][@"totalCount"]) {
			status = 999;
		}
		else if ([responseObj[@"other"][@"totalCount"] integerValue] <= currentCount + pageSize) {
			status = 999;
		}
	}
	YJRouterResponseModel *response = generateResponseModel(msg, status, data);
	sendCallback(callback, response);
}

+ (NSURLSessionDataTask *)commonPostRefresh:(NSString *)api currentCount:(NSInteger)currentCount pageSize:(NSInteger)pageSize otherParmas:(NSDictionary *)params modelClass:(Class)modelClass callback:(Callback)callback
{
	NSString *apiUrl = generateUrlStr(api);
	
	return [YJBaseNetManager POST:apiUrl parameters:generateRefreshParams(currentCount, pageSize, params) progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
		commonRefreshDeal(currentCount, pageSize, modelClass, responseObject, callback);
	} failure:^(NSURLSessionDataTask *task, NSError *error) {
		sendError(callback, task, error);
	}];
}

+ (NSURLSessionDataTask *)commonGetRefresh:(NSString *)api currentCount:(NSInteger)currentCount pageSize:(NSInteger)pageSize otherParmas:(NSDictionary *)params modelClass:(Class)modelClass callback:(Callback)callback
{
	NSString *apiUrl = generateUrlStr(api);
	
	return [YJBaseNetManager GET:apiUrl parameters:generateRefreshParams(currentCount, pageSize, params) progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
		commonRefreshDeal(currentCount, pageSize, modelClass, responseObject, callback);
	} failure:^(NSURLSessionDataTask *task, NSError *error) {
		sendError(callback, task, error);
	}];
}

+ (NSURLSessionDataTask *)commonUpload:(NSString *)api params:(NSDictionary *)params fileParams:(NSDictionary *)fileParams progress:(void (^)(NSProgress *))progress callback:(Callback)callback
{
	NSString *apiUrl = generateUrlStr(api);
	return [YJBaseNetManager POST:apiUrl parameters:params constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
		[fileParams enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, NSData * _Nonnull obj, BOOL * _Nonnull stop) {
			[formData appendPartWithFileData:obj name:key fileName:[key stringByAppendingString:@".png"] mimeType:@"image/jpeg"];
		}];
	} progress:^(NSProgress *uploadProgress) {
		if (progress) {
			progress(uploadProgress);
		}
	} success:^(NSURLSessionDataTask *task, id responseObject) {
		yj_sendCallback(callback, @"成功", 0, responseObject[@"data"]);
	} failure:^(NSURLSessionDataTask *task, NSError *error) {
		sendError(callback, task, error);
	}];
}

+ (NSURLSessionDataTask *)commonPostForm:(NSString *)api params:(id)params modelClass:(Class)modelClass callback:(Callback)callback
{
	NSString *apiUrl = generateUrlStr(api);
	return [YJBaseNetManager POST:apiUrl parameters:params constructingBodyWithBlock:nil progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
		commonModelDeal(modelClass, responseObject, callback);
	} failure:^(NSURLSessionDataTask *task, NSError *error) {
		sendError(callback, task, error);
	}];
}

NSDictionary *generateRefreshByTimeParams(NSString *startTime, NSInteger dataSize, NSDictionary *otherParams)
{
	if (dataSize == 0) {
		return otherParams;
	}
	NSMutableDictionary *dic;
	if (otherParams) {
		dic = otherParams.mutableCopy;
	} else {
		dic = [NSMutableDictionary dictionary];
	}
	if (startTime && startTime.length > 0) {
		dic[@"startTime"] = startTime;
	}
	dic[@"pageNumber"] = @(dataSize);
	return dic;
}

void commonRefreshByTimeDeal(NSInteger dataSize, Class modelClass, id responseObj, Callback callback)
{
	NSInteger status = [responseObj[@"status"] integerValue];
	NSString *msg = generateMsg(responseObj);
	id data = nil;
	if (status == 0) {
		if (modelClass && responseObj) {
			if ([responseObj[@"data"] isKindOfClass:[NSArray class]]) {
				data = [modelClass mj_objectArrayWithKeyValuesArray:responseObj[@"data"]];
				if (!data || [data count] <= dataSize) {
					status = 999;
				}
			} else {
				data = [modelClass mj_objectWithKeyValues:responseObj[@"data"]];
			}
		}
	}
	YJRouterResponseModel *response = generateResponseModel(msg, status, data);
	sendCallback(callback, response);
}

+ (NSURLSessionDataTask *)commonGetRefreshByTime:(NSString *)api startTime:(NSString *)startTime dataSize:(NSInteger)dataSize otherParmas:(NSDictionary *)params modelClass:(Class)modelClass callback:(Callback)callback
{
	NSString *apiUrl = generateUrlStr(api);
	return [YJBaseNetManager GET:apiUrl parameters:params progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
		commonRefreshByTimeDeal(dataSize, modelClass, responseObject, callback);
	} failure:^(NSURLSessionDataTask *task, NSError *error) {
		sendError(callback, task, error);
	}];
}

@end
