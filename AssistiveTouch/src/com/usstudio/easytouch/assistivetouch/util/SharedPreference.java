package com.usstudio.easytouch.assistivetouch.util;

import android.content.Context;
import com.mbapp.lib_tool.makemoney.WSettings;

public class SharedPreference extends WSettings {

	private final static String DEFAULT_BG_COLOR = "#808080";
	private final static String DEFAULT_ICON_NAME = "theme_classic";
	private final static int DEFAULT_ICON_SIZE = 144;
	private final static int DEFAULT_ICON_TRANSPARENT = 50;
	private final static int DEFAULT_PAGE_CHANGE_SPEED = 1;
	private final static String DEFAULT_MAIN_PAGE1 = "ic_action_new.None";
	private final static String DEFAULT_MAIN_PAGE2 = "ic_screen_lock_portrait_white.Lock";
	private final static String DEFAULT_MAIN_PAGE3 = "ic_action_new.None";
	private final static String DEFAULT_MAIN_PAGE4 = "ic_star_white.Favor";
	private final static String DEFAULT_MAIN_PAGE5 = "ic_clear_ram.Clean";
	private final static String DEFAULT_MAIN_PAGE6 = "ic_settings_applications_white.Setting";
	private final static String DEFAULT_MAIN_PAGE7 = "ic_action_new.None";
	private final static String DEFAULT_MAIN_PAGE8 = "ic_home_white.Home";
	private final static String DEFAULT_MAIN_PAGE9 = "ic_action_new.None";
	private final static String DEFAULT_SETTING_PAGE1 = "ic_signal_wifi_on_white.Wifi";
	private final static String DEFAULT_SETTING_PAGE2 = "ic_bluetooth_white.Bluetooth";
	private final static String DEFAULT_SETTING_PAGE3 = "ic_screen_rotation_white.Rotate Screen";
	private final static String DEFAULT_SETTING_PAGE4 = "ic_location_on_white.Location";
	private final static String DEFAULT_SETTING_PAGE5 = "ic_back_new";
	private final static String DEFAULT_SETTING_PAGE6 = "ic_volume_up_white.Volume Up";
	private final static String DEFAULT_SETTING_PAGE7 = "ic_airplanemode_on_white.Airplane";
	private final static String DEFAULT_SETTING_PAGE8 = "ic_flash_on_white.Flashlight";
	private final static String DEFAULT_SETTING_PAGE9 = "ic_volume_down_white.Volume Down";
	private final static String DEFAULT_APP_PAGE = "ic_action_new";
	private final static String DEFAULT_APP_PAGE5 = "ic_back_new";

	private static String SWITCH = "switch";
	private static String BG_COLOR = "bg_color";
	private static String ICON_NAME = "icon_name";
	private static String ICON_SIZE = "icon_size";
	private static String ICON_TRANSPARENT = "icon_transparent";
	private static String PAGE_CHANGE_SPEED = "page_change_speed";
	private static String FLASH_LIGHT = "flash_light";
	private static String MAIN_PAGE1 = "main_page1";
	private static String MAIN_PAGE2 = "main_page2";
	private static String MAIN_PAGE3 = "main_page3";
	private static String MAIN_PAGE4 = "main_page4";
	private static String MAIN_PAGE5 = "main_page5";
	private static String MAIN_PAGE6 = "main_page6";
	private static String MAIN_PAGE7 = "main_page7";
	private static String MAIN_PAGE8 = "main_page8";
	private static String MAIN_PAGE9 = "main_page9";
	private static String SETTING_PAGE1 = "setting_page1";
	private static String SETTING_PAGE2 = "setting_page2";
	private static String SETTING_PAGE3 = "setting_page3";
	private static String SETTING_PAGE4 = "setting_page4";
	private static String SETTING_PAGE5 = "setting_page5";
	private static String SETTING_PAGE6 = "setting_page6";
	private static String SETTING_PAGE7 = "setting_page7";
	private static String SETTING_PAGE8 = "setting_page8";
	private static String SETTING_PAGE9 = "setting_page9";
	private static String APP_PAGE1 = "app_page1";
	private static String APP_PAGE2 = "app_page2";
	private static String APP_PAGE3 = "app_page3";
	private static String APP_PAGE4 = "app_page4";
	private static String APP_PAGE5 = "app_page5";
	private static String APP_PAGE6 = "app_page6";
	private static String APP_PAGE7 = "app_page7";
	private static String APP_PAGE8 = "app_page8";
	private static String APP_PAGE9 = "app_page9";
	public static String TIME = "time";
	public static String CAN_CLEAN = "can_clean";

