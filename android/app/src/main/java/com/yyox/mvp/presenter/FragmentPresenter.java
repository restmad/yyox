package com.yyox.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.jess.arms.base.AppManager;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.utils.UiUtils;
import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.ParamsKey;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.kf5.sdk.ticket.ui.LookFeedBackActivity;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.Utils.Loading_view;
import com.yyox.Utils.MyToast;
import com.yyox.Utils.NoDoubleClickUtils;
import com.yyox.Utils.Preference;
import com.yyox.Utils.Utils;
import com.yyox.consts.CodeDefine;
import com.yyox.mvp.contract.FragmentContract;
import com.yyox.mvp.model.entity.BaseJson;
import com.yyox.mvp.model.entity.Message;
import com.yyox.mvp.model.entity.MessageItem;
import com.yyox.mvp.model.entity.Question;
import com.yyox.mvp.model.entity.Site;
import com.yyox.mvp.model.entity.UserDetail;
import com.yyox.mvp.model.realm.RealmUser;
import com.yyox.mvp.ui.activity.ServicePriceActivity;
import com.yyox.mvp.ui.activity.ServiceQuestionActivity;
import com.yyox.mvp.ui.activity.UserLoginActivity;
import com.yyox.mvp.ui.activity.WebActivity;
import com.yyox.mvp.ui.adapter.OrderFragmentAdpter;
import com.yyox.mvp.ui.adapter.ServiceFragmentAdpter;
import com.yyox.mvp.ui.adapter.SiteAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import common.WEApplication;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

/**
 * Created by dadaniu on 2017-01-24.
 */
@ActivityScope
public class FragmentPresenter extends BasePresenter<FragmentContract.Model, FragmentContract.View> {

    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<Message> mMessages = new ArrayList<>();
    private List<Question> mQuestions = new ArrayList<>();
    private List<Site> mSites = new ArrayList<>();
    private List<Site> mWebs = new ArrayList<>();
    private DefaultAdapter mAdapter;
    private DefaultAdapter mAdapterWeb;
    public static final int PER_PAGE = 10;
    private WEApplication weApplication;
    private boolean kfLogin = false;
    private boolean clickEstimate = false;
    private int IM = 1;
    private int LOOKFEED = 2;
    private int KFIntent;
    private Loading_view loading_view;

    @Inject
    public FragmentPresenter(FragmentContract.Model model, FragmentContract.View rootView, RxErrorHandler handler, AppManager appManager, Application application) {
        super(model, rootView);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
        if (!isLogin() || mMessages.size() == 0) {
            mMessages.add(null);
        }
    }

    private void KFLogin(Context context) {
        final Map<String, String> map = new ArrayMap<>();
        String userID;

        SharedPreferences sharedPreferences = context.getSharedPreferences("userEmail", 0);
        String mEmail = sharedPreferences.getString("Email", "");
        String mPhone = sharedPreferences.getString("phone", "");
        String mName = sharedPreferences.getString("name", "");

        if (mEmail != null && mEmail != "") {
            userID = mEmail;
            map.put(ParamsKey.EMAIL, userID);
            String device_token = Utils.getDevice_Token();
            map.put(ParamsKey.DEVICE_TOKEN, device_token);
        } else {
            String userlogo = UUID.randomUUID().toString();
            userID = userlogo + "@yyox.com";
            map.put(ParamsKey.EMAIL, userID);
        }
        if (mPhone != "" && mPhone != null) {
            map.put(ParamsKey.PHONE, mPhone);
        }
        if (mName != "" && mName != null) {
            map.put(ParamsKey.NAME, mName);
        }
        String Address = "yyox.kf5.com";
//          String Address = "laoliu.kf5.com";
        String Appid = "00158df494300eb413749913b7f818cf642e43f5a3e25267";
//          String Appid = "00158eaef2fac9251e8019aa3fab1b9194750c368f1d5b60";
        //String name = "Android 用户";
        SPUtils.saveAppID(Appid);
        SPUtils.saveHelpAddress(Address);
        SPUtils.saveUserAgent(Utils.getAgent(new SoftReference<Context>(Utils.getCurrentActivity())));

        UserInfoAPI.getInstance().createUser(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject jsonObject = SafeJson.parseObj(result);
                            int resultCode = SafeJson.safeInt(jsonObject, "error");
                            if (resultCode == 0) {
//                                Preference.saveBoolLogin(mAppManager.getCurrentActivity(), true);
                                Preference.saveBoolLogin(Utils.getCurrentActivity(), true);
                                final JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                                JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                                if (userObj != null) {
                                    String userToken = userObj.getString(Field.USERTOKEN);
                                    int id = userObj.getInt(Field.ID);
                                    SPUtils.saveUserToken(userToken);
                                    SPUtils.saveUserId(id);
                                    saveToken(map);
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loginUser(map);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String result) {
            }
        });

    }

