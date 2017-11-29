//
//  YJBaseNetManager.m
//  Transfer
//
//  Created by ddn on 16/12/14.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import "YJBaseNetManager.h"
#import "CookiesManager.h"

@implementation YJBaseNetManager
@synthesize httpManager;

YJSingleton_m(NetManager)

- (AFHTTPSessionManager *)httpManager
{
    if (!httpManager) {
        httpManager = [AFHTTPSessionManager manager];
			
		AFSecurityPolicy *policy = [AFSecurityPolicy policyWithPinningMode:AFSSLPinningModeNone];
		policy.allowInvalidCertificates = YES;
		policy.validatesDomainName = NO;
		httpManager.securityPolicy = policy;
		
		httpManager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"application/json", @"text/json", @"text/javascript", @"text/plain", nil];
		
		[CookiesManager setCookie];
		
		[NSDC addObserver:self selector:@selector(netStateChanged:) name:AFNetworkingTaskDidResumeNotification object:nil];
		[NSDC addObserver:self selector:@selector(netStateChanged:) name:AFNetworkingTaskDidCompleteErrorKey object:nil];
		[NSDC addObserver:self selector:@selector(netStateChanged:) name:AFNetworkingTaskDidSuspendNotification object:nil];
		[NSDC addObserver:self selector:@selector(netStateChanged:) name:AFNetworkingTaskDidCompleteNotification object:nil];
		
    }
    return httpManager;
}

- (void)netStateChanged:(NSNotification *)notify
{
	NSURLSessionDataTask *task = notify.object;
	if (task) {
		if (task.state == NSURLSessionTaskStateRunning) {
			[UIAP incrementNetworkActivityCount];
		} else {
			[UIAP decrementNetworkActivityCount];
		}
	}
}

- (void)dealloc
{
	[NSDC removeObserver:self];
}

+ (NSURLSessionDataTask *)GET:(NSString *)URLString parameters:(id)parameters progress:(void (^)(NSProgress *))downloadProgress success:(void (^)(NSURLSessionDataTask *, id))success failure:(void (^)(NSURLSessionDataTask *, NSError *))failure
{
    AFHTTPSessionManager *manager = [[self sharedNetManager] httpManager];
    return [manager GET:URLString parameters:parameters progress:downloadProgress success:success failure:failure];
}

+ (NSURLSessionDataTask *)POST:(NSString *)URLString parameters:(id)parameters progress:(void (^)(NSProgress *))uploadProgress success:(void (^)(NSURLSessionDataTask *, id))success failure:(void (^)(NSURLSessionDataTask *, NSError *))failure
{
    AFHTTPSessionManager *manager = [[self sharedNetManager] httpManager];
    return [manager POST:URLString parameters:parameters progress:uploadProgress success:success failure:failure];
}

+ (NSURLSessionDataTask *)POST:(NSString *)URLString parameters:(id)parameters constructingBodyWithBlock:(void (^)(id<AFMultipartFormData>))block progress:(void (^)(NSProgress *))uploadProgress success:(void (^)(NSURLSessionDataTask *, id))success failure:(void (^)(NSURLSessionDataTask *, NSError *))failure
{
    AFHTTPSessionManager *manager = [[self sharedNetManager] httpManager];
    return [manager POST:URLString parameters:parameters constructingBodyWithBlock:block progress:uploadProgress success:success failure:failure];
}

+ (NSURLSessionDataTask *)DELETE:(NSString *)URLString
					  parameters:(id)parameters
						 success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
						 failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure
{
	AFHTTPSessionManager *manager = [[self sharedNetManager] httpManager];
	return [manager DELETE:URLString parameters:parameters success:success failure:failure];
}

+ (NSURLSessionDataTask *)PUT:(NSString *)URLString parameters:(id)parameters success:(void (^)(NSURLSessionDataTask *, id))success failure:(void (^)(NSURLSessionDataTask *, NSError *))failure
{
	AFHTTPSessionManager *manager = [[self sharedNetManager] httpManager];
	return [manager PUT:URLString parameters:parameters success:success failure:failure];
}

@end
