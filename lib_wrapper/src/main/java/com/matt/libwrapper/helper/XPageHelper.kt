package com.matt.libwrapper.helper

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.matt.libwrapper.ui.base.MBaseQuickAdapter

/**
 * ============================================================
 * 作 者 :    wgyscsf@163.com
 * 更新时间 ：2019/06/28 19:00
 * 描 述 ：
 * ============================================================
 */
class XPageHelper(
    mBaseQuickAdapter: MBaseQuickAdapter<*, BaseViewHolder>,
    pageSize: Int = PAGE_DEFLIMIT_V
) {
    val mPageSize: Int = pageSize
    val mMBaseQuickAdapter = mBaseQuickAdapter
    val mPageMap: HashMap<String, Any> by lazy {
        val map = HashMap<String, Any>()
        map[PAGE_LIMIT_K] = this.mPageSize
        map[PAGE_OFFSET_K] = 0
        map
    }

    var mCurrPage = FIRST_PAGE

    fun pagePlus(): XPageHelper {
        mCurrPage++
        mPageMap[PAGE_OFFSET_K] = mMBaseQuickAdapter.data.size
        return this
    }

    val isFirstCurr: Boolean
        get() = mCurrPage == FIRST_PAGE

    fun pageReset(): XPageHelper {
        mCurrPage = FIRST_PAGE
        mPageMap[PAGE_OFFSET_K] = 0
        return this
    }

    fun processDataList(mMBaseQuickAdapter: MBaseQuickAdapter<*, BaseViewHolder>, it: List<*>?) {
        mMBaseQuickAdapter.loadMulitPageData(it as List<Nothing>?, mCurrPage, mPageSize)
    }

    companion object {
        const val FIRST_PAGE = 0
        const val PAGE_DEFLIMIT_V = 10
        const val PAGE_LIMIT_K = "limit"
        const val PAGE_OFFSET_K = "offset"

        @JvmOverloads
        fun newInstans(
            mBaseQuickAdapter: MBaseQuickAdapter<*, BaseViewHolder>,
            pageSize: Int = PAGE_DEFLIMIT_V
        ): XPageHelper {
            return XPageHelper(mBaseQuickAdapter, pageSize)
        }
    }
}