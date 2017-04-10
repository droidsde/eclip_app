package com.batterydoctor.superfastcharger.fastcharger.apps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogCustomer;
import com.batterydoctor.superfastcharger.fastcharger.dialog.DialogNotify;
import com.batterydoctor.superfastcharger.fastcharger.ui.fragment.AbsFragment;

public abstract class AppsFragment extends Fragment implements OnItemClickListener{

	protected static final int DIALOG_TYPE_WARNING_STOP = 0;
	protected static final int DIALOG_TYPE_NEED_ROOT = 1;
	/**
	 * là view gốc (root view)
	 */
	protected View view;
	protected ListView mListView;
	protected AppsAdapter mAdapter;
	protected AppsPackageName mAppsPackageName;
	protected List<PackageInfo> mPackageInfos = new ArrayList<PackageInfo>();
	protected ArrayList<ParserPackage> parserPackages = new ArrayList<ParserPackage>();
	protected ActivityManager activityManager;
	/**
	 * 
	 */
	protected PackageManager packageManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAppsPackageName = AppsPackageName.getApps(getActivity());
		activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		packageManager = getActivity().getPackageManager();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.apps_layout, container, false);
		mListView = (ListView) view.findViewById(R.id.list);
		mListView.setAdapter(mAdapter);
		mListView.setEmptyView(inflater.inflate(R.layout.progressbar_loading_list_emty, null));
		mListView.setOnItemClickListener(this);
		setItemClickListen();
		return view;
	}


	protected void onCreateDialog(int type,final ParserPackage data){
		DialogCustomer dialogCustomer = null;
		switch (type) {
		case DIALOG_TYPE_NEED_ROOT:
			dialogCustomer = new DialogNotify(getActivity());
			dialogCustomer.setMessageContent(R.string.dialog_need_root);
			dialogCustomer.show();
			break;
		case DIALOG_TYPE_WARNING_STOP:
			if(data == null) return;
			dialogCustomer = new DialogCustomer(getActivity());
			dialogCustomer.setTitle(R.string.dialog_title_warning);
			dialogCustomer.setMessageContent(R.string.dialog_waring_stop);
			dialogCustomer.setDialogCloseListen(new DialogCustomer.IDialogCloseListen() {
				
				@Override
				public void OnRightButtonDialogClick() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void OnLeftButtonDialogClick() {
					// TODO Auto-generated method stub
					doStopServiceOrProcess(data);
				}
			});
			dialogCustomer.show();
			break;
		default:
			break;
		}
	}
	protected abstract void doStopServiceOrProcess(ParserPackage parserPackage);
	protected PackageInfo getPackageInfo(String packName){
		Iterator<PackageInfo> iterator = mPackageInfos.iterator();
		while (iterator.hasNext()) {
			PackageInfo info = iterator.next();
			if(info.packageName.equals(packName)){
				return info;
			}
		}
		return null;
	}
	protected Intent getIntentToOpenSettingApp(String pkg) {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.fromParts("package", pkg, null));
		return intent;
	}
	protected abstract void setItemClickListen();
	protected abstract void loadAppsRunning();

}
