package com.matt.libwrapper.widget.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.matt.libwrapper.widget.SafeLinearLayoutManager;

import org.jetbrains.annotations.NotNull;


/**
 * 描 述 :    RecyclerView分割线 颜色 Drawable等(方向自动识别)
 **/
public class DividerDecoration extends RecyclerView.ItemDecoration {
    private final Rect mBounds = new Rect();
    protected int dividerColor = 0XFFebebeb;//默认分隔线颜色
    private int dividerHeight = SizeUtils.dp2px(0.5f);//默认线宽/高 0.5dp
    //前两个FF表示完全不透明 00表示完全透明
    private int backgroundColor = 0X00000000;//白色
    private Drawable mDivider;
    private Paint paint;
    private boolean onlySpace;  //只有间隔不画线
    private boolean drawFirstDivider = true;//是否画第一个
    private boolean drawLastDivider = true;//是否画最后一个
    private int leftPadding, rightPadding, topPadding, bottomPadding;

    public DividerDecoration() {
    }

    public DividerDecoration(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public DividerDecoration(int dividerHeight, int dividerColor) {
        this.dividerHeight = dividerHeight;
        this.dividerColor = dividerColor;
    }

    public DividerDecoration(int dividerHeight, boolean onlySpace) {
        this.dividerHeight = dividerHeight;
        this.onlySpace = onlySpace;
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        //当设置 mDivider时 使用mDivider宽高
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof SafeLinearLayoutManager) {
            SafeLinearLayoutManager manager = (SafeLinearLayoutManager) layoutManager;
            if (manager.getOrientation() == RecyclerView.VERTICAL) {
                outRect.set(0, 0, 0, mDivider == null ? dividerHeight : mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider == null ? dividerHeight : mDivider.getIntrinsicWidth(), 0);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        //只有间隔
        if (onlySpace) return;
        if (paint == null) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.FILL);
        }
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof SafeLinearLayoutManager) {
            SafeLinearLayoutManager manager = (SafeLinearLayoutManager) layoutManager;
            if (manager.getOrientation() == RecyclerView.VERTICAL) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int width = parent.getWidth();
        int right = width - rightPadding;

        final int childCount = parent.getChildCount();
        final int last = childCount - 1;
        for (int i = 0; i < childCount; i++) {
            if (i == 0 && !drawFirstDivider) continue;
            if (i == last && !drawLastDivider) continue;
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - (mDivider == null ? dividerHeight : mDivider.getIntrinsicHeight());
            //当mDivider不为null时使用mDivider否则 自己绘制
            if (mDivider != null) {
                mDivider.setBounds(leftPadding, top, right, bottom);
                mDivider.draw(canvas);
            } else {
                paint.setColor(dividerColor);
                canvas.drawRect(leftPadding, top, right, bottom, paint);
            }

            if (leftPadding > 0 || rightPadding > 0) {
                paint.setColor(backgroundColor);
                //覆盖左边/右边间距
                if (leftPadding > 0) canvas.drawRect(0, top, leftPadding, bottom, paint);
                //原有逻辑有点问题，update gy
                if (rightPadding > 0) canvas.drawRect(right, top, width, bottom, paint);
            }
        }
        canvas.restore();
    }


    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null) return;
        int height = parent.getHeight();
        int bottom = height - bottomPadding;

        final int childCount = parent.getChildCount();
        final int last = childCount - 1;
        for (int i = 0; i < childCount; i++) {
            if (i == 0 && !drawFirstDivider) continue;
            if (i == last && !drawLastDivider) continue;
            final View child = parent.getChildAt(i);
            layoutManager.getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(child.getTranslationX());
            final int left = right - (mDivider == null ? dividerHeight : mDivider.getIntrinsicWidth());
            if (mDivider != null) {
                mDivider.setBounds(left, topPadding, right, bottom);
                mDivider.draw(canvas);
            } else {
                paint.setColor(dividerColor);
                canvas.drawRect(left, topPadding, right, bottom, paint);
            }

            if (topPadding > 0 || bottomPadding > 0) {
                paint.setColor(backgroundColor);
                //覆盖顶部/底部间距
                if (topPadding > 0) canvas.drawRect(left, 0, right, topPadding, paint);
                //原有逻辑有点问题，update gy
                if (rightPadding > 0) canvas.drawRect(left, bottom, right, height, paint);
            }
        }
        canvas.restore();
    }

    public DividerDecoration setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        return this;
    }

    public DividerDecoration setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public DividerDecoration setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
        return this;
    }

    public DividerDecoration setRightPadding(int rightPadding) {
        this.rightPadding = rightPadding;
        return this;
    }

    public DividerDecoration setLeftRightPadding(int padding) {
        this.leftPadding = padding;
        this.rightPadding = padding;
        return this;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public void setTopBottomPadding(int padding) {
        this.topPadding = padding;
        this.bottomPadding = padding;
    }

    public void setPadding(int leftPadding, int rightPadding, int topPadding, int bottomPadding) {
        this.leftPadding = leftPadding;
        this.rightPadding = rightPadding;
        this.topPadding = topPadding;
        this.bottomPadding = bottomPadding;
    }

    /**
     * 设置线宽/高
     */
    public DividerDecoration setDividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
        return this;
    }

    public DividerDecoration setOnlySpace(boolean onlySpace) {
        this.onlySpace = onlySpace;
        return this;
    }

    public DividerDecoration setDrawLastDivider(boolean drawLastDivider) {
        this.drawLastDivider = drawLastDivider;
        return this;
    }

    public DividerDecoration setDrawFirstDivider(boolean drawFirstDivider) {
        this.drawFirstDivider = drawFirstDivider;
        return this;
    }

    /**
     * 设置Drawable Divider
     */
    public DividerDecoration setDrawable(Drawable mDivider) {
        this.mDivider = mDivider;
        return this;
    }
}
