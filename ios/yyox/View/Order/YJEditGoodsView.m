//
//  YJEditGoodsView.m
//  yyox
//
//  Created by ddn on 2017/2/28.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import "YJEditGoodsView.h"

@interface YJEditGoodsCell : UITableViewCell

@property (strong, nonatomic) UILabel *leftLabel;
@property (strong, nonatomic) UILabel *countLabel;
@property (strong, nonatomic) UIButton *selectBtn;

@property (copy, nonatomic) void(^clickOnSelect)(UIButton *);

@end

@implementation YJEditGoodsCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
	self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
	if (self) {
		[self setup];
	}
	return self;
}

- (void)setup
{
	_leftLabel = [UILabel new];
	_countLabel = [UILabel new];
	_selectBtn = [UIButton new];
	
	[self.contentView addSubview:_leftLabel];
	[self.contentView addSubview:_countLabel];
	[self.contentView addSubview:_selectBtn];
	
	_leftLabel.font = [UIFont systemFontOfSize:13];
	_leftLabel.textColor = [UIColor colorWithRGB:0x666666];
	
	_countLabel.font = [UIFont systemFontOfSize:13];
	_countLabel.textColor = [UIColor colorWithRGB:0x666666];
	
	[_selectBtn setImage:[UIImage imageNamed:@"circle"] forState:UIControlStateNormal];
	[_selectBtn setImage:[UIImage imageNamed:@"circle_real"] forState:UIControlStateSelected];
	[_selectBtn addTarget:self action:@selector(clickOnSelectBtn:) forControlEvents:UIControlEventTouchUpInside];
	
	[_leftLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.left.mas_equalTo(20);
		make.right.mas_equalTo(-110);
		make.centerY.mas_equalTo(0);
	}];
	[_selectBtn setContentEdgeInsets:UIEdgeInsetsMake(0, 20, 0, 20)];
	[_selectBtn setContentCompressionResistancePriority:UILayoutPriorityRequired forAxis:UILayoutConstraintAxisHorizontal];
	[_selectBtn mas_makeConstraints:^(MASConstraintMaker *make) {
		make.right.mas_equalTo(0);
		make.top.bottom.mas_equalTo(0);
	}];
	[_countLabel mas_makeConstraints:^(MASConstraintMaker *make) {
		make.right.mas_greaterThanOrEqualTo(_selectBtn.mas_left).offset(0);
		make.centerY.mas_equalTo(0);
		make.left.mas_equalTo(kScreenWidth-100);
	}];
	
	self.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
}

- (void)clickOnSelectBtn:(UIButton *)sender
{
	if (self.clickOnSelect) {
		self.clickOnSelect(sender);
	}
}

@end

@interface YJEditGoodsView() <UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;

@property (weak, nonatomic) IBOutlet UITextField *textField1;
@property (weak, nonatomic) IBOutlet UITextField *textField2;
@property (weak, nonatomic) IBOutlet UITextField *textField3;

@property (weak, nonatomic) IBOutlet UILabel *currencyLabel;
@property (weak, nonatomic) IBOutlet UILabel *subTitleLabel;

@property (weak, nonatomic) IBOutlet UITableView *listView;

@property (weak, nonatomic) IBOutlet UIButton *bottomBtn;

@property (strong, nonatomic) NSIndexPath *selectedIndexPath;

@property (strong, nonatomic) UITextField *currentTextField;

@property (copy, nonatomic) NSArray<YJGoodsModel *> *unreadyGoods;

@end

@implementation YJEditGoodsView

- (void)awakeFromNib
{
	[super awakeFromNib];
	_titleLabel.superview.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
	_textField1.superview.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
	_textField2.superview.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
	_textField3.superview.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
	_currencyLabel.edgeLines = UIEdgeInsetsMake(0, 0.3, 0, 0);
	_subTitleLabel.superview.edgeLines = UIEdgeInsetsMake(0, 0, 0.3, 0);
	_bottomBtn.superview.edgeLines = UIEdgeInsetsMake(0.3, 0, 0, 0);
	
	_textField1.returnKeyType = _textField2.returnKeyType = _textField3.returnKeyType = UIReturnKeyDone;
}

+ (instancetype)editGoodsView
{
	return [[NSBundle mainBundle] loadNibNamed:@"YJEditGoodsView" owner:nil options:nil][0];
}

- (instancetype)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self) {
		[self setup];
	}
	return self;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
	self = [super initWithCoder:aDecoder];
	if (self) {
		[self setup];
	}
	return self;
}

