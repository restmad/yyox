//
//  YJPredicteViewController.m
//  yyox
//
//  Created by ddn on 2017/1/16.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJPredicteViewController.h"
#import "YJImagePickerManager.h"
#import "YJLineImageViews.h"

#import "YJLargeImageViewController.h"
#import "YJSwitchModel.h"

#import "YJPickerView.h"
#import "YJTextField.h"

#import "YJWarehouseModel.h"

@interface YJPredicteViewController () <YJLineImageViewsDelegate, YJSmall_LargeDelegate>

@property (weak, nonatomic) IBOutlet UITextField *locationLabel;
@property (weak, nonatomic) IBOutlet YJTextField *titleTextField;
@property (weak, nonatomic) IBOutlet YJTextField *numberTextField;
@property (weak, nonatomic) IBOutlet YJLineImageViews *imageContainer;
@property (weak, nonatomic) IBOutlet UIImageView *addImageIndicator;
@property (weak, nonatomic) IBOutlet UIButton *doneButton;

@property (strong, nonatomic) UILabel *grayLabel;

@property (strong, nonatomic) NSMutableArray *largeModels;

@property (strong, nonatomic) YJMerchandiseModel *model;

@property (strong, nonatomic) NSMutableDictionary *params;

@end

@implementation YJPredicteViewController

- (NSMutableDictionary *)params
{
	if (!_params) {
		_params = [NSMutableDictionary dictionary];
	}
	return _params;
}

- (UILabel *)grayLabel
{
	if (!_grayLabel) {
		UILabel *label = [UILabel new];
		label.text = @"（选填）最多上传3张图片";
		label.font = [UIFont systemFontOfSize:13];
		label.textColor = [UIColor colorWithRGBA:0x8888886f];
		_grayLabel = label;
		[self.imageContainer addSubview:label];
		[label mas_updateConstraints:^(MASConstraintMaker *make) {
			make.right.top.bottom.mas_equalTo(0);
		}];
	}
	return _grayLabel;
}

- (NSMutableArray *)largeModels
{
	if (!_largeModels) {
		_largeModels = [NSMutableArray array];
	}
	return _largeModels;
}

- (void)viewDidLoad {
	[super viewDidLoad];
	
	self.title = @"预报订单";
	self.view.backgroundColor = kGlobalBackgroundColor;
	self.tableView.contentInset = UIEdgeInsetsMake(-35, 0, 0, 0);
	
	self.grayLabel.hidden = NO;
	self.imageContainer.delegate = self;
	self.imageContainer.limit = 3;
	
	if (self.model) {
		self.locationLabel.text = self.model.warehouseName;
		self.titleTextField.text = self.model.nickname;
		self.numberTextField.text = self.model.carrierNo;
		
		[self.doneButton setTitle:@"保存修改" forState:UIControlStateNormal];
		[self.imageContainer reset];
		__block NSInteger count = self.model.orderSreenshot.count;
		if (count > 0) {
			[SVProgressHUD showInView:self.view];
		}
		@weakify(self)
		for (NSInteger i=0; i<self.model.orderSreenshot.count; i++) {
			
			UIImageView *imageView = [self.imageContainer addImage:[UIImage imageNamed:@"order_placeholder"]];
			
			[imageView yj_setBase64Image:self.model.orderSreenshot[i] thumbnail:NO placeHolder:[UIImage imageNamed:@"order_placeholder"] completion:^(UIImage *image) {
				count --;
				if (count == 0) {
					@strongify(self)
					[SVProgressHUD dismissFromView:self.view];
				}
			}];
		}
	} else {
		[self.doneButton setTitle:@"提交预报" forState:UIControlStateNormal];
	}
}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	if (self.textFields.count == 0) {
		
		self.textFields = @[self.titleTextField, self.numberTextField];
		[self.textFields enumerateObjectsUsingBlock:^(UITextField * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
			obj.delegate = self;
		}];
	}
}

- (void)checkTextField:(UITextField *)textField
{
	BOOL error = YES;
	
	if ([textField isEqual:self.numberTextField]) {
		
		if (!self.numberTextField.text || self.numberTextField.text.length == 0) {
		} else if ([YJRegexte hasChinese:self.numberTextField.text]) {
		} else if (self.numberTextField.text.length < 8 || self.numberTextField.text.length > 200) {
		} else {
			error = NO;
		}
		
	}
	else if ([textField isEqual:self.titleTextField]) {
		if (self.titleTextField.text.length > 100) {
		} else {
			error = NO;
		}
	}
	if (error) {
		//end edit
		if (!self.currentTextField) {
			[(YJTextField *)textField setErrored:YES];
		}
		
		if ([(YJTextField *)textField errored]) {
			[(YJTextField *)textField showErrorImage];
		}
	} else {
		//end edit
		if (!self.currentTextField) {
			[(YJTextField *)textField setErrored:NO];
		}
		
		[(YJTextField *)textField hideErrorImage];
	}
}

