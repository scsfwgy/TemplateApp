package com.matt.libwrapper.ui.base.template

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import com.matt.libwrapper.R
import com.matt.libwrapper.exception.ParamsException
import com.matt.libwrapper.ui.base.LazyLoadBaseFragment
import com.matt.libwrapper.ui.base.loading.IRefresh
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.wrapper_fragment_template_bar.view.*

/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 创建日期 ：2020/6/29 14:45
 * 描 述 ：
 * ============================================================
 **/
abstract class TemplateBarFragment : LazyLoadBaseFragment(), IRefresh {
    val viewParent: ViewGroup by lazy {
        when {
            templateType() == Template.TEMPLATETYPE_DEFVIEW -> getDefView()
            templateType() == Template.TEMPLATETYPE_STRETCHSCROLLVIEW -> getScrollViewContainer()
            templateType() == Template.TEMPLATETYPE_REFRESHVIEW -> this.getSmartRefreshContainer()
            else -> throw IllegalArgumentException("templateType()参数错误！")
        }
    }

    @Template.Companion.TemplateType
    abstract fun templateType(): Int

    abstract fun addChildrenView(): Any//layout resId 、layout View


    override fun layoutId(): Int {
        return R.layout.wrapper_fragment_template_bar
    }

    override fun safeInitAll(rootView: View) {
        renderTemplateViews()
        initTemplateListeners()
    }


    private fun renderTemplateViews() {
        //采用了哪一组控件
        val defViewContainer = getDefView()
        val scrollViewContainer = getScrollView()
        val smartRefreshLayoutContainer = this.getSmartRefresh()
        when {
            templateType() == Template.TEMPLATETYPE_DEFVIEW -> {
                defViewContainer.visibility = View.VISIBLE
                scrollViewContainer.visibility = View.GONE
                smartRefreshLayoutContainer.visibility = View.GONE
            }
            templateType() == Template.TEMPLATETYPE_STRETCHSCROLLVIEW -> {
                defViewContainer.visibility = View.GONE
                scrollViewContainer.visibility = View.VISIBLE
                smartRefreshLayoutContainer.visibility = View.GONE
            }
            templateType() == Template.TEMPLATETYPE_REFRESHVIEW -> {
                defViewContainer.visibility = View.GONE
                scrollViewContainer.visibility = View.GONE
                smartRefreshLayoutContainer.visibility = View.VISIBLE
            }
            else -> {
                throw ParamsException("参数错误")
            }
        }

        //添加子控件
        when (val childrenView = addChildrenView()) {
            is View -> viewParent.addView(childrenView)
            is Int -> {
                LayoutInflater.from(mBaseActivity).inflate(childrenView, viewParent)
            }
            else -> throw IllegalArgumentException("子view类型只能是：View、int")
        }
    }

    private fun initTemplateListeners() {
        if (templateType() == Template.TEMPLATETYPE_REFRESHVIEW) {
            this.getSmartRefresh().setOnRefreshListener {
                onRefresh()
            }
        }
    }

    open fun onRefresh() {

    }


    fun getDefView(): LinearLayout {
        return mRootView.wftb_ll_defViewContiner
    }

    fun getScrollView(): ScrollView {
        return mRootView.wftb_ssv_scrollView
    }

    fun getScrollViewContainer(): LinearLayout {
        return mRootView.wftb_ll_scrollViewContiner
    }

    fun getSmartRefresh(): SmartRefreshLayout {
        return mRootView.wftb_dsrl_refreshView
    }

    fun getSmartRefreshContainer(): LinearLayout {
        return mRootView.wftb_ll_refreshViewContiner
    }

    override fun onRefreshFinish(success: Boolean) {
        getSmartRefresh().finishRefresh(success)
    }
}