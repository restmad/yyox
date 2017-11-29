//
//  YJAddressViewModel.h
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJRefreshViewModel.h"
#import "YJAddressModel.h"

@interface YJAddressViewModel : YJRefreshViewModel

- (NSIndexPath *)indexPathForDefautCell;

- (void)reloadItemAtIndexPath:(NSIndexPath *)indexPath;

@end
