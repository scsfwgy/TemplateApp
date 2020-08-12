package com.matt.libwrapper.ui.base

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.KeyEvent
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.matt.libimport.widget.SwipePanel
import com.matt.libimport.widget.hook.Api28Hook
import com.matt.libimport.widget.statusbar.StatusBarUtil
import com.matt.libwrapper.R
import com.matt.libwrapper.ui.base.loading.IDisposable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

/**
 * ============================================================
 * 作 者 :    matt
 * 更新时间 ：2020/03/07 10:32
 * 描 述 ：
 * ============================================================
 */
abstract class BaseActivity : AppCompatActivity(),
    IDisposable {

    val TAG: String by lazy {
        javaClass::class.java.simpleName
    }

    val mContext: Context by lazy {
        this
    }
    val mActivity: BaseActivity by lazy {
        this
    }

    val mCompositeDisposable by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        CompositeDisposable()
    }

    fun show(msg: String?, short: Boolean = true) {
        if (msg == null) return
        if (short) {
            ToastUtils.showShort(msg)
        } else {
            ToastUtils.showLong(msg)
        }
    }

    open fun isActivityFinish(): Boolean {
        val activity: BaseActivity = this
        return activity.isFinishing || activity.isDestroyed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (forceOrientationEnable()) {
            requestedOrientation = requestedOrientation()
        }
        super.onCreate(savedInstanceState)
        getIntentExtras(intent)
        if (eventBusEnable()) {
            EventBus.getDefault().register(this)
        }
        if (swipeBackEnable()) {
            swipeBack()
        }
        if (statusBarEnable()) {
            statusBar()
        }
    }


    open fun eventBusEnable(): Boolean {
        return false
    }

    open fun getIntentExtras(intent: Intent) {}

    override fun onDestroy() {
        super.onDestroy()
        if (eventBusEnable()) {
            EventBus.getDefault().unregister(this)
        }
        if (mCompositeDisposable.size() > 0) {
            mCompositeDisposable.clear()
        }
    }

    /**
     * 是否应用沉浸式
     *
     * @return 默认启用
     */
    open fun statusBarEnable(): Boolean {
        return true
    }

    /**
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @return 默认不启用
     */
    open fun statusBarTranslucent(): Boolean {
        return false
    }

    /**
     * 状态栏字体颜色
     *
     * @return 默认黑色
     */
    open fun isLightModel(): Boolean {
        return true
    }

    open fun statusBar(
        @ColorInt themeColorId: Int, @IntRange(
            from = 0,
            to = 255
        ) statusBarAlpha: Int = 0
    ) {
        //默认设置为Light模式
        if (isLightModel()) {
            StatusBarUtil.setLightMode(this)
        } else {
            StatusBarUtil.setDarkMode(this)
        }
        if (!swipeBackEnable()) {
            StatusBarUtil.setColor(mActivity, themeColorId, statusBarAlpha)
        } else {
            // StatusBarUtil.setColorForSwipeBack(mBaseActivity, themeColorId, statusBarAlpha);
            StatusBarUtil.setColor(mActivity, themeColorId, statusBarAlpha)
        }
        if (statusBarTranslucent()) {
            StatusBarUtil.setTranslucent(mActivity)
        }
    }

    open fun statusBar() {
        statusBar(getColor2(R.color.wrapper_appBar_design), 0)
    }

    /**
     * 是否允许侧滑删除
     *
     * @return 默认允许
     */
    open fun swipeBackEnable(): Boolean {
        return true
    }

    open fun getSwipePanel(): SwipePanel {
        val swipeLayout = SwipePanel(this)
        swipeLayout.setLeftDrawable(R.drawable.wrapper_base_back)
        swipeLayout.setLeftEdgeSize(SizeUtils.dp2px(15f))
        swipeLayout.setRightDrawable(R.drawable.wrapper_base_back)
        swipeLayout.setRightEdgeSize(SizeUtils.dp2px(15f))
        swipeLayout.wrapView(mActivity.findViewById(android.R.id.content))
        return swipeLayout
    }

    open fun swipeBack() {
        val swipePanel = getSwipePanel()
        swipePanel.setOnFullSwipeListener { direction ->
            swipePanel.close(direction)
            onSwipeBackListener(swipePanel, direction)
        }
    }

    open fun onSwipeBackListener(swipePanel: SwipePanel, direction: Int) {
        handlerBackEvent()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handlerBackEvent()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    open fun handlerBackEvent() {
        finish()
    }

    fun showToast(msg: String?, showLong: Boolean = false) {
        if (msg == null) return
        if (showLong) {
            ToastUtils.showLong(msg)
        } else {
            ToastUtils.showShort(msg)
        }
    }

    protected fun addFragment(fragment: BaseFragment?, @IdRes containerViewId: Int) {
        if (fragment == null) return
        supportFragmentManager.beginTransaction()
            .add(containerViewId, fragment, fragment::class.java.simpleName)
            .commitAllowingStateLoss()
    }

    open fun <T : ViewModel> getVM(modelClass: Class<T>): T {
        return ViewModelProvider(this).get(modelClass)
    }

    override fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.addAll(disposable)
    }

    protected open fun forceOrientationEnable(): Boolean {
        return true
    }

    /**
     * API28:api如果已经是透明禁止设置方向
     *
     * @param requestedOrientation
     */
    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Api28Hook.setRequestedOrientationHook(this)) return
        super.setRequestedOrientation(requestedOrientation)
    }

    /**
     * 指定屏幕方向，防止旋转造成页面重建导致的各种问题
     *
     * @return
     */
    protected open fun requestedOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun getDrawable2(@DrawableRes drawableId: Int): Drawable {
        return ContextCompat.getDrawable(mContext, drawableId)!!
    }

    @ColorInt
    fun getColor2(@ColorRes colorId: Int): Int {
        return ContextCompat.getColor(mContext, colorId)
    }

    fun getString2(@StringRes stringId: Int): String {
        return mContext.getString(stringId)
    }

    fun format(string: String, vararg args: Any): String {
        return String.format(string, *args)
    }
}