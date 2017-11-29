package com.yyox.di.module;

import com.yyox.mvp.model.api.service.AddressService;
import com.yyox.mvp.model.api.service.CommonService;
import com.yyox.mvp.model.api.service.OrderService;
import com.yyox.mvp.model.api.service.PackageService;
import com.yyox.mvp.model.api.service.UserService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by dadaniu on 2017-01-06.
 */
@Module
public class ServiceModule {

    @Singleton
    @Provides
    CommonService provideCommonService(Retrofit retrofit){
        return retrofit.create(CommonService.class);
    }

    @Singleton
    @Provides
    UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @Singleton
    @Provides
    AddressService provideAddressService(Retrofit retrofit){
        return retrofit.create(AddressService.class);
    }

    @Singleton
    @Provides
    OrderService provideOrderService(Retrofit retrofit){
        return retrofit.create(OrderService.class);
    }

    @Singleton
    @Provides
    PackageService providePackageService(Retrofit retrofit){
        return retrofit.create(PackageService.class);
    }

}
