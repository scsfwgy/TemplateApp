package com.matt.libwrapper.exception


/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 更新时间 ：2019/05/08 15:42
 * 描 述 ：
 * ============================================================
 */
class ApiSuccessException(message: String? = null, throwable: Throwable? = null)
    : BaseException(message, throwable)