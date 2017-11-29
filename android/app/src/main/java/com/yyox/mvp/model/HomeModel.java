package com.yyox.mvp.model;

import com.jess.arms.mvp.BaseModel;
import com.yyox.mvp.contract.HomeContract;
import com.yyox.mvp.model.api.cache.CacheManager;
import com.yyox.mvp.model.api.service.ServiceManager;

import javax.inject.Inject;

/**
 * Created by dadaniu on 2017-01-06.
 */

public class HomeModel extends BaseModel<ServiceManager, CacheManager> implements HomeContract.Model {

    @Inject
    public HomeModel(ServiceManager serviceManager, CacheManager cacheManager) {
        super(serviceManager, cacheManager);
    }

}
