//
//  YJShareManager.m
//  yyox
//
//  Created by ddn on 2017/5/22.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJShareManager.h"

#import <ShareSDK/ShareSDK.h>
#import <ShareSDKConnector/ShareSDKConnector.h>
#import <ShareSDKUI/ShareSDKUI.h>
#import <ShareSDKExtension/ShareSDK+Extension.h>

#import "WXApi.h"

#import "YJBottomTextBtn.h"
#import "UIWindow+YJExtension.h"

@interface YJShareViewItem : NSObject

@property (strong, nonatomic) UIImage *image;
@property (copy, nonatomic) NSString *name;

@end

@implementation YJShareViewItem


@end

@interface YJShareView()

@property (strong, nonatomic) NSMutableArray *items;
@property (copy, nonatomic) void((^callback)(NSInteger));

- (void)showWithItems:(NSArray<YJShareViewItem *> *)items callback:(void(^)(NSInteger idx))callback;

@end

@implementation YJShareView

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		[NSDC addObserver:self selector:@selector(clickOnCover) name:UIWindowClickOnAnimationContainer object:nil];
	}
	return self;
}

- (NSMutableArray *)items
{
	if (!_items) {
		_items = [NSMutableArray array];
	}
	return _items;
}

- (void)clickOnCover
{
	[self dismiss];
}

- (void)dealloc
{
	[NSDC removeObserver:self];
}

- (void)showWithItems:(NSArray<YJShareViewItem *> *)items callback:(void (^)(NSInteger))callback
{
	for (NSInteger i=0; i<items.count; i++) {
		YJShareViewItem *item = items[i];
		YJBottomTextBtn *btn = [YJBottomTextBtn buttonWithText:item.name image:item.image selectedImage:nil];
		btn.fontSize = 13;
		btn.spaceScale = 30./150.;
		btn.titleLabel.font = [UIFont systemFontOfSize:13];
		[btn setTitleColor:[UIColor colorWithRGB:0x666666] forState:UIControlStateNormal];
		btn.tag = i;
		[self.items addObject:btn];
		[btn addTarget:self action:@selector(clickOn:) forControlEvents:UIControlEventTouchUpInside];
	}
	self.callback = callback;
	
	[self setupUI];
	
	[UIAP.keyWindow showWithAnimation:^(UIView *container, dispatch_block_t finishAnimate) {
		[container addSubview:self];
		self.top = container.height;
		[UIView animateWithDuration:0.15 animations:^{
			self.top = container.height - self.height;
		} completion:^(BOOL finished) {
			if (finished) {
				finishAnimate();
			};
		}];
	}];
}

- (void)dismiss
{
	[UIAP.keyWindow dismissWithAnimation:^(UIView *container, dispatch_block_t finishAnimate) {
		[UIView animateWithDuration:0.15 animations:^{
			self.top = container.height;
		} completion:^(BOOL finished) {
			if (finished) {
				[self removeFromSuperview];
				finishAnimate();
			}
		}];
	}];
}

- (void)setupUI
{
	UIView *topView = [UIView new];
	[self addSubview:topView];
	
	UILabel *topLabel = [UILabel new];
	topLabel.text = @"分享到";
	topLabel.font = [UIFont systemFontOfSize:13];
	topLabel.textColor = [UIColor colorWithRGB:0x666666];
	topLabel.textAlignment = NSTextAlignmentCenter;
	[self addSubview:topLabel];
	
	UIButton *bottomBtn = [UIButton new];
	bottomBtn.tag = -1;
	[bottomBtn setTitle:@"取消分享" forState:UIControlStateNormal];
	bottomBtn.titleLabel.font = [UIFont systemFontOfSize:13];
	bottomBtn.titleLabel.textAlignment = NSTextAlignmentCenter;
	[bottomBtn setTitleColor:[UIColor colorWithRGB:0x666666] forState:UIControlStateNormal];
	[bottomBtn addTarget:self action:@selector(clickOn:) forControlEvents:UIControlEventTouchUpInside];
	[self addSubview:bottomBtn];
	
	topLabel.frame = CGRectMake(0, 15, kScreenWidth, topLabel.font.lineHeight);
	CGFloat h=0;
	NSInteger maxCount = 4;
//	CGFloat marginLR = 35./375. * kScreenWidth;
	CGFloat space = (kScreenWidth - self.items.count * 80) / (self.items.count + 1);
	CGFloat marginLR = space;
	for (NSInteger i=0; i<self.items.count; i++) {
		UIView *v = self.items[i];
		[topView addSubview:v];
		[v sizeToFit];
		v.width = 80;
		NSInteger column = (i + maxCount) / maxCount - 1;
		v.top = column * v.height + 17 + column * 20;
		NSInteger row = (i - column * maxCount) % maxCount;
		v.left = row * v.width + marginLR + row * space;
		if (i == self.items.count - 1) {
			h = CGRectGetMaxY(v.frame) + 25;
		}
	}
	topView.frame = CGRectMake(0, CGRectGetMaxY(topLabel.frame) + 17, kScreenWidth, h);
	bottomBtn.frame = CGRectMake(0, CGRectGetMaxY(topView.frame), kScreenWidth, 50);
	self.height = topLabel.height + topView.height + bottomBtn.height + 15 + 17;
	self.left = 0;
	self.width = kScreenWidth;
	
	topView.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
}

