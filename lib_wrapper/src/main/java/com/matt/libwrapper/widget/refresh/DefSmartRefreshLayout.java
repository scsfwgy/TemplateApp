package com.matt.libwrapper.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.lbk.lib_wrapper.widget.refresh.SmartRefreshHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshHeader;


public class DefSmartRefreshLayout extends SmartRefreshLayout {
    protected RefreshHeader mRefreshHeader;
    protected SmartRefreshFooter mRefreshFooter;

    public DefSmartRefreshLayout(Context context) {
        this(context, null);
    }

    public DefSmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs();
    }

    public void initAttrs() {
        mRefreshHeader = new SmartRefreshHeader(getContext());
        mRefreshFooter = new SmartRefreshFooter(getContext());
        setRefreshHeader(mRefreshHeader);
        setRefreshFooter(mRefreshFooter);

        //满足应用原有默认需求，需要时打开
        setEnableLoadMore(false);
    }
}
