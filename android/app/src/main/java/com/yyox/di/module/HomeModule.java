package com.yyox.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.mvp.contract.HomeContract;
import com.yyox.mvp.model.HomeModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dadaniu on 2017-01-06.
 */
@Module
public class HomeModule {

    private HomeContract.View view;

    public HomeModule(HomeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HomeContract.View provideHomeView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    HomeContract.Model provideHomeModel(HomeModel model){
        return model;
    }

}
