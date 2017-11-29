package com.yyox.di.component;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.di.module.UserModule;
import com.yyox.mvp.ui.activity.StartActivity;
import com.yyox.mvp.ui.activity.UserCouponActivity;
import com.yyox.mvp.ui.activity.UserEditActivity;
import com.yyox.mvp.ui.activity.UserForgetActivity;
import com.yyox.mvp.ui.activity.UserLoginActivity;
import com.yyox.mvp.ui.activity.UserModifyActivity;
import com.yyox.mvp.ui.activity.UserRechargeActivity;
import com.yyox.mvp.ui.activity.UserRecordActivity;
import com.yyox.mvp.ui.activity.UserRegisterActivity;
import com.yyox.mvp.ui.activity.UserSettingActivity;
import com.yyox.mvp.ui.activity.UserWarehouseActivity;

import common.AppComponent;
import dagger.Component;

/**
 * Created by dadaniu on 2017-02-08.
 */
@ActivityScope
@Component(modules = UserModule.class,dependencies = AppComponent.class)
public interface UserComponent {

    void inject(UserRegisterActivity activity);

    void inject(UserLoginActivity activity);

    void inject(UserForgetActivity activity);

    void inject(UserCouponActivity activity);

    void inject(UserRecordActivity activity);

    void inject(UserRechargeActivity activity);

    void inject(UserSettingActivity activity);

    void inject(UserWarehouseActivity activity);

    void inject(UserEditActivity activity);

    void inject(UserModifyActivity activity);

    void inject(StartActivity activity);
}
