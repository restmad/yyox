package com.yyox.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.mvp.contract.AddressContract;
import com.yyox.mvp.model.AddressModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dadaniu on 2017-01-12.
 */
@Module
public class AddressModule {

    private AddressContract.View view;

    public AddressModule(AddressContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AddressContract.View provideAddressView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    AddressContract.Model provideAddressModel(AddressModel model){
        return model;
    }

}
