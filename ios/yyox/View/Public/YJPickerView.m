//
//  YJPickerView.m
//  yyox
//
//  Created by ddn on 2017/1/12.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPickerView.h"
#import "UIWindow+YJExtension.h"

@implementation YJPickerViewItem

@end

@interface YJPickerViewDefaultCell: UIView

@property (strong, nonatomic) UILabel *titleLabel;

@end

@implementation YJPickerViewDefaultCell

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		[self setup];
	}
	return self;
}

- (void)setup
{
	_titleLabel = [UILabel new];
	_titleLabel.font = [UIFont systemFontOfSize:12];
	_titleLabel.textColor = [UIColor colorWithRGB:0x333333];
	
	[self addSubview:_titleLabel];
	[_titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(25);
		make.top.bottom.mas_equalTo(0);
		make.right.mas_equalTo(-25);
	}];
}

@end

@interface YJPickerView() <UIPickerViewDelegate, UIPickerViewDataSource>

@property (strong, nonatomic) UIPickerView *pickerView;

@property (strong, nonatomic) NSMutableArray *data;

@end

@implementation YJPickerView

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		self.backgroundColor = [UIColor colorWithRGB:0xf4f4f4];
		[self setup];
		
		[NSDC addObserver:self selector:@selector(listenWindowClick) name:UIWindowClickOnAnimationContainer object:nil];
	}
	return self;
}

- (void)dealloc
{
	[NSDC removeObserver:self];
}

- (void)listenWindowClick
{
	[self dismiss];
}

- (void)setup
{
	UIView *topView = [UIView new];
	[self addSubview:topView];
	topView.backgroundColor = [UIColor whiteColor];
	[topView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.left.right.mas_equalTo(0);
		make.height.mas_equalTo(40);
	}];
	
	UIButton *sureBtn = [UIButton new];
	[topView addSubview:sureBtn];
	[sureBtn setTitle:@"确定" forState:UIControlStateNormal];
	[sureBtn setContentEdgeInsets:UIEdgeInsetsMake(0, 20, 0, 20)];
	sureBtn.titleLabel.font = [UIFont systemFontOfSize:13];
	[sureBtn setTitleColor:[UIColor colorWithRGB:0x1e81d1] forState:UIControlStateNormal];
	[sureBtn mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.bottom.right.mas_equalTo(0);
	}];
	[sureBtn addTarget:self action:@selector(beforeDismiss:) forControlEvents:UIControlEventTouchUpInside];
	
	UIButton *cancelBtn = [UIButton new];
	[topView addSubview:cancelBtn];
	[cancelBtn setTitle:@"取消" forState:UIControlStateNormal];
	[cancelBtn setContentEdgeInsets:UIEdgeInsetsMake(0, 20, 0, 20)];
	cancelBtn.titleLabel.font = [UIFont systemFontOfSize:13];
	[cancelBtn setTitleColor:[UIColor colorWithRGB:0x1e81d1] forState:UIControlStateNormal];
	[cancelBtn mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.bottom.left.mas_equalTo(0);
	}];
	[cancelBtn addTarget:self action:@selector(beforeDismiss:) forControlEvents:UIControlEventTouchUpInside];
	
	self.pickerView = [UIPickerView new];
	[self addSubview:self.pickerView];
	[self.pickerView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(40);
		make.left.bottom.mas_equalTo(0);
	}];
	
	self.pickerView.delegate = self;
	self.pickerView.dataSource = self;
	self.pickerView.backgroundColor = [UIColor colorWithRGB:0xf4f4f4];
	self.pickerView.showsSelectionIndicator = YES;
	
	self.componentWidth = 0;
}

- (void)didMoveToWindow
{
	[super didMoveToWindow];
	
	if (self.window) {
		for (UIView *view in self.pickerView.subviews) {
			if (view.top > 0) {
				view.backgroundColor = [UIColor colorWithRGB:0xbbbbbb];
			}
		}
	}
}

