package com.batterydoctor.superfastcharger.fastcharger.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.ui.fragment.TabInfo;

public class TitleIndicator extends LinearLayout implements View.OnClickListener,ViewPager.OnPageChangeListener{

	private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Context mContext;
	private ViewPager mViewPager;
	private ViewPager.OnPageChangeListener mListener;
	TextView one;
	TextView two;
	ArrayList<TabInfo> tabInfos;
	private int mScrollState;
	private int mCurrentPage;
	private float mPositionOffset;
	Bitmap bitmap;
	private LayoutInflater inflater;
	public TitleIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		setFocusable(true);
		inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initialize();

		Resources resources = getResources();
		setSelectedColor(resources.getColor(R.color.main_tab_text_color_pressed));
		setBackgroundColor(resources.getColor(R.color.tab_indicator_background_color));
	}
	public void setSelectedColor(int selectedColor) {
        mPaint.setColor(selectedColor);
        invalidate();
    }
	private void initialize(){
		inflater.inflate(R.layout.title_indicator, this, true);
		one = (TextView) findViewById(R.id.tab_title);
		two = (TextView) findViewById(R.id.premium_tip);
	}
	public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }
        if (mViewPager != null) {
            //Clear us from the old pager.
            mViewPager.setOnPageChangeListener(null);
        }
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(this);
        invalidate();
    }
	public void setViewPager(ViewPager view, int initialPosition,ArrayList<TabInfo> tabInfos) {
		this.tabInfos = tabInfos;
        setViewPager(view);
        setCurrentItem(initialPosition);
        setTextIndicator(tabInfos.get(0).getString(), tabInfos.get(1).getString());
    }
	public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();
    }
	public void setTextIndicator(String string1,String string2){
		one.setText(string1);
		one.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setCurrentItem(0);
			}
		});
		two.setText(string2);
		two.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setCurrentItem(1);
			}
		});
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (mViewPager == null) {
            return;
        }
		final int count = mViewPager.getAdapter().getCount();
        if (count == 0) {
            return;
        }
        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }
        final int paddingLeft = getPaddingLeft();
        final int pageWidth = (int)((getWidth() - paddingLeft - getPaddingRight()) / (1f * count));
        final int left = (int)(paddingLeft + pageWidth * (mCurrentPage + mPositionOffset));
        final int right = left + pageWidth;
        final int top = getHeight()-8;
        final int bottom = getHeight();//getHeight() - getPaddingBottom();
//        this.drawable.draw(canvas)
        canvas.drawRect(left, top, right, bottom, mPaint);
	}
	@Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;

        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub
		mCurrentPage = position;
        mPositionOffset = positionOffset;
        if(position == 0){
        	one.setSelected(true);
        	two.setSelected(false);
        }else{
        	one.setSelected(false);
        	two.setSelected(true);
        }
        invalidate();
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
	}
	@Override
    public void onPageSelected(int position) {
        if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            mPositionOffset = 0;
            invalidate();
        }
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub
		SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        requestLayout();
	}
	@Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;
    }
	static class SavedState extends BaseSavedState {
        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = this;
    }

}
