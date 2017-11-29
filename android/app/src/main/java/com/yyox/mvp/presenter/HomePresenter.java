package com.yyox.mvp.presenter;

import android.app.Application;

import com.jess.arms.base.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.yyox.mvp.contract.HomeContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * Created by dadaniu on 2017-01-06.
 */

public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {

    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;

    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView, RxErrorHandler handler, AppManager appManager, Application application) {
        super(model, rootView);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }

}
