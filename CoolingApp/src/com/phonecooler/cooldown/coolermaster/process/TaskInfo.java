package com.phonecooler.cooldown.coolermaster.process;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CheckBox;

public class TaskInfo implements Parcelable {

	private ApplicationInfo appinfo;
	public long mem;
	private PackagesInfo pkgInfo;
	private PackageManager pm;
	private ActivityManager.RunningAppProcessInfo runinfo;
	private String title;
	@SuppressWarnings("unused")
	private CheckBox chkTask;
	private boolean chceked;

	public TaskInfo(Context context, ActivityManager.RunningAppProcessInfo runinfo) {

		this.appinfo = null;
		this.pkgInfo = null;
		this.title = "";
		this.runinfo = runinfo;
		PackageManager pkm = context.getApplicationContext().getPackageManager();
		this.pm = pkm;
	}

	public TaskInfo(Context context, ApplicationInfo appinfo) {

		this.appinfo = null;
		this.pkgInfo = null;
		this.runinfo = null;
		this.title = "";
		this.appinfo = appinfo;
		PackageManager pkm = context.getApplicationContext().getPackageManager();
		this.pm = pkm;
	}
	
	public TaskInfo(Parcel in){
		appinfo = (ApplicationInfo) in.readParcelable(ApplicationInfo.class.getClassLoader());
		pkgInfo = (PackagesInfo) in.readParcelable(PackagesInfo.class.getClassLoader());
		runinfo = (ActivityManager.RunningAppProcessInfo) in.readParcelable(ActivityManager.RunningAppProcessInfo.class.getClassLoader());
		mem = in.readLong();
		title = in.readString();
		chceked = in.readByte() != 0;
	}

	public void getAppInfo(PackagesInfo pkInfo) {

		if (appinfo == null) {
			try {
				String s = runinfo.processName;
				this.appinfo = pm.getApplicationInfo(s, 128);
				try {
					this.title = appinfo.loadLabel(pm).toString();
				} catch (Exception e) {

				}
			} catch (Exception e) {
			}
		}
	}

	public int getIcon() {
		return appinfo.icon;
	}

	public String getPackageName() {
		return appinfo.packageName;
	}

	public String getTitle() {

		if ("".equals(title)) {
			try {
				this.title = appinfo.loadLabel(pm).toString();
			} catch (Exception e) {

			}
		}
		return title;
	}

	public void setMem(int mem) {
		this.mem = mem;
	}

	public boolean isGoodProcess() {

		if (appinfo != null) {
			return true;
		} else {
			return false;
		}
	}

	public ApplicationInfo getAppinfo() {
		return appinfo;
	}

	public void setAppinfo(ApplicationInfo appinfo) {
		this.appinfo = appinfo;
	}

	public PackagesInfo getPkgInfo() {
		return pkgInfo;
	}

	public void setPkgInfo(PackagesInfo pkgInfo) {
		this.pkgInfo = pkgInfo;
	}

	public ActivityManager.RunningAppProcessInfo getRuninfo() {
		return runinfo;
	}

	public void setRuninfo(ActivityManager.RunningAppProcessInfo runinfo) {
		this.runinfo = runinfo;
	}

	public boolean isChceked() {
		return chceked;
	}

	public void setChceked(boolean chceked) {
		this.chceked = chceked;
	}

	public void setChkTask(CheckBox chkTask) {
		this.chkTask = chkTask;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(appinfo, flags);
		dest.writeParcelable(pkgInfo, flags);
		dest.writeParcelable(runinfo, flags);
		dest.writeLong(mem);
		dest.writeString(title);
		dest.writeByte((byte) (chceked ? 1 : 0));
	}

	public static final Parcelable.Creator<TaskInfo> CREATOR = new Parcelable.Creator<TaskInfo>()
    {
        public TaskInfo createFromParcel(Parcel in)
        {
            return new TaskInfo(in);
        }
        public TaskInfo[] newArray(int size)
        {
            return new TaskInfo[size];
        }
    };
}
