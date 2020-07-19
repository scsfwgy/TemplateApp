package com.matt.libwrapper.ui.base

import android.content.Context
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
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
}