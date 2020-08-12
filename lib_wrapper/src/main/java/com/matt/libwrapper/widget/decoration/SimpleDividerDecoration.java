package com.matt.libwrapper.widget.decoration;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.matt.libwrapper.R;


/**
 * ============================================================
 * <p/>
 * 版 权 :    天厚投资 版权所有 (c)
 * 描 述 :    RecyclerView分割线 颜色 Drawable等(方向自动识别)。
 * <p/>
 * ============================================================
 **/
public class SimpleDividerDecoration extends DividerDecoration {

    private static final int DEF_PADDING = SizeUtils.dp2px(12);

    /**
     * 需要切换皮肤必须实现这个方法
     */
    public SimpleDividerDecoration(Context context) {
        this(context, true, true);
    }

    public SimpleDividerDecoration(Context context, boolean leftPadding, boolean rightPadding) {
        if (leftPadding) setLeftPadding(DEF_PADDING);
        if (rightPadding) setRightPadding(DEF_PADDING);

        refreshSkinBgColor(context);
    }

    public void refreshSkinBgColor(Context context) {
        refreshSkinBgColor(context, ContextCompat.getColor(context, R.color.wrapper_item_design));
    }

    public void refreshSkinBgColor(Context context, @ColorInt int bgColor) {
        refreshSkinBgColor(context, ContextCompat.getColor(context, R.color.wrapper_divider_design), bgColor);
    }

    public void refreshSkinBgColor(Context context, @ColorInt int divisionColor, @ColorInt int bgColor) {
        if (context == null) {
            throw new IllegalArgumentException("上下文为空");
        }
        setDividerColor(divisionColor);
        setBackgroundColor(bgColor);
        setDrawFirstDivider(true);
        setDrawLastDivider(false);
    }
}
