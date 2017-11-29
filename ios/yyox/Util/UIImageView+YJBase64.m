//
//  UIImageView+YJBase64.m
//  yyox
//
//  Created by ddn on 2017/7/11.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "UIImageView+YJBase64.h"
#import "YJBaseNetManager.h"

@implementation UIImageView (YJBase64)

- (void)setUrlStr:(NSString *)urlStr
{
	[self willChangeValueForKey:@"urlStr"];
	objc_setAssociatedObject(self, _cmd, urlStr, OBJC_ASSOCIATION_COPY_NONATOMIC);
	[self didChangeValueForKey:@"urlStr"];
}

- (NSString *)urlStr
{
	return objc_getAssociatedObject(self, @selector(setUrlStr:));
}

- (void)yj_setBase64Image:(NSString *)urlStr placeHolder:(UIImage *)placeHolder
{
	[self yj_setBase64Image:urlStr thumbnail:NO placeHolder:placeHolder];
}

- (void)yj_setBase64Image:(NSString *)urlStr thumbnail:(BOOL)thumbnail placeHolder:(UIImage *)placeHolder
{
	[self yj_setBase64Image:urlStr thumbnail:thumbnail placeHolder:placeHolder completion:nil];
}

- (void)yj_setBase64Image:(NSString *)urlStr placeHolder:(UIImage *)placeHolder completion:(void(^)(UIImage *))completion
{
	[self yj_setBase64Image:urlStr thumbnail:NO placeHolder:placeHolder completion:completion];
}

- (void)yj_setBase64Image:(NSString *)urlStr thumbnail:(BOOL)thumbnail placeHolder:(UIImage *)placeHolder completion:(void(^)(UIImage *))completion
{
	if (placeHolder) {
		self.image = placeHolder;
	}
	if (!urlStr || urlStr.length == 0) {
		return;
	}
	[self cancelTasks];
	[self setUrlStr:urlStr];
	NSMutableDictionary *params = @{}.mutableCopy;
	params[@"url"] = urlStr;
	if (thumbnail) {
		params[@"isThumbnail"] = @"y";
	}
	NSURLSessionDataTask *task = [YJRouter commonGetModel:AmazonUrl params:params modelClass:nil callback:^(YJRouterResponseModel *result) {
		if (result.data && [result.data isKindOfClass:[NSString class]]) {
			NSData *data = [NSData dataWithBase64EncodedString:result.data];
			UIImage *image = [UIImage imageWithData:data];
			dispatch_async(dispatch_get_main_queue(), ^{
				self.image = image;
				if (completion) {
					completion(image);
				}
			});
		} else {
			if (completion) {
				completion(nil);
			}
		}
	}];
	[self weakHoldTask:task];
}

@end
