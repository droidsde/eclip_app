package com.batterydoctor.superfastcharger.fastcharger.apps;

import java.util.List;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.batterydoctor.superfastcharger.fastcharger.R;
/**
 * �?ối tượng này sẽ lấy ra các thông tin của một packagemane
 * @author nghia
 *
 */
public class ParserPackage{
	public static final int TYPE_PROCESS_RUNNING = 1;
	public static final int TYPE_SERVICE_RUNNING = 2;
	public String namePackage;
	public String name;
	public Drawable icon;
	public Drawable progress;
	/**
	 * tỉ lệ trên max ram của 1 thanh process
	 */
	public float usage = 0;
	public int pId = -1;
	/**
	 * lượng ram MB
	 */
	public int usageRam = 0;
	List<?> list;
	/**
	 * hàm này dùng để lấy các thông tin của một processRunning
	 * @param packageManager
	 * @param appProcessInfos
	 * @param mResources
	 * @param packageInfo
	 * @param len
	 */
	public ParserPackage(PackageManager packageManager,Resources mResources,PackageInfo packageInfo,int idRun, float len) {
		// TODO Auto-generated constructor stub
		this.usage = len;
		this.pId = idRun;
		this.namePackage = packageInfo.packageName;
		this.icon = packageInfo.applicationInfo.loadIcon(packageManager);
		this.name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
		this.progress  = new drawUsageHistory(mResources.getDrawable(R.drawable.list_item_progress_bar), 
						mResources.getDrawable(R.drawable.list_item_progress_bkg), len);
	}
	public ParserPackage(PackageManager packageManager,PackageInfo packageInfo,int ram,int pID) {
		// TODO Auto-generated constructor stub
		this.icon = packageInfo.applicationInfo.loadIcon(packageManager);
		this.name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
		this.namePackage = packageInfo.packageName;
		this.usageRam = ram;
		this.pId = pID;
	}
	public int getUsageRam(){
		return usageRam;
	}
	public float getUsage(){
		return usage;
	}
	public String getNamePacket(){
		return namePackage;
	}
	public String getName(){
		return name;
	}
	public Drawable getIcon(){
		return icon;
	}
	public Drawable getProgess(){
		return progress;
	}
	public boolean isAppRunning(){
		return true;
	}
	public int getPid(){
		return pId;
	}
	/**
	 * ve len process bar
	 * @author nghia
	 *
	 */
	public class drawUsageHistory extends Drawable {
		Drawable drawable1; // content;
		Drawable drawable2;// backgroud;
		double lendraw;
		int d = -1;

		public drawUsageHistory(Drawable dr1, Drawable dr2, double len) {
			// TODO Auto-generated constructor stub
			this.drawable1 = dr1;
			this.drawable2 = dr2;
			if (len >= 1) {
				len = 1.0f;
			}
			this.lendraw = len;
		}

		@Override
		public void draw(Canvas arg0) {
			// TODO Auto-generated method stub
			this.drawable2.setBounds(getBounds());
			this.drawable2.draw(arg0);
			if (d == -1) {
				this.d = a();
				this.drawable1.setBounds(0, 0, this.d,
						this.drawable1.getIntrinsicHeight());
			}
			this.drawable1.draw(arg0);
		}

		@Override
		public int getOpacity() {
			// TODO Auto-generated method stub
			return PixelFormat.TRANSLUCENT;
		}
		@Override
		public int getIntrinsicHeight() {
			// TODO Auto-generated method stub
			return this.drawable1.getIntrinsicHeight();
		}
		@Override
		public void setAlpha(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setColorFilter(ColorFilter arg0) {
			// TODO Auto-generated method stub

		}

		private int a() {
			return Math.max((int) (getBounds().width() * this.lendraw),
					this.drawable1.getIntrinsicWidth());
		}
	}
}