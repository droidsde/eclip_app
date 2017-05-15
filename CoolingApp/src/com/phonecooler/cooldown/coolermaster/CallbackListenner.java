package com.phonecooler.cooldown.coolermaster;

import java.util.ArrayList;

import com.phonecooler.cooldown.coolermaster.process.TaskInfo;

public interface CallbackListenner {

	public void closeScanActivity(ArrayList<TaskInfo> arrList);
	
	public void updateCoolActivity(int killed);
}
