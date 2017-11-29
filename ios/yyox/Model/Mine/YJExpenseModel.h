//
//  YJExpenseModel.h
//  yyox
//
//  Created by ddn on 2017/1/13.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJExpenseModel : NSObject <NSCoding>

@property (copy, nonatomic) NSString *FREEZE_MONEY_DATE;
@property (copy, nonatomic) NSString *STATEMENT_TYPE_NAME;
@property (copy, nonatomic) NSString *AMOUNT;

@end
