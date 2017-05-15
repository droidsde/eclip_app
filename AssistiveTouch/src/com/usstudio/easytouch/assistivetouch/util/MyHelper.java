package com.usstudio.easytouch.assistivetouch.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class MyHelper {

	public static void saveFile(Context context, Bitmap b, String picName) {
		FileOutputStream fos;
		try {
			fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
			b.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteFile(Context context, String picName){
		context.deleteFile(picName);
	}

	public static Bitmap loadBitmap(Context context, String picName) {
		Bitmap b = null;
		FileInputStream fis;
		try {
			fis = context.openFileInput(picName);
			b = BitmapFactory.decodeStream(fis);
			fis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = null;

		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if (bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}

		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); 
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static void openApp(Context context, String packageName) {
		PackageManager manager = context.getPackageManager();
		Intent i = manager.getLaunchIntentForPackage(packageName);
		if (i == null) {
			return;
			// throw new PackageManager.NameNotFoundException();
		}
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		context.startActivity(i);
	}
}
