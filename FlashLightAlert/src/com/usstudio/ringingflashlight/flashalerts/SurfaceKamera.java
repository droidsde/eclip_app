package com.usstudio.ringingflashlight.flashalerts;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class SurfaceKamera extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = SurfaceKamera.class.getSimpleName();
	private static SurfaceHolder mHolder;
	private static Camera mCamera;

	public SurfaceKamera(Context context, Camera camera) {
		super(context);
		if (FlashlightCaller.LOG) Log.d(TAG, "CameraSurface");
		mCamera = camera;

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the preview.
		try {
			if (FlashlightCaller.LOG) Log.d(TAG, "surfaceCreated");
			mCamera.setPreviewDisplay(holder);

			mCamera.startPreview();
		} catch (Exception e) {
			if (FlashlightCaller.LOG) Log.d(TAG, "Error setting camera preview: " + e.getMessage());
			
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (FlashlightCaller.LOG) Log.d(TAG, "surfaceDestroyed");
		// empty. Take care of releasing the Camera preview in your activity.
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			if (FlashlightCaller.LOG) Log.d(TAG, "Error stopping camera preview: " + e.getMessage());
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		if (FlashlightCaller.LOG) Log.d(TAG, "surfaceChanged");

		if (mHolder.getSurface() == null) {
			// preview surface does not exist

		}

		//		// stop preview before making changes
		//				try {
		//					mCamera.stopPreview();
		//				} catch (Exception e){
		//					if(CallerFlashlight.LOG)Log.d(TAG, "Error stopping camera preview: " + e.getMessage());
		//				}
		//
		//		// set preview size and make any resize, rotate or
		//		// reformatting changes here
		//
		//		// start preview with new settings
		//		try {
		//			mCamera.setPreviewDisplay(mHolder);
		//			mCamera.startPreview();
		//
		//		} catch (Exception e){
		//			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		//		}
	}
}