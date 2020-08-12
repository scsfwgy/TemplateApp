package com.matt.template

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.matt.libwrapper.ui.base.template.Template
import com.matt.libwrapper.ui.base.template.TemplateBarActivity

class AppBarDemoActivity : TemplateBarActivity() {
    companion object {
        fun goIntent(context: Context) {
            val intent = Intent(context, AppBarDemoActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun templateType(): Int {
        return Template.TEMPLATETYPE_DEFVIEW
    }

    override fun addChildrenView(): Any {
        return R.layout.activity_app_bar
    }

    override fun renderTitle(): Any {
        return "通用appBarDemo"
    }

    override fun onCatchCreate(savedInstanceState: Bundle?) {
        super.onCatchCreate(savedInstanceState)

    }
}