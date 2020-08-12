package com.matt.libwrapper.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * ============================================================
 * 作 者 :    freer-2
 * 更新时间 ：2019/10/29 10:51
 * 描 述 ：https://www.jianshu.com/p/2eca433869e9
 * ============================================================
 */
public class SafeLinearLayoutManager extends LinearLayoutManager {
    public SafeLinearLayoutManager(Context context) {
        super(context);
    }

    public SafeLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public SafeLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
