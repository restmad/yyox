package com.yyox.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.mvp.contract.FragmentContract;
import com.yyox.mvp.model.FragmentModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dadaniu on 2017-01-24.
 */
@Module
public class FragmentModule {

    private FragmentContract.View view;

    public FragmentModule(FragmentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FragmentContract.View provideFragmentView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    FragmentContract.Model provideFragmentModel(FragmentModel model){
        return model;
    }

}
