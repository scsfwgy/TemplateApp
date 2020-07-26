package com.matt.libwrapper.ui.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import razerdp.basepopup.BasePopupWindow

/**
 *
 * Author : wgyscsf@163.com
 * Github : https://github.com/scsfwgy
 * Date   : 2020/7/19 11:11 AM
 * 描 述 ：
 *
 **/
abstract class BasePopWindow(val mContext: Context) : BasePopupWindow(mContext) {

    val TAG: String by lazy {
        javaClass::class.java.simpleName
    }

    val mActivity: BaseActivity by lazy {
        mContext as BaseActivity
    }

    /**
     * 给一个默认的入场动画
     */
    override fun onCreateShowAnimation(): Animation {
        val showAnimation: Animation = ScaleAnimation(1f, 1f, 0f, 1f)
        showAnimation.duration = 200
        return showAnimation
    }

    /**
     * 给一个默认的出场动画
     */
    override fun onCreateDismissAnimation(): Animation {
        val showAnimation: Animation = ScaleAnimation(1f, 1f, 1f, 0f)
        showAnimation.duration = 200
        return showAnimation
    }

    fun getDrawable2(@DrawableRes drawableId: Int): Drawable {
        return ContextCompat.getDrawable(mContext, drawableId)!!
    }

    @ColorInt
    fun getColor2(@ColorRes colorId: Int): Int {
        return ContextCompat.getColor(mContext, colorId)
    }

    fun getString2(@StringRes stringId: Int): String {
        return mContext.getString(stringId)
    }
}