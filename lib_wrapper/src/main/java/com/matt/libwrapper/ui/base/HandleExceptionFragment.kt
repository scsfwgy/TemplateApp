package com.matt.libwrapper.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matt.libwrapper.exception.ExceptionManager
import com.matt.libwrapper.exception.ExceptionType
import com.matt.libwrapper.ui.base.BaseFragment
import com.matt.libwrapper.ui.base.HandleExceptionActivity


/**
 * 创建日期 : 2018/7/9
 * 描 述 :   handle错误
 */
abstract class HandleExceptionFragment : BaseFragment() {

    //内部获取rootView的一个中间变量
    private var mInterRootView: View? = null

    /**
     * 子类使用这个rootView
     */
    val mRootView: View by lazy {
        val view =
            mInterRootView ?: throw IllegalArgumentException("mInterRootView不可以为null,请检查业务逻辑是否有问题？")
        view
    }

    abstract fun layoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            onCatchCreate(savedInstanceState)
        } catch (e: Exception) {
            catchMainException(e)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            super.onCreateView(inflater, container, savedInstanceState)
            return onCatchCreateView(inflater, container, savedInstanceState)
        } catch (e: Exception) {
            catchMainException(e)
        }
        return null
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
            return onCatchDestroy()
        } catch (e: Exception) {
            catchMainException(e)
        }
    }

    open fun onCatchCreate(savedInstanceState: Bundle?) {
        getBundleExtras(arguments)
    }

    open fun onCatchCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mInterRootView == null) {
            val rootView = inflater.inflate(layoutId(), container, false)
            mInterRootView = rootView
            safeInitAll(rootView)
        }
        return mInterRootView
    }

    open fun onCatchDestroy() {}

    open fun getBundleExtras(bundle: Bundle?) {}

    abstract fun safeInitAll(rootView: View)

    open fun catchMainException(exception: Exception) {
        ExceptionManager.handlerException(exception, mBaseActivity, ExceptionType.UI)
    }
}
