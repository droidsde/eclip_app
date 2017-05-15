package com.usstudio.easytouch.assistivetouch.service;

import com.usstudio.easytouch.assistivetouch.R;
import com.usstudio.easytouch.assistivetouch.MainActivity;
import com.usstudio.easytouch.assistivetouch.dialog.TaskDialog;
import com.usstudio.easytouch.assistivetouch.util.SharedPreference;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AssistiveTouchService extends Service {

	int lastX, lastY;
	int paramX, paramY;
	int width, height;

	private final int TIME_DELAY = 2000;
	private LayoutInflater inflater;
	private LinearLayout assistiveView;
	private GestureDetector gestureDetector;
	private ImageView assistiveImage;
	private WindowManager.LayoutParams params;
	private WindowManager windowManager;
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			setAlphaAssistiveIcon();
		}
	};

	private OnTouchListener mViewTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (gestureDetector.onTouchEvent(event)) {
				// single tap
				v.performClick();
				removeAssisView();
				TaskDialog dialog = new TaskDialog(AssistiveTouchService.this);
				dialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						// addAssisView();
						windowManager.addView(assistiveView, params);
						handler.postDelayed(runnable, TIME_DELAY);
					}
				});
				dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				dialog.getWindow().setDimAmount(0f);
				dialog.show();
				return true;
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					handler.removeCallbacks(runnable);
					assistiveImage.setAlpha(1f);
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = params.x;
					paramY = params.y;
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;
					params.x = paramX + dx;
					params.y = paramY + dy;
					windowManager.updateViewLayout(assistiveView, params);
					break;
				case MotionEvent.ACTION_UP:
					int[] temp = { params.x, params.y, width - params.x, height - params.y };
					int min = temp[0];
					for (int i = 1; i < temp.length; i++) {
						if (temp[i] < min) {
							min = temp[i];
						}
					}
					if (min == temp[0]) {
						params.x = 0;
					} else if (min == temp[1]) {
						params.y = 0;
					} else if (min == temp[2]) {
						params.x = width - assistiveView.getWidth();
					} else if (min == temp[3]) {
						params.y = height - assistiveView.getHeight();
					}
					windowManager.updateViewLayout(assistiveView, params);
					handler.postDelayed(runnable, TIME_DELAY);

					break;
				default:
					break;
				}
			}

			return false;
		}
	};

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		showNotification();

		inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		assistiveView = (LinearLayout) inflater.inflate(R.layout.assistive_layout, null);
		gestureDetector = new GestureDetector(this, new SingleTapConfirm());
		assistiveImage = (ImageView) assistiveView.findViewById(R.id.assistive_image);
		// TODO
		setAssistiveIcon();
		resizeAssistiveIcon();
		assistiveView.setOnTouchListener(mViewTouchListener);
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		addAssisView();

		DisplayMetrics displaymetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;

	}

	@Override
	public void onDestroy() {
		removeAssisView();
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;
	}

	public void setAssistiveIcon() {
		handler.removeCallbacks(runnable);
		assistiveImage.setAlpha(1f);
		int resId = getResources().getIdentifier(SharedPreference.readIconName(AssistiveTouchService.this), "drawable",
				getPackageName());
		assistiveImage.setImageResource(resId);
		handler.postDelayed(runnable, TIME_DELAY);
	}

	public void resizeAssistiveIcon() {
		LayoutParams lParams = assistiveImage.getLayoutParams();
		int size = SharedPreference.readIconSize(AssistiveTouchService.this);
		lParams.height = size;
		lParams.width = size;
		assistiveImage.setLayoutParams(lParams);
	}

	public void setAlphaAssistiveIcon() {
		if (assistiveImage != null) {
			float alpha = (float) SharedPreference.readIconTransparent(AssistiveTouchService.this) / 100;
			assistiveImage.setAlpha(alpha);
		}
	}

	private void showNotification() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("AssistiveTouch is running").setContentText("Touch to open");
		Intent resultIntent = new Intent(this, MainActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
				PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		// builder.setAutoCancel(true);
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = builder.build();
		nm.notify(1, notification);
		startForeground(1, notification);
	}

	private void addAssisView() {
		params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.LEFT | Gravity.TOP;
		params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;

		windowManager.addView(assistiveView, params);
		handler.postDelayed(runnable, TIME_DELAY);
	}

	private void removeAssisView() {
		if (windowManager != null) {
			windowManager.removeView(assistiveView);
		}
	}

	private class SingleTapConfirm extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapUp(MotionEvent event) {
			return true;
		}
	}

	public class LocalBinder extends Binder {
		public AssistiveTouchService getService() {
			// Return this instance of AssistiveTouchService so clients can call
			// public methods
			return AssistiveTouchService.this;
		}
	}
}