	public static void saveSwitch(Context context, boolean sw) {
		save(context, SWITCH, sw);
	}

	public static boolean readSwitch(Context context) {
		return read(context, SWITCH, true);
	}

	public static void saveTime(Context context, long time) {
		saveLong(context, TIME, time);
	}

	public static long readTime(Context context) {
		return readLong(context, TIME, 0l);
	}
	
	public static void saveClean(Context context, boolean cl) {
		save(context, CAN_CLEAN, cl);
	}

	public static boolean readClean(Context context) {
		return read(context, CAN_CLEAN, true);
	}

	public static void saveFlashLight(Context context, boolean fl) {
		save(context, FLASH_LIGHT, fl);
	}

	public static boolean readFlashLight(Context context) {
		return read(context, FLASH_LIGHT, false);
	}

	public static void saveBgColor(Context context, String bg_color) {
		save(context, BG_COLOR, bg_color);
	}

	public static String readBgColor(Context context) {
		return read(context, BG_COLOR, DEFAULT_BG_COLOR);
	}

	public static void saveIconName(Context context, String icon_name) {
		save(context, ICON_NAME, icon_name);
	}

	public static String readIconName(Context context) {
		return read(context, ICON_NAME, DEFAULT_ICON_NAME);
	}

	public static void saveIconSize(Context context, int icon_size) {
		save(context, ICON_SIZE, icon_size);
	}

	public static int readIconSize(Context context) {
		return read(context, ICON_SIZE, DEFAULT_ICON_SIZE);
	}

	public static void saveIconTransparent(Context context, int icon_transparent) {
		save(context, ICON_TRANSPARENT, icon_transparent);
	}

	public static int readIconTransparent(Context context) {
		return read(context, ICON_TRANSPARENT, DEFAULT_ICON_TRANSPARENT);
	}

	public static void savePageChangeSpeed(Context context, int speed) {
		save(context, PAGE_CHANGE_SPEED, speed);
	}

	public static int readPageChangeSpeed(Context context) {
		return read(context, PAGE_CHANGE_SPEED, DEFAULT_PAGE_CHANGE_SPEED);
	}

	public static void saveMainPage(Context context, String main_page, int mainPage) {
		switch (mainPage) {
		case 1:
			saveMainPage1(context, main_page);
			break;
		case 2:
			saveMainPage2(context, main_page);
			break;
		case 3:
			saveMainPage3(context, main_page);
			break;
		case 4:
			saveMainPage4(context, main_page);
			break;
		case 5:
			saveMainPage5(context, main_page);
			break;
		case 6:
			saveMainPage6(context, main_page);
			break;
		case 7:
			saveMainPage7(context, main_page);
			break;
		case 8:
			saveMainPage8(context, main_page);
			break;
		case 9:
			saveMainPage9(context, main_page);
			break;

		default:
			break;
		}
	}

	public static String readMainPage(Context context, int mainPage) {
		switch (mainPage) {
		case 1:
			return readMainPage1(context);
		case 2:
			return readMainPage2(context);
		case 3:
			return readMainPage3(context);
		case 4:
			return readMainPage4(context);
		case 5:
			return readMainPage5(context);
		case 6:
			return readMainPage6(context);
		case 7:
			return readMainPage7(context);
		case 8:
			return readMainPage8(context);
		case 9:
			return readMainPage9(context);

		default:
			return readMainPage1(context);
		}
	}

	public static void saveSettingPage(Context context, String setting_page, int settingPage) {
		switch (settingPage) {
		case 1:
			saveSettingPage1(context, setting_page);
			break;
		case 2:
			saveSettingPage2(context, setting_page);
			break;
		case 3:
			saveSettingPage3(context, setting_page);
			break;
		case 4:
			saveSettingPage4(context, setting_page);
			break;
		case 5:
			saveSettingPage5(context, setting_page);
			break;
		case 6:
			saveSettingPage6(context, setting_page);
			break;
		case 7:
			saveSettingPage7(context, setting_page);
			break;
		case 8:
			saveSettingPage8(context, setting_page);
			break;
		case 9:
			saveSettingPage9(context, setting_page);
			break;

		default:
			break;
		}
	}

