package wolfsolflib.com.view;


import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class W2SetEvent extends W1GetView {
	public void WsetOnClickListener(OnClickListener listener, Integer... id) {
		for (int i = 0; i < id.length; i++) {
			WgetView(id[i]).setOnClickListener(listener);
		}
	}

	public void WsetOnLongClickListener(OnLongClickListener listener,
			Integer... id) {
		for (int i = 0; i < id.length; i++) {
			WgetView(id[i]).setOnLongClickListener(listener);
		}
	}

	public void WsetOnCheckedChangeListener(OnCheckedChangeListener listener,
			Integer... id) {
		for (int i = 0; i < id.length; i++) {
			WgetCheckBox(id[i]).setOnCheckedChangeListener(listener);
		}
	}

	public void WsetOnCheckedChangeListenerRadioGroup(
			RadioGroup.OnCheckedChangeListener listener, Integer... id) {
		for (int i = 0; i < id.length; i++) {
			WgetRadioGroup(id[i]).setOnCheckedChangeListener(listener);
		}
	}

	public void WaddTextChangedListener(TextWatcher listener, Integer... id) {
		for (int i = 0; i < id.length; i++) {
			WgetEditText(id[i]).addTextChangedListener(listener);
		}
	}

	public void WsetOnSeekBarChangeListener(OnSeekBarChangeListener listener,
			Integer... id) {
		for (int i = 0; i < id.length; i++) {
			WgetSeekBar(id[i]).setOnSeekBarChangeListener(listener);
		}
	}
//	public void WsetOnValueChangedListener(OnValueChangedListener listener,Integer... id ){
//		for (int i = 0; i < id.length; i++) {
//			WgetSliderFlatM(id[i]).setOnValueChangedListener(listener);
//		}
//	}
//	public void WsetOnToggleButtonFlatUIChangeListener(OnCheckedChangeListener listener,
//			Integer... id) {
//		for (int i = 0; i < id.length; i++) {
//			WgetToggleButtonFlatUI(id[i]).setOnCheckedChangeListener(listener);
//		}
//	}
}

