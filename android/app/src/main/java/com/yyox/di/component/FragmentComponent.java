package com.yyox.di.component;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.di.module.FragmentModule;
import com.yyox.mvp.ui.activity.BuyFragment;
import com.yyox.mvp.ui.activity.MineFragment;
import com.yyox.mvp.ui.activity.OrderFragment;
import com.yyox.mvp.ui.activity.ServiceFragment;

import common.AppComponent;
import dagger.Component;

/**
 * Created by dadaniu on 2017-01-24.
 */
@ActivityScope
@Component(modules = FragmentModule.class,dependencies = AppComponent.class)
public interface FragmentComponent {

    void inject(BuyFragment fragment);

    void inject(MineFragment fragment);

    void inject(OrderFragment fragment);

    void  inject(ServiceFragment fragment);
}
