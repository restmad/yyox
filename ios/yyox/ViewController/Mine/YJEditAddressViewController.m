//
//  YJEditAddressViewController.m
//  yyox
//
//  Created by ddn on 2017/1/11.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJEditAddressViewController.h"
#import "YJUploadPicView.h"
#import "YJEditAddressCell.h"
#import "YJPickerView.h"
#import "YJRegionModel.h"
#import "YJRegexte.h"
#import "YJImagePickerManager.h"

#import "UIWindow+YJExtension.h"

@interface YJEditAddressViewController () <UITableViewDataSource, UITableViewDelegate>

@property (strong, nonatomic) UIButton *doneButton;

@property (strong, nonatomic) UITableView *tableView;

@property (strong, nonatomic) YJUploadPicView *uploadView;

@property (strong, nonatomic) YJPickerView *pickerView;

@property (assign, nonatomic) YJUploadPicViewButton uploadType;

@property (strong, nonatomic) NSMutableDictionary *params;

@property (copy, nonatomic) NSArray *mustCates;

@property (strong, nonatomic) NSMutableArray *regions;

@end

@implementation YJEditAddressViewController

- (NSMutableArray *)regions
{
	if (!_regions) {
		_regions = [NSMutableArray array];
	}
	return _regions;
}


/**
 转换为后台所需字段

 @param cate 待转字段
 @return 转换后的字段
 */
NSString *switchCateToParams(NSString *cate)
{
	if ([cate isEqualToString:@"姓名"]) {
		return @"name";
	}
	if ([cate isEqualToString:@"手机号"]) {
		return @"mobile";
	}
	if ([cate isEqualToString:@"省"]) {
		return @"province";
	}
	if ([cate isEqualToString:@"市"]) {
		return @"city";
	}
	if ([cate isEqualToString:@"区"]) {
		return @"district";
	}
	if ([cate isEqualToString:@"详细地址"]) {
		return @"detailaddress";
	}
	if ([cate isEqualToString:@"邮政编码"]) {
		return @"zipcode";
	}
	if ([cate isEqualToString:@"身份证号"]) {
		return @"idCardNumber";
	}
	if ([cate isEqualToString:@"frontImage"]) {
		return @"cardImageFrontUrl ";
	}
	if ([cate isEqualToString:@"backImage"]) {
		return @"cardImageBackUrl  ";
	}
	if ([cate isEqualToString:@"isdefault"]) {
		return @"isdefault";
	}
	return nil;
}

- (instancetype)init
{
	self = [super init];
	if (self) {
		self.view.backgroundColor = [UIColor whiteColor];
		[self setup];
		_params = [NSMutableDictionary dictionary];
		self.type = YJEditAddressEditAll;
	}
	return self;
}