- (void)clickOn:(UIButton *)sender
{
	if (self.callback) {
		self.callback(sender.tag);
	}
}

@end

@implementation YJShareModel

@end

@implementation YJShareManager

+ (void)initialShare
{
	[ShareSDK registerApp:SHARESDKAPPKEY activePlatforms:@[@(SSDKPlatformTypeWechat)] onImport:^(SSDKPlatformType platformType) {
		switch (platformType)
		{
			case SSDKPlatformTypeWechat:
				[ShareSDKConnector connectWeChat:[WXApi class]];
				break;
			default:
				break;
		}
	} onConfiguration:^(SSDKPlatformType platformType, NSMutableDictionary *appInfo) {
		switch (platformType)
		{
			case SSDKPlatformTypeWechat:
				[appInfo SSDKSetupWeChatByAppId:WECHATAPPID
								  appSecret:WECHATAPPSECRET];
				break;
			default:
				break;
		}
	}];
}

+ (id)showShareView:(YJShareModel *)shareModel callback:(BOOL (^)(BOOL))callback
{
	YJShareView *shareView = [YJShareView new];
	shareView.backgroundColor = [UIColor whiteColor];
	YJShareViewItem *item1 = [YJShareViewItem new];
	item1.name = @"微信";
	item1.image = [UIImage imageNamed:@"微信"];
	
	YJShareViewItem *item2 = [YJShareViewItem new];
	item2.name = @"朋友圈";
	item2.image = [UIImage imageNamed:@"朋友圈"];
	
	void(^stateChanged)(SSDKResponseState) = ^(SSDKResponseState state) {
		switch (state) {
			case SSDKResponseStateSuccess:
			{
			if (callback) {
				if (callback(YES)) {
					[shareView dismiss];
				}
			}
			break;
			}
			case SSDKResponseStateFail:
			case SSDKResponseStateCancel:
			{
			if (callback) {
				if (callback(NO)) {
					[shareView dismiss];
				}
			}
			break;
			}
			default:
				break;
		}
	};
	
	@weakify(shareView)
	[shareView showWithItems:@[item1, item2] callback:^(NSInteger idx) {
		@strongify(shareView)
		SSDKPlatformType type = 0;
		NSMutableDictionary *shareParams = @{}.mutableCopy;
		if (idx == 0) {
			type = SSDKPlatformSubTypeWechatSession;
			[shareParams SSDKSetupShareParamsByText:shareModel.content images:shareModel.images url:shareModel.url title:shareModel.title type:SSDKContentTypeWebPage];
		} else if (idx == 1) {
			type = SSDKPlatformSubTypeWechatTimeline;
			[shareParams SSDKSetupShareParamsByText:shareModel.content images:shareModel.images url:shareModel.url title:shareModel.content type:SSDKContentTypeWebPage];
		}
		if (type) {
			[shareParams SSDKEnableUseClientShare];
			[ShareSDK share:type parameters:shareParams onStateChanged:^(SSDKResponseState state, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error) {
				stateChanged(state);
			}];
		} else {
			[shareView dismiss];
		}
	}];
	
	return shareView;
}

+ (BOOL)shareable
{
	BOOL wx = [ShareSDK isClientInstalled:SSDKPlatformTypeWechat];
	return wx;
}

@end
