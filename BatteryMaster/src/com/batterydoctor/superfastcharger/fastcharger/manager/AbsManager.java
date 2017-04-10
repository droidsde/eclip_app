package com.batterydoctor.superfastcharger.fastcharger.manager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.nvn.log.LogBuider;

import android.content.Context;

public abstract class AbsManager {
	private Object object = new Object();
	protected List<IManager> list;
	protected Context mContext;
	protected boolean b =false;
	protected int i = 0;
	protected boolean isUserChageMode = false;
	public AbsManager(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext  = context.getApplicationContext();
	}
	public void setImanager(IManager imanager) {
		// TODO Auto-generated constructor stub
		synchronized(object){
			if(list == null)
				list = new LinkedList<IManager>();
			if(!list.contains(imanager))
				list.add(imanager);
		}
	}
	public void removeImanager(IManager manager){
		synchronized(object){
			this.list.remove(manager);
		}
	}

	final void update(){
		if(list==null) return;
		Iterator<IManager> iterator = this.list.iterator();
		while (iterator.hasNext()) {
			LogBuider.e(this.toString(), "update:  " + b);
			((IManager)iterator.next()).update(this, b, i,isUserChageMode);
		}
	}
	public abstract boolean getState();
	public abstract void setState(boolean i,boolean userChangeMode);
	public abstract void setLongClick();
	public interface IManager{
		/**
		 * 
		 * @param manager
		 * @param paramBoolean : trang thai on/off
		 * @param paramInt	: gia tri trong truong hop co nhieu trang thai
		 */
		public void update(AbsManager manager,boolean paramBoolean,int paramInt,boolean isUserChangeMode);
	}
}
