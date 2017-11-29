package com.yyox.di.component;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.di.module.AddressModule;
import com.yyox.mvp.ui.activity.AddressActivity;
import com.yyox.mvp.ui.activity.AddressSaveActivity;

import common.AppComponent;
import dagger.Component;

/**
 * Created by dadaniu on 2017-01-12.
 */
@ActivityScope
@Component(modules = AddressModule.class,dependencies = AppComponent.class)
public interface AddressComponent {

    void inject(AddressActivity activity);

    void inject(AddressSaveActivity activity);

}
