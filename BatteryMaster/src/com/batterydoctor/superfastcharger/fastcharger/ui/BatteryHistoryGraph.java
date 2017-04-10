package com.batterydoctor.superfastcharger.fastcharger.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;

import com.nvn.data.pref.HistoryPref;
import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.ui.data.PointGraph;

public class BatteryHistoryGraph extends View {
	private static final String TAG = "BatteryHistoryGraph";
	private static final String[] BATTERY_LEVEL = { "100", "80", "60", "40",
			"20" };
	private static final String[] TIME_LEVEL = { "00:00", "04:00", "08:00",
			"12:00", "16:00", "20:00", "24:00" };
	private static final int BATTERY_LEVEL_COLOR = 0xeeffffff;//Color.rgb(132, 137, 141);
	private static final int AXES_LINE_COLOR = Color.rgb(115, 120, 125);
	private static final int VERTICAL_AXIS_LINE_COLOR = Color
			.rgb(180, 183, 183);
	private static final int WHITE_BG_COLOR = 0x2089afc2;
	private static final int GREEN_LINE_COLOR = 0xff62bdea;//Color.rgb(25, 199, 20);
	private static final int GREY_LINE_COLOR = 0xff5e6062;//Color.rgb(130, 135, 140);
	private ArrayList<PointGraph> mDataList = new ArrayList<PointGraph>(
			4 * HistoryPref.NUMBER_POINT_IN_PER_4_HOUR + 1);
	private ArrayList<PointF> mPoints = new ArrayList<PointF>();
	private Bitmap mPointHasRecord;
	private Bitmap mPointNoRecord;
	private int mRows = 0;
	private int mColumns = 0;
	Paint paintFill = new Paint();
	/**
	 * vẽ trục
	 */
	private Paint mAxisPaint;
	private Paint mAxisVerticalPaint;
	private Paint mGreyPaint;
	private Paint mWhitePaint;
	private Paint mDottedLinePaint;
//	Shader SHADER;
	private Rect mRect0 = new Rect();
	private float mDensity = 1.0F;
	private int mTableStartX = 0;
	private int mTableStartY = 0;
	private int mTopSpace = 0;
	private int mBottomSpace = 0;
	private int mLeftSpace = 0;
	private int mRightSpace = 0;
	/**
	 * kich thuoc moi ô
	 */
	private int mRowInterval = 0;
	private int mColInterval = 0;
	/**
	 * ve duong
	 */
	private Paint mLinePaint;
	/**
	 * ve so
	 */
	private Paint mNumberPaint;

	public BatteryHistoryGraph(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		init(context);
		initSize(context);
		initPaints();
	}

	private void init(Context context) {
		Resources resources = getResources();
		this.mPointHasRecord = BitmapFactory.decodeResource(resources,
				R.drawable.battery_info_green_point);
		this.mPointNoRecord = BitmapFactory.decodeResource(resources,
				R.drawable.battery_info_grey_point);
		this.mColumns = TIME_LEVEL.length;
		this.mRows = BATTERY_LEVEL.length;
	}

	private void initSize(Context paramContext) {
		this.mDensity = paramContext.getResources().getDisplayMetrics().density;
		this.mTopSpace = 10;
		this.mBottomSpace = (int) (20.0F * this.mDensity);
		this.mLeftSpace = (int) (25.0F * this.mDensity);
		this.mRightSpace = (int) (20.0F * this.mDensity);
		this.mTableStartX = this.mLeftSpace;
		this.mTableStartY = this.mTopSpace;
	}

