//
//  NSObject+YJWeaks.h
//  yyox
//
//  Created by ddn on 2017/7/12.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSObject (YJWeaks)

@property (strong, nonatomic) NSPointerArray *weaks;
- (void)weakHoldTask:(NSURLSessionDataTask *)task;

- (void)cancelTasks;

@end
