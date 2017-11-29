package com.yyox.di.component;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.di.module.OrderModule;
import com.yyox.mvp.ui.activity.BuyAddSiteActivity;
import com.yyox.mvp.ui.activity.OrderActivity;
import com.yyox.mvp.ui.activity.OrderChannelActivity;
import com.yyox.mvp.ui.activity.OrderDetailActivity;
import com.yyox.mvp.ui.activity.OrderPackageDetailActivity;
import com.yyox.mvp.ui.activity.OrderPayActivity;
import com.yyox.mvp.ui.activity.OrderPaySuccessActivity;
import com.yyox.mvp.ui.activity.OrderTrackingActivity;
import com.yyox.mvp.ui.activity.ServicePriceActivity;
import com.yyox.mvp.ui.activity.ServiceQuestionActivity;

import common.AppComponent;
import dagger.Component;

/**
 * Created by dadaniu on 2017-02-03.
 */
@ActivityScope
@Component(modules = OrderModule.class,dependencies = AppComponent.class)
public interface OrderComponent {

    void inject(OrderActivity activity);

    void inject(OrderChannelActivity activity);

    void inject(OrderPayActivity activity);

    void inject(OrderPaySuccessActivity activity);

    void inject(OrderTrackingActivity activity);

    void inject(OrderDetailActivity activity);

    void inject(OrderPackageDetailActivity activity);

    void inject(ServicePriceActivity activity);

    void inject(ServiceQuestionActivity activity);

    void inject(BuyAddSiteActivity activity);

}
