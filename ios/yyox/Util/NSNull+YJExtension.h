//
//  NSNull+YJExtension.h
//  yyox
//
//  Created by ddn on 2017/1/25.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSNull (YJExtension)

- (id)objectForKeyedSubscript:(id)sub;

- (NSInteger)length;

- (NSString *)stringByTrimmingCharactersInSet:(NSCharacterSet *)set;

@end