- (void)initialWithOrder:(YJMerchandiseModel *)model
{
	_model = model;
	
	[self setParamsWithModel:model];
}

- (void)setParamsWithModel:(YJMerchandiseModel *)model
{
	self.params[@"carrierNo"] = model.carrierNo;
	self.params[@"nickname"] = model.nickname;
	self.params[@"warehouseId"] = model.warehouseId;
	self.params[@"inventoryBasicId"] = model.id;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (indexPath.row > 3) return;
	
	cell.separatorInset = UIEdgeInsetsZero;
	cell.layoutMargins = UIEdgeInsetsZero;
	cell.preservesSuperviewLayoutMargins = NO;
}

- (IBAction)clickOnDone:(UIButton *)sender {
	[self editOrder];
}


/**
 点击完成，更新／保存信息
 */
- (void)editOrder
{
	if (!self.params[@"warehouseId"]) {
		[SVProgressHUD showInfoInView:self.view withStatus:@"请选择仓库"];
		return;
	}
	[SVProgressHUD showInView:self.view];
	if (!self.numberTextField.text || self.numberTextField.text.length == 0) {
		return [SVProgressHUD showInfoInView:self.view withStatus:@"请填写物流号"];
	} else if ([YJRegexte hasChinese:self.numberTextField.text]) {
		return [SVProgressHUD showInfoInView:self.view withStatus:@"请填写正确的物流号"];
	} else if (self.numberTextField.text.length < 8 || self.numberTextField.text.length > 200) {
		return [SVProgressHUD showInfoInView:self.view withStatus:@"请填写8-200位物流号"];
	}
	self.params[@"carrierNo"] = self.numberTextField.text ?: @"";
	if (self.titleTextField.text.length > 100) {
		return [SVProgressHUD showInfoInView:self.view withStatus:@"包裹昵称最多100个字符"];
	}
	self.params[@"nickname"] = self.titleTextField.text ?: @"";
	self.params[@"inventoryBasicId"] = self.model.id ?: @"";
	
	[self uploadImagesCompletion:^(BOOL success) {
		if (success) {
			NSURLSessionDataTask *task = [YJRouter commonPostModel:PutPendingOrderApi params:self.params modelClass:nil callback:^(YJRouterResponseModel *result) {
				if (result.code == 0) {
					if (self.model) {
						[self.model mj_setKeyValues:self.params];
						self.model.orderSreenshot = [result.data allValues].reverseObjectEnumerator.allObjects;
					}
					[SVProgressHUD showSuccessInView:self.view withStatus:result.msg];
					if (self.callback) {
						self.callback(self.model);
					}
					dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
						[self.navigationController popViewControllerAnimated:YES];
					});
				} else {
					[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
				}
			}];
			[self weakHoldTask:task];
		}
	}];
}


/**
 推出照片选择器
 */
- (void)pickImage
{
	if (self.imageContainer.images.count == self.imageContainer.limit) {
		return [SVProgressHUD showInfoInView:self.view withStatus:@"最多上传3张图片"];
	}
	@weakify(self)
	[YJImagePickerManager showImagePicker:^(YJRouterResponseModel *result) {
		@strongify(self)
		if (result.code != 0) {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		} else {
			UIImage *image = result.data;
			[self scaleWithImage:image];
		}
	} fromController:self];
}

/**
 压缩图片

 @param image 待压缩图片
 */
- (void)scaleWithImage:(UIImage *)image
{
	[SVProgressHUD showInView:self.view withStatus:@"正在压缩..."];
	@weakify(self)
	[YJImagePickerManager scaleImage:image toSize:0.65 callback:^(YJRouterResponseModel *result) {
		@strongify(self)
		if (result.code != 0) {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		} else {
			[SVProgressHUD dismissFromView:self.view];
			UIImage *newImage = result.data;
			[self.imageContainer addImage:newImage];
		}
	}];
}

/**
 上传图片

 @param completion 完成回调
 */
