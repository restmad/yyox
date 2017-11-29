//
//  YJUserModel.h
//  yyox
//
//  Created by ddn on 2017/1/17.
//  Copyright © 2017年 Panjiang. All rights reserved.
//

#import <Realm/Realm.h>

@interface YJUserModel : RLMObject

@property (copy, nonatomic) NSString *member;

@property (strong, nonatomic) NSNumber<RLMInt> *couponCount;

@property (copy, nonatomic) NSString *name;
@property (copy, nonatomic) NSString *nickname;

@property (copy, nonatomic) NSString *firstName;
@property (copy, nonatomic) NSString *lastName;

//@property (strong, nonatomic) NSNumber<RLMDouble> *difBalanceCnyNum;
@property (strong, nonatomic) NSNumber<RLMDouble> *balanceCnyFormat2;

@property (copy, nonatomic) NSString *id;

@property (copy, nonatomic) NSString *avatarUrl;

@property (copy, nonatomic) NSString *mail;

@property (copy, nonatomic) NSString *identifier;

@property (copy, nonatomic) NSString *level;

@property (copy, nonatomic) NSString *mobile;

@property (copy, nonatomic) NSString *qq;

@end
