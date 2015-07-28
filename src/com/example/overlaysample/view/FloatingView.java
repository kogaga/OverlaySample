package com.example.overlaysample.view;

import com.example.overlaysample.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * FloatingView
 * @author koga
 */
public class FloatingView extends FrameLayout {
	private View mBody;
	private View mDroid;
	private TextView mTextView;

	public FloatingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		mBody = findViewById(R.id.body);
		mDroid = findViewById(R.id.droid);
		mTextView = (TextView) findViewById(R.id.textview);

		rotateAnim();
		mBody.setVisibility(INVISIBLE);
	}

	/**
	 * ドロイド君回転
	 */
	private void rotateAnim() {
		ViewPropertyAnimator animator = mDroid.animate();
		animator.rotationY(mDroid.getRotationY() == 0 ? 360 : 0);
		animator.setDuration(2000);
		animator.setInterpolator(new LinearInterpolator());
		animator.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				rotateAnim();
			}
		});
	}

	/**
	 * 出てくる時のアニメーション
	 */
	public void show() {
		mBody.setScaleX(0);
		mBody.setScaleY(0);
		mBody.setVisibility(VISIBLE);
		mBody.animate().scaleX(1).scaleY(1);
	}

	/**
	 * 消える時のアニメーション
	 * @param l
	 */
	public void hide(AnimatorListenerAdapter l) {
		mBody.animate().scaleX(0).scaleY(0).setListener(l);
	}

	/**
	 * テキストセット
	 * @param text
	 */
	public void setText(String text) {
		if (mTextView != null) {
			mTextView.setText(text);
		}
	}
}
