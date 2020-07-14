package com.matt.libwrapper.ui.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ============================================================
 * 作 者 :    matt
 * 更新时间 ：2020/03/16 11:20
 * 描 述 ：
 * ============================================================
 */
abstract class BaseFragment : Fragment() {
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

    open fun <T : ViewModel> getVMByActivity(modelClass: Class<T>): T {
        return mBaseActivity.getVM(modelClass)
    }

    open fun <T : ViewModel> getVMByFragment(modelClass: Class<T>): T {
        return ViewModelProvider(this).get(modelClass)
    }
}