	private void initPaints() {
		this.paintFill.setStyle(Style.FILL);
		this.paintFill.setAntiAlias(true);
		this.paintFill.setColor(GREEN_LINE_COLOR);
//		SHADER = new LinearGradient(0, 0, 0, getHeight(),Color.rgb(98, 189, 234), Color.WHITE, Shader.TileMode.MIRROR);
		this.mAxisPaint = new Paint();
		this.mAxisPaint.setColor(AXES_LINE_COLOR);
		this.mAxisPaint.setStrokeWidth(2.0F);
		this.mAxisVerticalPaint = new Paint();
		this.mAxisVerticalPaint.setColor(VERTICAL_AXIS_LINE_COLOR);
		this.mAxisVerticalPaint.setStrokeWidth(1.0F);
		this.mGreyPaint = new Paint();
		this.mGreyPaint.setColor(0);
		this.mWhitePaint = new Paint();
		this.mWhitePaint.setColor(WHITE_BG_COLOR);
		this.mNumberPaint = new Paint();
		this.mNumberPaint.setColor(-16777216);
		this.mNumberPaint.setAntiAlias(true);
		this.mNumberPaint.setTextSize(10.0F * this.mDensity);
		this.mNumberPaint.getTextBounds("0", 0, 1, this.mRect0);
		this.mLinePaint = new Paint();
		this.mLinePaint.setColor(GREEN_LINE_COLOR);
		this.mLinePaint.setAntiAlias(true);
		this.mLinePaint.setStrokeWidth(1.5F * this.mDensity);
		this.mDottedLinePaint = new Paint();
		this.mDottedLinePaint.setColor(GREY_LINE_COLOR);
		this.mDottedLinePaint.setStyle(Paint.Style.STROKE);
		this.mDottedLinePaint.setAntiAlias(true);
		this.mDottedLinePaint.setStrokeWidth(2.0F * this.mDensity);
		DashPathEffect localDashPathEffect = new DashPathEffect(new float[] {
				2.0F, 4.0F }, 1.0F);
		this.mDottedLinePaint.setPathEffect(localDashPathEffect);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		drawBackground(canvas);
//		LogBuider.e(TAG, "onDraw");
		drawBatteryInfoLine(canvas);
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
	}
	private void caculatePosition(int paramInt1, int paramInt2) {
		int i = paramInt1 - this.mLeftSpace - this.mRightSpace;
		this.mRowInterval = ((paramInt2 - this.mTopSpace - this.mBottomSpace) / this.mRows);
		this.mColInterval = (i / (this.mColumns - 1));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		caculatePosition(w, h);
	}

	private void drawBackground(Canvas paramCanvas) {
		int j = this.mTableStartX;
		int k = this.mTableStartY;
		int m = j + this.mColInterval * this.mColumns;// ?i?m cu?i c?a c?t cu?i
														// cùng.
		int n = k + this.mRowInterval * this.mRows;// ?i?m cu?i c?a hàng cu?i
													// cùng.
		int i1 = k + this.mRowInterval;
		int i3 = k;
		// V? màu cho các hàng
		for (int i = 0; i < this.mRows; i++) {
			if (i % 2 == 0) {
				paramCanvas.drawRect(j, i3, m, i1, this.mWhitePaint);
			} else {
				paramCanvas.drawRect(j, i3, m, i1, this.mGreyPaint);
			}
			i3 += this.mRowInterval;
			i1 += this.mRowInterval;
		}
		// Vẽ trục
		paramCanvas.drawLine(j, k - 5, j, n, this.mAxisPaint);
		paramCanvas.drawLine(j, n, m + 5, n, this.mAxisPaint);
		// V? S?
		this.mNumberPaint.setTextSize((float) (10.5D * getResources()
				.getDisplayMetrics().density));
		this.mNumberPaint.setTextAlign(Paint.Align.RIGHT);
		this.mNumberPaint.setColor(BATTERY_LEVEL_COLOR);
		int i4 = j - 5;
		int i5 = k + this.mRect0.height() / 2;
		for (int i7 = 0; i7 < this.mRows; i7++) {
			paramCanvas.drawText(BATTERY_LEVEL[i7], i4, i5, this.mNumberPaint);
			i5 += this.mRowInterval;
		}
		this.mNumberPaint.setTextAlign(Paint.Align.CENTER);
		int i8 = n + 10 + this.mRect0.height();
	    int i9 = j;
	    for (int i10 = 0; i10 < this.mColumns; i10++) {
			paramCanvas.drawText(TIME_LEVEL[i10], i9, i8, this.mNumberPaint);
			i9 += mColInterval;
		}
	}

	public void initTest() {
		addPoint(new PointGraph(true, false, 50));
		addPoint(new PointGraph(false, false, 20));
		addPoint(new PointGraph(false, false, 30));
		addPoint(new PointGraph(false, false, 70));
		addPoint(new PointGraph(true, false, 55));
		addPoint(new PointGraph(false, false, 10));
		addPoint(new PointGraph(false, false, 5));
		addPoint(new PointGraph(false, true, 0));
		addPoint(new PointGraph(true, true, 100));
		addPoint(new PointGraph(false, true, 10));
		addPoint(new PointGraph(false, true, 80));
		addPoint(new PointGraph(false, true, 20));
		addPoint(new PointGraph(true, true, 50));
		addPoint(new PointGraph(false, true, 90));
		addPoint(new PointGraph(false, true, 20));
		addPoint(new PointGraph(false, true, 60));
		addPoint(new PointGraph(true, true, 40));
	}

