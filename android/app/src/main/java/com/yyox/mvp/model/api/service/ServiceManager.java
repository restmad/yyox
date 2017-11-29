package com.yyox.mvp.model.api.service;

import com.jess.arms.http.BaseServiceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by dadaniu on 2017-01-06.
 */
@Singleton
public class ServiceManager implements BaseServiceManager {

    private CommonService mCommonService;
    private UserService mUserService;
    private AddressService mAddressService;
    private OrderService mOrderService;
    private PackageService mPackageService;

    /**
     * 如果需要添加service只需在构造方法中添加对应的service,在提供get方法返回出去,只要在ServiceModule提供了该service
     * Dagger2会自行注入
     * @param commonService
     */
    @Inject
    public ServiceManager(CommonService commonService, UserService userService, AddressService addressService, OrderService orderService, PackageService packageService){
        this.mCommonService = commonService;
        this.mUserService = userService;
        this.mAddressService = addressService;
        this.mOrderService = orderService;
        this.mPackageService = packageService;
    }

    @Override
    public void onDestory() {

    }

    public CommonService getCommonService() {
        return mCommonService;
    }

    public UserService getUserService() {
        return mUserService;
    }

    public AddressService getAddressService() {
        return mAddressService;
    }

    public OrderService getOrderService() {
        return mOrderService;
    }

    public PackageService getPackageService() {
        return mPackageService;
    }

}
