package com.yyox.di.module;

import com.yyox.mvp.model.api.cache.CommonCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache.internal.RxCache;

/**
 * Created by dadaniu on 2017-01-06.
 */
@Module
public class CacheModule {

    @Singleton
    @Provides
    CommonCache provideCommonService(RxCache rxCache) {
        return rxCache.using(CommonCache.class);
    }

}
