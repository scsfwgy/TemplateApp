package com.matt.libwrapper.ui.base

import android.os.Bundle
import com.matt.libwrapper.exception.ExceptionManager
import com.matt.libwrapper.exception.ExceptionType


/**
 * ============================================================
 * 作 者 :    matt@163.com
 * 更新时间 ：2019/05/08 15:35
 * 描 述 ：主动catch异常,在一定程度上更加确保应用的"安全"
 * ============================================================
 */
abstract class HandleExceptionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            onCatchCreate(savedInstanceState)
        } catch (e: Exception) {
            catchMainException(e)
        }
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
            onCatchDestroy()
        } catch (e: Exception) {
            catchMainException(e)
        }

    }

    abstract fun onCatchCreate(savedInstanceState: Bundle?)


    open fun onCatchDestroy() {

    }

    fun catchMainException(exception: Exception) {
        ExceptionManager.handlerException(exception, mActivity, ExceptionType.UI)
    }
}