package com.yyox.mvp.ui.holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
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
import com.yyox.mvp.model.entity.Question;
import com.yyox.mvp.ui.activity.WebActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import common.WEApplication;

/**
 * Created by 95 on 2017/5/10.
 */
public class ServiceHeaderHolder extends BaseHolder<Question> {
   @Nullable
   @BindView(R.id.Service_banner)
   Banner mBanner;

    @Nullable
    @BindView(R.id.btn_price)
    Button mBtn_price;

    @Nullable
    @BindView(R.id.btn_service)
    Button mBtn_service;

    @Nullable
    @BindView(R.id.btn_safe)
    Button mBtn_safe;

    @Nullable
    @BindView(R.id.btn_online)
    Button mBtn_online;

    @Nullable
    @BindView(R.id.btn_online_service)
    Button mBtn_online_service;

    @Nullable
    @BindView(R.id.service_btn_seek)
    Button mService_btn_seek;


    public ServiceHeaderHolder(View view) {
        super(view);
    }

    @Override
    public void setData(Question data) {
        List<Integer> list=new ArrayList<>();
        list.add(R.mipmap.service_banner);

        mBanner.setImages(list).setImageLoader(new GlideImageLoader()).start();

        mBanner.setOnClickListener(this);
        mBtn_price.setOnClickListener(this);
        mBtn_service.setOnClickListener(this);
        mBtn_safe.setOnClickListener(this);
        mBtn_online.setOnClickListener(this);
        mBtn_online_service.setOnClickListener(this);
        mService_btn_seek.setOnClickListener(this);
        mBanner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(WEApplication.getContext(),WebActivity.class);
                intent.putExtra("showurl", CommonUtils.getBaseUrl()+"app/banner/index.html");
                intent.putExtra("title","邮客服务");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                WEApplication.getContext().startActivity(intent);
            }
        });
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
