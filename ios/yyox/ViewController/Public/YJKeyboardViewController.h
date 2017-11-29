//
//  YJKeyboardViewController.h
//  yyox
//
//  Created by ddn on 2017/1/6.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YJKeyboardViewController : UITableViewController <UITextFieldDelegate>

@property (strong, nonatomic) NSArray<UITextField *> *textFields;

@property (weak, nonatomic) UITextField *currentTextField;

@property (assign, nonatomic) BOOL shouldCheck;

@property (assign, nonatomic) BOOL autoShowKeyboard;

- (void)checkTextField:(UITextField *)textField;

@end
