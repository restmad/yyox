//
//  NSString+YJExtension.h
//  yyox
//
//  Created by ddn on 2017/1/23.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (YJExtension)

+ (NSString *)timeDistanceTo:(NSString *)time;

- (NSString *)transformToCurrency;

- (BOOL)containsOneOfStrings:(NSArray<NSString *> *)strings;

@end
