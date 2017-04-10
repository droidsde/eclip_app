package com.batterydoctor.superfastcharger.fastcharger.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.batterydoctor.superfastcharger.fastcharger.ui.fragment.MyScrollRelativeLayoutListener;

public class MyScroolRelativeLayout extends RelativeLayout {
	private static final int DOWN_EXCESS_MAX = 0;
	private static final int TOUCH_STATE_REST = 1;
	private static final int TOUCH_STATE_SCROLLING_DOWN = 256;
	private static final int TOUCH_STATE_SCROLLING_UP = 512;
	private static final int UP_EXCESS_MAX = 2;

	private Context mContext;
	private Scroller mScroller;
	private int mTouchSlop;
	private int mMaximumVelocity;
	private int mMinimumVelocity;
	private MyScrollRelativeLayoutListener myScrollRelativeLayoutListener;
	private VelocityTracker mVelocityTracker;
	private boolean isScrollDown = false;
	private int mTouchState = 1;
	private float mLastMotionY;

	int down_excess_move = 0;
	int up_excess_move = 0;

	int mDownMoveMax = 400;
	int mUpMoveMax = 0;
	int mMoveX = 0;
	int mMoveY = 0;
	boolean isScroll = false;
	private int mDuration = 150;

	public MyScroolRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mScroller = new Scroller(mContext);
		ViewConfiguration localViewConfiguration = ViewConfiguration
				.get(mContext);
		this.mTouchSlop = localViewConfiguration.getScaledTouchSlop();
		this.mMinimumVelocity = localViewConfiguration
				.getScaledMinimumFlingVelocity();
		this.mMaximumVelocity = localViewConfiguration
				.getScaledMaximumFlingVelocity();
	}

	private void fling(float paramFloat) {
		if (paramFloat < 0.0F) {
			scrollUp();
			return;
		}
		scrollDown();
	}

	private void initVelocityTrackerIfNotExists() {
		if (this.mVelocityTracker == null)
			this.mVelocityTracker = VelocityTracker.obtain();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		initVelocityTrackerIfNotExists();
		this.mVelocityTracker.addMovement(event);
		float f1 = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (event.getPointerCount() != 1) {
				return false;
			}
			if(isScrollDown()){
				if(f1< mDownMoveMax){
					isScroll = false;
					return false;
				}else{
					isScroll = true;
				}
			}
			this.mTouchState = TOUCH_STATE_REST;
			this.mLastMotionY = f1;
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_MOVE:
			int i = (int) (mLastMotionY - f1);
			mLastMotionY = f1;
			if(!isScroll){
				return false;
			}
			if (i < 0) {
				this.mTouchState = TOUCH_STATE_SCROLLING_DOWN;
				if (this.mDownMoveMax + this.mMoveY > 0) {
					int k = Math.max(-(this.mDownMoveMax + this.mMoveY), i);
					this.mMoveY = (k + this.mMoveY);
					onScrollBy(0, k);
					break;
				}
				if ((this.mDownMoveMax + this.mMoveY != 0)|| (this.down_excess_move >= 0))
					break;
				this.down_excess_move -= i / 2;
				onScrollBy(0, i / 2);
				break;
			} else {
				this.mTouchState = TOUCH_STATE_SCROLLING_UP;
				if (this.mUpMoveMax - this.mMoveY > 0) {
					int j = Math.min(this.mUpMoveMax - this.mMoveY, i);
					this.mMoveY = (j + this.mMoveY);
					onScrollBy(0, j);
					break;
				}
				if ((this.mUpMoveMax - this.mMoveY != 0)
						|| (this.up_excess_move >= 0))
					break;
				this.up_excess_move += i / 2;
				onScrollBy(0, i / 2);
				break;
			}
		case MotionEvent.ACTION_UP:
			VelocityTracker localVelocityTracker = this.mVelocityTracker;
			localVelocityTracker.computeCurrentVelocity(1000,
					this.mMaximumVelocity);
			float f2 = localVelocityTracker.getYVelocity();
			if ((this.up_excess_move > 0) || (this.down_excess_move > 0)) {
				if (this.up_excess_move > 0) {
					onScrollBy(0, -this.up_excess_move);
					invalidate();
					this.up_excess_move = 0;
				}
				if (this.down_excess_move > 0) {
					onScrollBy(0, this.down_excess_move);
					invalidate();
					this.down_excess_move = 0;
				}
			}
			recycleVelocityTracker();
			if (this.mTouchState == TOUCH_STATE_SCROLLING_UP) {
				if (Math.abs(f2) > this.mMinimumVelocity) {
					fling(f2);
					break;
				}
				if (Math.abs(this.mMoveY) >= 2 * this.mDownMoveMax / 3) {
					scrollDown();
					invalidate();
					break;
				}
			} else {
				if (this.mTouchState != TOUCH_STATE_SCROLLING_DOWN)
					if (Math.abs(f2) > this.mMinimumVelocity) {
						fling(f2);
						break;
					}
				if (Math.abs(this.mMoveY) > this.mDownMoveMax / 3) {
					scrollDown();
					invalidate();
					break;
				}
			}
			scrollUp();
			invalidate();
			this.mTouchState = TOUCH_STATE_REST;
			break;
		default:
			break;
		}
		isScroll = true;
		return true;
		// return super.onTouchEvent(event);
	}

	private void recycleVelocityTracker() {
		if (this.mVelocityTracker != null) {
			this.mVelocityTracker.recycle();
			this.mVelocityTracker = null;
		}
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if (this.mScroller.computeScrollOffset()) {
			scrollTo(0, this.mScroller.getCurrY());
			postInvalidate();
		}
	}

	public void scrollUp() {
		this.isScrollDown = false;
		this.mScroller.startScroll(0, this.mMoveY, 0, -this.mMoveY,
				this.mDuration);
		if (this.myScrollRelativeLayoutListener != null)
			this.myScrollRelativeLayoutListener.reset(false);
		this.mMoveY = this.mScroller.getFinalY();
		computeScroll();
	}

	public void scrollDown() {
		this.isScrollDown = true;
		this.mScroller.startScroll(0, this.mMoveY, 0,
				-(this.mDownMoveMax + this.mMoveY), this.mDuration);
		if (this.myScrollRelativeLayoutListener != null)
			this.myScrollRelativeLayoutListener.reset(true);
		this.mMoveY = this.mScroller.getFinalY();
		computeScroll();
	}

	public boolean isScrollDown() {
		return this.isScrollDown;
	}

	private void onScrollBy(int paramInt1, int paramInt2) {
		scrollBy(paramInt1, paramInt2);
		if (this.myScrollRelativeLayoutListener != null) {
			this.myScrollRelativeLayoutListener.onScrollBy(paramInt1, paramInt2);
			this.myScrollRelativeLayoutListener.onScrollTo(this.mMoveX,this.mMoveY);
		}
	}

	public void setmDownMoveMax(int paramInt) {
		this.mDownMoveMax = paramInt;
	}

	public void setmUpMoveMax(int paramInt) {
		this.mUpMoveMax = paramInt;
	}

	public void setOnScrollListener(
			MyScrollRelativeLayoutListener paramonScrollListener) {
		this.myScrollRelativeLayoutListener = paramonScrollListener;
	}
}
