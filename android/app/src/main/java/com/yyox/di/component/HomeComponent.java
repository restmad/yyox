package com.yyox.di.component;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.di.module.HomeModule;
import com.yyox.mvp.ui.activity.HomeActivity;

import common.AppComponent;
import dagger.Component;

/**
 * Created by dadaniu on 2017-01-06.
 */
@ActivityScope
@Component(modules = HomeModule.class,dependencies = AppComponent.class)
public interface HomeComponent {

    void inject(HomeActivity activity);

}