    private void loginUser(Map<String, String> map) {
        UserInfoAPI.getInstance().loginUser(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject jsonObject = SafeJson.parseObj(result);
                            int resultCode = SafeJson.safeInt(jsonObject, "error");
                            if (resultCode == 0) {
//                                Preference.saveBoolLogin(mAppManager.getCurrentActivity(), true);
                                Preference.saveBoolLogin(Utils.getCurrentActivity(), true);
                                final JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                                JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                                if (userObj != null) {
                                    String userToken = userObj.getString(Field.USERTOKEN);
                                    int id = userObj.getInt(Field.ID);
                                    SPUtils.saveUserToken(userToken);
                                    SPUtils.saveUserId(id);
                                    saveToken(map);
                                }
                            } else {
                                clickEstimate = false;
                                kfLogin = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String message = jsonObject.getString("message");
//                                            Toast.makeText(mAppManager.getCurrentActivity(), message, Toast.LENGTH_SHORT).show();
                                            MyToast.makeText(Utils.getCurrentActivity(), message, MyToast.LENGTH_LONG).show();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (loading_view != null) {
                                                        loading_view.dismiss();
                                                    }
                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (loading_view != null) {
                                                        loading_view.dismiss();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onFailure(String result) {

            }
        });
    }

    private void saveToken(Map<String, String> map) {
        UserInfoAPI.getInstance().saveDeviceToken(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {
                clickEstimate = false;
                kfLogin = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loading_view != null) {
                            loading_view.dismiss();
                        }
                    }
                });
                if (KFIntent == IM) {//在线客服
                    Intent intent1 = new Intent(Utils.getCurrentActivity(), KF5ChatActivity.class);
                    Utils.getCurrentActivity().startActivityForResult(intent1, CodeDefine.SERVICE_REQUEST);
                } else if (KFIntent == LOOKFEED) {
                    Utils.getCurrentActivity().startActivity(new Intent(Utils.getCurrentActivity(), LookFeedBackActivity.class));
                }
            }

            @Override
            public void onFailure(String result) {
                clickEstimate = false;
                kfLogin = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loading_view != null) {
                            loading_view.dismiss();
                        }
                    }
                });
            }
        });

        UserInfoAPI.getInstance().getUserInfo(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onFailure(String result) {
            }
        });
    }

    private void login() {
        new AlertDialog.Builder(mAppManager.getCurrentActivity()).setTitle("提示")
                .setMessage("您还未登录,请先登录")
                .setPositiveButton("下次",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //取消
                            }
                        })
                .setNegativeButton("登录",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除订单
                                Intent intentLogin = new Intent(mAppManager.getCurrentActivity(), UserLoginActivity.class);
                                mAppManager.getCurrentActivity().startActivityForResult(intentLogin, CodeDefine.LOGIN_REQUEST);
                            }
                        }).show();
    }

    private boolean isLogin() {
        try {
            weApplication = (WEApplication) WEApplication.getContext().getApplicationContext();
            RealmUser realmUser = weApplication.getRealmUser();
            if (realmUser != null && (realmUser.getName() != "" || realmUser.getEmail() != null)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            mRootView.showMessage("login error");
            return false;
        }
    }

    public void serviceAdapter(Context context) {
        mAdapter = new ServiceFragmentAdpter(mQuestions);
        mRootView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                if (NoDoubleClickUtils.isDoubleClick()) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.btn_price:
                        if (isLogin()) {
                            Intent intent = new Intent(context, ServicePriceActivity.class);
                            context.startActivity(intent);
                        } else {
                            login();
                        }
                        break;
                    case R.id.btn_service:
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("showurl", CommonUtils.getBaseUrl() + "/app/server/serve-yyox/serve-yyox.html");
                        intent.putExtra("title", "服务");
                        context.startActivity(intent);
                        break;
                    case R.id.btn_safe:
                        Intent intentSafe = new Intent(context, WebActivity.class);
                        intentSafe.putExtra("showurl", CommonUtils.getBaseUrl() + "/app/server/guarantee/guarantee.html");
                        intentSafe.putExtra("title", "保障");
                        context.startActivity(intentSafe);
                        break;
                    case R.id.btn_online:
                        if (isLogin()) {//在线工单
                            if (!kfLogin && !clickEstimate) {
                                clickEstimate = true;
                                KFIntent = LOOKFEED;
                                KFLogin(context);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Activity currentActivity = Utils.getCurrentActivity();
                                        loading_view = new Loading_view(currentActivity, R.style.CustomDialog);
                                        if (loading_view != null){
                                            loading_view.show();
                                        }
                                    }
                                });
                            }
                        } else {
                            login();
                        }
                        break;
                    case R.id.btn_online_service:
                        //易创云登录
                        if (isLogin()) {
                            if (!kfLogin && !clickEstimate) {
                                clickEstimate = true;
                                KFIntent = IM;
                                KFLogin(context);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Activity currentActivity = Utils.getCurrentActivity();
                                        loading_view = new Loading_view(currentActivity, R.style.CustomDialog);
                                        if (loading_view != null){
                                            loading_view.show();
                                        }
                                    }
                                });
                            }
                        } else {
                            login();
                        }
                        break;
                    case R.id.service_btn_seek:
                        Intent QuestionMore = new Intent(context, ServiceQuestionActivity.class);
                        context.startActivity(QuestionMore);
                        break;
                    case R.id.service_footer:
                        Intent intentQuestionMore = new Intent(context, ServiceQuestionActivity.class);
                        context.startActivity(intentQuestionMore);
                        break;
                }
            }
        });
    }

    public void messageSetAdapter() {
        mAdapter = new OrderFragmentAdpter(mMessages);
        mRootView.setAdapter(mAdapter);//设置Adapter

    }

    public void siteSetAdapter() {
        mAdapter = new SiteAdapter(mSites);
        mRootView.setAdapter(mAdapter);//设置Adapter
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                Site site = mSites.get(position);
                if (view.getId() == R.id.ib_site_item_del) {
                    mRootView.showMessage("del");
                } else {
                    Intent intentSafe = new Intent(WEApplication.getContext(), WebActivity.class);
                    intentSafe.putExtra("showurl", site.getUrl());
                    intentSafe.putExtra("title", site.getName());
                    UiUtils.startActivity(intentSafe);
                }
            }
        });
    }

    public void webSetAdapter() {
        mAdapterWeb = new SiteAdapter(mWebs);
        mRootView.setAdapterWeb(mAdapterWeb);//设置Adapter
        mAdapterWeb.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                Site site = mWebs.get(position);
                if (view.getId() == R.id.ib_site_item_del) {
                    mRootView.showMessage("del");
                } else {
                    Intent intentSafe = new Intent(WEApplication.getContext(), WebActivity.class);
                    intentSafe.putExtra("showurl", site.getUrl());
                    intentSafe.putExtra("title", site.getName());
                    UiUtils.startActivity(intentSafe);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.mMessages = null;
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }

    public void requestUserDetail() {
        mModel.getUserDetail()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.<BaseJson<UserDetail>>bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<UserDetail>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<UserDetail> userDetailBaseJson) {
                        if (userDetailBaseJson.getStatus() == 0) {
                            UserDetail data = userDetailBaseJson.getData();
                            mRootView.setUserDetail(data);
                        } else {
                            mRootView.showMessage(userDetailBaseJson.getMsgs());
                        }
                    }
                });
    }

    public void requestMessagess(final boolean pullToRefresh) {
        String operateTime = null;
        if (pullToRefresh) {
            operateTime = "";
            mMessages.clear();
            Message message0 = new Message();
            mMessages.add(message0);
            mAdapter.notifyDataSetChanged();
        } else if (!pullToRefresh && mMessages.size() > 1) {
            operateTime = mMessages.get(mMessages.size() - 1).getOperateTime();
        }
        if (operateTime != null) {
            mModel.getMessages(operateTime, PER_PAGE)
                    .subscribeOn(Schedulers.io())
                    .retryWhen(new RetryWithDelay(3, 200))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            if (pullToRefresh)
                                mRootView.showLoading();//显示上拉刷新的进度条
                            else
                                mRootView.startLoadMore();
                        }
                    }).subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterTerminate(new Action0() {
                        @Override
                        public void call() {
                            if (pullToRefresh)
                                mRootView.hideLoading();//隐藏上拉刷新的进度条
                            else
                                mRootView.endLoadMore();
                        }
                    })
                    .compose(RxUtils.<BaseJson<MessageItem>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                    .subscribe(new ErrorHandleSubscriber<BaseJson<MessageItem>>(mErrorHandler) {
                        @Override
                        public void onNext(BaseJson<MessageItem> messageItemBaseJson) {
                            if (messageItemBaseJson.getStatus() == 0) {
                                MessageItem messageItem = messageItemBaseJson.getData();
                                if (messageItem.getResult().size() > 10) {
                                    if (pullToRefresh) {
                                        mMessages.clear();//如果是上拉刷新则晴空列表
                                        Message message0 = new Message();
                                        message0.setInitCustomer(messageItem.getInitCustomer());
                                        mMessages.add(message0);
                                    }
                                    for (int i = 0; i < messageItem.getResult().size(); i++) {
                                        if (i != messageItem.getResult().size() - 1) {
                                            mMessages.add(messageItem.getResult().get(i));
                                        }
                                    }
                                    mAdapter.notifyDataSetChanged();//通知更新数据
                                } else {
                                    if (pullToRefresh) {
                                        mMessages.clear();
                                        Message message0 = new Message();
                                        message0.setInitCustomer(messageItem.getInitCustomer());
                                        mMessages.add(message0);
                                    }
                                    for (Message message : messageItem.getResult()) {
                                        mMessages.add(message);
                                    }
                                    mAdapter.notifyDataSetChanged();//通知更新数据
                                    mRootView.datadFew();
                                }
                            } else {
                                mAdapter.notifyDataSetChanged();
                                mRootView.hideLoading();
                                mRootView.datadFew();
                                mRootView.showMessage(messageItemBaseJson.getMsgs());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mRootView.datadFew();
                        }
                    });
        }
    }

    public List<Message> getMesage() {
        return mMessages;
    }

    public void requestQuestions(final boolean pullToRefresh, String value) {
        if (pullToRefresh) {
            mQuestions.clear();
            mAdapter.notifyDataSetChanged();
        }

        mModel.getQuestions(value)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.showLoading();//显示上拉刷新的进度条
                        else
                            mRootView.startLoadMore();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.hideLoading();//隐藏上拉刷新的进度条
                        else
                            mRootView.endLoadMore();
                    }
                })
                .compose(RxUtils.<List<Question>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<List<Question>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Question> questionList) {
                        for (int i = 0; i < questionList.size(); i++) {
                            mQuestions.add(questionList.get(i));
                        }
                        mAdapter.notifyDataSetChanged();//通知更新数据
                        mRootView.datadFew();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.datadFew();
                    }
                });
    }

    public void requestSites(final boolean pullToRefresh, boolean edit) {
        if (pullToRefresh || edit) {
            mSites.clear();
            mAdapter.notifyDataSetChanged();
        }

        mModel.getSites()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.showLoading();//显示上拉刷新的进度条
                        else
                            mRootView.startLoadMore();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.hideLoading();//隐藏上拉刷新的进度条
                        else
                            mRootView.endLoadMore();
                    }
                })
                .compose(RxUtils.<List<Site>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<List<Site>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Site> siteList) {
                        for (int i = 0; i < siteList.size(); i++) {
                            Site site = siteList.get(i);
                            if (edit) {
                                site.setEdit(true);
                            }
                            mSites.add(site);
                        }
                        mAdapter.notifyDataSetChanged();//通知更新数据
                        mRootView.datadFew();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.datadFew();
                    }
                });
    }

    public void requestWebs(final boolean pullToRefresh, boolean edit) {
        if (pullToRefresh || edit) {
            mWebs.clear();
            mAdapterWeb.notifyDataSetChanged();
        }

        mModel.getWebs()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.showLoading();//显示上拉刷新的进度条
                        else
                            mRootView.startLoadMore();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (pullToRefresh)
                            mRootView.hideLoading();//隐藏上拉刷新的进度条
                        else
                            mRootView.endLoadMore();
                    }
                })
                .compose(RxUtils.<List<Site>>bindToLifecycle(mRootView))//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(new ErrorHandleSubscriber<List<Site>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Site> siteList) {
                        for (int i = 0; i < siteList.size(); i++) {
                            Site site = siteList.get(i);
                            if (edit) {
                                site.setEdit(true);
                            }
                            mWebs.add(site);
                        }
                        mAdapterWeb.notifyDataSetChanged();//通知更新数据
                        mRootView.datadFew();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.datadFew();
                    }
                });
    }

}
