package com.matt.libwrapper.ui.base.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.matt.libwrapper.R
import com.matt.libwrapper.helper.XPageHelper
import com.matt.libwrapper.ui.base.MBaseQuickAdapter
import com.matt.libwrapper.ui.base.template.Template.Companion.TEMPLATETYPE_REFRESHVIEW
import com.matt.libwrapper.widget.SafeLinearLayoutManager
import com.matt.libwrapper.widget.decoration.SimpleDividerDecoration
import kotlinx.android.synthetic.main.wrapper_activity_template_list.*

/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 创建日期 ：2020/6/29 16:09
 * 描 述 ：
 * ============================================================
 **/
abstract class TemplateListActivity<T> : TemplateBarActivity() {
    override fun templateType(): Int {
        return TEMPLATETYPE_REFRESHVIEW
    }

    override fun addChildrenView(): Any {
        return R.layout.wrapper_activity_template_list
    }

    val mPage: XPageHelper by lazy {
        pageHelper2Ins()
    }

    val mMBaseQuickAdapter: MBaseQuickAdapter<T, BaseViewHolder> by lazy {
        object : MBaseQuickAdapter<T, BaseViewHolder>(mContext, itemLayoutId()) {
            override fun convertNotNull(helper: BaseViewHolder, item: T) {
                convertItem(helper, item)
            }
        }
    }

    abstract fun itemLayoutId(): Int

    abstract fun convertItem(helper: BaseViewHolder, item: T)

    override fun onCatchCreate(savedInstanceState: Bundle?) {
        super.onCatchCreate(savedInstanceState)
        initTemplateAdapter()
        initTemplateListener()
        initTemplateData()
    }

    open fun initTemplateAdapter() {
        getRecycleView().run {
            layoutManager = SafeLinearLayoutManager(mContext)
            addItemDecoration(SimpleDividerDecoration(mContext, true, false))
            adapter = mMBaseQuickAdapter
        }
    }

    open fun initTemplateListener() {
        mMBaseQuickAdapter.setOnItemClickListener { _, _, position ->
            val it = getItemData(position)
            onItemClick(it)
        }
        val refreshEnable = refreshEnable()
        val smartRefreshLayoutContainer = getSmartRefresh()
        smartRefreshLayoutContainer.setEnableRefresh(refreshEnable)
        if (refreshEnable) {
            smartRefreshLayoutContainer.setOnRefreshListener {
                onReLoadData()
            }
        }
        if (loadMoreEnable()) {
            mMBaseQuickAdapter.setOnLoadMoreListener(OnLoadMoreListener {
                onItemLoadMore()
            })
        }
    }

    open fun initTemplateData() {
        if (autoLoadData()) {
            if (autoLoadDataByAutoRefresh()) {
                getSmartRefresh().autoRefresh()
            } else {
                onReLoadData()
            }
        }
    }

    open fun autoLoadData(): Boolean {
        return true
    }

    /**
     * 初始化加载是通过刷新触发还是直接加载数据
     */
    open fun autoLoadDataByAutoRefresh(): Boolean {
        return true
    }

    open fun refreshEnable(): Boolean {
        return true
    }

    open fun loadMoreEnable(): Boolean {
        return true
    }

    open fun addContainerHead(vararg items: View) {
        val viewGroup = viewParent
        items.forEachIndexed { index, view ->
            viewGroup.addView(view, items.size - index - 1)
        }
    }

    open fun addContainerHead(layoutId: Int) {
        val inflate = LayoutInflater.from(mActivity).inflate(layoutId, viewParent, false)
        addContainerHead(inflate)
    }

    fun getItemData(position: Int): T {
        return mMBaseQuickAdapter.getItem(position)
            ?: throw IllegalArgumentException("不允许为null,请检查逻辑")
    }

    open fun onItemLoadMore() {
        mPage.pagePlus()
        onLoadData()
    }

    open fun onReLoadData() {
        mPage.pageReset()
        onLoadData()
    }

    open fun onLoadData() {

    }

    open fun onItemClick(it: T) {

    }

    fun getRecycleView(): RecyclerView {
        return watl_rv_recycle
    }

    open fun pageHelper2Ins(): XPageHelper {
        return XPageHelper(mMBaseQuickAdapter)
    }

}