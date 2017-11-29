package com.yyox.mvp.model.api.cache;

import com.jess.arms.http.BaseCacheManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by dadaniu on 2017-01-06.
 */
@Singleton
public class CacheManager implements BaseCacheManager {

    private CommonCache mCommonCache;

    /**
     * 如果需要添加Cache只需在构造方法中添加对应的Cache,
     * 在提供get方法返回出去,只要在CacheModule提供了该Cache Dagger2会自行注入
     * @param commonCache
     */
    @Inject
    public CacheManager(CommonCache commonCache) {
        this.mCommonCache = commonCache;
    }

    public CommonCache getCommonCache() {
        return mCommonCache;
    }

    @Override
    public void onDestory() {

    }
}
