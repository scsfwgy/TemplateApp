package com.matt.libwrapper.utils

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by chao.qu at 2017/10/20
 *
 * @author quchao
 */
object RxUtils {
    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return FlowableTransformer
    </T> */
    fun <T> rxFlSchedulerHelper(): FlowableTransformer<T, T> {
        return FlowableTransformer { flowable: Flowable<T> ->
            flowable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
    </T> */
    fun <T> rxObSchedulerHelper(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable: Observable<T> ->
            observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 得到 Observable
     *
     * @param <T> 指定的泛型类型
     * @return Observable
    </T> */
    private fun <T> createData(t: T): Observable<T> {
        return Observable.create { emitter: ObservableEmitter<T> ->
            try {
                emitter.onNext(t)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    @JvmOverloads
    @JvmStatic
    fun timer(milliseconds: Long, delay: Long? = null): Observable<Long> {
        return Observable.timer(delay ?: milliseconds, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun interval(milliseconds: Long, delay: Long? = null): Observable<Long> {
        return Observable.interval(delay ?: milliseconds, milliseconds, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}