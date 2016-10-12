package fastCharger.BatteryFixes.repairbattery;

import fastCharger.BatteryFixes.repairbattery.Adapter.ViewPagerAdapter;
import fastCharger.BatteryFixes.repairbattery.Fragment.OneFragment;
import fastCharger.BatteryFixes.repairbattery.Fragment.TwoFragment;

import wolfsolflib.com.activity.AppCompatActivityAds;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
}