	private void drawBatteryInfoLine(Canvas paramCanvas) {
		mPoints.clear();
		int i = this.mTableStartX;
		int j = this.mTableStartY;
		int k = j + this.mRowInterval * this.mRows;
		float f1 = (float)((float)this.mColInterval / (float)HistoryPref.NUMBER_POINT_IN_PER_4_HOUR);// kích th??c 1 ?o?n trên 1 kho?ng
											// th?i gian
		int m = k - j;// khoảng vẽ theo trục Y
		int i1 = this.mDataList.size();
		int denta = 0;
		// có d? li?u
		for (int x = 0; x < i1; x++) {
			PointGraph pointGraph = this.mDataList.get(x);
			PointF point = new PointF();
			point.x = (i + f1 * x)+denta;// vi tri diem?
			int yy = m * pointGraph.index / 100;
			point.y = k - yy;
			mPoints.add(point);
			if (pointGraph.isNote) {
				drawPoint(paramCanvas, point, pointGraph.isExis);
			}
//			denta +=1;
		}
		int pointsize = mPoints.size();
		for (int x = 0; x < pointsize; x++) {
			if ((x + 1) < pointsize) {
				drawLine(paramCanvas, mPoints.get(x), mPoints.get(x + 1),
						mDataList.get(x+1).isExis);
				fillRect(paramCanvas, mPoints.get(x), mPoints.get(x+1),mDataList.get(x+1).isExis);
			}
		}
	}
	public void clearData(){
		if (this.mDataList != null)
		      this.mDataList.clear();
		if (this.mPoints != null)
		      this.mPoints.clear();
	}
	private void drawLine(Canvas paramCanvas, PointF paramPoint1,
			PointF paramPoint2, boolean paramBoolean) {
		if (paramBoolean)
			this.mLinePaint.setColor(GREEN_LINE_COLOR);
		else {
			this.mLinePaint.setColor(GREY_LINE_COLOR);
		}
		paramCanvas.drawLine(paramPoint1.x, paramPoint1.y, paramPoint2.x,
				paramPoint2.y, this.mLinePaint);
	}

	private void drawPoint(Canvas paramCanvas, PointF paramPoint,
			boolean paramBoolean) {

		if (this.mPointHasRecord == null) {
			this.mPointHasRecord = BitmapFactory.decodeResource(getResources(),
					R.drawable.battery_info_green_point);
		}
		if (this.mPointNoRecord == null) {
			this.mPointNoRecord = BitmapFactory.decodeResource(getResources(),
					R.drawable.battery_info_grey_point);
		}
		if (paramBoolean) {
			paramCanvas.drawBitmap(this.mPointHasRecord, paramPoint.x
					- this.mPointHasRecord.getWidth() / 2, paramPoint.y
					- this.mPointHasRecord.getHeight() / 2, null);
			return;
		}
		paramCanvas.drawBitmap(this.mPointNoRecord, paramPoint.x
				- this.mPointNoRecord.getWidth() / 2, paramPoint.y
				- this.mPointNoRecord.getHeight() / 2, null);
	}
	private void fillRect(Canvas canvas,PointF point1, PointF point2,boolean exit){
		Path path = new Path();
		int k = this.mTableStartY;
		int n = k + this.mRowInterval * this.mRows;
		path.moveTo(point1.x, point1.y);
		path.lineTo(point2.x, point2.y);
		path.lineTo(point2.x, n);
		path.lineTo(point1.x, n);
		if(exit){
			paintFill.setColor(0x4062bdea);
		}else{
			paintFill.setColor(0x405e6062);
		}
		canvas.drawPath(path, paintFill);
	}
	public void addPoint(PointGraph graph) {
		if (this.mDataList == null)
			this.mDataList = new ArrayList<PointGraph>();
		this.mDataList.add(graph);
	}

	
	public void recycle() {
		if (this.mPointNoRecord != null) {
			this.mPointNoRecord.recycle();
			this.mPointNoRecord = null;
		}
		if (this.mPointHasRecord != null) {
			this.mPointHasRecord.recycle();
			this.mPointHasRecord = null;
		}
	}
}
