package com.yyox.mvp.ui.holder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.yyox.R;
import com.yyox.mvp.model.entity.OrderPackage;

import butterknife.BindView;
import common.WEApplication;
import rx.Observable;

/**
 * Created by dadaniu on 2017-01-14.
 */

public class PackageDetailItemHolder extends BaseHolder<OrderPackage>  {

    @Nullable
    @BindView(R.id.tv_item_package_detail_list_name)
    TextView mName;

    @Nullable
    @BindView(R.id.tv_item_package_detail_list_ups)
    TextView mUps;

    @Nullable
    @BindView(R.id.tv_item_package_detail_list_weight)
    TextView mWeight;

    @Nullable
    @BindView(R.id.iv_item_package_detail_list_edit)
    ImageButton mImageButton_Edit;

    @Nullable
    @BindView(R.id.package_map)
    LinearLayout mLinearLayout;



    @Nullable
    @BindView(R.id.iv_item_package_detail_list_img1)
    ImageView mImg1;

    @Nullable
    @BindView(R.id.iv_item_package_detail_list_img2)
    ImageView mImg2;

    @Nullable
    @BindView(R.id.iv_item_package_detail_list_img3)
    ImageView mImg3;

    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    private final WEApplication mApplication;

    public PackageDetailItemHolder(View itemView) {
        super(itemView);
        mImageButton_Edit.setOnClickListener(this);
        //可以在任何可以拿到Application的地方,拿到AppComponent,从而得到用Dagger管理的单例对象
        mApplication = (WEApplication) itemView.getContext().getApplicationContext();
        mImageLoader = mApplication.getAppComponent().imageLoader();
        mLinearLayout.setOnClickListener(this);
    }

    @Override
    public void setData(OrderPackage data) {
        if(data.getNickname() != null && !data.getNickname().isEmpty()){
            Observable.just("包裹昵称：" + data.getNickname()).subscribe(RxTextView.text(mName));
        }else{
            Observable.just("包裹昵称：未命名").subscribe(RxTextView.text(mName));
        }
        Observable.just("运单号："+data.getCarrierNo()).subscribe(RxTextView.text(mUps));
        Observable.just("包裹重量："+data.getWeight()+"kg").subscribe(RxTextView.text(mWeight));
        if(data.getOrderSreenshot() != null){
            if(data.getOrderSreenshot().size() > 0) {
                itemView.findViewById(R.id.package_map).setVisibility(View.VISIBLE);

                mImg1.setImageBitmap(data.getBitmap1());
            }
            if(data.getOrderSreenshot().size() > 1) {
                itemView.findViewById(R.id.package_map).setVisibility(View.VISIBLE);

                mImg2.setImageBitmap(data.getBitmap2());
            }
            if(data.getOrderSreenshot().size() > 2) {
                itemView.findViewById(R.id.package_map).setVisibility(View.VISIBLE);

                mImg3.setImageBitmap(data.getBitmap3());
            }
        }

    }

}
