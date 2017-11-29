//
//  YJiCarouselViewModel.h
//  yyox
//
//  Created by ddn on 2016/12/27.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YJiCarouselModel.h"


@protocol YJiCarouselViewModelDelegate <NSObject>

@optional
- (void)carousel:(iCarousel *)carousel didSelectItemAtIndex:(NSInteger)index;
- (void)carousel:(iCarousel *)carousel currentItemIndexDidChange:(NSInteger)idx;
- (UIView *)carousel:(iCarousel *)carousel viewForItemAtIndex:(NSInteger)index reusingView:(UIView *)view;

@end

@interface YJiCarouselViewModel : NSObject

@property (strong, nonatomic) NSArray<YJiCarouselModel *> *datas;

@property (weak, nonatomic) id<YJiCarouselViewModelDelegate> delegate;

@property (assign, nonatomic) BOOL autoScroll;

@property (assign, nonatomic) iCarouselType type;

@property (weak, nonatomic) iCarousel *carousel;

@end