- (void)setup
{
	//监听键盘
	[NSDC addObserver:self selector:@selector(keyboardFrameWillChange:) name:UIKeyboardWillChangeFrameNotification object:nil];
}

- (void)dealloc
{
	[NSDC removeObserver:self];
}

- (void)initialSetting
{
	if (self.goods) {
		self.titleLabel.text = @"编辑商品";
		self.unreadyGoods = [@[@[self.goods], self.merchandise.unreadyGoodsList ?: @[]] valueForKeyPath:@"@unionOfArrays.self"];
	} else {
		self.titleLabel.text = @"添加商品";
		self.unreadyGoods = self.merchandise.unreadyGoodsList ?: @[];
	}
	self.selectedIndexPath = nil;
	self.textField1.text = nil;
	self.textField2.text = nil;
	self.textField3.text = nil;
}

- (void)reloadData
{
	[self initialSetting];
	[self.listView reloadData];
	if (self.goods) {
		self.textField1.text = self.goods.brandCNY;
		self.textField2.text = self.goods.productNameCNY;
		self.textField3.text = self.goods.amount.stringValue;
		self.currencyLabel.text = [self.goods.currencyName transformToCurrency];
	} else {
		if (self.merchandise.goodList.firstObject.currencyName) {
			self.currencyLabel.text = [self.merchandise.goodList.firstObject.currencyName transformToCurrency];
		}
	}
}

- (IBAction)clickOnDone:(id)sender {
	if (self.clickOnDone) {
		YJGoodsModel *selectGoods = self.selectedIndexPath ? self.unreadyGoods[self.selectedIndexPath.row] : nil;
		NSMutableDictionary *params = @{}.mutableCopy;
		params[@"brandCNY"] = self.textField1.text;
		params[@"productNameCNY"] = self.textField2.text;
		params[@"amount"] = @(self.textField3.text.doubleValue);
		if (selectGoods) {
			params[@"stock"] = selectGoods.stock;
			params[@"id"] = selectGoods.id;
			params[@"currencyName"] = selectGoods.currencyName;
		}
		self.clickOnDone(self.goods, params, selectGoods);
	}
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
	[textField resignFirstResponder];
	[UIView animateWithDuration:0.25 animations:^{
		self.transform = CGAffineTransformIdentity;
	}];
	return YES;
}

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
	self.currentTextField = textField;
	[UIView animateWithDuration:0.25 animations:^{
		self.transform = CGAffineTransformMakeTranslation(0, -(kScreenHeight - self.height));
	}];
	return YES;
}

- (void)keyboardFrameWillChange:(NSNotification *)notify
{
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return self.unreadyGoods.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	static NSString *const identify = @"cell";
	YJEditGoodsCell *cell = [tableView dequeueReusableCellWithIdentifier:identify];
	if (!cell) {
		cell = [YJEditGoodsCell new];
	}
	YJGoodsModel *goods = self.unreadyGoods[indexPath.row];
	
	if (goods.goolsType && !self.selectedIndexPath) {
		self.selectedIndexPath = indexPath;
	}
	
	cell.leftLabel.text = [NSString stringWithFormat:@"%@", goods.productName];
	cell.countLabel.text = [NSString stringWithFormat:@"*%zd", goods.stock.integerValue];
	if (self.selectedIndexPath) {
		cell.selectBtn.selected = self.selectedIndexPath.row == indexPath.row;
	}
	@weakify(self)
	[cell setClickOnSelect:^(UIButton *btn) {
		@strongify(self)
		if (self.selectedIndexPath && self.selectedIndexPath.row != indexPath.row) {
			YJEditGoodsCell *cell = [self.listView cellForRowAtIndexPath:self.selectedIndexPath];
			cell.selectBtn.selected = NO;
			btn.selected = YES;
			self.selectedIndexPath = indexPath;
		} else {
			btn.selected = !btn.selected;
			if (!btn.selected) {
				self.selectedIndexPath = nil;
			} else {
				self.selectedIndexPath = indexPath;
			}
		}
	}];
	return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return 43;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
	cell.separatorInset = UIEdgeInsetsZero;
	cell.layoutMargins = UIEdgeInsetsZero;
	cell.preservesSuperviewLayoutMargins = NO;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	YJGoodsModel *goods = self.unreadyGoods[indexPath.row];
	if (goods.productName && goods.productName.length > 0) {
		[SVProgressHUD showMessageInView:self withStatus:[NSString stringWithFormat:@" %@", goods.productName]];
	}
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

@end
