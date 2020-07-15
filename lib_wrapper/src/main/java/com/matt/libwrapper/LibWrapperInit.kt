package com.matt.libwrapper

import android.app.Application
import com.matt.libimport.LibImportInit

/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 创建日期 ：2020/7/14 11:23 AM
 * 描 述 ：
 * ============================================================
 **/
object LibWrapperInit {
    val TAG = LibWrapperInit::class.java.simpleName
    fun init(application: Application) {
        LibImportInit.init(application)
    }
}