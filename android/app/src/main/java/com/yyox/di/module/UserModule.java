package com.yyox.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.mvp.contract.UserContract;
import com.yyox.mvp.model.UserModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dadaniu on 2017-02-08.
 */
@Module
public class UserModule {

    private UserContract.View view;

    /**
     * 构建UserModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     * @param view
     */
    public UserModule(UserContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UserContract.View provideUserView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    UserContract.Model provideUserModel(UserModel model){
        return model;
    }

}
