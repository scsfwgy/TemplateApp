package com.matt.libimport.widget.hook;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ============================================================
 * 作 者 :    freer-2
 * 更新时间 ：2019/05/28 10:43
 * 描 述 ：解决api28透明度和方向同时设定崩溃的一个bug。注意一定是api28才执行以下修复代码，因为用了反射，会在一定程度消耗性能。
 * 不设置windowIsTranslucent就不用hook
 * ============================================================
 */
public class Api28Hook {
    public static final String TAG = Api28Hook.class.getSimpleName();
    /**
     * 是否禁用hook
     */
    private static boolean forbidHook = false;

    public static boolean isForbidHook() {
        return forbidHook;
    }

    public static void setForbidHook(boolean forbidHook) {
        Api28Hook.forbidHook = forbidHook;
    }

    /**
     * onCreate时在在onCreate的super.onCreate()前调用
     *
     * @param activity
     */
    public static void onCreateHook(@NonNull Activity activity) {
        if (isForbidHook()) return;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating(activity)) {
            boolean result = fixOrientation(activity);
            Log.i(TAG, "onCreate fixOrientation when Oreo, result = " + result);
        }
    }

    /**
     * 是否可以设置Orientation？在setRequestedOrientation的super.setRequestedOrientation()前调用
     *
     * @param activity
     * @return true:终止执行super.setRequestedOrientation()
     */
    public static boolean setRequestedOrientationHook(Activity activity) {
        if (isForbidHook()) return false;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating(activity)) {
            Log.w(TAG, "avoid calling setRequestedOrientation when Oreo.");
            return true;
        }
        return false;
    }

    private static boolean isTranslucentOrFloating(Activity activity) {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = activity.obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }


    /**
     * 修复缺陷：不允许设置固定方向
     *
     * @param activity
     * @return 是否修复成功
     */
    public static boolean fixOrientation(Activity activity) {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(activity);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
