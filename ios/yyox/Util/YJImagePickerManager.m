//
//  YJImagePickerManager.m
//  yyox
//
//  Created by ddn on 2017/1/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJImagePickerManager.h"

@interface YJImagePickerManager() <UIImagePickerControllerDelegate, UINavigationControllerDelegate>

@property (copy, nonatomic) void(^callback)(YJRouterResponseModel *result);

@property (weak, nonatomic) UIViewController *presentVc;

@property (assign, nonatomic) CGFloat scaleSize;

@end

@implementation YJImagePickerManager

YJSingleton_m(ImagePickerManager)

+ (void)showImagePicker:(void (^)(YJRouterResponseModel *))callback fromController:(UIViewController *)vc
{
	YJImagePickerManager *mgr = [self sharedImagePickerManager];
	mgr.presentVc = vc;
	mgr.callback = callback;
	[mgr pickImage];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<NSString *,id> *)info
{
	[picker dismissViewControllerAnimated:YES completion:nil];
	
	if (![info[UIImagePickerControllerMediaType] isEqualToString:(NSString *)kUTTypeImage]) {
		YJRouterResponseModel *result = [YJRouterResponseModel new];
		result.msg = @"请选取图片";
		result.code = 1;
		if (self.callback) {
			self.callback(result);
			self.callback = nil;
		}
		return;
	}
//	BOOL ava = NO;
//	NSURL *ext = info[UIImagePickerControllerReferenceURL];
//	NSUInteger location = [ext.absoluteString.lowercaseString rangeOfString:@"ext="].location;
//	if (location != -1) {
//		NSString *type = [ext.absoluteString.lowercaseString substringFromIndex:location + 4];
//		if ([type hasPrefix:@"jpg"] || [type hasPrefix:@"jpeg"] || [type hasPrefix:@"png"] || [type hasPrefix:@"bmp"]) {
//			ava = YES;
//		}
//	}
//	
//	if (!ava) {
//		YJRouterResponseModel *result = [YJRouterResponseModel new];
//		result.msg = @"请选取图片";
//		result.code = 1;
//		if (self.callback) {
//			self.callback(result);
//			self.callback = nil;
//		}
//		return;
//	}
	
	UIImage *image = info[UIImagePickerControllerOriginalImage];
	
	if (self.callback) {
		YJRouterResponseModel *result = [YJRouterResponseModel new];
		result.msg = @"成功";
		result.code = 0;
		result.data = image;
		if (self.callback) {
			self.callback(result);
			self.callback = nil;
		}
	}
}

- (void)pickImage
{
	UIAlertController *alert = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
	
	if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
		UIAlertAction *camera = [UIAlertAction actionWithTitle:@"拍照" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
			[self showCamera];
		}];
		[alert addAction:camera];
	}
	
	if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeSavedPhotosAlbum]) {
		UIAlertAction *photo = [UIAlertAction actionWithTitle:@"相册" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
			[self showPhotoLibrary];
		}];
		[alert addAction:photo];
	}
	
	UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
		[alert dismissViewControllerAnimated:YES completion:nil];
	}];
	[alert addAction:cancel];
	
	[self.presentVc presentViewController:alert animated:YES completion:nil];
}

- (void)showCamera
{
	UIImagePickerController *picker = [UIImagePickerController new];
	picker.sourceType = UIImagePickerControllerSourceTypeCamera;
	picker.delegate = self;
	picker.showsCameraControls = YES;
	[self.presentVc presentViewController:picker animated:YES completion:nil];
}

- (void)showPhotoLibrary
{
	UIImagePickerController *picker = [UIImagePickerController new];
	picker.sourceType = UIImagePickerControllerSourceTypeSavedPhotosAlbum;
	picker.delegate = self;
	
	[self.presentVc presentViewController:picker animated:YES completion:nil];
}

+ (void)scaleImage:(UIImage *)image toSize:(CGFloat)size callback:(void (^)(YJRouterResponseModel *))callback
{
	YJImagePickerManager *mgr = [YJImagePickerManager sharedImagePickerManager];
	mgr.scaleSize = size;
	[mgr scaleWithImage:image callback:callback];
}

- (void)scaleWithImage:(UIImage *)image callback:(void (^)(YJRouterResponseModel *))callback
{
	__block CGFloat scale = [self checkImageScale:image];
	
	if (scale < 1) {
		if (scale < 0.5) {
			scale = 0.5;
		}
		
		dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
			
			UIImage *newImage;
			while (scale > 0.1) {
				@autoreleasepool {
					newImage = [self resizeImage:image scale:scale];
					CGFloat newScale = [self checkImageScale:newImage];
					
					if (newScale >= 1) {
						break;
					}
					scale -= 0.05;
				}
			}
			dispatch_async(dispatch_get_main_queue(), ^{
				if (callback) {
					YJRouterResponseModel *result = [YJRouterResponseModel new];
					if (scale <= 0.1) {
						result.msg = @"图片压缩失败，请重新上传";
						result.code = 1;
					} else {
						result.msg = @"压缩成功";
						result.code = 0;
						result.data = newImage;
					}
					callback(result);
				}
			});
		});
	} else {
		YJRouterResponseModel *result = [YJRouterResponseModel new];
		result.msg = @"压缩成功";
		result.code = 0;
		result.data = image;
		callback(result);
	}
}

- (CGFloat)checkImageScale:(UIImage *)image
{
	NSData *data = [NSKeyedArchiver archivedDataWithRootObject:image];
	CGFloat length = data.length / 1000. / 1000.;
	
	return self.scaleSize / length;
}

- (UIImage *)resizeImage:(UIImage *)image scale:(CGFloat)scale
{
	return [image yy_imageByResizeToSize:CGSizeMake(image.size.width * scale, image.size.height * scale)];
}


@end
