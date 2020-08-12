package com.matt.libwrapper.ui.base.template

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.matt.libwrapper.R
import com.matt.libwrapper.exception.ParamsException
import com.matt.libwrapper.ui.base.HandleExceptionActivity
import com.matt.libwrapper.ui.base.template.Template.Companion.TEMPLATETYPE_DEFVIEW
import com.matt.libwrapper.ui.base.template.Template.Companion.TEMPLATETYPE_REFRESHVIEW
import com.matt.libwrapper.ui.base.template.Template.Companion.TEMPLATETYPE_STRETCHSCROLLVIEW
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.wrapper_activity_template_bar.*
import kotlinx.android.synthetic.main.wrapper_include_app_bar.view.*

abstract class TemplateBarActivity : HandleExceptionActivity() {

    val viewParent: ViewGroup by lazy {
        when {
            templateType() == TEMPLATETYPE_DEFVIEW -> getDefView()
            templateType() == TEMPLATETYPE_STRETCHSCROLLVIEW -> getScrollViewContainer()
            templateType() == TEMPLATETYPE_REFRESHVIEW -> getSmartRefreshContainer()
            else -> throw IllegalArgumentException("templateType()参数错误！")
        }
    }

    @Template.Companion.TemplateType
    abstract fun templateType(): Int

    abstract fun addChildrenView(): Any//layout resId  ；view


    abstract fun renderTitle(): Any

    protected open fun renderTemplateViews() {
        val leftTxtView = getLeftTxtView()
        //渲染标题
        when (val title = renderTitle()) {
            is String -> {
                leftTxtView.text = title
            }
            is Int -> {
                leftTxtView.setText(title)
            }
            else -> {
                throw ParamsException("title 类型错误")
            }
        }
        //采用了哪一组控件
        val defViewContainer = getDefView()
        val scrollViewContainer = getScrollView()
        val smartRefreshLayoutContainer = getSmartRefresh()
        when {
            templateType() == TEMPLATETYPE_DEFVIEW -> {
                defViewContainer.visibility = View.VISIBLE
                scrollViewContainer.visibility = View.GONE
                smartRefreshLayoutContainer.visibility = View.GONE
            }
            templateType() == TEMPLATETYPE_STRETCHSCROLLVIEW -> {
                defViewContainer.visibility = View.GONE
                scrollViewContainer.visibility = View.VISIBLE
                smartRefreshLayoutContainer.visibility = View.GONE
            }
            templateType() == TEMPLATETYPE_REFRESHVIEW -> {
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
                LayoutInflater.from(mActivity).inflate(childrenView, viewParent)
            }
            else -> throw IllegalArgumentException("子view类型只能是：View、int")
        }
    }

    /**
     * 操作toolBar,重载实现大部分按钮
     */
    fun setTemplateToolBarMsg(
        leftTxt: String? = null,
        rightTxt: String? = null,
        rightTxtClickListener: View.OnClickListener? = null,
        rightImage: Drawable? = null,
        rightImageClickListener: View.OnClickListener? = null
    ) {
        val leftTxtView = getLeftTxtView()
        val rightImgView = getRightImgView()
        val rightTxtView = getRightTxtView()

        leftTxtView.text = leftTxt

        if (rightTxt != null) {
            rightTxtView.run {
                visibility = View.VISIBLE
                text = rightTxt
                setOnClickListener(rightTxtClickListener)
            }
        } else {
            rightTxtView.visibility = View.GONE
        }

        if (rightImage != null) {
            rightImgView.run {
                visibility = View.VISIBLE
                setImageDrawable(rightImage)
                setOnClickListener(rightImageClickListener)
            }
        } else {
            rightImgView.visibility = View.GONE
        }
    }

    fun getDefView(): LinearLayout {
        return watb_ll_defViewContiner
    }

    fun getScrollView(): ScrollView {
        return watb_ssv_scrollView
    }

    fun getScrollViewContainer(): LinearLayout {
        return watb_ll_scrollViewContiner
    }

    fun getSmartRefresh(): SmartRefreshLayout {
        return watb_dsrl_refreshView
    }

    fun getSmartRefreshContainer(): LinearLayout {
        return watb_ll_refreshViewContiner
    }

    fun getLeftImageView(): ImageView {
        return watb_i_barContiner.wiab_iv_finish
    }

    fun getLeftTxtView(): TextView {
        return watb_i_barContiner.wiab_tv_title
    }

    fun getRightTxtView(): TextView {
        return watb_i_barContiner.wiab_tv_rightTxt
    }

    fun getRightImgView(): ImageView {
        return watb_i_barContiner.wiab_iv_rightImage
    }

    fun getBarContainer(): View {
        return watb_i_barContiner
    }

    override fun onCatchCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.wrapper_activity_template_bar)
        renderTemplateViews()
        initTemplateListeners()
    }

    private fun initTemplateListeners() {
        getLeftImageView().setOnClickListener {
            handlerBackEvent()
        }

        if (templateType() == TEMPLATETYPE_REFRESHVIEW) {
            getSmartRefresh().setOnRefreshListener {
                onRefresh()
            }
        }
    }

    open fun onRefresh() {

    }
}