package com.mbapp.lib_tool.view;

import java.util.ArrayList;

public class W5DataTransfer extends W4Listview {
	// -----------------Value---------------
	// -----Integer
	// Getter
	public int WonGetIntValue(int id) {
		return 0;
	}

	public int WgetIntValue(int id) {
		return WonGetIntValue(id);
	}

	public int WgetIntValue() {
		return WonGetIntValue(0);
	}

	// Setter
	public void WonSetIntValue(int value) {
	}

	public void WsetIntValue(int value) {
		WonSetIntValue(value);
	}

	// -----String
	// Getter
	public String WonGetStringValue(int id) {
		return null;
	}

	public String WgetStringValue(int id) {
		return WonGetStringValue(id);
	}

	public String WgetStringValue() {
		return WonGetStringValue(0);
	}

	// Setter
	public void WonSetStringValue(String value) {
	}

	public void WsetStringValue(String value) {
		WonSetStringValue(value);
	}

	// -----Object
	// Getter
	public Object PonGetValue(int id) {
		return null;
	}

	public Object PgetValue(int id) {
		return WonGetStringValue(id);
	}

	public Object PgetValue() {
		return WonGetStringValue(0);
	}

	// Setter
	public void WonSetValue(Object value) {
	}

	public void WsetValue(Object value) {
		WonSetValue(value);
	}

	// -----------------ArrayList---------------
	// -----String
	// Getter
	public ArrayList<String> WonGetStringArray(int id) {
		return null;
	}

	public ArrayList<String> PgetStringArray(int id) {
		return WonGetStringArray(id);
	}

	public ArrayList<String> WgetStringArray() {
		return WonGetStringArray(0);
	}

	// Setter
	public void WonSetStringArray(int id, ArrayList<String> arr) {
	}

	public void WonSetStringArray(ArrayList<String> arr) {
		WonSetStringArray(0, arr);
	}

	public void WsetArrayString(int id, ArrayList<String> arr) {
		WonSetStringArray(id, arr);
	}

	// -----Object
	// Getter
	public <T> ArrayList<T> WonGetArray(int id) {
		return null;
	}

	public <T> ArrayList<T> WgetArray(int id) {
		return WonGetArray(id);
	}

	public <T> ArrayList<T> WgetArray() {
		return WonGetArray(0);
	}

	// Setter
	public <T> void WonSetArray(int id, ArrayList<T> arr) {
	}

	public <T> void WsetArray(ArrayList<T> arr) {
		WonSetArray(0, arr);
	}

	public <T> void WsetArray(int id, ArrayList<T> arr) {
		WonSetArray(id, arr);
	}

}
