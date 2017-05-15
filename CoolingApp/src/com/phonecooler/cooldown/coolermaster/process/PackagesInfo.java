package com.phonecooler.cooldown.coolermaster.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class PackagesInfo implements Parcelable{

	private List<ApplicationInfo> appList;

	public PackagesInfo(Context context) {
		List<ApplicationInfo> list = context.getApplicationContext().getPackageManager()
				.getInstalledApplications(0);
		this.appList = list;
	}

	public PackagesInfo(Context context, String s) {
		List<ApplicationInfo> list = context.getApplicationContext().getPackageManager()
				.getInstalledApplications(128);
		this.appList = list;
	}
	
	public PackagesInfo(Parcel in){
		appList = new ArrayList<ApplicationInfo>();
		in.readList(appList, null);
	}

	public ApplicationInfo getInfo(String s) {
		
		ApplicationInfo applicationInfo = null;
		if (s != null) {
			for (Iterator<ApplicationInfo> iterator = appList.iterator(); iterator.hasNext();) {
				applicationInfo = (ApplicationInfo) iterator.next();
				String s1 = applicationInfo.processName;
				if (s.equals(s1)){
					Log.d("SHORT: ", s+" "+s1);
					break;
				}	
			}
		}

		return applicationInfo;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(appList);
	}
	
	public static final Parcelable.Creator<PackagesInfo> CREATOR = new Parcelable.Creator<PackagesInfo>()
    {
        public PackagesInfo createFromParcel(Parcel in)
        {
            return new PackagesInfo(in);
        }
        public PackagesInfo[] newArray(int size)
        {
            return new PackagesInfo[size];
        }
    };
}
