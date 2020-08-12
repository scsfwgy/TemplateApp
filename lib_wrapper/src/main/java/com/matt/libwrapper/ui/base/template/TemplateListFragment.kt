package com.matt.libwrapper.ui.base.template

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
import kotlinx.android.synthetic.main.wrapper_fragment_template_list.view.*


/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 创建日期 ：2020/6/29 16:09
 * 描 述 ：
 * ============================================================
 **/
abstract class TemplateListFragment<T> : TemplateBarFragment() {
    override fun templateType(): Int {
        return TEMPLATETYPE_REFRESHVIEW
    }

    override fun addChildrenView(): Any {
        return R.layout.wrapper_fragment_template_list
    }

    val mPage: XPageHelper by lazy {
        pageHelper2Ins()
    }

    val mMBaseQuickAdapter: MBaseQuickAdapter<T, BaseViewHolder> by lazy {
        baseQuickAdapter()
            ?: object : MBaseQuickAdapter<T, BaseViewHolder>(mContext, itemLayoutId()) {
                override fun convertNotNull(helper: BaseViewHolder, item: T) {
                    convertItem(helper, item)
                }
            }
    }

    abstract fun itemLayoutId(): Int

    abstract fun convertItem(helper: BaseViewHolder, item: T)


    /**
     * 允许子类实现自己的adapter,如果不实现则使用默认的
     */
    open fun baseQuickAdapter(): MBaseQuickAdapter<T, BaseViewHolder>? {
        return null
    }

    override fun safeInitAll(rootView: View) {
        super.safeInitAll(rootView)
        initTemplateAdapter()
        initTemplateListener()
        initTemplateData()
    }

    open fun initTemplateAdapter() {
        getRecycleView().run {
            val generateItemDecoration = generateItemDecoration()
            layoutManager = SafeLinearLayoutManager(mContext)
            if (generateItemDecoration != null) {
                addItemDecoration(generateItemDecoration)
            }
            adapter = mMBaseQuickAdapter
        }
    }

    open fun generateItemDecoration(): RecyclerView.ItemDecoration? {
        return SimpleDividerDecoration(mContext, true, false)
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
            if (refreshEnable() && autoLoadDataByAutoRefresh()) {
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
        return false
    }

    open fun addContainerHead(vararg items: View) {
        val viewGroup = viewParent
        items.forEachIndexed { index, view ->
            viewGroup.addView(view, items.size - index - 1)
        }
    }

    open fun addContainerHead(layoutId: Int) {
        val inflate = LayoutInflater.from(mBaseActivity).inflate(layoutId, viewParent, false)
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
        return mRootView.wftl_rv_recycle
    }

    open fun pageHelper2Ins(): XPageHelper {
        return XPageHelper(mMBaseQuickAdapter)
    }

}