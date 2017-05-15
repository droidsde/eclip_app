package com.boost.clean.speedbooster;

import wolfsolflib.com.activity.AppCompatActivityAds;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.boost.clean.speedbooster.fragments.BaseFragment;
import com.boost.clean.speedbooster.fragments.BatterySavingPlanFragment;
import com.boost.clean.speedbooster.fragments.BoostResultFragment;
import com.boost.clean.speedbooster.fragments.CleanResultFragment;
import com.boost.clean.speedbooster.fragments.HomeFragment;
import com.boost.clean.speedbooster.fragments.SettingFragment;
import com.boost.clean.speedbooster.utils.KeyboardUtil;
import com.boost.clean.speedbooster.utils.Utils;

public class MainActivity extends AppCompatActivityAds implements BaseFragment.OnBaseFragmentListener {

    /**
     * HeaderBarType define types of header
     */
    public enum HeaderBarType {
        TYPE_HOME, TYPE_CLEAN, TYPE_CLEAN_UP, TYPE_BATTERY_PLAN
    }

    private Menu mMenu;
    String prefname = "speed booster 1.2";

    private Fragment mCurrentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
            getSupportActionBar().setElevation(0);
        }
        createAdsAndBill();
        countRunApp();
    }
    
    public void countRunApp(){
        SharedPreferences pre=getSharedPreferences
                (prefname, MODE_PRIVATE);
        int dem = pre.getInt("dem", 0);
        dem++;
        SharedPreferences.Editor editor=pre.edit();
        editor.putInt("dem", dem);
        //chấp nhận lưu xuống file
        editor.commit();
    }
    
    public void savingPreferences()
    {
        // getSharedPreferences
        SharedPreferences pre=getSharedPreferences
                (prefname, MODE_PRIVATE);
        
        SharedPreferences.Editor editor=pre.edit();
        //lưu vào editor
        editor.putBoolean("rate", true);
         //chấp nhận lưu xuống file
        editor.commit();
    }
    
    public void createAdsAndBill(){		
		WsetAdmobId(ContentValueApp.AD_UNIT_ID, ContentValueApp.AD_UNIT_ID_INTERSTITIAL);
		WloadAdsBanner(R.id.adBanner);
		WloadAdsInterstitial();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        replaceFragment(new HomeFragment(), false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            View view = getCurrentFocus();
            if (view != null) {
                KeyboardUtil.hideSoftKeyboard(MainActivity.this);
            }
            if (!(mCurrentFragment instanceof HomeFragment)) {
                onBackPressed();
            }
        } else if (id == R.id.action_settings) {
            replaceFragment(new SettingFragment(), false);
        } else if (id == R.id.action_rate) {
            Utils.rateApp(MainActivity.this);
        } 
//        else if (id == R.id.action_remove_ads) {
//            Utils.removeAds(MainActivity.this);
//        } 
        else if (id == R.id.action_alarm) {
            replaceFragment(new BatterySavingPlanFragment(), false);
        }

        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (addToBackStack) {
            transaction.setCustomAnimations(R.anim.open_main, R.anim.close_next);
        } else {
            transaction.setCustomAnimations(R.anim.open_next, R.anim.close_main,
                    R.anim.open_main, R.anim.close_next);
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        } else {
            transaction.addToBackStack(fragment.toString());
        }
        if (fragment.getTag() == null) {
            transaction.replace(R.id.contentFrame, fragment, fragment.toString());
        } else {
            transaction.replace(R.id.contentFrame, fragment, fragment.getTag());
        }
        transaction.commit();
        mCurrentFragment = fragment;
    }

    @Override
    public void onBackPressed() {
    	if (mCurrentFragment instanceof CleanResultFragment
                || mCurrentFragment instanceof BoostResultFragment) {
            return;
        }
        onBack();
    }

    public void onBack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() != 1) {
            FragmentManager.BackStackEntry backStackEntry = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 2);
            mCurrentFragment = getSupportFragmentManager().findFragmentByTag(backStackEntry.getName());
            super.onBackPressed();
        } else {
	    	SharedPreferences pre=getSharedPreferences
	                (prefname,MODE_PRIVATE);
	        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false
	        boolean bchk = pre.getBoolean("rate", false);
	        int dem = pre.getInt("dem",0);
	        if(dem>2 && !bchk) {
	        	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	        	//thiết lập tiêu đ�? cho Dialog
	        	builder.setTitle("Quick Charge 3.0");
	        	//Thiết lập nội dung cho Dialog
	        	builder.setMessage("Thank for using my app !");
	        	//để thiết lập Icon
	        	builder.setIcon(R.mipmap.ic_launcher);
	        	 
	        	builder.setPositiveButton("Rate App", new DialogInterface.OnClickListener() {
	        	    @Override
	        	    public void onClick(DialogInterface dialog, int which) {
	        	    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_app)));
	                    startActivity(browserIntent);
	                    savingPreferences();
	                    finish();
	        	    }
	        	});
	        	
	        	builder.setNegativeButton("Late", new DialogInterface.OnClickListener() {
	        	    @Override
	        	    public void onClick(DialogInterface dialog, int which) {
	        	    	finish();
	        	    }
	        	});
	        	 
	        	builder.create().show();
	     
	        }else {
	            finish();
	        }
        }
    }

    @Override
    public void setTitleHeader(String title) {
        if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void setTypeHeader(HeaderBarType type) {
        switch (type) {
            case TYPE_CLEAN:
                if (null != getSupportActionBar() && null != mMenu) {
                    getSupportActionBar().show();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
                   // mMenu.findItem(R.id.action_remove_ads).setVisible(false);
                    mMenu.findItem(R.id.action_rate).setVisible(false);
                    mMenu.findItem(R.id.action_settings).setVisible(false);
                    mMenu.findItem(R.id.action_alarm).setVisible(false);
                }
                break;
            case TYPE_CLEAN_UP:
                if (null != getSupportActionBar() && null != mMenu) {
                    getSupportActionBar().hide();
                }
                break;
            case TYPE_BATTERY_PLAN:
                if (null != getSupportActionBar() && null != mMenu) {
                    getSupportActionBar().show();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
                  //  mMenu.findItem(R.id.action_remove_ads).setVisible(false);
                    mMenu.findItem(R.id.action_rate).setVisible(false);
                    mMenu.findItem(R.id.action_settings).setVisible(false);
                    mMenu.findItem(R.id.action_alarm).setVisible(true);
                }
                break;
            default:
                if (null != getSupportActionBar() && null != mMenu) {
                    getSupportActionBar().show();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
                 //   mMenu.findItem(R.id.action_remove_ads).setVisible(true);
                    mMenu.findItem(R.id.action_rate).setVisible(true);
                    mMenu.findItem(R.id.action_settings).setVisible(true);
                    mMenu.findItem(R.id.action_alarm).setVisible(false);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case HomeFragment.WRITE_EXTERNAL_STORAGE_REQUEST: {
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }
        }
    }
}
