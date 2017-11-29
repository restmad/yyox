package com.yyox.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/18 11:32
 * email:812219713@qq.com
 */

public class Utils {
   private static String device_Token = "";
    public static String getAgent(SoftReference<Context> softReference) {

        String agent = "";
        try {
            String ua = System.getProperty("http.agent");
            String packageName = softReference.get().getPackageName();
            PackageInfo info = softReference.get().getPackageManager().getPackageInfo(packageName, 0);
            agent = ua + ", " + packageName + "/" + info.versionName /*+ "呵呵，这里是我在测试"*/;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return agent;
    }
    public static void setDevice_Token(String token){
        device_Token = token;
    }
    public static String getDevice_Token(){
            return device_Token;
        }

    /**
     * 获取当前Activity实例对象
     * @return
     */
    public static Activity getCurrentActivity () {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(
                    null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
