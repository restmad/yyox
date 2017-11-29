//
//  YJTextView.h
//  yyox
//
//  Created by ddn on 2017/2/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <YYText/YYText.h>

@interface YJTextView : YYTextView

@property (strong, nonatomic) UIImage *errorImage;

@property (assign, nonatomic) BOOL errored;

- (void)showErrorImage;
- (void)hideErrorImage;
- (BOOL)errorIsVisiable;

@end
