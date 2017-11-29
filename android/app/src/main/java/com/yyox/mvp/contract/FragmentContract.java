package com.yyox.mvp.contract;


import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.mvp.BaseView;
import com.jess.arms.mvp.IModel;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.MessageItem;
import com.yyox.mvp.model.entity.OrderCount;
import com.yyox.mvp.model.entity.Question;
import com.yyox.mvp.model.entity.Site;
import com.yyox.mvp.model.entity.UserDetail;
import com.yyox.mvp.model.entity.Warehouse;

import java.util.List;

import rx.Observable;

/**
 * Created by dadaniu on 2017-01-24.
 */

public interface FragmentContract {

    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        void setAdapter(DefaultAdapter adapter);
        void setAdapterWeb(DefaultAdapter adapter);
        //申请权限
        RxPermissions getRxPermissions();
        void setUserDetail(UserDetail userDetail);
        void setOrderCount(OrderCount orderCount);
        void startLoadMore();
        void endLoadMore();
        void datadFew();//没有更多数据
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        Observable<BaseJson<MessageItem>> getMessages(String date, int pageNumber);
        Observable<BaseJson<UserDetail>> getUserDetail();
        Observable<List<Question>> getQuestions(String value);
        Observable<List<Site>> getSites();
        Observable<List<Site>> getWebs();
    }

}
