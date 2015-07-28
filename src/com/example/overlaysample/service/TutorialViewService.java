package com.example.overlaysample.service;

import com.example.overlaysample.view.TutorialView;
import com.example.overlaysample.R;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Tutorial的に使うサンプル
 * @author koga
 */
public class TutorialViewService extends OverlayService {
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public View inflateOverlayView() {
		TutorialView tutorialView = (TutorialView) inflate(R.layout.tutorial_view);
		return tutorialView;
	}

	@Override
	public LayoutParams getOverlayParams() {
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		params.format = PixelFormat.TRANSLUCENT;
		return params;
	}
}
