//
//  YJAdressCell.h
//  yyox
//
//  Created by ddn on 2017/1/10.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YJAddressCellModel.h"

typedef enum : NSUInteger {
	YJAddressCellTypeNone,
	YJAddressCellTypeDisclosure,
	YJAddressCellTypeSelect,
} YJAddressCellType;

@interface YJAdressCell : UICollectionViewCell

@property (copy, nonatomic) void(^clickOn)(NSString *title);

@property (weak, nonatomic) YJAddressCellModel *cellModel;

@property (assign, nonatomic) YJAddressCellType type;

@property (assign, nonatomic) BOOL isWant;

@end
