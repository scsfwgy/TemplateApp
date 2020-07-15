package com.matt.libwrapper.widget

import android.util.Log
import com.matt.libwrapper.LibWrapperInit
import com.matt.libwrapper.widget.simple.SimpleCatchObserver
import com.matt.libwrapper.widget.simple.SimpleObserver
import io.reactivex.disposables.Disposable

/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 创建日期 ：2020/7/8 5:47 PM
 * 描 述 ：
 * ============================================================
 **/
open class ObserverWrapper<T>(private val mIDisposable: IDisposable? = null) :
    SimpleCatchObserver<T>() {

    val TAG = LibWrapperInit.TAG

    override fun onCatchSubscribe(d: Disposable) {
        super.onCatchSubscribe(d)
        val iDisposable = mIDisposable
        if (iDisposable != null) {
            iDisposable.addDisposable(d)
        } else {
            Log.w(TAG, "没有传递mIDisposable,需要自己手动回收Disposable，不然会内存泄露！！！")
        }
    }
}