package wolfsolflib.com.makemoney;

import wolfsoftlib.com.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;




public class Billing {
	AppCompatActivity context;
	final String TAG = "Billing";
	private String[] SKU_PREMIUM = {"framework.premium_1","framework.premium_2","framework.premium_3"};
	private String currentSKU = "framework.premium_1";
	final int RC_REQUEST = 10001;
	private  IabHelper mHelper;
	//MaterialDialog mMaterialDialog;
	 AlertDialog dialogDone;

	public Billing(AppCompatActivity context, String base64EncodedPublicKey) {
		this.context = context;
		Log.d(TAG, "Creating IAB helper.");
		mHelper = new IabHelper(context, base64EncodedPublicKey);
		mHelper.enableDebugLogging(true);
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// complain("Problem setting up in-app billing: " + result);
					return;
				}
				if (mHelper == null)
					return;
				Log.d(TAG, "Setup successful. Querying inventory.");
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
//		mMaterialDialog = new MaterialDialog(context);

	}
	private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");
			if (mHelper == null) {
				return;
			}
			if (result.isFailure()) {
				return;
			}
			Log.d(TAG, "Query inventory was successful.");
			Purchase premiumPurchase;
			for (String sku : SKU_PREMIUM) {
				currentSKU = sku;
				premiumPurchase = inventory.getPurchase(sku);
				if (premiumPurchase != null) {
					if (verifyDeveloperPayload(premiumPurchase)) {
						
						Upgrade.upgradePremium(context);
						break;
					} else {
						Upgrade.downgradePremium(context);
					}
				} else {
					Upgrade.downgradePremium(context);
					break;
				}

			}
			context.supportInvalidateOptionsMenu();
			Log.d("PRO", "User is "
					+ (Upgrade.isPremium(context) ? "PREMIUM" : "NOT PREMIUM"));

			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};
	private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				// complain("Error purchasing: " + result);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				// complain("Error purchasing. Authenticity verification failed.");
				return;
			}

			Log.d(TAG, "Purchase successful.");

			if (purchase.getSku().equals(currentSKU)) {
				// bought the premium upgrade!
				Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
			//	alert(R.string.Wupgrade_done);
				dialog_ads(context);
				Upgrade.upgradePremium(context);

			}
		}
	};

	public boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		Log.d("payload result", payload);
		// return false;
		return Upgrade.checkHashID(context, payload);
	}

	public void destroyBilling() {
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}

	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
				+ data);
		if (mHelper == null)
//			return false;
			return true;
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			return true; // call super.onactivity result
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
			return false;
		}
	}

//	public void alert(int message) {
//		 if (mMaterialDialog != null) {
//	            mMaterialDialog.setTitle("Done")
//	                .setMessage(message)
//	                .setPositiveButton(
//	                    "OK", new View.OnClickListener() {
//	                        @Override
//	                        public void onClick(View v) {
//	                            mMaterialDialog.dismiss();
//	        
//
//	                        }
//	                    }
//	                ).show();
//	            }
//	}
	public  void dialog_ads(Context mContext){
   	 LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.wdialog_done_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        Button btnOK = (Button) view.findViewById(R.id.wbtnOK);
		builder.setView(view);
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogDone.dismiss();
				
			}
		});
		dialogDone = builder.create();
		dialogDone.show();
		
        
   }

	
	public void showBillingDialog() {
		String payload = Upgrade.getHashID(context);
		Log.d("payload", payload);
		try {
			mHelper.launchPurchaseFlow(context, currentSKU, RC_REQUEST,
					mPurchaseFinishedListener, payload);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
}
