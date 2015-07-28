package com.example.overlaysample.service;

import com.example.overlaysample.view.FloatingView;
import com.example.overlaysample.R;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * FloatingView的に使うサンプル
 * @author koga
 */
public class FloatingViewService extends OverlayService {
	private WindowManager.LayoutParams mParams;
	private final IBinder mBinder = new LocalBinder();

	public class LocalBinder extends Binder {
		public FloatingViewService getService() {
			return FloatingViewService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public View inflateOverlayView() {
		FloatingView floatingView = (FloatingView) inflate(R.layout.floating_view);

		// Listenerセット
		setOnDragListener(floatingView);
		floatingView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendBroadcast(new Intent("com.example.overlaysample.action.LAUNCH"));
			}
		});
		return floatingView;
	}

	@Override
	public LayoutParams getOverlayParams() {
		mParams = new WindowManager.LayoutParams();
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mParams.format = PixelFormat.TRANSLUCENT;
		mParams.gravity = Gravity.TOP | Gravity.LEFT;
		mParams.x = 50;
		mParams.y = 50;
		return mParams;
	}

	/**
	 * テキストセット
	 * @param text
	 */
	public void setText(String text) {
		FloatingView overlayView = (FloatingView) getOverlayView();
		if (overlayView != null) {
			overlayView.setText(text);
		}
	}

	/**
	 * 出るときアニメーション
	 */
	public void show() {
		FloatingView floatingView = (FloatingView) getOverlayView();
		if (floatingView != null) {
			floatingView.show();
		}
	}

	/**
	 * 消えるときアニメーション
	 * @param l
	 */
	public void hide(AnimatorListenerAdapter l) {
		FloatingView floatingView = (FloatingView) getOverlayView();
		if (floatingView != null) {
			floatingView.hide(l);
		}
	}

	/**
	 * Viewをグリグリする
	 * @param v
	 */
	private void setOnDragListener(View v) {
		if (v == null) {
			return;
		}

		v.setOnTouchListener(new OnTouchListener() {
			private int initialX, initialY;
			private float initialTouchX, initialTouchY;

			private void scaleAnim(View v, float scale) {
				v.findViewById(R.id.body).animate().scaleX(scale).scaleY(scale).setDuration(100);
			}

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = mParams.x;
					initialY = mParams.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					scaleAnim(v, 0.95f);
					break;
				case MotionEvent.ACTION_MOVE:
					mParams.x = initialX + (int) (event.getRawX() - initialTouchX);
					mParams.y = initialY + (int) (event.getRawY() - initialTouchY);
					// View移動
					updateViewLayout(mParams);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					scaleAnim(v, 1.0f);
					// 一定距離動いたらClickを無効にする
					if (Math.abs(initialX - mParams.x) > 5.0f || Math.abs(initialY - mParams.y) > 5.0f) {
						return true;
					}
					break;
				}
				return false;
			}
		});
	}
}
