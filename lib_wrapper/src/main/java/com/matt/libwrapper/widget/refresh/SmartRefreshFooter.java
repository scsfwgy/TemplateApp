package com.matt.libwrapper.widget.refresh;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.matt.libwrapper.R;
import com.scwang.smart.refresh.footer.ClassicsFooter;


/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 更新时间 ：2019/01/22 11:32
 * 描 述 ：
 * ============================================================
 */
public class SmartRefreshFooter extends ClassicsFooter {

    public SmartRefreshFooter(Context context) {
        this(context, null);
    }

    public SmartRefreshFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs();
        refreshSkinColor();
    }

    private void initAttrs() {

    }

    protected void refreshSkinColor() {
        setBackgroundColor(getSkinColor(R.color.wrapper_container_bg_design));
    }

    @ColorInt
    protected int getSkinColor(@ColorRes int colorId) {
        if (colorId == 0) return Color.TRANSPARENT;
        if (isInEditMode()) return ContextCompat.getColor(getContext(), colorId);
        return ContextCompat.getColor(getContext(), colorId);
    }

}