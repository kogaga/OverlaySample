package com.example.overlaysample;

import java.util.Timer;
import java.util.TimerTask;
import com.example.overlaysample.service.FloatingViewService;
import com.example.overlaysample.service.TutorialViewService;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

public class MainActivity extends Activity implements OnClickListener {
	private FloatingViewService mFloatingViewService;
	private Handler mHandler = new Handler();
	private Timer mTimer;
	private RadioGroup mRadioGroup;
	private Button mButton1;
	private Button mButton2;
	private Button mButton3;
	private Button mButton4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Viewの紐付け
		findViews();

		// ボタンの状態変更
		setEnabled(-1);
	}

	/**
	 * Viewの紐付け
	 */
	private void findViews() {
		mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
		mButton1 = (Button) findAndSetListener(R.id.button1);
		mButton2 = (Button) findAndSetListener(R.id.button2);
		mButton3 = (Button) findAndSetListener(R.id.button3);
		mButton4 = (Button) findAndSetListener(R.id.button4);
	}

	/**
	 * findしてClickListenerも付ける
	 * @param id
	 * @return
	 */
	private View findAndSetListener(int id) {
		View v = findViewById(id);
		v.setOnClickListener(this);
		return v;
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service == null) {
				return;
			}

			if (service instanceof FloatingViewService.LocalBinder) {
				mFloatingViewService = ((FloatingViewService.LocalBinder) service).getService();
				mFloatingViewService.show();
				startTimer();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mFloatingViewService = null;
		}
	};

	/**
	 * Timer停止
	 */
	private void stopTimer() {
		if (mTimer != null) {
			try {
				mTimer.cancel();
				mTimer.purge();
			} catch (NullPointerException e) {

			}
		}
	}

	/**
	 * Timer開始
	 */
	private void startTimer() {
		stopTimer();

		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				postValue();
			}
		}, 0, 100);
	}

	/**
	 * FloatingViewにUNIXTIMEを送る
	 */
	private void postValue() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mFloatingViewService != null) {
					mFloatingViewService.setText(String.valueOf(System.currentTimeMillis()));
				}
			}
		});
	}

	/**
	 * OverlayさせるレイヤーをRadioButtonで選択
	 * @return
	 */
	private int getOverlayType() {
		if (mRadioGroup == null) {
			return WindowManager.LayoutParams.TYPE_PHONE;
		}

		switch (mRadioGroup.getCheckedRadioButtonId()) {
		case R.id.type_system_error:
			return WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
		case R.id.type_system_overlay:
			return WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		case R.id.type_system_alert:
			return WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		case R.id.type_priority_phone:
			return WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		case R.id.type_toast:
			return WindowManager.LayoutParams.TYPE_TOAST;
		case R.id.type_phone:
		default:
			return WindowManager.LayoutParams.TYPE_PHONE;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			startFloatingViewService();
			break;
		case R.id.button2:
			mFloatingViewService.hide(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					stopFloatingViewService();
				}
			});
			break;
		case R.id.button3:
			startTutorialViewService();
			break;
		case R.id.button4:
			stopTutorialViewService();
			break;
		}
		setEnabled(v.getId());
	}

	/**
	 * ボタンの有効/無効化
	 * @param id
	 *            -1で初期状態
	 */
	private void setEnabled(int id) {
		if (id == -1) {
			id = R.id.button2;
		}

		mButton1.setEnabled(id == R.id.button2 || id == R.id.button4);
		mButton2.setEnabled(id == R.id.button1);
		mButton3.setEnabled(id == R.id.button2 || id == R.id.button4);
		mButton4.setEnabled(id == R.id.button3);
	}

	/**
	 * FloatingViewを開始
	 */
	private void startFloatingViewService() {
		Intent intent = new Intent(this, FloatingViewService.class);
		intent.putExtra("type", getOverlayType());
		startService(intent);
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	/**
	 * FloatingViewを停止
	 */
	private void stopFloatingViewService() {
		mFloatingViewService = null;
		unbindService(mServiceConnection);
		stopService(new Intent(this, FloatingViewService.class));
	}

	/**
	 * TutorialViewを開始
	 */
	private void startTutorialViewService() {
		Intent intent = new Intent(this, TutorialViewService.class);
		intent.putExtra("type", getOverlayType());
		startService(intent);
	}

	/**
	 * TutorialViewを停止
	 */
	private void stopTutorialViewService() {
		stopService(new Intent(this, TutorialViewService.class));
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		moveTaskToBack(true);
	}
}
