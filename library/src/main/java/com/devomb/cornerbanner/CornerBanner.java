package com.devomb.cornerbanner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Ombrax on 26/07/2015.
 */
public class CornerBanner extends TextView {

    //region inner field
    private int width;
    private int height;

    private int parentWidth;
    private int parentHeight;

    private boolean defaultPosition;

    private FrameLayout wrapper;

    private Rect childMargins;
    private int childGravity;
    //endregion

    //region constructor
    public CornerBanner(Context context) {
        super(context);
        init();
    }
    //endregion

    //region setup
    private void init() {
        setGravity(android.view.Gravity.CENTER);
        setTextColor(Color.WHITE);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        setAllCaps(true);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setBackgroundColor(getResources().getColor(R.color.blue));
        setPadding(8, 4, 8, 4);
        setPivotX(0);
        setPivotY(0);
    }
    //endregion

    //region method
    public void attachTo(View view, Position position) {
        if (getParent() == null) {
            defaultPosition = (position == Position.TOP_LEFT_CORNER);
            wrap(view);
        } else {
            throw new IllegalArgumentException("This " + CornerBanner.class.getSimpleName() + " is already attached to a View.");
        }
    }

    public void attachTo(View view) {
        this.attachTo(view, Position.TOP_LEFT_CORNER);
    }

    public void redraw() {
        measure();
    }
    //endregion

    //region helper
    //region wrapper
    private void wrap(View attachView) {
        ViewGroup parent = (ViewGroup) attachView.getParent();
        int indexOfChild = parent.indexOfChild(attachView);
        parent.removeView(attachView);
        createWrapper(attachView);
        measure();
        transferGravityToNewParent(wrapper);
        parent.addView(wrapper, indexOfChild);
        transferMarginsToNewParent(wrapper);//NOTE: Must be called after adding view (Margins wouldn't be applied)
    }

    private void createWrapper(View childView) {
        wrapper = new FrameLayout(getContext());
        wrapper.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        getChildMargins(childView);//NOTE: Must be called before adding view (Margins would be lost)
        wrapper.addView(childView);
        getChildGravity(childView);//NOTE: Must be called after adding view (LayoutParams would throw exception)
        wrapper.addView(this);
    }
    //endregion

    //region attributes
    private void getChildMargins(View childView) {
        ViewGroup.MarginLayoutParams viewParams = (ViewGroup.MarginLayoutParams) childView.getLayoutParams();
        childMargins = new Rect(viewParams.leftMargin, viewParams.topMargin, viewParams.rightMargin, viewParams.bottomMargin);
        viewParams.setMargins(0, 0, 0, 0);
        childView.setLayoutParams(viewParams);
    }

    private void transferMarginsToNewParent(FrameLayout frameLayout) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
        marginLayoutParams.setMargins(childMargins.left, childMargins.top, childMargins.right, childMargins.bottom);
    }

    private void getChildGravity(View childView) {
        FrameLayout.LayoutParams childLayoutParams = (FrameLayout.LayoutParams) childView.getLayoutParams();
        childGravity = childLayoutParams.gravity;
    }

    private void transferGravityToNewParent(FrameLayout parentLayout) {
        FrameLayout.LayoutParams parentLayoutParams = (FrameLayout.LayoutParams) parentLayout.getLayoutParams();
        parentLayoutParams.gravity = childGravity;
    }
    //endregion

    //region layout
    private void applyLayoutChanges() {
        rotate();
        adjustToRotation();
        scaleAndTranslate();
    }

    private void rotate() {
        this.setRotation(defaultPosition ? -45 : 45);
    }

    private void adjustToRotation() {
        float offset = getIsoscelesRightTriangleSide(width);
        if (defaultPosition) {
            setY(offset);
        } else {
            setX(parentWidth - offset);
        }
    }

    private void scaleAndTranslate() {
        float heightOffset = getIsoscelesRightTriangleSide(height);
        int newWidth = Math.round(width + (2 * heightOffset));
        float widthOffset = getIsoscelesRightTriangleSide(newWidth);
        getLayoutParams().width = newWidth;
        setX(defaultPosition ? -heightOffset : parentWidth - widthOffset + heightOffset);
        setY(defaultPosition ? (widthOffset - heightOffset) : -heightOffset);
    }
    //endregion

    //region calc
    private float getIsoscelesRightTriangleSide(int hypotenuseLength) {
        return (float) Math.sqrt((Math.pow(hypotenuseLength, 2)) / 2);
    }
    //endregion

    //region measure
    private void measure() {
        measure(0, 0);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        applyLayoutChanges();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (parentWidth == 0 || parentHeight == 0) {
            parentWidth = wrapper.getMeasuredWidth();
            parentHeight = wrapper.getMeasuredHeight();
            if (parentWidth != 0 && parentHeight != 0) {
                applyLayoutChanges();
            }
        }
    }

    //endregion
    //endregion

    //region enum
    public enum Position {
        TOP_LEFT_CORNER,
        TOP_RIGHT_CORNER
    }
    //endregion
}
