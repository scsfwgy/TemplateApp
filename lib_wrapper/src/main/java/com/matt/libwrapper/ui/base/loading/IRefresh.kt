package com.matt.libwrapper.ui.base.loading

/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 创建日期 ：2020/8/10 2:26 PM
 * 描 述 ：
 * ============================================================
 **/
interface IRefresh {
    fun onRefreshFinish(success: Boolean = true)
}