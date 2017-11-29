//
//  YJKeyboardViewController.m
//  yyox
//
//  Created by ddn on 2017/1/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJKeyboardViewController.h"

@interface YJKeyboardViewController () <YYKeyboardObserver>



@end

@implementation YJKeyboardViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	
	[[YYKeyboardManager defaultManager] addObserver:self];
	
	[NSDC addObserver:self selector:@selector(textFieldDidChanged:) name:UITextFieldTextDidChangeNotification object:nil];
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	if (self.autoShowKeyboard && (self.currentTextField || self.textFields.count > 0)) {
		[self.currentTextField ?: self.textFields.firstObject becomeFirstResponder];
	}
}

- (void)dealloc
{
	[[YYKeyboardManager defaultManager] removeObserver:self];
	[NSDC removeObserver:self];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
	if (![textField isEqual:self.textFields.lastObject]) {
		NSInteger idx = [self.textFields indexOfObject:textField];
		UITextField *next = self.textFields[idx + 1];
		[next becomeFirstResponder];
	}
	return YES;
}

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
	self.currentTextField = textField;
	
	YYKeyboardManager *mgr = [YYKeyboardManager defaultManager];
	[self animateWithKeyboardFrame:mgr.keyboardFrame InDuration:0.25];
}

- (void)textFieldDidChanged:(NSNotification *)notify
{
	self.currentTextField = notify.object;
	UITextField *textField = notify.object;
	[self checkTextField:textField];
}

- (BOOL)textFieldShouldEndEditing:(UITextField *)textField
{
	self.currentTextField = nil;
	
	[self checkTextField:textField];
	
	return YES;
}

- (void)checkTextField:(UITextField *)textField
{
	
}

- (void)keyboardChangedWithTransition:(YYKeyboardTransition)transition
{
	if (!self.currentTextField) return;
	
	NSTimeInterval duration = transition.animationDuration;
	
	[self animateWithKeyboardFrame:transition.toFrame InDuration:duration];
}

- (void)animateWithKeyboardFrame:(CGRect)frame InDuration:(NSTimeInterval)duration
{
	YYKeyboardManager *mgr = [YYKeyboardManager defaultManager];
	
	CGRect toFrame = [mgr convertRect:frame toView:self.view];
	
	
	CGRect textFieldFrame = [self.currentTextField convertRect:self.currentTextField.bounds toView:self.view];
	
	if (toFrame.origin.y < CGRectGetMaxY(textFieldFrame)) {
		[UIView animateWithDuration:duration animations:^{
			self.view.transform = CGAffineTransformMakeTranslation(0, toFrame.origin.y - CGRectGetMaxY(textFieldFrame));
		}];
	} else {
		[UIView animateWithDuration:duration animations:^{
			self.view.transform = CGAffineTransformIdentity;
		}];
	}
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
