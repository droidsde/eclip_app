package com.batterydoctor.superfastcharger.fastcharger.ui.fragment;

import java.lang.reflect.InvocationTargetException;

import android.support.v4.app.Fragment;

import com.nvn.log.LogBuider;

public class TabInfo {
	private static final String TAG = "powermanager.ui.fragment.Tabinfo";
	private Class class1 = null;
	private Fragment absFragment = null;
	int int1;
	int int2;
	String mString = null;
	public TabInfo(int i1, String string, Class cl) {
		// TODO Auto-generated constructor stub
		this(i1,0,string,cl);
	}
	public TabInfo(int i1,int i2,String string, Class cl) {
		// TODO Auto-generated constructor stub
		this.int1 = i1;
		this.int2 = i2;
		this.mString = string;
		this.class1 = cl;
	}
	public String getString(){
		return this.mString;
	}
	public Fragment  getFragemnt(){
		try {
			this.absFragment = (Fragment) this.class1.getConstructor(new Class[0]).newInstance(new Object[0]);
			LogBuider.i(TAG  + "absFragment", absFragment.toString());
			return absFragment;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
