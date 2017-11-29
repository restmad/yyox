//
//  YJStageView.h
//  yyox
//
//  Created by ddn on 2016/12/28.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YJStageView : UIView

- (void)updateTexts:(NSInteger (^)(NSString *text, NSInteger idx))update;

- (void)setClickOn:(void (^)(NSString *text, NSInteger idx))clickOn;

@end
