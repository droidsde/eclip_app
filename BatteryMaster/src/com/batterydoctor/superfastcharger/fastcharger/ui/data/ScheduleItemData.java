package com.batterydoctor.superfastcharger.fastcharger.ui.data;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.batterydoctor.superfastcharger.fastcharger.R;

public class ScheduleItemData implements Parcelable {

	public int startHour;
	public int startMinute;
	public int stopHour;
	public int stopMinute;
	public boolean[] repeatDay;
	/**
	 *  mode sẽ chuyển
	 */
	public String typestart;
	public String typestop;
	public String key;
	public static final Parcelable.Creator<ScheduleItemData> CREATOR = new Parcelable.Creator<ScheduleItemData>() {
		public ScheduleItemData createFromParcel(Parcel in) {
			return new ScheduleItemData(in);
		}

		public ScheduleItemData[] newArray(int size) {
			return new ScheduleItemData[size];
		}
	};
	public ScheduleItemData() {
		// TODO Auto-generated constructor stub
	}
	public ScheduleItemData(Parcel parcel){
		startHour = parcel.readInt();
		startMinute = parcel.readInt();
		stopHour = parcel.readInt();
		stopMinute = parcel.readInt();;
		typestart = parcel.readString();
		typestop = parcel.readString();
		parcel.readBooleanArray(repeatDay);
	}
	public String timeToString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(timeToString(startHour) + ":"
				+ timeToString(startMinute));
		stringBuilder.append(" - ");
		stringBuilder.append(timeToString(stopHour) + ":"
				+ timeToString(stopMinute));
		return stringBuilder.toString();
	}

	public String repeatToString(Resources resources) {
		int checkWorkDay = 0;
		int checkEveryDay = 0;
		boolean check = true;
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < repeatDay.length; i++) {
			if (repeatDay[i]) {
				stringBuilder.append(resources.getString(dayResId[i]) + " ");
				checkEveryDay++;
				if (check) {
					checkWorkDay++;
				}
			} else {
				check = false;
			}
		}
		if (checkEveryDay == 7) {
			return resources
					.getString(R.string.schedule_newshedule_text_everyday);
		}
		if (checkWorkDay == 5) {
			return resources
					.getString(R.string.schedule_newshedule_text_workDay);
		}
		return stringBuilder.toString();
	}

	private String timeToString(int time) {
		if (time < 10)
			return "0" + String.valueOf(time);
		return String.valueOf(time);
	}

	private int dayResId[] = { R.string.schedule_newshedule_text_mon,
			R.string.schedule_newshedule_text_tue,
			R.string.schedule_newshedule_text_wed,
			R.string.schedule_newshedule_text_thu,
			R.string.schedule_newshedule_text_fri,
			R.string.schedule_newshedule_text_sat,
			R.string.schedule_newshedule_text_sun };

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(startHour);
		dest.writeInt(startMinute);
		dest.writeInt(stopHour);
		dest.writeInt(stopMinute);
		dest.writeString(typestart);
		dest.writeString(typestop);
		dest.writeBooleanArray(repeatDay);
	}
}
