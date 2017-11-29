//
//  YJRouter.h
//  yyox
//
//  Created by ddn on 2017/1/3.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJRouterResponseModel.h"

typedef void(^Callback)(YJRouterResponseModel *result);

@interface YJRouter : NSObject

/**
 get请求，可以根据modelClass转换model

 @param api api
 @param params 参数
 @param modelClass 模型类
 @param callback 回调
 @return task
 */
+ (NSURLSessionDataTask *)commonGetModel:(NSString *)api params:(id)params modelClass:(Class)modelClass callback:(Callback)callback;
/**
 post请求，可以根据modelClass转换model
 
 @param api api
 @param params 参数
 @param modelClass 模型类
 @param callback 回调
 @return task
 */
+ (NSURLSessionDataTask *)commonPostModel:(NSString *)api params:(id)params modelClass:(Class)modelClass callback:(Callback)callback;

/**
 delete请求

 @param api api
 @param params 参数
 @param callback 回调
 @return task
 */
+ (NSURLSessionDataTask *)commonDelete:(NSString *)api params:(id)params callback:(Callback)callback;
/**
 put请求
 
 @param api api
 @param params 参数
 @param callback 回调
 @return task
 */
+ (NSURLSessionDataTask *)commonPUT:(NSString *)api params:(id)params callback:(Callback)callback;
/**
 post请求 可分页

 @param api api
 @param currentCount 当前数量
 @param pageSize 每页大小
 @param params 参数
 @param modelClass 模型类
 @param callback 回调
 @return task
 */
+ (NSURLSessionDataTask *)commonPostRefresh:(NSString *)api currentCount:(NSInteger)currentCount pageSize:(NSInteger)pageSize otherParmas:(NSDictionary *)params modelClass:(Class)modelClass callback:(Callback)callback;
/**
 get请求 可分页
 
 @param api api
 @param currentCount 当前数量
 @param pageSize 每页大小
 @param params 参数
 @param modelClass 模型类
 @param callback 回调
 @return task
 */
+ (NSURLSessionDataTask *)commonGetRefresh:(NSString *)api currentCount:(NSInteger)currentCount pageSize:(NSInteger)pageSize otherParmas:(NSDictionary *)params modelClass:(Class)modelClass callback:(Callback)callback;
/**
 上传文件

 @param api api
 @param params 参数
 @param fileParams 文件参数
 @param progress 进度条
 @param callback 回调
 @return task
 */
+ (NSURLSessionDataTask *)commonUpload:(NSString *)api params:(NSDictionary *)params fileParams:(NSDictionary *)fileParams progress:(void(^)(NSProgress *progress))progress callback:(Callback)callback;
/**
 表单请求

 @param api api
 @param params 参数
 @param modelClass 模型类
 @param callback 回调
 @return task
 */
+ (NSURLSessionDataTask *)commonPostForm:(NSString *)api params:(id)params modelClass:(Class)modelClass callback:(Callback)callback;
/**
 get 请求

 @param api api
 @param startTime 起始时间
 @param dataSize 期望数据个数
 @param params 参数
 @param modelClass 模型类
 @param callback 回调
 @return task
 */
+ (NSURLSessionDataTask *)commonGetRefreshByTime:(NSString *)api startTime:(NSString *)startTime dataSize:(NSInteger)dataSize otherParmas:(NSDictionary *)params modelClass:(Class)modelClass callback:(Callback)callback;

@end
