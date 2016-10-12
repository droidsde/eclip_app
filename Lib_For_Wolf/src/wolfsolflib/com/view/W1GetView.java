package wolfsolflib.com.view;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;



public  class W1GetView extends AppCompatActivity {
	public View WgetView(int id) {
		
		return (View) findViewById(id);
	}

	public Button WgetButton(int id) {
		return (Button) findViewById(id);
	}
	public Button WgetButton(int id,Typeface mTypeface) {
		Button mButton = (Button) findViewById(id);
		mButton.setTypeface(mTypeface);
		return mButton ;
	}
	public Button WgetButton(int id,String mTypeface) {
		Button mButton = (Button) findViewById(id);
		mButton.setTypeface(Typeface.createFromAsset(getAssets(),mTypeface));
		return mButton ;
	}

	public TextView WgetTextView(int id) {
		return (TextView) findViewById(id);
	}
	public TextView WgetTextView(int id,Typeface mTypeface) {
		TextView mTextView = (TextView) findViewById(id);
		mTextView.setTypeface(mTypeface);
		return mTextView ;
	}
	public TextView WgetTextView(int id,String mTypeface) {
		TextView mTextView = (TextView) findViewById(id);
		mTextView.setTypeface(Typeface.createFromAsset(getAssets(),mTypeface));
		return mTextView ;
	}

	public EditText WgetEditText(int id) {
		return (EditText) findViewById(id);
	}
	public EditText WgetEditText(int id,Typeface mTypeface) {
		EditText mEditText = (EditText) findViewById(id);
		mEditText.setTypeface(mTypeface);
		return mEditText ;
	}
	public EditText WgetEditText(int id,String mTypeface) {
		EditText mEditText = (EditText) findViewById(id);
		mEditText.setTypeface(Typeface.createFromAsset(getAssets(),mTypeface));
		return mEditText ;
	}

	public ImageView WgetImageView(int id) {
		return (ImageView) findViewById(id);
	}

	public ImageButton WgetImageButton(int id) {
		return (ImageButton) findViewById(id);
	}

	public CheckBox WgetCheckBox(int id) {
		return (CheckBox) findViewById(id);
	}

	public RadioButton WgetRadioButton(int id) {
		return (RadioButton) findViewById(id);
	}

	public RadioGroup WgetRadioGroup(int id) {
		return (RadioGroup) findViewById(id);
	}

	public Spinner WgetSpinner(int id) {
		return (Spinner) findViewById(id);
	}

	public ProgressBar WgetProgressBar(int id) {
		return (ProgressBar) findViewById(id);
	}

	public SeekBar WgetSeekBar(int id) {
		return (SeekBar) findViewById(id);
	}

	public RatingBar WgetRatingBar(int id) {
		return (RatingBar) findViewById(id);
	}

	public ListView WgetListView(int id) {
		return (ListView) findViewById(id);
	}

	public LinearLayout WgetLinearLayout(int id) {
		return (LinearLayout) findViewById(id);
	}

	public RelativeLayout WgetRelativeLayout(int id) {
		return (RelativeLayout) findViewById(id);
	}
	
	public SwitchCompat WgetSwitchCompat(int id){
		return (SwitchCompat)findViewById(id);
	}
	public SwitchCompat WgetSwitchCompat(int id,Typeface mTypeface){
		SwitchCompat mSwitchCompat = (SwitchCompat)findViewById(id);
		mSwitchCompat.setTypeface(mTypeface);
		return mSwitchCompat ;
	}
	public SwitchCompat WgetSwitchCompat(int id,String mTypeface){
		SwitchCompat mSwitchCompat = (SwitchCompat)findViewById(id);
		mSwitchCompat.setTypeface(Typeface.createFromAsset(getAssets(),mTypeface));
		return mSwitchCompat ;
	}

	


	
	
}
