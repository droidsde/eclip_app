package wolfsolflib.com.view;


public class W3SetValue extends W2SetEvent {
	// Button
	public void WsetTextButton(int id, String text) {
		WgetButton(id).setText(text);
	}

	public void WsetTextButton(int id, int text) {
		WgetButton(id).setText(text);
	}

	public void WsetTextButton(int id, boolean value, int trueText,
			int falseText) {
		WgetButton(id).setText(value ? trueText : falseText);
	}

	public void WsetTextButton(int id, boolean value, String trueText,
			String falseText) {
		WgetButton(id).setText(value ? trueText : falseText);
	}

	// Textview
	public void WsetTextTextView(int id, String text) {
		WgetTextView(id).setText(text);
	}

	public void WsetTextTextView(int id, int text) {
		WgetTextView(id).setText(text);
	}

	public void WsetTextTextView(int id, boolean value, int trueText,
			int falseText) {
		WgetTextView(id).setText(value ? trueText : falseText);
	}

	public void WsetTextTextView(int id, boolean value, String trueText,
			String falseText) {
		WgetTextView(id).setText(value ? trueText : falseText);
	}

	// Edittext
	public void WsetTextEditText(int id, String text) {
		WgetEditText(id).setText(text);
	}

	public void WsetTextEditText(int id, int text) {
		WgetEditText(id).setText(text);
	}

	public void WsetTextEditText(int id, boolean value, int trueText,
			int falseText) {
		WgetEditText(id).setText(value ? trueText : falseText);
	}

	public void WsetTextEditText(int id, boolean value, String trueText,
			String falseText) {
		WgetEditText(id).setText(value ? trueText : falseText);
	}
}