- (void)uploadImagesCompletion:(void(^)(BOOL success))completion
{
	NSMutableDictionary *params = @{}.mutableCopy;
	for (NSInteger i=0; i<self.imageContainer.images.count; i++) {
		NSData *imageData = UIImagePNGRepresentation(self.imageContainer.images[i]);
		if (!imageData) {
			[SVProgressHUD showErrorInView:self.view withStatus:@"图片格式不支持"];
			completion(NO);
			return;
		}
		NSString *key = [NSString stringWithFormat:@"inventoryPic%zd", i+1];
		params[key] = imageData;
	}
	[SVProgressHUD showInView:self.view withStatus:nil];
	NSURLSessionDataTask *task = [YJRouter commonUpload:UploadPredictePicApi params:nil fileParams:params progress:^(NSProgress *progress) {
		
	} callback:^(YJRouterResponseModel *result) {
		if (result.code == 0) {
			completion(YES);
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
			completion(NO);
		}
	}];
	[self weakHoldTask:task];
}

#pragma mark - Table view data source

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (self.addImageIndicator.hidden) {
		return indexPath.row == 0 || indexPath.row == 3;
	}
	return indexPath.row == 0 || indexPath.row == 3;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	if (indexPath.row == 3) {
		[tableView endEditing:YES];
		[self pickImage];
	} else if (indexPath.row == 0) {
		[tableView endEditing:YES];
		[self showWarehouse];
	}
}

/**
 仓库列表
 */
- (void)showWarehouse
{
	YJPickerView *pickView = [YJPickerView new];
	pickView.height = 180.5;
	pickView.width = self.tableView.width;
	pickView.componentWidth = pickView.width;
	pickView.left = 0;
	[SVProgressHUD showInView:self.view];
	@weakify(self)
	NSURLSessionDataTask *task = [YJRouter commonGetRefresh:WarehoushListApi currentCount:0 pageSize:0 otherParmas:nil modelClass:[YJWarehouseModel class] callback:^(YJRouterResponseModel *result) {
		if (result.code == 0 || result.code == 999) {
			if (result.data) {
				[SVProgressHUD dismissFromView:self.view];
				[pickView initialData:@[[result.data valueForKeyPath:@"name"]]];
				@weakify(pickView);
				[pickView setCallback:^(NSArray *values, BOOL cancel) {
					@strongify(pickView)
					if (!cancel) {
						@strongify(self)
						for (YJPickerViewItem *item in values) {
							self.locationLabel.text = [result.data[item.row] name];
							self.params[@"warehouseId"] = [result.data[item.row] id];
							self.params[@"warehouseName"] = [result.data[item.row] name];
						}
					}
					[pickView dismiss];
				}];
				[pickView show];
			} else {
				[SVProgressHUD showErrorInView:self.view withStatus:@"没有可用的仓库"];
			}
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
}

- (void)lineImageViews:(YJLineImageViews *)lineImageViews clickOnIdx:(NSInteger)idx
{
	[self.largeModels removeAllObjects];
	for (NSInteger idx = 0; idx < lineImageViews.images.count; idx ++) {
		UIImage *newImage = lineImageViews.images[idx];
		YJSwitchModel *switchModel = [YJSwitchModel new];
		switchModel.w = newImage.size.width;
		switchModel.h = newImage.size.height;
		switchModel.image = newImage;
		[self.largeModels addObject:switchModel];
	}
	
	YJLargeImageViewController *largeVc = [YJLargeImageViewController small_largeViewControllerWithModels:self.largeModels cellClass:nil];
	largeVc.currentIndexPath = [NSIndexPath indexPathForItem:idx inSection:0];
	largeVc.delegate = self;
	[largeVc setEditable:YES];
	[self.navigationController pushViewController:largeVc animated:YES];
}

- (void)lineImageViews:(YJLineImageViews *)lineImageView didAddImageViewAtIdx:(NSInteger)idx
{
	self.addImageIndicator.hidden = self.imageContainer.images.count == self.imageContainer.limit;
	self.grayLabel.hidden = self.imageContainer.images.count > 0;
}

- (void)lineImageViews:(YJLineImageViews *)lineImageView didRemoveImageViewAtIdx:(NSInteger)idx
{
	[self.largeModels removeObjectAtIndex:idx];
	self.addImageIndicator.hidden = self.imageContainer.images.count == self.imageContainer.limit;
	self.grayLabel.hidden = self.imageContainer.images.count > 0;
}


/**
 照片查看器的删除回调

 @param small_largeVc 照片查看器
 @param index 删除图片所处位置
 @return 是否允许删除
 */
- (BOOL)small_largeViewController:(YJSmall_largeViewController *)small_largeVc shouldRemoveIndex:(NSInteger)index
{
	[self.imageContainer deleteImageAtIdx:index];
	if (self.largeModels.count == 0) {
		[small_largeVc.navigationController popViewControllerAnimated:YES];
	}
	return YES;
}

@end