- (void)setComponentWidth:(CGFloat)componentWidth
{
	_componentWidth = componentWidth;
	if (self.componentWidth) {
		[self.pickerView mas_updateConstraints:^(MASConstraintMaker *make) {
			make.width.mas_equalTo(self.componentWidth * self.data.count);
		}];
	}
}

- (void)beforeDismiss:(UIButton *)sender
{
	if (!self.callback) return;
	NSMutableArray *values = [NSMutableArray array];
	for (NSInteger i=0; i<self.pickerView.numberOfComponents; i++) {
		NSInteger idx = [self.pickerView selectedRowInComponent:i];
		YJPickerViewItem *item = [YJPickerViewItem new];
		item.column = i;
		item.row = idx;
		[values addObject:item];
	}
	if ([sender.titleLabel.text isEqualToString:@"确定"]) {
		self.callback(values, NO);
	} else {
		self.callback(values, YES);
	}
}

- (void)show
{
	[UIAP.keyWindow endEditing:YES];
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

- (void)initialData:(NSArray *)data
{
	self.data = data.mutableCopy;
	if (self.componentWidth) {
		[self.pickerView mas_updateConstraints:^(MASConstraintMaker *make) {
			make.width.mas_equalTo(self.componentWidth * self.data.count);
		}];
		[self setNeedsLayout];
		[self layoutIfNeeded];
		
//		self.pickerView.width = self.componentWidth * (self.data.count);
	}
}

- (void)reloadWithData:(NSArray *)data
{
	self.data = data.mutableCopy;
	if (self.componentWidth) {
		[self.pickerView mas_updateConstraints:^(MASConstraintMaker *make) {
			make.width.mas_equalTo(self.componentWidth * self.data.count);
		}];
		[self setNeedsLayout];
		[self layoutIfNeeded];
		
//		self.pickerView.width = self.componentWidth * (self.data.count);
	}
	[self.pickerView reloadAllComponents];
}

- (void)reloadComponent:(NSInteger)component withData:(NSArray *)data
{
	if (!data || data.count == 0) {
		return;
	}
	if (self.data.count > component) {
		[self.data.mutableCopy replaceObjectAtIndex:component withObject:data];
		[self.pickerView reloadComponent:component];
	} else {
		[self.data appendObject:data];
		[self.pickerView reloadAllComponents];
	}
	
	if (self.componentWidth) {
		[self.pickerView mas_updateConstraints:^(MASConstraintMaker *make) {
			make.width.mas_equalTo(self.componentWidth * self.data.count);
		}];
		[self setNeedsLayout];
		[self layoutIfNeeded];
//		self.pickerView.width = self.componentWidth * (self.data.count);
	}
}

- (void)scrollToComponent:(NSInteger)component completion:(void (^)())completion
{
	if (self.data.count > component) {
		[UIView animateWithDuration:0.25 animations:^{
			[self.pickerView mas_updateConstraints:^(MASConstraintMaker *make) {
				make.left.mas_equalTo(self.mas_left).offset(-component*self.componentWidth);
			}];
			[self setNeedsLayout];
			[self layoutIfNeeded];
			
//			self.pickerView.left = -component*self.componentWidth;
		} completion:^(BOOL finished) {
			if (finished && completion) {
				completion();
			}
		}];
	}
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
	return self.data.count;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
	return [self.data[component] count];
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
	
}

- (CGFloat)pickerView:(UIPickerView *)pickerView rowHeightForComponent:(NSInteger)component
{
	return 32;
}

- (UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view
{
	if (!view) {
		YJPickerViewDefaultCell *cell = [YJPickerViewDefaultCell new];
		cell.titleLabel.text = self.data[component][row];
		view = cell;
	}
	return view;
}

- (CGFloat)pickerView:(UIPickerView *)pickerView widthForComponent:(NSInteger)component
{
	return self.componentWidth ?: pickerView.width;
}

@end







