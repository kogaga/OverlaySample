package com.example.overlaysample.view;

import com.example.overlaysample.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

/**
 * TutorialView
 * @author koga
 */
public class TutorialView extends RelativeLayout {
	private View mRedFrame;

	public TutorialView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mRedFrame = findViewById(R.id.red_frame);
		blink();
	}

	/**
	 * 点滅
	 */
	private void blink() {
		ViewPropertyAnimator animator = mRedFrame.animate();
		float scale = mRedFrame.getScaleX() == 1.0f ? 1.1f : 1.0f;
		animator.scaleX(scale);
		animator.scaleY(scale);
		animator.setInterpolator(new LinearInterpolator());
		animator.setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				blink();
			}
		});
	}
}
