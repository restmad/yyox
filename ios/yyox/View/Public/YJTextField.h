//
//  YJTextField.h
//  yyox
//
//  Created by ddn on 2017/2/15.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YJTextField : UITextField

@property (strong, nonatomic) UIImage *errorImage;

@property (assign, nonatomic) BOOL errored;

- (void)showErrorImage;
- (void)hideErrorImage;
- (BOOL)errorIsVisiable;

@end
