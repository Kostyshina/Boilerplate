package com.andersenlab.boilerplate.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import com.andersenlab.boilerplate.R;

/**
 * Custom bottom sheet bar. If view added to Coordinator layout it's
 * applied {@link ScrollAwareBehavior} instance as layout_behavior parameter. If parent layout is
 * instance of FrameLayout, LinearLayout or CoordinatorLayout - view appearing at
 * the bottom of the parent automatically.
 */

public class FloatingBottomSheet extends FrameLayout {

    @BindView(R.id.fl_button_container) FrameLayout flButtonContainer;
    @BindView(R.id.btn_action) Button btnAction;

    private ScrollAwareBehavior<FloatingBottomSheet> behavior;
    private OnClickListener onActionClickListener;

    public FloatingBottomSheet(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public FloatingBottomSheet(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FloatingBottomSheet(@NonNull Context context, @Nullable AttributeSet attrs,
                               @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        Timber.i("init");
        behavior = new ScrollAwareBehavior<>(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.FloatingBottomSheet, defStyleAttr, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.floating_bottom_sheet, this, true);
        ButterKnife.bind(this, view);

        flButtonContainer.setBackgroundColor(a.getColor(R.styleable.FloatingBottomSheet_android_background,
                ContextCompat.getColor(context, android.R.color.white)));
        btnAction.setText(a.getString(R.styleable.FloatingBottomSheet_android_text));

        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnAction.setOnClickListener(v -> onSendClick());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        ViewGroup.LayoutParams params = this.getLayoutParams();
        if (getParent() instanceof FrameLayout) {
            ((FrameLayout.LayoutParams) params).gravity = Gravity.BOTTOM;
        } else if (getParent() instanceof LinearLayout) {
            ((LinearLayout.LayoutParams) params).gravity = Gravity.BOTTOM;
        }
        this.setLayoutParams(params);


        if (getParent() instanceof CoordinatorLayout) {
            CoordinatorLayout.LayoutParams constrParams =
                    (CoordinatorLayout.LayoutParams) this.getLayoutParams();
            constrParams.gravity = Gravity.BOTTOM;
            constrParams.setBehavior(behavior);
            this.setLayoutParams(constrParams);
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        onActionClickListener = listener;
    }

    private void onSendClick() {
        if (onActionClickListener != null) {
            onActionClickListener.onClick(this);
        }
    }
}
