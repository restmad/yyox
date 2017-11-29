//
//  YJStatusProcessView.m
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJStatusProcessView.h"

@interface YJStatusProcessView()

@property (strong, nonatomic) NSMutableArray *circleLayers;
@property (strong, nonatomic) NSMutableArray *lineLayers;
@property (strong, nonatomic) NSMutableArray *textLayers;

@property (strong, nonatomic) CATextLayer *startLayer;
@property (strong, nonatomic) CATextLayer *endLayer;

@end

@implementation YJStatusProcessView

- (CAShapeLayer *)createCircleLayer
{
	CAShapeLayer *circleLayer = [CAShapeLayer new];
	circleLayer.bounds = CGRectMake(0, 0, 20, 20);
	
	circleLayer.path = [UIBezierPath bezierPathWithArcCenter:CGPointMake(10, 10) radius:5 startAngle:0 endAngle:M_PI * 2 clockwise:YES].CGPath;
	
	[self.layer addSublayer:circleLayer];
	
	return circleLayer;
}

- (CALayer *)createLineLayer
{
	CALayer *lineLayer = [CALayer layer];
	lineLayer.bounds = CGRectMake(0, 0, (kScreenWidth - 55 - 20 * 5)/4, 2);
	[self.layer addSublayer:lineLayer];
	return lineLayer;
}

- (CATextLayer *)createTextLayer:(NSString *)text
{
	CATextLayer *textLayer = [CATextLayer layer];
	textLayer.string = text;
	textLayer.wrapped = NO;
	textLayer.truncationMode = kCATruncationEnd;
	textLayer.fontSize = 12;
	textLayer.bounds = CGRectMake(0, 0, kScreenWidth/5, 18);
	textLayer.alignmentMode = kCAAlignmentCenter;
	textLayer.contentsScale = [UIScreen mainScreen].scale;
	[self.layer addSublayer:textLayer];
	return textLayer;
}

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		self.backgroundColor = [UIColor whiteColor];
		
		_circleLayers = [NSMutableArray arrayWithCapacity:5];
		_lineLayers = [NSMutableArray arrayWithCapacity:4];
		_textLayers = [NSMutableArray arrayWithCapacity:5];
		NSArray *texts = @[@"入库", @"出库", @"清关", @"国内配送", @"签收"];
		for (NSInteger i=0; i<5; i++) {
			[_circleLayers addObject:[self createCircleLayer]];
			[_textLayers addObject:[self createTextLayer:texts[i]]];
			if (i < 4) {
				[_lineLayers addObject:[self createLineLayer]];
			}
		}
		self.startLayer = [self createTextLayer:@" "];
		self.startLayer.anchorPoint = CGPointMake(0, 0);
		self.startLayer.alignmentMode = kCAAlignmentLeft;
		self.endLayer = [self createTextLayer:@" "];
		self.endLayer.anchorPoint = CGPointMake(1, 0);
		self.endLayer.alignmentMode = kCAAlignmentRight;
		
		[self resetColor];
	}
	return self;
}

- (void)layoutSubviews
{
	[super layoutSubviews];
	
	for (NSInteger i=0; i<5; i++) {
		CGFloat circleDis = (self.width - 65) / 4;
		CALayer *circleLayer = self.circleLayers[i];
		circleLayer.position = CGPointMake(32.5 + i * circleDis, 44);
		
		CALayer *textLayer = self.textLayers[i];
		textLayer.position = CGPointMake(circleLayer.position.x, 68);
	}
	
	for (NSInteger i=0; i<4; i++) {
		CALayer *lineLayer = self.lineLayers[i];
		CALayer *preCircleLayer = self.circleLayers[i];
		CALayer *nextCircleLayer = self.circleLayers[i + 1];
		lineLayer.position = CGPointMake((nextCircleLayer.position.x - preCircleLayer.position.x) * 0.5 + preCircleLayer.position.x, preCircleLayer.position.y);
	}
	
	self.startLayer.position = CGPointMake(25.1 - 5, 15);
	
	self.endLayer.position = CGPointMake(self.width - 25.1 + 5, 15);
}

- (void)setStart:(NSString *)start
{
	_start = start;
	self.startLayer.string = start;
}

- (void)setEnd:(NSString *)end
{
	_end = end;
	self.endLayer.string = end;
}

- (void)setOrderStatus:(YJOrderStatus)orderStatus
{
	[self resetColor];
	
	if (orderStatus == YJOrderStatusWaitIn) {
		return;
	}
	
	for (NSInteger i=0; i<orderStatus-1; i++) {
		CAShapeLayer *circleLayer = self.circleLayers[i];
		circleLayer.fillColor = [UIColor colorWithRGB:0x1e81d1].CGColor;
		
		CATextLayer *textLayer = self.textLayers[i];
		textLayer.foregroundColor = [UIColor colorWithRGB:0x1e81d1].CGColor;
		
		if (i < YJOrderStatusDone) {
			CALayer *lineLayer = self.lineLayers[i];
			lineLayer.backgroundColor = [UIColor colorWithRGB:0x1e81d1].CGColor;
		}
	}
	CAShapeLayer *circleLayer = self.circleLayers[orderStatus-1];
	circleLayer.fillColor = [UIColor colorWithRGB:0xfc9b31].CGColor;
	
	CATextLayer *textLayer = self.textLayers[orderStatus-1];
	textLayer.foregroundColor = [UIColor colorWithRGB:0xfc9b31].CGColor;
	
	if (orderStatus < YJOrderStatusDone) {
		CALayer *lineLayer = self.lineLayers[orderStatus-1];
		lineLayer.backgroundColor = [UIColor colorWithRGB:0xfc9b31].CGColor;
	}
}

- (void)resetColor
{
	[self.circleLayers setValue:(id)[UIColor colorWithRGB:0xcecece].CGColor forKeyPath:@"fillColor"];
	[self.lineLayers setValue:(id)[UIColor colorWithRGB:0xcecece].CGColor forKeyPath:@"backgroundColor"];
	[self.textLayers setValue:(id)[UIColor colorWithRGB:0x333333].CGColor forKeyPath:@"foregroundColor"];
	
	self.startLayer.foregroundColor = [UIColor colorWithRGB:0x333333].CGColor;
	self.endLayer.foregroundColor = [UIColor colorWithRGB:0x333333].CGColor;
}

@end
