package com.nvn.lib;

import java.io.IOException;
import java.io.RandomAccessFile;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.manager.MscreenManager;
import com.batterydoctor.superfastcharger.fastcharger.manager.MscreenOffManager;

public class Units {


	public static final int ON = -10;
	public static final int OFF = -9;
	public static int parserIntMode(int type){
		switch (type) {
		case ON:
			return R.string.mode_on;
		case OFF:
			return R.string.mode_off;
		case MscreenOffManager.SCREEN_OFF_TIMEOUT_10m:
			return R.string.mode_sreen_timeout_10min;
		case MscreenOffManager.SCREEN_OFF_TIMEOUT_15s:
			return R.string.mode_sreen_timeout_15sec;
		case MscreenOffManager.SCREEN_OFF_TIMEOUT_1m:
			return R.string.mode_sreen_timeout_1min;
		case MscreenOffManager.SCREEN_OFF_TIMEOUT_2m:
			return R.string.mode_sreen_timeout_2min;
		case MscreenOffManager.SCREEN_OFF_TIMEOUT_30s:
			return R.string.mode_sreen_timeout_30sec;
		case MscreenOffManager.SCREEN_OFF_TIMEOUT_30m:
			return R.string.mode_sreen_timeout_30min;
		case MscreenManager.SCREEN_BRIGHTNESS_0:
			return R.string.mode_brightness_percent_0;
		case MscreenManager.SCREEN_BRIGHTNESS_50:
			return R.string.mode_brightness_percent_50;
		case MscreenManager.SCREEN_BRIGHTNESS_100:
			return R.string.mode_brightness_percent_100;
		default:
			break;
		}
		return R.string.mode_on;
	}
	/**
	 * 
	 * @return ram total in MB
	 */
	@SuppressWarnings("resource")
	public synchronized static int getTotalRAM() {
		RandomAccessFile reader = null;
		try {
			reader = new RandomAccessFile("/proc/meminfo", "r");
			String load = reader.readLine();
			String[] totrm = load.split(" kB");
			String[] trm = totrm[0].split(" ");
			int tm = Integer.parseInt(trm[trm.length - 1]) / 1024;
			return tm;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Streams.close(reader);
		}
		return -1;
	}

}
