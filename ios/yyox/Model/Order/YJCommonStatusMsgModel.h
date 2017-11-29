//
//  YJCommonStatusMsgModel.h
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YJCommonStatusDetailMsgModel : NSObject

@property (copy, nonatomic) NSString *actionDateWithFormat;
@property (copy, nonatomic) NSString *history;

@end

@interface YJCommonStatusMsgModel : NSObject

@property (copy, nonatomic) NSString *url;
@property (copy, nonatomic) NSString *summary;
@property (copy, nonatomic) NSString *companyCode;
@property (copy, nonatomic) NSString *title;
@property (copy, nonatomic) NSString *customerId;

@property (copy, nonatomic) NSString *searchNo;

@property (copy, nonatomic) NSArray<YJCommonStatusDetailMsgModel *> *list;

@end
