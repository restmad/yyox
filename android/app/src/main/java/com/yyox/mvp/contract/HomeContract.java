package com.yyox.mvp.contract;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.mvp.BaseView;
import com.jess.arms.mvp.IModel;
import com.tbruyelle.rxpermissions.RxPermissions;

/**
 * Created by dadaniu on 2017-01-06.
 */

public interface HomeContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
    }
}
