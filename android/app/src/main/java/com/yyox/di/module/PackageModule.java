package com.yyox.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.mvp.contract.PackageContract;
import com.yyox.mvp.model.PackageModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dadaniu on 2017-02-27.
 */
@Module
public class PackageModule {

    private PackageContract.View view;

    public PackageModule(PackageContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PackageContract.View providePackageView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    PackageContract.Model providePackageModel(PackageModel model){
        return model;
    }

}
