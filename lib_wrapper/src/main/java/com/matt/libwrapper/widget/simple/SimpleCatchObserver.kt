package com.matt.libwrapper.widget.simple

import com.matt.libwrapper.exception.ExceptionManager
import com.matt.libwrapper.exception.ExceptionType
import io.reactivex.disposables.Disposable

/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 创建日期 ：2020/7/8 5:43 PM
 * 描 述 ：
 * ============================================================
 */
abstract class SimpleCatchObserver<T> : SimpleObserver<T>() {
    override fun onSubscribe(d: Disposable) {
        try {
            onCatchSubscribe(d)
        } catch (e: Exception) {
            onHandlerException(e)
        }
    }

    override fun onError(e: Throwable) {
        try {
            onCatchError(e)
        } catch (e: Exception) {
            onHandlerException(e)
        }
    }

    override fun onNext(t: T) {
        try {
            onCatchNext(t)
        } catch (e: Exception) {
            onHandlerException(e)
        }
    }


    override fun onComplete() {
        try {
            onCatchComplete()
        } catch (e: Exception) {
            onHandlerException(e)
        }
    }

    open fun onCatchSubscribe(d: Disposable) {

    }

    open fun onCatchError(e: Throwable) {

    }

    open fun onCatchNext(t: T) {

    }

    open fun onCatchComplete() {

    }

    open fun onHandlerException(e: Throwable) {
        ExceptionManager.handlerException(e, null, ExceptionType.RX)
    }

}