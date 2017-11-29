//
//  UICollectionViewCell+YJAutoSize.m
//  yyox
//
//  Created by ddn on 2017/1/3.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "UICollectionViewCell+YJAutoSize.h"
#import <objc/runtime.h>

@implementation UICollectionViewCell (YJAutoSize)

- (UICollectionView *)collectionView
{
	UIResponder *responder = self;
	UICollectionView *view;
	while (responder) {
		if ([responder isKindOfClass:[UICollectionView class]]) {
			view = (UICollectionView *)responder;
			break;
		}
		else {
			responder = responder.nextResponder;
		}
	}
	return view;
}

@end

@interface UICollectionView (YJAutoSize)

@end

@implementation UICollectionView (YJAutoSize)

//UICollectionViewCell *yj_cellItem(id self, SEL _cmd, UICollectionView *collectionView, NSIndexPath *indexPath)
//{
//#pragma clang diagnostic push
//#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
//	SEL sel = NSSelectorFromString(@"yj_cellItem::");
//	if (!sel) return nil;
//	if ([self respondsToSelector:NSSelectorFromString(@"yj_cellItem::")]) {
//		UICollectionViewCell *cell = (UICollectionViewCell *)[self performSelector:NSSelectorFromString(@"yj_cellItem::") withObject:collectionView withObject:indexPath];
//		cell.collectionView = collectionView;
//		return cell;
//	}
//#pragma clang diagnostic pop
//	return nil;
//}
//
//+ (void)load
//{
//	[self swizzleInstanceMethod:@selector(setDataSource:) with:@selector(yj_setDataSource:)];
//}
//
//- (void)yj_setDataSource:(id<UICollectionViewDataSource>)dataSource
//{
//	[self yj_setDataSource:dataSource];
//	
//	Method sysMethod = class_getInstanceMethod([dataSource class], @selector(collectionView:cellForItemAtIndexPath:));
//	if (!sysMethod) {
//		YJLog(@"yj_setDataSource error");
//		return;
//	};
//	int success = class_addMethod([dataSource class], NSSelectorFromString(@"yj_cellItem::"), (IMP)yj_cellItem, method_getTypeEncoding(sysMethod));
//	if (!success) {
//		YJLog(@"class_addMethod error");
//	};
//	
//	Method yjMethod = class_getInstanceMethod([dataSource class], NSSelectorFromString(@"yj_cellItem::"));
//	method_exchangeImplementations(sysMethod, yjMethod);
//}

@end
