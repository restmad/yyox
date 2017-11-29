package com.yyox.mvp.ui.holder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jess.arms.base.BaseHolder;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.loader.ImageLoader;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.mvp.model.entity.Message;
import com.yyox.mvp.model.entity.OrderCount;
import com.yyox.mvp.model.realm.RealmUser;
import com.yyox.mvp.ui.activity.WebActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import common.WEApplication;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by 95 on 2017/5/9.
 */
public class MessageHeaderHolder extends BaseHolder<Message> {
    @Nullable
    @BindView(R.id.banner)
    Banner mBanner;

    @Nullable
    @BindView(R.id.btn_order_report)
    Button mBtn_order_report;

    @Nullable
    @BindView(R.id.btn_order_pending)
    Button mBtn_order_pending;

    @Nullable
    @BindView(R.id.btn_order_completed)
    Button mBtn_order_completed;

    @Nullable
    @BindView(R.id.btn_order_status1)
    Button mStatus1;

    @Nullable
    @BindView(R.id.btn_order_status2)
    Button mStatus2;

    @Nullable
    @BindView(R.id.btn_order_status3)
    Button mStatus3;

    @Nullable
    @BindView(R.id.btn_order_status4)
    Button mStatus4;

    @Nullable
    @BindView(R.id.btn_order_status5)
    Button mStatus5;

    private Badge mBadge_Pending;
    private Badge mBadge_Completed;
    private Badge mBadge_Status1;
    private Badge mBadge_Status2;
    private Badge mBadge_Status3;
    private Badge mBadge_Status4;
    private Badge mBadge_Status5;
    private WEApplication weApplication;

    public MessageHeaderHolder(View view) {
        super(view);
    }

    @Override
    public void setData(Message data) {
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.home_iamge1);
        list.add(R.mipmap.home_iamge2);

        mBanner.setImages(list).setImageLoader(new GlideImageLoader()).start();
        mBanner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(WEApplication.getContext(), WebActivity.class);
                if (position == 1) {
                    intent.putExtra("showurl", CommonUtils.getBaseUrl() + "app/index1.html");
                } else if (position == 2) {
                    intent.putExtra("showurl", CommonUtils.getBaseUrl() + "app/index2.html");
                }
                intent.putExtra("title", "邮客全球转运");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                WEApplication.getContext().startActivity(intent);
            }
        });
        mBtn_order_report.setOnClickListener(this);
        mBtn_order_pending.setOnClickListener(this);
        mBtn_order_completed.setOnClickListener(this);
        mStatus1.setOnClickListener(this);
        mStatus2.setOnClickListener(this);
        mStatus3.setOnClickListener(this);
        mStatus4.setOnClickListener(this);
        mStatus5.setOnClickListener(this);

        mBadge_Pending = new QBadgeView(WEApplication.getContext()).bindTarget(mBtn_order_pending);
        mBadge_Pending.setBadgeGravity(Gravity.END | Gravity.TOP);
        mBadge_Completed = new QBadgeView(WEApplication.getContext()).bindTarget(mBtn_order_completed);
        mBadge_Completed.setBadgeGravity(Gravity.END | Gravity.TOP);
        mBadge_Status1 = new QBadgeView(WEApplication.getContext()).bindTarget(mStatus1);
        mBadge_Status1.setBadgeGravity(Gravity.END | Gravity.TOP);
        mBadge_Status2 = new QBadgeView(WEApplication.getContext()).bindTarget(mStatus2);
        mBadge_Status2.setBadgeGravity(Gravity.END | Gravity.TOP);
        mBadge_Status3 = new QBadgeView(WEApplication.getContext()).bindTarget(mStatus3);
        mBadge_Status3.setBadgeGravity(Gravity.END | Gravity.TOP);
        mBadge_Status4 = new QBadgeView(WEApplication.getContext()).bindTarget(mStatus4);
        mBadge_Status4.setBadgeGravity(Gravity.END | Gravity.TOP);
        mBadge_Status5 = new QBadgeView(WEApplication.getContext()).bindTarget(mStatus5);
        mBadge_Status5.setBadgeGravity(Gravity.END | Gravity.TOP);

        if (isLogon()) {
            OrderCount orderCount = data.getInitCustomer();
            String statusPending = String.valueOf(orderCount.getWaitForDispose());//待处理
            String statusCompleted = String.valueOf(orderCount.getFinish());//已完成
            String status1 = String.valueOf(orderCount.getNotputNo());//待入库
            String status2 = String.valueOf(orderCount.getForoutboundNo());//待出库
            String status3 = String.valueOf(orderCount.getHaveoutboundNo());//已出库
            String status4 = String.valueOf(orderCount.getClear());//清关
            String status5 = String.valueOf(orderCount.getDelivering());//配送

            if (0 == Integer.valueOf(statusPending)) {
                mBadge_Pending.hide(false);
            } else {
                mBadge_Pending.setBadgeNumber(Integer.valueOf(statusPending));
            }
            if (0 == Integer.valueOf(statusCompleted)) {
                mBadge_Completed.hide(false);
            } else {
                /*
                SharedPreferences sharedPreferences =  WEApplication.getContext().getSharedPreferences("userEmail", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("Email", "");
                SharedPreferences sharedPreferencess =  WEApplication.getContext().getSharedPreferences(email, Context.MODE_PRIVATE);
                int number = sharedPreferencess.getInt("number", 0);
                if (Integer.valueOf(statusCompleted)-number>0){
                    mBadge_Completed.setBadgeNumber(Integer.valueOf(statusCompleted)-number);
                }else {
                    mBadge_Completed.hide(false);
                }
                */
                SharedPreferences sharedPreferences =  WEApplication.getContext().getSharedPreferences("userEmail", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("Email", "");
                SharedPreferences sharedPreferencess =  WEApplication.getContext().getSharedPreferences(email, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencess.edit();
                editor.putInt("number",Integer.valueOf(Integer.valueOf(statusCompleted)));
                editor.commit();
                mBadge_Completed.setBadgeNumber(Integer.valueOf(statusCompleted));
            }
            if (0 == Integer.valueOf(status1)) {
                mBadge_Status1.hide(false);
            } else {
                mBadge_Status1.setBadgeNumber(Integer.valueOf(status1));
            }
            if (0 == Integer.valueOf(status2)) {
                mBadge_Status2.hide(false);
            } else {
                mBadge_Status2.setBadgeNumber(Integer.valueOf(status2));
            }
            if (0 == Integer.valueOf(status3)) {
                mBadge_Status3.hide(false);
            } else {
                mBadge_Status3.setBadgeNumber(Integer.valueOf(status3));
            }
            if (0 == Integer.valueOf(status4)) {
                mBadge_Status4.hide(false);
            } else {
                mBadge_Status4.setBadgeNumber(Integer.valueOf(status4));
            }
            if (0 == Integer.valueOf(status5)) {
                mBadge_Status5.hide(false);
            } else {
                mBadge_Status5.setBadgeNumber(Integer.valueOf(status5));
            }
        }

    }

    private boolean isLogon() {
        try {
            weApplication = (WEApplication) WEApplication.getContext().getApplicationContext();
            RealmUser realmUser = weApplication.getRealmUser();
            if (realmUser != null && realmUser.getName() != "") {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            //Glide 加载图片简单用法
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .crossFade()
                    .into(imageView);
        }
    }

}
