//
//  YJEditAddressCell.h
//  yyox
//
//  Created by ddn on 2017/1/11.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YJEditAddressCell : UITableViewCell

@property (copy, nonatomic) NSString *cate;
@property (copy, nonatomic) NSString *content;
@property (assign, nonatomic) BOOL hasIcon;
@property (copy, nonatomic) NSString *placeHolderText;

@property (weak, nonatomic) UITableView *tableView;

@property (assign, nonatomic) BOOL hideRegionButton;

@property (assign, nonatomic) BOOL canEditText;

@property (copy, nonatomic) void(^textChanged)(NSString *cate, NSString *content);

- (void)setRegexte:(BOOL(^)(NSString *target))regexte;

- (void)showErrorImage;
- (void)hideErrorImage;
- (BOOL)errorIsVisiable;

@end
