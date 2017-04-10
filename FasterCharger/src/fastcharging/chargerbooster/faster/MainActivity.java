package fastcharging.chargerbooster.faster;

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
import android.widget.Toast;

import fastcharging.chargerbooster.faster.R;

import fastcharging.chargerbooster.faster.Adapter.ViewPagerAdapter;
import fastcharging.chargerbooster.faster.Fragment.OneFragment;
import fastcharging.chargerbooster.faster.Fragment.TwoFragment;

public class MainActivity extends AppCompatActivityAds
         {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    int permissionCheck;
    String prefname = "quick charge 1.2";
    private int[] tabIcons = {
            R.drawable.ic_charging,
            R.drawable.ic_battery,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        WsetAdmobId("ca-app-pub-7232493315188211/3824033481","ca-app-pub-7232493315188211/5300766684");
		WloadAdsBanner(R.id.loQuanCao);
		WloadAdsInterstitial();
		
		countRunApp();

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        
       // checkMainfest();
		
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
        	builder.setTitle("Quick Charge 3.0");
        	//Thiết lập nội dung cho Dialog
        	builder.setMessage("Thank for using my app !");
        	//để thiết lập Icon
        	builder.setIcon(R.drawable.ic_launch);
        	 
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

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
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
        adapter.addFragment(new OneFragment(), "ONE");
        adapter.addFragment(new TwoFragment(), "TWO");
        viewPager.setAnimation(inFromRightAnimation());
        viewPager.setAdapter(adapter);
    }
    

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
