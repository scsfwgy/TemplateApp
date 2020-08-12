package com.matt.template

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()
    }

    private fun initListener() {
        am_b_appbar.setOnClickListener { AppBarDemoActivity.goIntent(this) }
        am_b_list.setOnClickListener {
            ListDemoActivity.goIntent(this)
        }
    }
}