	public static String readSettingPage(Context context, int settingPage) {
		switch (settingPage) {
		case 1:
			return readSettingPage1(context);
		case 2:
			return readSettingPage2(context);
		case 3:
			return readSettingPage3(context);
		case 4:
			return readSettingPage4(context);
		case 5:
			return readSettingPage5(context);
		case 6:
			return readSettingPage6(context);
		case 7:
			return readSettingPage7(context);
		case 8:
			return readSettingPage8(context);
		case 9:
			return readSettingPage9(context);

		default:
			return readSettingPage1(context);
		}
	}

	public static void saveAppPage(Context context, String app_page, int appPage) {
		switch (appPage) {
		case 1:
			saveAppPage1(context, app_page);
			break;
		case 2:
			saveAppPage2(context, app_page);
			break;
		case 3:
			saveAppPage3(context, app_page);
			break;
		case 4:
			saveAppPage4(context, app_page);
			break;
		case 5:
			saveAppPage5(context, app_page);
			break;
		case 6:
			saveAppPage6(context, app_page);
			break;
		case 7:
			saveAppPage7(context, app_page);
			break;
		case 8:
			saveAppPage8(context, app_page);
			break;
		case 9:
			saveAppPage9(context, app_page);
			break;

		default:
			break;
		}
	}

	public static String readAppPage(Context context, int appPage) {
		switch (appPage) {
		case 1:
			return readAppPage1(context);
		case 2:
			return readAppPage2(context);
		case 3:
			return readAppPage3(context);
		case 4:
			return readAppPage4(context);
		case 5:
			return readAppPage5(context);
		case 6:
			return readAppPage6(context);
		case 7:
			return readAppPage7(context);
		case 8:
			return readAppPage8(context);
		case 9:
			return readAppPage9(context);

		default:
			return readAppPage1(context);
		}
	}

	private static void saveMainPage1(Context context, String main_page1) {
		save(context, MAIN_PAGE1, main_page1);
	}

	private static String readMainPage1(Context context) {
		return read(context, MAIN_PAGE1, DEFAULT_MAIN_PAGE1);
	}

	private static void saveMainPage2(Context context, String main_page2) {
		save(context, MAIN_PAGE2, main_page2);
	}

	private static String readMainPage2(Context context) {
		return read(context, MAIN_PAGE2, DEFAULT_MAIN_PAGE2);
	}

	private static void saveMainPage3(Context context, String main_page3) {
		save(context, MAIN_PAGE3, main_page3);
	}

	private static String readMainPage3(Context context) {
		return read(context, MAIN_PAGE3, DEFAULT_MAIN_PAGE3);
	}

	private static void saveMainPage4(Context context, String main_page4) {
		save(context, MAIN_PAGE4, main_page4);
	}

	private static String readMainPage4(Context context) {
		return read(context, MAIN_PAGE4, DEFAULT_MAIN_PAGE4);
	}

	private static void saveMainPage5(Context context, String main_page5) {
		save(context, MAIN_PAGE5, main_page5);
	}

	private static String readMainPage5(Context context) {
		return read(context, MAIN_PAGE5, DEFAULT_MAIN_PAGE5);
	}

	private static void saveMainPage6(Context context, String main_page6) {
		save(context, MAIN_PAGE6, main_page6);
	}

	private static String readMainPage6(Context context) {
		return read(context, MAIN_PAGE6, DEFAULT_MAIN_PAGE6);
	}

	private static void saveMainPage7(Context context, String main_page7) {
		save(context, MAIN_PAGE7, main_page7);
	}

	private static String readMainPage7(Context context) {
		return read(context, MAIN_PAGE7, DEFAULT_MAIN_PAGE7);
	}

	private static void saveMainPage8(Context context, String main_page8) {
		save(context, MAIN_PAGE8, main_page8);
	}

	private static String readMainPage8(Context context) {
		return read(context, MAIN_PAGE8, DEFAULT_MAIN_PAGE8);
	}

	private static void saveMainPage9(Context context, String main_page9) {
		save(context, MAIN_PAGE9, main_page9);
	}

	private static String readMainPage9(Context context) {
		return read(context, MAIN_PAGE9, DEFAULT_MAIN_PAGE9);
	}

	private static void saveSettingPage1(Context context, String setting_page1) {
		save(context, SETTING_PAGE1, setting_page1);
	}

	private static String readSettingPage1(Context context) {
		return read(context, SETTING_PAGE1, DEFAULT_SETTING_PAGE1);
	}

