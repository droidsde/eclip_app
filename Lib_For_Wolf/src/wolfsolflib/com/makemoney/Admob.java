package wolfsolflib.com.makemoney;

import java.util.ArrayList;

import wolfsoftlib.com.Json.WCheckNetworkConnection;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;



public class Admob {
	
	private final String TAG = "Admob FrameWork";
	private   String AD_UNIT_ID_INTERSTITIAL = "";
	//private   String TEST_DEVICE = "716B421C641C0219EB3107DBC22334E1";
	private String AD_UNIT_ID ="";
	AppCompatActivity context;
	InterstitialAd interstitial_Exit;
	AdView adView;
	private ArrayList<String> TEST_DEVICE = new ArrayList<String>();
	public Admob(AppCompatActivity context, String AD_UNIT_ID) {
		this.context = context;
		this.AD_UNIT_ID = AD_UNIT_ID;
		
		this.AD_UNIT_ID_INTERSTITIAL = AD_UNIT_ID;
		}
	public Admob(AppCompatActivity context, String AD_UNIT_ID,
			String AD_UNIT_ID_INTERSTITIAL) {
		this.context = context;
		this.AD_UNIT_ID = AD_UNIT_ID;
		this.AD_UNIT_ID_INTERSTITIAL = AD_UNIT_ID_INTERSTITIAL;
	}

	private AdRequest getAdRequest() {
		AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
		for (String id : TEST_DEVICE) {
			adRequestBuilder.addTestDevice(id);
		}
		return adRequestBuilder.build();
	}
	public  void displayInterstitial() {
		if (!Upgrade.isPremium(context)){
			final InterstitialAd interstitial = new InterstitialAd(context);
			interstitial.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
			AdRequest adRequest = getAdRequest();
			interstitial.loadAd(adRequest);
			interstitial.setAdListener(new AdListener() {

				@Override
				public void onAdLoaded() {
					// TODO Auto-generated method stub
					super.onAdLoaded();
					if (interstitial.isLoaded()) {
						interstitial.show();
						
					}
				}

			});
		}
	}
	public void addTestDevice(String TEST_DEVICE_ID) {
		TEST_DEVICE.add(TEST_DEVICE_ID);
	}
	public void displayBanner(View view, int LayoutAdsId) {
		if (!Upgrade.isPremium(context)) {
			adView = new AdView(context);
			adView.setAdSize(AdSize.BANNER);
			adView.setAdUnitId(AD_UNIT_ID);
			final LinearLayout ads = (LinearLayout) view
					.findViewById(LayoutAdsId);
			
			ads.removeAllViews();
			ads.addView(adView);
			if(!WCheckNetworkConnection.isConnectionAvailable(context))
				ads.setVisibility(View.GONE);
			
			AdRequest adRequest = getAdRequest();
			
			adView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					// TODO Auto-generated method stub
					super.onAdLoaded();
					Log.d(TAG, "banner display");
				}
			});
			adView.loadAd(adRequest);
			
		}
	}

	public void displayBanner(int LayoutAdsId) {
		if (!Upgrade.isPremium(context)) {
			adView = new AdView(context);
			adView.setAdSize(AdSize.BANNER);
			adView.setAdUnitId(AD_UNIT_ID);
			final LinearLayout ads = (LinearLayout) context
					.findViewById(LayoutAdsId);
			
			ads.removeAllViews();
			ads.addView(adView);
			if(!WCheckNetworkConnection.isConnectionAvailable(context))
				ads.setVisibility(View.GONE);
			AdRequest adRequest = getAdRequest();
			adView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					// TODO Auto-generated method stub
					super.onAdLoaded();
					Log.d(TAG, "banner display");
				}
			});
			adView.loadAd(adRequest);
			
		}
	}
	public void displaySmartBanner(int LayoutAdsId) {
		if (!Upgrade.isPremium(context)) {
			adView = new AdView(context);
			adView.setAdSize(AdSize.SMART_BANNER);
			adView.setAdUnitId(AD_UNIT_ID);
			final LinearLayout ads = (LinearLayout) context
					.findViewById(LayoutAdsId);
			
			ads.removeAllViews();
			ads.addView(adView);
			if(!WCheckNetworkConnection.isConnectionAvailable(context))
				ads.setVisibility(View.GONE);
			AdRequest adRequest = getAdRequest();
			adView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					// TODO Auto-generated method stub
					super.onAdLoaded();
					Log.d(TAG, "banner display");
				}
			});
			adView.loadAd(adRequest);
			
		}
	}
	public void loadExitApp(){	
		if (!Upgrade.isPremium(context)){
		interstitial_Exit = new InterstitialAd(context);
		interstitial_Exit.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
		AdRequest adRequest =getAdRequest();	
		interstitial_Exit.loadAd(adRequest);
		interstitial_Exit.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// TODO Auto-generated method stub
				super.onAdLoaded();
				
			}

			@Override
			public void onAdClosed() {
				// TODO Auto-generated method stub
				super.onAdClosed();
				context.finish();
			}
			
			
		});	
		}
	}
			
	public boolean checkLoadAds(){
		if(interstitial_Exit ==null) return false;
		else return interstitial_Exit.isLoaded();
	}
	public void DisplayExit(){
			interstitial_Exit.show();
	
		
	}
//	public void adsBanner(int idAds){
//		AdView adView = (AdView) context.findViewById(idAds);
//		AdRequest adRequest = new AdRequest.Builder()
//    //  .addTestDevice("716B421C641C0219EB3107DBC22334E1")
//        .build();
//	    adView.loadAd(adRequest);
//	}
	/**
	 * <b>call after super.onResume</b>
	 * 
	 * @author Hoang Phuong
	 */
	public void onResume() {
		if (adView != null) {
			adView.resume();
		}
	}

	/**
	 * <b>call before super.onPause</b>
	 * 
	 * @author Hoang Phuong
	 */
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
	}

	/**
	 * <b>call before super.onDestroy</b>
	 * 
	 * @author Hoang Phuong
	 */
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
	}
	
}
