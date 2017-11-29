package com.yyox.Utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by 95 on 2017/6/8.
 */

public class PhoneMessage {
    public static String getLine1Number(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getLine1Number();
    }

    //deviceId
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getDeviceId();
    }

    //运营商名称
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getNetworkOperatorName();
    }

    //sim卡序列号
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getSimSerialNumber();
    }

    //IMSI
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getSubscriberId();
    }
    //sim卡所在国家
    public static String getNetworkCountryIso(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return "" + tm.getNetworkCountryIso();
    }
    //运营商编号。
    public static String getNetworkOperator(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return tm.getNetworkOperator();
    }
    //android 获取当前手机型号
    public static String getPhoneModel(Context context) {
        Build bd = new Build();
        return  bd.MODEL;
    }


    //android 获取当前手机品牌
    public static String getPhoneProduct(Context context) {
        Build bd = new Build();
        return  bd.PRODUCT;
    }
    //android 获取屏幕分辩率
    public static String getMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        int h = dm.heightPixels;
        int w = dm.widthPixels;
        return  h+ "*" +w;
    }

    //android获取当前时区
    public static String getTimeZone(Context context) {
        TimeZone tz = TimeZone.getDefault();
        String s = tz.getID();
        System.out.println(s);
        return s;
    }

    //android获取当前日期时间
    public static String getDateAndTime(Context context) {
        SimpleDateFormat formatter = new SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    //获取手机系统语言 0中文简体 1其它
    public static String getLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return "0";
        else
            return "1";
    }
    public static String getDisplayMetrics(Context cx) {
        String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm =cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth =dm.widthPixels;
        int screenHeight =dm.heightPixels;
        float density =dm.density;
        float xdpi =dm.xdpi;
        float ydpi =dm.ydpi;
        str += "The absolute width:"+ String.valueOf(screenWidth) + "pixels\n";
        str += "The absoluteheightin:" + String.valueOf(screenHeight)
                + "pixels\n";
        str += "The logical densityof the display.:" + String.valueOf(density)
                + "\n";
        str += "X dimension :" +String.valueOf(xdpi) + "pixels per inch\n";
        str += "Y dimension :" +String.valueOf(ydpi) + "pixels per inch\n";
        return str;
    }
}
