package com.yyox.di.component;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.di.module.PackageModule;
import com.yyox.mvp.ui.activity.PackageActivity;
import com.yyox.mvp.ui.activity.PackageSaveActivity;
import com.yyox.mvp.ui.activity.PackagesDetailActivity;

import common.AppComponent;
import dagger.Component;

/**
 * Created by dadaniu on 2017-02-27.
 */
@ActivityScope
@Component(modules = PackageModule.class,dependencies = AppComponent.class)
public interface PackageComponent {

    void inject(PackageActivity activity);

    void inject(PackageSaveActivity activity);

    void inject(PackagesDetailActivity activity);

}
