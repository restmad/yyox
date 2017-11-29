package com.yyox.mvp.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.umeng.message.PushAgent;
import com.yyox.R;

/**
 * Created by 95 on 2017/6/8.
 */
public class UserPhoneActivity extends AppCompatActivity{
    private RadioGroup mPaiGroup;
    private RadioButton mApiID1;
    private RadioButton mApiID2;
    private RadioButton mApiID3;
    private RadioButton mApiID4;
    private TextView mText;
    private DisplayMetrics dm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_phone);
        initView();
        initDate();
        //友盟设置
        PushAgent.getInstance(this).onAppStart();
    }
    public void btn_back_click(View v){
        this.finish();
    }
    private void initDate() {
        SharedPreferences apiSharedPreferences = getSharedPreferences("api", MODE_PRIVATE);
        SharedPreferences.Editor ApiEditor = apiSharedPreferences.edit();
        int withinApi = apiSharedPreferences.getInt("withinApi", 0);
        if (withinApi == 1){
            mApiID1.setChecked(true);
        }else if (withinApi == 0 || withinApi == 2){
            mApiID2.setChecked(true);
        } else if (withinApi == 3){
            mApiID3.setChecked(true);
        }else if (withinApi == 4){
            mApiID4.setChecked(true);
        }
        mPaiGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int RadioButtonId) {
                if (mApiID1.getId() == RadioButtonId){
                    ApiEditor.putInt("withinApi",1);
                    ApiEditor.commit();
                }else if (mApiID2.getId() == RadioButtonId){
                    ApiEditor.putInt("withinApi",2);
                    ApiEditor.commit();
                }else if (mApiID3.getId() == RadioButtonId){
                    ApiEditor.putInt("withinApi",3);
                    ApiEditor.commit();
                }else if (mApiID4.getId() == RadioButtonId){
                    ApiEditor.putInt("withinApi",4);
                    ApiEditor.commit();
                }
            }
        });

//        mText.setText("手机型号:"+ PhoneMessage.getPhoneModel(this)+"手机品牌:"+PhoneMessage.getPhoneProduct(this)+"屏幕分辩率"+PhoneMessage.getMetrics(this));
        mText.setText(getInfo()+getHeightAndWidth());
    }

    private void initView() {
        mPaiGroup = (RadioGroup) findViewById(R.id.paiGroup);
        mApiID1 = (RadioButton) findViewById(R.id.apiID1);
        mApiID2 = (RadioButton) findViewById(R.id.apiID2);
        mApiID3 = (RadioButton) findViewById(R.id.apiID3);
        mApiID4 = (RadioButton) findViewById(R.id.apiID4);
        mText = (TextView) findViewById(R.id.phone);
    }
    private String getInfo() {
        TelephonyManager mTm = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = android.os.Build.MODEL; // 手机型号
        String mtyb= android.os.Build.BRAND;//手机品牌
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        return "\n手机型号："+mtype+"\n手机品牌："+mtyb+"\n手机号码"+numer;
    }
    /**
     * 获得手机屏幕宽高
     * @return
     */
    public String getHeightAndWidth(){
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //获得手机的宽带和高度像素单位为px
        String str = "\n手机屏幕分辨率为1:" + dm.widthPixels
                +" * "+ dm.heightPixels
        +"\n手机屏幕分辨率为2:" + screenWidth
                +" * "+ screenHeight;
        return str;
    }
}
