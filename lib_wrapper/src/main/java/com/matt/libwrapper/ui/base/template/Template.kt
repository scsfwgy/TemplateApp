package com.matt.libwrapper.ui.base.template

import androidx.annotation.IntDef

/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 创建日期 ：2020/6/29 14:46
 * 描 述 ：
 * ============================================================
 **/
class Template {
    companion object {
        const val TEMPLATETYPE_DEFVIEW = 0
        const val TEMPLATETYPE_STRETCHSCROLLVIEW = 1
        const val TEMPLATETYPE_REFRESHVIEW = 2

        /**
         * 模板类型
         */
        @IntDef(TEMPLATETYPE_DEFVIEW, TEMPLATETYPE_STRETCHSCROLLVIEW, TEMPLATETYPE_REFRESHVIEW)
        @Retention(AnnotationRetention.SOURCE)
        annotation class TemplateType
    }
}