//
//  YJPickerView.h
//  yyox
//
//  Created by ddn on 2017/1/12.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YJPickerViewItem : NSObject

@property (assign, nonatomic) NSInteger column;
@property (assign, nonatomic) NSInteger row;

@end

@interface YJPickerView : UIView

@property (copy, nonatomic) void(^callback)(NSArray<YJPickerViewItem *> *items, BOOL cancel);

@property (assign, nonatomic) CGFloat componentWidth;

- (void)initialData:(NSArray *)data;
- (void)reloadWithData:(NSArray *)data;

- (void)scrollToComponent:(NSInteger)component completion:(void(^)())completion;
- (void)reloadComponent:(NSInteger)component withData:(NSArray *)data;

- (void)show;
- (void)dismiss;

@end
