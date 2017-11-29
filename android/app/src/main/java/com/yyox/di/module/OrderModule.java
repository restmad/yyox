package com.yyox.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.yyox.mvp.contract.OrderContract;
import com.yyox.mvp.model.OrderModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dadaniu on 2017-02-03.
 */
@Module
public class OrderModule {

    private OrderContract.View view;

    public OrderModule(OrderContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    OrderContract.View provideOrderView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    OrderContract.Model provideOrderModel(OrderModel model){
        return model;
    }

}
