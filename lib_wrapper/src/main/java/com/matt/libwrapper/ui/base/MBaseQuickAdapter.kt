package com.matt.libwrapper.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.matt.libwrapper.R
import com.matt.libwrapper.exception.ParamsException

/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 更新时间 ：2019/06/20 17:14
 * 描 述 ：
 * ============================================================
 */
abstract class MBaseQuickAdapter<T, K : BaseViewHolder> constructor(
    context: Context,
    layoutResId: Int,
    data: MutableList<T>? = null
) : BaseQuickAdapter<T, K>(layoutResId, data), LoadMoreModule {

    val mEmptyView: View by lazy {
        LayoutInflater.from(mBaseActivity).inflate(R.layout.wrapper_default_empty_text, null)
    }

    lateinit var mEmptyText: TextView

    val mBaseActivity = context

    private val mLoadingFormat: String by lazy {
        mBaseActivity.getString(R.string.wrapper_common_data_Loading)
    }
    private val mNoDataFormat: String by lazy {
        mBaseActivity.getString(R.string.wrapper_common_data_NoData)
    }

    init {
        initAttrs()
    }

    override fun convert(holder: K, item: T) {
        convertNotNull(holder, item)
    }

    abstract fun convertNotNull(helper: K, item: T)

    //默认属性
    private fun initAttrs() { //empty
        mEmptyText = mEmptyView.findViewById(R.id.empty_text)
        setEmptyView(mEmptyView)
        showLoadingText()
        loadMoreModule?.isEnableLoadMore = false
        loadMoreModule?.preLoadNumber = 1
        headerWithEmptyEnable = true
        closeLoadAnimation()
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener?) {
        loadMoreModule?.setOnLoadMoreListener(listener)
    }

    fun loadMoreComplete() {
        loadMoreModule?.loadMoreComplete()
    }

    fun loadMoreEnd(gone: Boolean = false) {
        loadMoreModule?.loadMoreEnd(gone)
    }

    @JvmOverloads
    fun showLoadingText(str: String? = null) {
        if (StringUtils.isEmpty(str)) {
            mEmptyText.text = mLoadingFormat
            return
        }
        mEmptyText.text = str
    }

    @JvmOverloads
    fun showNoDataText(str: String? = null) {
        if (StringUtils.isEmpty(str)) {
            mEmptyText.text = mNoDataFormat
            return
        }
        mEmptyText.text = str
    }

    fun getmEmptyText(): TextView {
        return mEmptyText
    }


    fun loadSinglePageData(newListData: List<T>?) {
        loadSinglePageData(this, newListData)
    }

    fun loadMulitPageData(
        newListData: List<T>?,
        page: Int,
        perPageSize: Int = 10,
        firstPageIndex: Int = 0,
        forceHasMore: Boolean = false,
        loadMoreViewGone: Boolean = true
    ) {
        loadMulitPageData(
            this,
            newListData, page,
            perPageSize,
            firstPageIndex,
            forceHasMore,
            loadMoreViewGone
        )
    }

    fun closeLoadAnimation() {
        animationEnable = false
    }

    fun setEnableLoadMore(enableLoadMore: Boolean) {
        loadMoreModule?.isEnableLoadMore = enableLoadMore
    }

    companion object {
        /**
         * 数据只有单页数据时使用
         */
        fun <T, K : BaseViewHolder> loadSinglePageData(
            mBaseQuickAdapter: MBaseQuickAdapter<T, K>,
            newListData: List<T>?
        ) {
            val allDataList = mBaseQuickAdapter.data
            allDataList.clear()
            if (newListData == null || newListData.isEmpty()) {
                mBaseQuickAdapter.showNoDataText()
                mBaseQuickAdapter.notifyDataSetChanged()
                return
            }
            allDataList.addAll(newListData)
            mBaseQuickAdapter.notifyDataSetChanged()
        }

        /**
         * 加载分页数据。容易踩的坑，这里的封装解决这些问题：
         * 1.null list处理，后端数据过来直接扔进来即可
         * 2.暂无数据处理
         * 3.[mBaseQuickAdapter.loadMoreComplete()]之后再刷新也不会再加载更多。
         *
         * @param newListData 网络过来的数据列表
         * @param forceHasMore 是否强制允许加载更多，默认false。
         * true:不管[newListData.size]是否小于[perPageSize],都允许继续加载更多
         * @param loadMoreViewVisable 是否隐藏加载更多后的View
         */
        fun <T, K : BaseViewHolder> loadMulitPageData(
            mBaseQuickAdapter: MBaseQuickAdapter<T, K>,
            newListData: List<T>?, page: Int,
            perPageSize: Int = 10,
            firstPageIndex: Int = 0,
            forceHasMore: Boolean = false,
            loadMoreViewGone: Boolean = true
        ) {
            if (perPageSize <= 0) throw ParamsException("perPageSize<=0 not allow!!!")
            if (page < firstPageIndex) throw ParamsException("page<firstPage not allow!!!")
            //特殊处理
            if (newListData == null) {
                if (mBaseQuickAdapter.data.isEmpty()) {
                    mBaseQuickAdapter.showNoDataText()
                    mBaseQuickAdapter.notifyDataSetChanged()
                }
                return
            }
            val firstPage = page == firstPageIndex
            if (firstPage) {
                //重置状态：比如加载更多状态（可以再次加载更多）、loading状态
                mBaseQuickAdapter.setNewData(newListData.toMutableList())
            } else {
                mBaseQuickAdapter.data.addAll(newListData)
            }
            val allDataList = mBaseQuickAdapter.data
            if (allDataList.isEmpty()) {
                mBaseQuickAdapter.showNoDataText()
                mBaseQuickAdapter.notifyDataSetChanged()
                return
            }
            if (newListData.size < perPageSize && !forceHasMore) {
                //没有更多数据
                mBaseQuickAdapter.loadMoreEnd(loadMoreViewGone)
            } else {
                //本次结束，还有更多数据
                mBaseQuickAdapter.loadMoreComplete()
            }
            mBaseQuickAdapter.notifyDataSetChanged()
        }

    }

}