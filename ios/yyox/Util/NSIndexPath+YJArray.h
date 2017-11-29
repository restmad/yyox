//
//  NSIndexPath+YJArray.h
//  yyox
//
//  Created by ddn on 2017/1/3.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSIndexPath (YJArray)

+ (NSArray *)indexPathsForCells:(NSRange)range inSection:(NSInteger)section;

+ (NSArray *)indexPathsForIndextSet:(NSIndexSet *)indexSet inSection:(NSInteger)section;

@end
