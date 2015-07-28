package com.example.overlaysample.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * Service基底クラス
 * @author koga
 */
public abstract class OverlayService extends Service {
	/**
	 * OverlayさせるViewを取得
	 * @return
	 */
	abstract public View inflateOverlayView();

	/**
	 * Overlayの設定取得(type以外)
	 * @return
	 */
	abstract public WindowManager.LayoutParams getOverlayParams();

	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mParams;
	private View mOverlayView;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		int type = intent.getIntExtra("type", WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		addOverlayView(type);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Viewを削除
		removeOverlayView();
	}

	/**
	 * inflate
	 * @param id
	 * @return
	 */
	protected View inflate(int id) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		return layoutInflater.inflate(id, null);
	}

	/**
	 * Viewを削除
	 */
	private void removeOverlayView() {
		if (mWindowManager != null && mOverlayView != null) {
			mWindowManager.removeView(mOverlayView);
			mOverlayView = null;
		}
	}

	/**
	 * inflateしたViewを差し込む
	 * @param type
	 */
	private void addOverlayView(int type) {
		// Viewがすでにaddされていたら削除
		removeOverlayView();

		// inflate
		mOverlayView = inflateOverlayView();

		if (mWindowManager != null && mOverlayView != null) {
			// Overlay設定
			mParams = getOverlayParams();
			mParams.type = type;

			// Viewを差し込む
			mWindowManager.addView(mOverlayView, mParams);
		}
	}

	/**
	 * OverlayさせたViewのLayoutParamsを更新
	 * @param params
	 */
	protected void updateViewLayout(WindowManager.LayoutParams params) {
		if (mWindowManager != null && mOverlayView != null && params != null) {
			mWindowManager.updateViewLayout(mOverlayView, params);
		}
	}

	/**
	 * Overlay中のViewを取得
	 * @return
	 */
	protected View getOverlayView() {
		return mOverlayView;
	}
}
