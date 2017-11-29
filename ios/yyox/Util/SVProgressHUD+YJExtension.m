//
//  SVProgressHUD+YJExtension.m
//  yyox
//
//  Created by ddn on 2017/1/13.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "SVProgressHUD+YJExtension.h"

@implementation SVProgressHUD (YJExtension)

+ (void)defaultSet
{
	[SVProgressHUD setDefaultStyle:SVProgressHUDStyleDark];
	[SVProgressHUD setMinimumDismissTimeInterval:1];
	[SVProgressHUD setDefaultMaskType:SVProgressHUDMaskTypeClear];
}

+ (void)showInView:(UIView *)view
{
	[self showInView:view withStatus:nil];
}

+ (void)showInView:(UIView *)view withStatus:(NSString *)status
{
	[self showInView:view withStatus:status userEnable:NO];
}

+ (void)showInView:(UIView *)view withStatus:(NSString *)status userEnable:(BOOL)userEnable
{
	if (view) {
		SVProgressHUD *hud = [self genrateHudInView:view];
		hud.userInteractionEnabled = !userEnable;
		[hud tapGes].enabled = NO;
		[hud showProgress:-1 status:status];
	}
}

+ (void)showSuccessInView:(UIView *)view withStatus:(NSString *)status
{
	if (view) {
		SVProgressHUD *hud = [self genrateHudInView:view];
		[hud tapGes].enabled = YES;
		[hud showImage:hud.successImage status:status duration:hud.minimumDismissTimeInterval];
	}
}

+ (void)showErrorInView:(UIView *)view withStatus:(NSString *)status
{
	if (view) {
		SVProgressHUD *hud = [self genrateHudInView:view];
		[hud tapGes].enabled = YES;
		[hud showImage:hud.errorImage status:status duration:hud.minimumDismissTimeInterval];
	}
}

+ (void)showInfoInView:(UIView *)view withStatus:(NSString *)status
{
	if (view) {
		SVProgressHUD *hud = [self genrateHudInView:view];
		[hud tapGes].enabled = YES;
		[hud showImage:hud.infoImage status:status duration:hud.minimumDismissTimeInterval];
	}
}

+ (void)showMessageInView:(UIView *)view withStatus:(NSString *)status
{
	if (view) {
		SVProgressHUD *hud = [self genrateHudInView:view];
		[hud tapGes].enabled = YES;
		[hud showImage:nil status:status duration:hud.minimumDismissTimeInterval];
	}
}

+ (void)dismissFromView:(UIView *)view
{
	if (view) {
		SVProgressHUD *hud = [self genrateHudInView:view];
		[hud dismiss];
	}
}

+ (SVProgressHUD *)genrateHudInView:(UIView *)view
{
	__block SVProgressHUD *hud = nil;
	[view.subviews enumerateObjectsUsingBlock:^(__kindof UIView * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
		if ([obj isKindOfClass:[SVProgressHUD class]]) {
			hud = obj;
			*stop = YES;
		}
	}];
	if (!hud) {
		hud = [SVProgressHUD new];
		hud.defaultStyle = SVProgressHUDStyleDark;
		hud.minimumDismissTimeInterval = 3.;
		hud.defaultMaskType = SVProgressHUDMaskTypeNone;
		hud.frame = view.bounds;
		[view addSubview:hud];
		hud.userInteractionEnabled = YES;
		
		@weakify(hud)
		UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
			@strongify(hud)
			if (hud.alpha == 1) {
				[hud dismiss];
			}
		}];
		[hud addGestureRecognizer:tap];
		[hud setTapGes:tap];
	}
	return hud;
}

- (void)setTapGes:(UITapGestureRecognizer *)tap
{
	objc_setAssociatedObject(self, _cmd, tap, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (UITapGestureRecognizer *)tapGes
{
	return objc_getAssociatedObject(self, @selector(setTapGes:));
}

- (void)yj_dealloc
{
	[self yj_dealloc];
	[[NSNotificationCenter defaultCenter] removeObserver:self];
}

+ (void)initialize
{
	[self swizzleInstanceMethod:NSSelectorFromString(@"dealloc") with:@selector(yj_dealloc)];
}

@end
