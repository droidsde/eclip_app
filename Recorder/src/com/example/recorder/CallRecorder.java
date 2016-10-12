package com.example.recorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.recorder.adapter.AudioPlayActivity;
import com.example.recorder.adapter.LvFileAdapter;
import com.example.recorder.frament.OneFragment;
import com.example.recorder.frament.TwoFragment;

import android.R.bool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class CallRecorder
    extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener
{
    private static final int MENU_UPDATE = Menu.FIRST;
    private static final int MENU_PREFERENCES = Menu.FIRST+1;
    public static boolean isChangeMenu = false;
    public static Menu menu;

    private static final int SHOW_PREFERENCES = 1;

    private static final String[] TABS = {"Preferences"};
    public static Boolean isCheck = true;
    
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Switch mySwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
 
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
 
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
 
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        setItemView();
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	setupViewPager(viewPager);
    }
    
    
    
    public void setItemView()
    {
    	mySwitch = (Switch) findViewById(R.id.mySwitch);
    	mySwitch.setChecked(true);
    	mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

    		   @Override
    		   public void onCheckedChanged(CompoundButton buttonView,
    		     boolean isChecked) {
    			   isCheck = isChecked;
    			   String xx = Boolean.toString(isChecked);
    		    if(isChecked){
    		    	
    		    	Toast.makeText(getApplicationContext(), xx, Toast.LENGTH_LONG).show();
    		    }else{
    		    }
    		    Toast.makeText(getApplicationContext(), xx, Toast.LENGTH_LONG).show();
    		   }
    		  });
    }
    
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "INBOX");
        adapter.addFragment(new TwoFragment(), "SAVED");
        viewPager.setAdapter(adapter);
        
    }
    
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
 
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
 
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
 
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
 
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
 
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
            	isChangeMenu=false;
            	menu.findItem(R.id.action_delete).setVisible(isChangeMenu);
                menu.findItem(R.id.action_share).setVisible(isChangeMenu);
                menu.findItem(R.id.action_save).setVisible(isChangeMenu);
            	deleteFile();
                return true;
            case R.id.action_share:
            	isChangeMenu=false;
            	menu.findItem(R.id.action_delete).setVisible(isChangeMenu);
                menu.findItem(R.id.action_share).setVisible(isChangeMenu);
                menu.findItem(R.id.action_save).setVisible(isChangeMenu);
                Uri uri = Uri.parse(LvFileAdapter.linkFile);
			    Intent share = new Intent(Intent.ACTION_SEND);
			    share.setType("audio/*");
			    share.putExtra(Intent.EXTRA_STREAM, uri);
			    startActivity(Intent.createChooser(share, "Share Sound File"));
                return true;
            case R.id.action_save:
            	isChangeMenu=false;
            	menu.findItem(R.id.action_delete).setVisible(isChangeMenu);
                menu.findItem(R.id.action_share).setVisible(isChangeMenu);
                menu.findItem(R.id.action_save).setVisible(isChangeMenu);
                saveFile();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	menu.findItem(R.id.action_delete).setVisible(isChangeMenu);
        menu.findItem(R.id.action_share).setVisible(isChangeMenu);
        menu.findItem(R.id.action_save).setVisible(isChangeMenu);
        Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_share_grey600_24dp); 
 	   	 myIcon1.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP); 
 	   Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_delete_white_24dp); 
	   	 myIcon2.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
	   	Drawable myIcon3 = getResources().getDrawable(R.drawable.ic_save_white_24dp); 
	   	 myIcon3.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
	   	menu.findItem(R.id.action_delete).setIcon(myIcon2);
	   	menu.findItem(R.id.action_save).setIcon(myIcon3);
	   	menu.findItem(R.id.action_share).setIcon(myIcon1);
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_date) {
        	Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager
        			+ ":" + viewPager.getCurrentItem());
            if (viewPager.getCurrentItem() == 0 && page != null) {
                 ((OneFragment)page).ShowSavedFiles();     
            }else {
            	((TwoFragment)page).ShowSavedFiles(); 
			} 
        }  else if (id == R.id.nav_contact) {
//            setupViewPagerByContact(viewPager);
        	Fragment page2 = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager
        			+ ":" + viewPager.getCurrentItem());
            if (viewPager.getCurrentItem() == 0 && page2 != null) {
                 ((OneFragment)page2).showByContact();     
            }else {
            	((TwoFragment)page2).showByContact(); 
			} 
        }else if(id==R.id.nav_setting)
        {
        	Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
        }else if (id==R.id.nav_to_pro) {
        	Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
		}else if(id==R.id.nav_rate)
		{
			Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
		}else if(id==R.id.nav_share)
		{
			Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
		}else if(id==R.id.nav_more)
		{
			Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
		}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    
    private void saveFile()
    {
    	AlertDialog.Builder b=new AlertDialog.Builder(CallRecorder.this);
		LayoutInflater inflater = CallRecorder.this.getLayoutInflater();
	    b.setMessage("Save this recording?");
		b.setPositiveButton("SAVE", new DialogInterface. OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String root = Environment.getExternalStorageDirectory().toString();
				String audioSave = root +"/audioSave/";
				moveFile(root +"/ss/", LvFileAdapter.name, audioSave);
				setupViewPager(viewPager);
				dialog.cancel();
			}
		});
		b.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		b.create().show();
    }
    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath); 
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);        
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

                // write the output file
                out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();  


        } 

             catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
              catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
    
    public boolean deleteFile(){
    	AlertDialog.Builder b=new AlertDialog.Builder(CallRecorder.this);
		LayoutInflater inflater = this.getLayoutInflater();
	    b.setMessage("Delete this audio?");
		b.setPositiveButton("OK", new DialogInterface. OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				
				 try {
			            // delete the original file
			            new File(LvFileAdapter.linkFile).delete();  

			        }
			       catch (Exception e) {
			            Log.e("tag", e.getMessage());
			        }
				 setupViewPager(viewPager);
				 dialog.cancel();
			}
		});
		b.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});

		b.create().show();
		return false;
    }

    public static class MyTabIndicator extends LinearLayout
    {
        public MyTabIndicator(Context context, String label)
        {
            super(context);
            
            View tab = LayoutInflater.from(context).inflate(R.layout.tab_indicator, this);

            TextView tv = (TextView)tab.findViewById(R.id.tab_label);
            tv.setText(label);
        }
    }
    
    private OnBackStackChangedListener getListener()
    {
        OnBackStackChangedListener result = new OnBackStackChangedListener()
        {
            public void onBackStackChanged() 
            {                   
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null)
                {
                    OneFragment currFrag = (OneFragment)manager.
                    findFragmentById(0);

                    currFrag.onResume();
                }                   
            }
        };

        return result;
    }
}