- (void)setup
{
	_tableView = [UITableView new];
	[self.view addSubview:_tableView];
	
	_tableView.dataSource = self;
	_tableView.delegate = self;
	_tableView.sectionFooterHeight = 1;
	_tableView.keyboardDismissMode = UIScrollViewKeyboardDismissModeOnDrag;
	
	[_tableView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.edges.mas_equalTo(0);
	}];
	_uploadView = [YJUploadPicView new];
	
	@weakify(self)
	[_uploadView setClickOn:^(YJUploadPicViewButton buttonType) {
		@strongify(self)
		self.uploadType = buttonType;
		switch (buttonType) {
			case YJUploadPicViewFrontButton:
			{
				[self pickImage];
			}
				break;
			case YJUploadPicViewBackButton:
			{
				[self pickImage];
			}
				break;
			case YJUploadPicViewBeDefaultButton:
			{
				if (self.addressModel.isdefault) return ;
				self.uploadView.isDefault = !self.uploadView.isDefault;
				self.params[switchCateToParams(@"isdefault")] = @(self.uploadView.isDefault);
			}
				break;
				
			default:
				break;
		}
	}];

	[_tableView addSubview:_uploadView];
	
	[_uploadView mas_makeConstraints:^(MASConstraintMaker *make) {
		make.top.mas_equalTo(50 * 6);
		make.width.mas_equalTo(_tableView.mas_width);
	}];
	
	_doneButton = [UIButton new];
	[_doneButton setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_normal"] forState:UIControlStateNormal];
	[_doneButton setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_highlighted"] forState:UIControlStateHighlighted];
//	[_doneButton setBackgroundImage:[UIImage imageNamed:@"user_auth_btn_disable"] forState:UIControlStateDisabled];
	
	[_doneButton setTitle:@"保存" forState:UIControlStateNormal];
	_doneButton.titleLabel.font = [UIFont systemFontOfSize:15];
	
	_doneButton.height = 49;
	_doneButton.width = self.view.width;
	[_doneButton addTarget:self action:@selector(clickOnDone) forControlEvents:UIControlEventTouchUpInside];
	
	[self.view addSubview:_doneButton];
	[_doneButton mas_makeConstraints:^(MASConstraintMaker *make) {
		make.bottom.left.right.mas_equalTo(0);
		make.height.mas_equalTo(49);
	}];
	
	
	_pickerView = [YJPickerView new];
	_pickerView.width = kScreenWidth;
	_pickerView.height = kScreenWidth * 0.5;
	_pickerView.componentWidth = kScreenWidth;
	
	[NSDC addObserver:self selector:@selector(pickerViewDidDismiss) name:UIWindowDidDismissAnimationContainer object:nil];
	
	[_pickerView setCallback:^(NSArray<YJPickerViewItem *> *values, BOOL cancel) {
		@strongify(self)
		if (cancel) {
			if (values.count > 1) {
				[self.regions removeLastObject];
				[self.pickerView reloadWithData:[self.regions valueForKeyPath:@"regionname"]];
				[self.pickerView scrollToComponent:values.count - 2 completion:^{
				}];
				
			} else {
				[self.pickerView dismiss];
			}
			return ;
		}
		if (values.count < 3) {
			YJRegionModel *region = self.regions[values.lastObject.column][values.lastObject.row];
			[SVProgressHUD showInView:self.pickerView withStatus:nil];
			NSURLSessionDataTask *task = [YJRouter commonGetRefresh:RegionsApi currentCount:0 pageSize:0 otherParmas:@{@"parentId": @(region.id)} modelClass:[YJRegionModel class] callback:^(YJRouterResponseModel *result) {
				if ((result.code == 0 || result.code == 999) && result.data && [result.data isKindOfClass:[NSArray class]]) {
					[SVProgressHUD dismissFromView:self.pickerView];
					if ([result.data count] > 0) {
						[self.regions appendObject:result.data];
						[self.pickerView reloadComponent:values.count withData:[result.data valueForKeyPath:@"regionname"]];
						[self.pickerView scrollToComponent:values.count completion:nil];
					}
					if ([result.data count] == 0) {
						self.params[switchCateToParams(@"省")] = [self.regions[values.firstObject.column][values.firstObject.row] regionname];
						self.params[switchCateToParams(@"市")] = [self.regions[values[1].column][values[1].row] regionname];
						self.params[switchCateToParams(@"区")] = @"";
						[self.tableView reloadRow:2 inSection:0 withRowAnimation:UITableViewRowAnimationNone];
						[self.pickerView dismiss];
					}
				}else {
					[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
				}
			}];
			if (task) {
				[self weakHoldTask:task];
			}
		} else {
			self.params[switchCateToParams(@"省")] = [self.regions[values.firstObject.column][values.firstObject.row] regionname];
			self.params[switchCateToParams(@"市")] = [self.regions[values[1].column][values[1].row] regionname];
			self.params[switchCateToParams(@"区")] = [self.regions[values.lastObject.column][values.lastObject.row] regionname];
			[self.tableView reloadRow:2 inSection:0 withRowAnimation:UITableViewRowAnimationNone];
			[self.pickerView dismiss];
		}
	}];
}

- (void)pickerViewDidDismiss
{
	[self setTableViewContentSize];
}

- (void)setTableViewContentSize
{
	UIEdgeInsets inset = self.tableView.contentInset;
	inset.bottom = 50;
	self.tableView.contentInset = inset;
	self.tableView.scrollIndicatorInsets = self.tableView.contentInset;
	self.tableView.contentSize = CGSizeMake(self.tableView.width, CGRectGetMaxY(self.uploadView.frame) + 50);
}

- (void)viewDidLoad {
    [super viewDidLoad];
	
}

- (void)viewDidLayoutSubviews
{
	[super viewDidLayoutSubviews];
	[self setTableViewContentSize];
}

- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	[self setTableViewContentSize];
	
	if (self.autoRefresh) {
		self.autoRefresh = NO;
		if (!self.addressModel) return;
		
		[self getAddressDetail];
	}
}


/**
 获取地址详情
 */
- (void)getAddressDetail
{
	[SVProgressHUD showInView:self.view withStatus:nil];
	NSURLSessionDataTask *task = [YJRouter commonGetModel:[NSString stringWithFormat:@"%@/%zd", DetailAddressApi, self.addressModel.id.integerValue] params:nil modelClass:[YJAddressModel class] callback:^(YJRouterResponseModel *result) {
		if (result.code == 0 || result.code == 999) {
			
			[self.addressModel mj_setKeyValues:result.data];
			self.addressModel = _addressModel;
			[self.tableView reloadData];
			
			self.params[switchCateToParams(@"frontImage")] = self.addressModel.cardImageFrontUrl;
			self.params[switchCateToParams(@"backImage")] = self.addressModel.cardImageBackUrl;
			
			if (self.addressModel.cardImageFrontUrl.length > 0 && self.addressModel.cardImageBackUrl.length > 0) {
				[self.uploadView beginLoadImage:^id(UIImageView *frontImageView, UIImageView *backImageView) {
					
					[frontImageView yj_setBase64Image:self.addressModel.cardImageFrontUrl thumbnail:YES placeHolder:nil];
					[backImageView yj_setBase64Image:self.addressModel.cardImageBackUrl thumbnail:YES placeHolder:nil];
					
					return @[frontImageView, backImageView];
				}];
			}
			[SVProgressHUD dismissFromView:self.view];
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
}

- (void)clickOnDone
{
	if ([self.doneButton.titleLabel.text isEqualToString:@"编辑"]) {
		self.type = YJEditAddressEditAll;
		return;
	}
	
	NSString *msg = [self checkParams:nil];
	if (msg) {
		[SVProgressHUD showErrorInView:self.view withStatus:msg];
		return;
	}
	
	[self updateInfo];
}


/**
 参数检查

 @param params 参数
 @return 错误信息，如果没有错误，则返回nil
 */
- (NSString *)checkParams:(NSString *)params
{
	if (self.type == YJEditAddressCard) {
		if (!self.uploadView.frontImage || !self.uploadView.backImage) {
			return @"您还没有上传身份证";
		}
	}
	
	NSString *msg = nil;
	
	NSString *tel = self.params[switchCateToParams(@"手机号")];
	NSString *name = self.params[switchCateToParams(@"姓名")];
	NSString *address = [NSString stringWithFormat:@"%@%@%@", self.params[switchCateToParams(@"省")], self.params[switchCateToParams(@"市")], self.params[switchCateToParams(@"区")]];
	NSString *detailAddress = self.params[switchCateToParams(@"详细地址")];
	NSString *num = self.params[switchCateToParams(@"邮政编码")];
	
	NSString *cardid = self.params[switchCateToParams(@"身份证号")];
	//	NSString *frontImageUrl = self.params[switchCateToParams(@"frontImage")];
	//	NSString *backImageUrl = self.params[switchCateToParams(@"backImage")];
	
	int ss = !self.uploadView.frontImage + !self.uploadView.backImage;
	
	if (![YJRegexte isChineseName:name]) {
		msg = @"请输入2-10个汉字组成的姓名";
	}
	else if (![YJRegexte isTelNumber:tel]) {
		msg = @"请填写正确的手机号";
	}
	else if (!address || address.length == 0) {
		msg = @"请填写收货地址";
	}
	else if (!detailAddress || detailAddress.length == 0) {
		msg = @"请填写详细地址";
	}
	else if (![YJRegexte isZipcode:num]) {
		msg = @"邮政编码应该是6位纯数字";
	}
	else if (![YJRegexte isCardID:cardid]) {
		msg = @"身份证号有误";
	}
	else if (ss != 2 && ss != 0) {
		msg = @"身份信息不完整";
	}
	return msg;
}


/**
 更新／保存信息  ／  上传照片
 */
- (void)updateInfo
{
	if (!self.params[@"country"]) {
		self.params[@"country"] = @"中国";
	}
	if (self.addressModel) {
		self.params[@"id"] = self.addressModel.id;
	} else {
		self.params[@"id"] = @0;
	}
	BOOL isdefault = [self.params[switchCateToParams(@"isdefault")] boolValue];
	self.params[switchCateToParams(@"isdefault")] = isdefault ? @"true" : @"false";
	NSString *api = SaveAddressApi;
	if (self.type == YJEditAddressCard) {
		api = SpecUploadPersonInfoApi;
	}
	[SVProgressHUD showInView:self.view];
	NSURLSessionDataTask *task = [YJRouter commonPostModel:api params:self.params.mutableCopy modelClass:nil callback:^(YJRouterResponseModel *result) {
		if (result.code == 0) {
			YJAddressModel *model = [YJAddressModel mj_objectWithKeyValues:self.params];
			BOOL changeDefault = self.addressModel.isdefault != model.isdefault;
			[self.addressModel mj_setKeyValues:model];
			self.addressModel.idCardNumber = self.params[switchCateToParams(@"身份证号")];
			self.addressModel.cardImageFrontUrl = self.params[switchCateToParams(@"frontImage")];
			self.addressModel.cardImageBackUrl = self.params[switchCateToParams(@"backImage")];
			[SVProgressHUD showSuccessInView:self.view withStatus:result.msg];
			if (self.callback) {
				self.callback(changeDefault);
				self.callback = nil;
			}
			dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
				if (self.type == YJEditAddressCard) {
					[self.navigationController popToViewController:self.navigationController.viewControllers[1] animated:YES];
				} else {
					[self.navigationController popViewControllerAnimated:YES];
				}
			});
		} else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	[self weakHoldTask:task];
}

- (void)setType:(YJEditAddress)type
{
	_type = type;
	if (type == YJEditAddressNone) {
		[self.doneButton setTitle:@"编辑" forState:UIControlStateNormal];
		self.uploadView.userInteractionEnabled = NO;
		self.uploadView.hasInfo = NO;
	} else {
		self.uploadView.hasDefaultBtn = type != YJEditAddressCard;
		[self.doneButton setTitle:@"保存" forState:UIControlStateNormal];
		self.uploadView.userInteractionEnabled = YES;
		self.uploadView.hasInfo = YES;
		if ([self.title isEqualToString:@"地址详情"]) {
			self.title = @"编辑收货地址";
		}
	}
	[self.tableView reloadData];
}

- (void)setAddressModel:(YJAddressModel *)addressModel
{
	_addressModel = addressModel;
	self.params = [addressModel mj_keyValuesWithIgnoredKeys:@[@"idCardNumber", @"cardImageFrontUrl", @"cardImageBackUrl"]].mutableCopy;
	self.params[switchCateToParams(@"身份证号")] = addressModel.idCardNumber ?: @"";
	self.params[switchCateToParams(@"frontImage")] = addressModel.cardImageFrontUrl ?: @"";
	self.params[switchCateToParams(@"backImage")] = addressModel.cardImageBackUrl ?: @"";
	self.uploadView.isDefault = [self.params[switchCateToParams(@"isdefault")] boolValue];
	
}

#pragma mark - tableView
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return 6;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	YJEditAddressCell *cell =
	cell = [[YJEditAddressCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cell"];
	cell.tableView = tableView;
	[self setCellInfo:cell indexPath:indexPath];
	if (indexPath.row == 5) {
		cell.canEditText = self.type == YJEditAddressEditAll || self.type == YJEditAddressCard;
	} else {
		cell.canEditText = self.type == YJEditAddressEditAll;
	}
	return cell;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
	return [UIView new];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return 50;
}

- (BOOL)tableView:(UITableView *)tableView shouldHighlightRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (self.type != YJEditAddressEditAll) {
		return NO;
	}
	return indexPath.row == 2;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	[tableView endEditing:YES];
	
	if (self.regions.count > 0) {
		[self.pickerView initialData:[self.regions valueForKeyPath:@"regionname"]];
		[self.pickerView show];
		return;
	}
	[SVProgressHUD showInView:self.view withStatus:nil];
	NSURLSessionDataTask *task = [YJRouter commonGetRefresh:RegionsApi currentCount:0 pageSize:0 otherParmas:@{@"parentId": @0} modelClass:[YJRegionModel class] callback:^(YJRouterResponseModel *result) {
		if ((result.code == 0 || result.code == 999) && [result.data count] > 0) {
			[SVProgressHUD dismissFromView:self.view];
			[self.pickerView initialData:@[[result.data valueForKeyPath:@"regionname"]]];
			[self.pickerView show];
			[self.regions removeAllObjects];
			[self.regions addObject:result.data];
		}else {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		}
	}];
	if (task) {
		[self weakHoldTask:task];
	}
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	cell.separatorInset = UIEdgeInsetsZero;
	cell.layoutMargins = UIEdgeInsetsZero;
	cell.preservesSuperviewLayoutMargins = NO;
}

- (void)setCellInfo:(YJEditAddressCell *)cell indexPath:(NSIndexPath *)indexPath
{
	switch (indexPath.row) {
		case 0:
		{
		cell.hasIcon = YES;
		cell.cate = @"姓名";
		cell.content = self.params[switchCateToParams(@"姓名")];
		cell.placeHolderText = @"请输入姓名";
		[cell setRegexte:^BOOL(NSString *target) {
			return [YJRegexte isChineseName:target];
		}];
			break;
		}
		case 1:
		{
		cell.hasIcon = YES;
		cell.cate = @"手机号";
		cell.content = self.params[switchCateToParams(@"手机号")];
		cell.placeHolderText = @"请输入手机号";
		[cell setRegexte:^BOOL(NSString *target) {
			return [YJRegexte isTelNumber:target];
		}];
			break;
		}
		case 2:
		{
		cell.hasIcon = YES;
		cell.cate = @"所在地区";
		cell.content = [NSString stringWithFormat:@"%@%@%@", self.params[switchCateToParams(@"省")] ?: @"", self.params[switchCateToParams(@"市")] ?: @"", self.params[switchCateToParams(@"区")] ?: @""];
		cell.placeHolderText = @"";
		cell.hideRegionButton = self.type != YJEditAddressEditAll;
		[cell setRegexte:^BOOL(NSString *target) {
			return target && target.length > 0;
		}];
			break;
		}
		case 3:
		{
		cell.hasIcon = YES;
		cell.cate = @"详细地址";
		cell.content = self.params[switchCateToParams(@"详细地址")];
		cell.placeHolderText = @"请输入详细地址";
		[cell setRegexte:^BOOL(NSString *target) {
			return target && target.length > 0;
		}];
			break;
		}
		case 4:
		{
		cell.hasIcon = YES;
		cell.cate = @"邮政编码";
		cell.content = self.params[switchCateToParams(@"邮政编码")];
		cell.placeHolderText = @"请输入邮政编码";
		[cell setRegexte:^BOOL(NSString *target) {
			return [YJRegexte isZipcode:target];
		}];
			break;
		}
		case 5:
		{
		cell.hasIcon = YES;
		cell.cate = @"身份证号";
		cell.content = self.params[switchCateToParams(@"身份证号")];
		cell.placeHolderText = @"请输入身份证号码";
		[cell setRegexte:^BOOL(NSString *target) {
			return [YJRegexte isCardID:target];
		}];
			break;
		}
		default:
			break;
	}
	
	@weakify(self)
	[cell setTextChanged:^(NSString *cate, NSString *content) {
		@strongify(self)
		if ([cate isEqualToString:@"所在地区"]) return;
		self.params[switchCateToParams(cate)] = content;
	}];
}

#pragma mark - imagePicker

/**
 展示系统相册
 */
- (void)pickImage
{
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
	[SVProgressHUD showInView:self.view withStatus:@"正在压缩图片..."];
	
	@weakify(self)
	[YJImagePickerManager scaleImage:image toSize:2 callback:^(YJRouterResponseModel *result) {
		@strongify(self)
		if (result.code != 0) {
			[SVProgressHUD showErrorInView:self.view withStatus:result.msg];
		} else {
			[self uploadImage:result.data];
		}
	}];
}


/**
 上传并展示图片

 @param image 待上传图片
 */
- (void)uploadImage:(UIImage *)image
{
	YJUploadPicViewButton type = self.uploadType;
	NSData *imageData = UIImagePNGRepresentation(image);
	if (!imageData) {
		[SVProgressHUD showErrorInView:self.view withStatus:@"图片格式不支持"];
		return;
	}
	
	@weakify(self)
	[SVProgressHUD showInView:self.view withStatus:@"正在上传..."];
	[self.uploadView beginLoadImage:^id(UIImageView *frontImageView, UIImageView *backImageView) {
		
		@strongify(self)
		
		if (type == YJUploadPicViewFrontButton) {
			NSURLSessionDataTask *task = [YJRouter commonUpload:UploadCardApi params:nil fileParams:@{@"cardFront": imageData} progress:^(NSProgress *progress) {
				
			} callback:^(YJRouterResponseModel *result) {
				if (result.code == 0) {
					frontImageView.image = image;
					[SVProgressHUD dismissFromView:self.view];
				} else {
					[SVProgressHUD showErrorInView:self.view withStatus:@"上传失败"];
				}
			}];
			[self weakHoldTask:task];
			return frontImageView;
		} else if (type == YJUploadPicViewBackButton) {
			NSURLSessionDataTask *task = [YJRouter commonUpload:UploadCardApi params:nil fileParams:@{@"cardBack": imageData} progress:^(NSProgress *progress) {
				
			} callback:^(YJRouterResponseModel *result) {
				if (result.code == 0) {
					backImageView.image = image;
					[SVProgressHUD dismissFromView:self.view];
				} else {
					[SVProgressHUD showErrorInView:backImageView.superview withStatus:@"上传失败"];
				}
			}];
			[self weakHoldTask:task];
			return backImageView;
		} else {
			[SVProgressHUD dismissFromView:self.view];
			return nil;
		}
	}];
}

@end







