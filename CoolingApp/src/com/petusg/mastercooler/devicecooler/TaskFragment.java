package com.petusg.mastercooler.devicecooler;

import java.util.ArrayList;

import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.petusg.mastercooler.devicecooler.R;
import com.petusg.mastercooler.devicecooler.process.ProcessListAdapter;
import com.petusg.mastercooler.devicecooler.process.TaskInfo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

public class TaskFragment extends Fragment
		implements OnDismissCallback, OnClickListener, TaskKillDialog.DialogTaskKillListener {

	private ListView swipeListView;
	private ProcessListAdapter adapter = null;
	public int mem = 0;
	private FloatingActionButton btnKill;
	private ActivityManager acm = null;
	private long beforeMemory;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.main_fragment, container, false);

		swipeListView = (ListView) v.findViewById(R.id.list);
		btnKill = (FloatingActionButton) v.findViewById(R.id.btnKill);
		btnKill.setOnClickListener(this);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle bundle = getActivity().getIntent().getExtras();
		ArrayList<TaskInfo> arrList = bundle.getParcelableArrayList("data");
		adapter = new ProcessListAdapter(getActivity(), arrList);
		swipeListView.setFocusableInTouchMode(true);
		ScaleInAnimationAdapter scaleAnimAdapter = new ScaleInAnimationAdapter(
				new SwipeDismissAdapter(adapter, TaskFragment.this));
		scaleAnimAdapter.setAbsListView(swipeListView);
		swipeListView.setAdapter(scaleAnimAdapter);

		acm = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
	}

	private boolean isImportant(String pname) {

		if (pname.equals("android") || pname.equals("android.process.acore") || pname.equals("system")
				|| pname.equals("com.android.phone") || pname.equals("com.android.systemui")
				|| pname.equals("com.android.launcher")) {
			return true;
		} else {
			return false;
		}
	}

	// listview swipe listener event, when swipe left or right
	// clean specific process from the list
	@Override
	public void onDismiss(AbsListView list, int[] reversepos) {

		for (int pos : reversepos) {
			TaskInfo info = (TaskInfo) adapter.getItem(pos);
			if (isImportant(info.getPackageName())) {
				TaskKillDialog dailog = new TaskKillDialog();
				dailog.setPos(pos);
				dailog.setIcon(info.getIcon());
				dailog.setAppName(info.getTitle());
				dailog.setDialgTaskKillListener(TaskFragment.this);
				dailog.setCancelable(false);
				dailog.show(getFragmentManager(), "killfrag");
				adapter.notifyDataSetChanged();
			} else {
				acm.killBackgroundProcesses(info.getPackageName());
				adapter.remove(adapter.getItem(pos));
				adapter.notifyDataSetChanged();
			}
		}

	}

	@Override
	public void onClick(View v) {

		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		acm.getMemoryInfo(memInfo);
		beforeMemory = memInfo.availMem;

		Intent intent = new Intent(getActivity(), CoolActivity.class);
		intent.putExtra("beforeMemory", beforeMemory);

		ArrayList<TaskInfo> listTask = new ArrayList<TaskInfo>();
		for (int i = 0; i < adapter.getCount(); i++) {
			listTask.add(adapter.getItem(i));
		}
		intent.putExtra("listTask", listTask);
		startActivity(intent);
		getActivity().finish();
	}

	@Override
	public void onTaskKIll(int pos) {

		TaskInfo info = (TaskInfo) adapter.getItem(pos);
		acm.killBackgroundProcesses(info.getPackageName());
		adapter.remove(adapter.getItem(pos));
		adapter.notifyDataSetChanged();
	}

}
