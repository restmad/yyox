//
//  YJBaseNetManager.h
//  Transfer
//
//  Created by ddn on 16/12/14.
//  Copyright © 2016年 张永俊. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJBaseNetManager : NSObject

@property (strong, nonatomic, readonly) AFHTTPSessionManager *httpManager;

YJSingleton_h(NetManager)

+ (NSURLSessionDataTask *)GET:(NSString *)URLString
                            parameters:(id)parameters
                              progress:(void (^)(NSProgress *downloadProgress))downloadProgress
                               success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
                               failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;

+ (NSURLSessionDataTask *)POST:(NSString *)URLString
                    parameters:(id)parameters
                      progress:(void (^)(NSProgress *))uploadProgress
                       success:(void (^)(NSURLSessionDataTask *task, id))success
                       failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;

+ (NSURLSessionDataTask *)POST:(NSString *)URLString
                             parameters:(id)parameters
              constructingBodyWithBlock:(void (^)(id <AFMultipartFormData> formData))block
                               progress:(void (^)(NSProgress *uploadProgress))uploadProgress
                                success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
                                failure:(void (^)(NSURLSessionDataTask * task, NSError *error))failure;

+ (NSURLSessionDataTask *)DELETE:(NSString *)URLString
											parameters:(id)parameters
												 success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
												 failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;

+ (NSURLSessionDataTask *)PUT:(NSString *)URLString
										 parameters:(id)parameters
												success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
											failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;

@end
