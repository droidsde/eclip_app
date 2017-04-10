package com.petusg.mastercooler.devicecooler;

import java.util.ArrayList;

import com.petusg.mastercooler.devicecooler.process.TaskInfo;

public interface CallbackListenner {

	public void closeScanActivity(ArrayList<TaskInfo> arrList);
	
	public void updateCoolActivity(int killed);
}
