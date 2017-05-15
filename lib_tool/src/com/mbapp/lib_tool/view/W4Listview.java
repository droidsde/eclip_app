package com.mbapp.lib_tool.view;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class W4Listview extends W3SetValue {
	private int ListViewID;
	ArrayAdapter<?> adapter;

	public void PsetListview(int id) {
		ListViewID = id;
	}

	public void WsetListViewItemClick(OnItemClickListener listener) {
		WgetListView(ListViewID).setOnItemClickListener(listener);
	}
	public void WsetListViewItemClick(int ID, OnItemClickListener listener) {
		WgetListView(ListViewID).setOnItemClickListener(listener);
	}
	public void WsetListViewItemLongClick(OnItemLongClickListener listener) {
		WgetListView(ListViewID).setOnItemLongClickListener(listener);
	}
	public void WsetListViewItemLongClick(int ID, OnItemLongClickListener listener) {
		WgetListView(ListViewID).setOnItemLongClickListener(listener);
	}
	public <T> void WsetListViewAdapter(ArrayAdapter<?> adapter) {
		WgetListView(ListViewID).setAdapter(adapter);
		this.adapter = adapter;
	}

	public <T> void WsetListViewAdapter(int ID, ArrayAdapter<?> adapter) {
		WgetListView(ID).setAdapter(adapter);
	}

	public ListView WgetListview() {
		return (ListView) findViewById(ListViewID);
	}

	public void WupdateListview() {
		adapter.notifyDataSetChanged();
	}
}
