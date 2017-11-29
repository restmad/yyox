package com.yyox.mvp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umeng.message.PushAgent;
import com.yyox.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class UserInviteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_invite);
        //友盟设置
        PushAgent.getInstance(this).onAppStart();

        ShareSDK.initSDK(this);
    }

    public void btn_back_click(View v) {
        this.finish();
    }

    public void shareSDK(View v) {
        showShare();
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();

        oks.setTitle("tl");
        oks.setTitleUrl("http://fir.im/6md5");
        oks.setText("田亮");
        oks.setImagePath("file:///android_asset/home_iamge2.png");
        oks.setUrl("http://fir.im/6md5");
        oks.setComment("我是测试评论文本");
        oks.setSite(getString(R.string.app_name));
        oks.setSiteUrl("http://fir.im/6md5");
        oks.show(this);
    }
}
