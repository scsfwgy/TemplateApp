package com.matt.template

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.matt.libwrapper.ui.base.template.Template
import com.matt.libwrapper.ui.base.template.TemplateBarActivity
import com.matt.libwrapper.ui.base.template.TemplateListActivity
import kotlinx.android.synthetic.main.item_activity_list_demo.view.*

class ListDemoActivity : TemplateListActivity<String>() {
    companion object {
        fun goIntent(context: Context) {
            val intent = Intent(context, ListDemoActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun renderTitle(): Any {
        return "通用列表Demo"
    }

    override fun itemLayoutId(): Int {
        return R.layout.item_activity_list_demo
    }

    override fun convertItem(helper: BaseViewHolder, item: String) {
        helper.itemView.run {
            iald_tv_text.text = item
        }
    }


    override fun onCatchCreate(savedInstanceState: Bundle?) {
        super.onCatchCreate(savedInstanceState)
    }

    override fun onLoadData() {
        super.onLoadData()
        val list = ArrayList<String>()
        for (index in 0..100) {
            list.add(index.toString())
        }
        mMBaseQuickAdapter.loadSinglePageData(list)

        getSmartRefresh().finishRefresh()
    }


}