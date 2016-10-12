package wolfsolflib.com.view;

import android.content.Context;
import android.graphics.Typeface;


public class WTypeView {
	public static Typeface  WgetTypeface(Context mContext,String mUrlFont){
		return Typeface.createFromAsset(mContext.getAssets(),mUrlFont);
		}
	

}
