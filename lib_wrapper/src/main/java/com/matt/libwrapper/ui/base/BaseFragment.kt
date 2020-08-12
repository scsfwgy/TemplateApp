package com.matt.libwrapper.ui.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matt.libwrapper.ui.base.loading.IDisposable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

/**
 * ============================================================
 * 作 者 :    matt
 * 更新时间 ：2020/03/16 11:20
 * 描 述 ：
 * ============================================================
 */
abstract class BaseFragment : Fragment(),
    IDisposable {
    val TAG: String by lazy {
        javaClass::class.java.simpleName
    }

    val mContext: Context by lazy {
        context ?: throw IllegalArgumentException("mContext不允许为null")
    }
    val mBaseFragment by lazy {
        this
    }
    val mBaseActivity: BaseActivity by lazy {
        val context = mContext
        if (context is BaseActivity) {
            context
        } else {
            throw IllegalArgumentException("Activity必须继承BaseActivity去实现逻辑")
        }
    }

    val mCompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (eventBusEnable()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (eventBusEnable()) {
            EventBus.getDefault().unregister(this)
        }
        mCompositeDisposable.clear()
    }

    open fun eventBusEnable(): Boolean {
        return false
    }

    open fun <T : ViewModel> getVMByActivity(modelClass: Class<T>): T {
        return mBaseActivity.getVM(modelClass)
    }

    open fun <T : ViewModel> getVMByFragment(modelClass: Class<T>): T {
        return ViewModelProvider(this).get(modelClass)
    }

    override fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }
}