package common;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.githang.androidcrash.AndroidCrash;
import com.githang.androidcrash.reporter.mailreporter.CrashEmailReporter;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.di.module.GlobeConfigModule;
import com.jess.arms.http.GlobeHttpHandler;
import com.jess.arms.utils.UiUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.yyox.BuildConfig;
import com.yyox.R;
import com.yyox.Utils.Loading_view;
import com.yyox.Utils.Utils;
import com.yyox.di.module.CacheModule;
import com.yyox.di.module.ServiceModule;
import com.yyox.mvp.model.api.Api;
import com.yyox.mvp.model.realm.RealmUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErroListener;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import timber.log.Timber;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

/**
 * Created by jess on 8/5/16 11:07
 * contact with jess.yan.effort@gmail.com
 */
public class WEApplication extends BaseApplication {
    private AppComponent mAppComponent;
    private RefWatcher mRefWatcher;//leakCanary观察器
    private RealmUser mRealmUser;
    private PushAgent mPushAgent;
    private String activityName;
    private Loading_view loading_view;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(getAppModule())//baseApplication提供
                .clientModule(getClientModule())//baseApplication提供
                .imageModule(getImageModule())//baseApplication提供
                .globeConfigModule(getGlobeConfigModule())//全局配置
                .serviceModule(new ServiceModule())//需自行创建
                .cacheModule(new CacheModule())//需自行创建
                .build();

        mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                Utils.setDevice_Token(deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });
        mPushAgent.onAppStart();

        if (BuildConfig.LOG_DEBUG) {//Timber日志打印
            Timber.plant(new Timber.DebugTree());
        }

        installLeakCanary();//leakCanary内存泄露检查

        //初始化Realm
        Realm.init(this);
        initRealmUser();
        initEmailReporter();
    }

    /**
     * 使用EMAIL发送日志
     */
    private void initEmailReporter() {
        CrashEmailReporter reporter = new CrashEmailReporter(this);
        reporter.setReceiver("tianliang@pj-l.com");
//        reporter.setReceiver("wangweitong@pj-l.com");
        reporter.setSender("tianliang@pj-l.com");
        reporter.setSendPassword("tl.123456");
        reporter.setSMTPHost("smtp.pj-l.com");
        reporter.setPort("465");
        AndroidCrash.getInstance().setCrashReporter(reporter).init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppComponent != null)
            this.mAppComponent = null;
        if (mRefWatcher != null)
            this.mRefWatcher = null;
    }

    /**
     * 安装leakCanary检测内存泄露
     */
    protected void installLeakCanary() {
        this.mRefWatcher = BuildConfig.USE_CANARY ? LeakCanary.install(this) : RefWatcher.DISABLED;
    }

    /**
     * 获得leakCanary观察器
     *
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {
        WEApplication application = (WEApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }


    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例, 在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    public AppComponent getAppComponent() {
        return mAppComponent;
    }


    /**
     * app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     *
     * @return
     */
    @Override
    protected GlobeConfigModule getGlobeConfigModule() {
       /* String baseUrl = Api.APP_DOMAIN;
        SharedPreferences apiSharedPreferences = getSharedPreferences("api", MODE_PRIVATE);
        int withinApi = apiSharedPreferences.getInt("withinApi", 0);
        if (withinApi == 1) {
            baseUrl = "http://10.0.0.19:8080/app/";
        } else if (withinApi == 2) {
//            baseUrl = "http://52.69.220.34:80/app";
            baseUrl = "http://app.yyox.com/app/";
//            baseUrl = "http://52.199.12.84:80/app/";
        } else if (withinApi == 3) {
            baseUrl = "http://6d089bc4.ngrok.4kb.cn/app/";
        } else if (withinApi == 4) {
            baseUrl = "http://192.168.4.224:8080/app/";
        }*/
        return GlobeConfigModule
                .buidler()
                .baseurl(Api.APP_DOMAIN)
                .globeHttpHandler(new GlobeHttpHandler() {// 这里可以提供一个全局处理http响应结果的处理类,
                    // 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                    @Override
                    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                        //这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                        //重新请求token,并重新执行请求
                        Log.e("getGlobeConfigModule", "httpResult ------>:" + httpResult);
                        loadingclose();
                        try {
                            if (!TextUtils.isEmpty(httpResult)) {
                                JSONObject object = new JSONObject(httpResult);
                                int status = object.getInt("status");
                                String msgs = object.getString("msgs");
                                if (2 == status) {
                                    //退出登录
                                    logoutRealmUser();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return response;
                        }
                        //这里如果发现token过期,可以先请求最新的token,然后在拿新的token放入request里去重新请求
                        //注意在这个回调之前已经调用过proceed,所以这里必须自己去建立网络请求,如使用okhttp使用新的request去请求
                        // create a new request and modify it accordingly using the new token
//                    Request newRequest = chain.request().newBuilder().header("token", newToken).build();

//                    // retry the request
//                    response.body().close();
                        //如果使用okhttp将新的请求,请求成功后,将返回的response  return出去即可

                        //如果不需要返回新的结果,则直接把response参数返回出去
                        return response;
                    }

                    // 这里可以在请求服务器之前可以拿到request,做一些操作比如给request统一添加token或者header
                    @Override
                    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) throws IOException {
                        //如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的requeat如增加header,不做操作则返回request
                        Log.e("getGlobeConfigModule", "httpRequest ------>:" + chain.request().toString() + request.body());
                        if (chain.request().toString().indexOf("commitOrder") != -1 || chain.request().toString().indexOf("oneKeyInventory" )!=-1){
                            activityName = null;
                        }
                        Loading();
                        if (request.body() instanceof RequestBody) {
                            RequestBody requestBody = request.body();
                            Buffer buffer = new Buffer();
                            requestBody.writeTo(buffer);//??
                            Charset charset = Charset.forName("UTF-8");
                            String paramsStr = buffer.readString(charset);
                            Log.e("getGlobeConfigModule", "httpRequest ------>:" + paramsStr);
                        }//{"msgs":"您无权访问！请登录","status":2}
                        //return chain.request().newBuilder().header("token", tokenId).build();
                        return request;
                    }
                })
                .responseErroListener(new ResponseErroListener() {
                    //     用来提供处理所有错误的监听
                    //     rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效
                    @Override
                    public void handleResponseError(Context context, Exception e) {
                        Timber.tag(TAG).w("------------>" + e.getMessage());
                        loadingclose();
                        UiUtils.SnackbarText("网络异常");
                    }
                }).build();

    }

    private void Loading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ContextWrapper contextWrapper = mAppManager.getCurrentActivity() == null ? mAppComponent.Application() : mAppManager.getCurrentActivity();
                Activity currentActivity = Utils.getCurrentActivity();
                if (currentActivity != null) {
                    if (currentActivity.toString().indexOf("PackageActivity") != -1) {
                        return;
                    } else if (currentActivity.toString().indexOf("HomeActivity") != -1) {
                        return;
                    } else if (currentActivity.toString().indexOf("OrderActivity") != -1) {
                        return;
                    } else if (currentActivity.toString().indexOf("UserCouponActivity") != -1) {
                        return;
                    } else if (currentActivity.toString().indexOf("UserRecordActivity") != -1) {
                        return;
                    } else if (activityName == null || currentActivity.toString().indexOf(activityName) == -1) {
                        activityName = currentActivity.toString();
                        loading_view = new Loading_view(currentActivity, R.style.CustomDialog);
                        loading_view.show();
                    }
                }
            }
        });
    }

    private void loadingclose() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loading_view != null) {
                    loading_view.dismiss();
                }
            }
        });

    }

    private void initRealmUser() {
        //注册成功写数据库
        RealmConfiguration config = new RealmConfiguration.Builder().name("yyox.realm").build();
        Realm objRealm = Realm.getInstance(config);

        //查找
        mRealmUser = objRealm.where(RealmUser.class).findFirst();
    }

    private void logoutRealmUser() {
        //注册成功写数据库
        RealmConfiguration config = new RealmConfiguration.Builder().name("yyox.realm").build();
        Realm objRealm = Realm.getInstance(config);

        //查找
        RealmUser realmUser = objRealm.where(RealmUser.class).findFirst();
        if (realmUser != null) {
            //先删除
            objRealm.beginTransaction();
            RealmResults results = objRealm.where(RealmUser.class).findAll();
            results.deleteAllFromRealm();
            objRealm.commitTransaction();
            //再添加
            objRealm.beginTransaction();
            RealmUser realmUserNew = objRealm.createObject(RealmUser.class);
            realmUserNew.setName("");
            objRealm.commitTransaction();

            mRealmUser = null;
        }
    }

    public RealmUser getRealmUser() {
        return mRealmUser;
    }

    public void setRealmUser(RealmUser realmUser) {
        this.mRealmUser = realmUser;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