	private static void saveSettingPage2(Context context, String setting_page2) {
		save(context, SETTING_PAGE2, setting_page2);
	}

	private static String readSettingPage2(Context context) {
		return read(context, SETTING_PAGE2, DEFAULT_SETTING_PAGE2);
	}

	private static void saveSettingPage3(Context context, String setting_page3) {
		save(context, SETTING_PAGE3, setting_page3);
	}

	private static String readSettingPage3(Context context) {
		return read(context, SETTING_PAGE3, DEFAULT_SETTING_PAGE3);
	}

	private static void saveSettingPage4(Context context, String setting_page4) {
		save(context, SETTING_PAGE4, setting_page4);
	}

	private static String readSettingPage4(Context context) {
		return read(context, SETTING_PAGE4, DEFAULT_SETTING_PAGE4);
	}

	private static void saveSettingPage5(Context context, String setting_page5) {
		save(context, SETTING_PAGE5, setting_page5);
	}

	private static String readSettingPage5(Context context) {
		return read(context, SETTING_PAGE5, DEFAULT_SETTING_PAGE5);
	}

	private static void saveSettingPage6(Context context, String setting_page6) {
		save(context, SETTING_PAGE6, setting_page6);
	}

	private static String readSettingPage6(Context context) {
		return read(context, SETTING_PAGE6, DEFAULT_SETTING_PAGE6);
	}

	private static void saveSettingPage7(Context context, String setting_page7) {
		save(context, SETTING_PAGE7, setting_page7);
	}

	private static String readSettingPage7(Context context) {
		return read(context, SETTING_PAGE7, DEFAULT_SETTING_PAGE7);
	}

	private static void saveSettingPage8(Context context, String setting_page8) {
		save(context, SETTING_PAGE8, setting_page8);
	}

	private static String readSettingPage8(Context context) {
		return read(context, SETTING_PAGE8, DEFAULT_SETTING_PAGE8);
	}

	private static void saveSettingPage9(Context context, String setting_page9) {
		save(context, SETTING_PAGE9, setting_page9);
	}

	private static String readSettingPage9(Context context) {
		return read(context, SETTING_PAGE9, DEFAULT_SETTING_PAGE9);
	}

	private static void saveAppPage1(Context context, String app_page1) {
		save(context, APP_PAGE1, app_page1);
	}

	private static String readAppPage1(Context context) {
		return read(context, APP_PAGE1, DEFAULT_APP_PAGE);
	}

	private static void saveAppPage2(Context context, String app_page2) {
		save(context, APP_PAGE2, app_page2);
	}

	private static String readAppPage2(Context context) {
		return read(context, APP_PAGE2, DEFAULT_APP_PAGE);
	}

	private static void saveAppPage3(Context context, String app_page3) {
		save(context, APP_PAGE3, app_page3);
	}

	private static String readAppPage3(Context context) {
		return read(context, APP_PAGE3, DEFAULT_APP_PAGE);
	}

	private static void saveAppPage4(Context context, String app_page4) {
		save(context, APP_PAGE4, app_page4);
	}

	private static String readAppPage4(Context context) {
		return read(context, APP_PAGE4, DEFAULT_APP_PAGE);
	}

	private static void saveAppPage5(Context context, String app_page5) {
		save(context, APP_PAGE5, app_page5);
	}

	private static String readAppPage5(Context context) {
		return read(context, APP_PAGE5, DEFAULT_APP_PAGE5);
	}

	private static void saveAppPage6(Context context, String app_page6) {
		save(context, APP_PAGE6, app_page6);
	}

	private static String readAppPage6(Context context) {
		return read(context, APP_PAGE6, DEFAULT_APP_PAGE);
	}

	private static void saveAppPage7(Context context, String app_page7) {
		save(context, APP_PAGE7, app_page7);
	}

	private static String readAppPage7(Context context) {
		return read(context, APP_PAGE7, DEFAULT_APP_PAGE);
	}

	private static void saveAppPage8(Context context, String app_page8) {
		save(context, APP_PAGE8, app_page8);
	}

	private static String readAppPage8(Context context) {
		return read(context, APP_PAGE8, DEFAULT_APP_PAGE);
	}

	private static void saveAppPage9(Context context, String app_page9) {
		save(context, APP_PAGE9, app_page9);
	}

	private static String readAppPage9(Context context) {
		return read(context, APP_PAGE9, DEFAULT_APP_PAGE);
	}
}