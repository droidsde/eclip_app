package fastCharger.BatteryFixes.repairbattery;

import fastCharger.BatteryFixes.repairbattery.Adapter.ViewPagerAdapter;
import fastCharger.BatteryFixes.repairbattery.Fragment.OneFragment;
import fastCharger.BatteryFixes.repairbattery.Fragment.TwoFragment;

import wolfsolflib.com.activity.AppCompatActivityAds;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import fastCharger.BatteryFixes.repairbattery.R;


public class MainActivity extends AppCompatActivityAds
         {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    int permissionCheck;
    String prefname = "repair 1.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WsetAdmobId("ca-app-pub-7232493315188211/9258021084","ca-app-pub-7232493315188211/1734754282");
		WloadAdsBanner(R.id.loQuanCao);
//		WloadAdsInterstitial();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
//        setupTabIcons();
        countRunApp();

    }
    
    

    private void setupTabIcons() {
        tabLayout.setAnimation(inFromRightAnimation());
    }

    public Animation inFromRightAnimation()
    {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(340);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TwoFragment(), this.getString(R.string.one));
        adapter.addFragment(new OneFragment(), this.getString(R.string.two));
        viewPager.setAnimation(inFromRightAnimation());
        viewPager.setAdapter(adapter);
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    
    @Override
    public void onBackPressed() {
        SharedPreferences pre=getSharedPreferences
                (prefname,MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false
        boolean bchk = pre.getBoolean("rate", false);
        int dem = pre.getInt("dem",0);
        if(dem>2 && !bchk) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        	//thiết lập tiêu đề cho Dialog
        	builder.setTitle("Repair Battery 2.0");
        	//Thiết lập nội dung cho Dialog
        	builder.setMessage("Thank for using my app !");
        	//để thiết lập Icon
        	builder.setIcon(R.drawable.icon);
        	 
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

    }

    
}
