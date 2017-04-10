package com.batterydoctor.superfastcharger.fastcharger.ui;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nvn.data.pref.HistoryPref;
import com.batterydoctor.superfastcharger.fastcharger.R;
import com.batterydoctor.superfastcharger.fastcharger.ui.data.PointGraph;

public class MyHistoryChartView extends LinearLayout {
	private static final int POINT_NOT_EXIT_LEVEL = 50;
	BatteryHistoryGraph infoGraph1;
	BatteryHistoryGraph infoGraph2;
	BatteryHistoryGraph infoGraph3;
	TextView textView;
	ImageView[] imageViews = new ImageView[3];
	ViewPager viewPager;
	ArrayList<BatteryHistoryGraph> infoGraphs;

	public MyHistoryChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		inflate(getContext(), R.layout.history_graph, this);
		init();
	}

	void init() {
		infoGraphs = new ArrayList<BatteryHistoryGraph>();
		viewPager = (ViewPager) findViewById(R.id.batteryInfoLayouttest);
		imageViews[0] = (ImageView) findViewById(R.id.point0);
		imageViews[1] = (ImageView) findViewById(R.id.point1);
		imageViews[2] = (ImageView) findViewById(R.id.point2);
		textView = (TextView) findViewById(R.id.chargerecord_week_total);
		infoGraph1 = new BatteryHistoryGraph(getContext());
		infoGraph2 = new BatteryHistoryGraph(getContext());
		infoGraph3 = new BatteryHistoryGraph(getContext());
		infoGraphs.add(infoGraph1);
		infoGraphs.add(infoGraph2);
		infoGraphs.add(infoGraph3);
		ViewPagesAdapter adapter = new ViewPagesAdapter(infoGraphs);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				int len = imageViews.length;
				if (arg0 < len) {
					for (int i = 0; i < len; i++) {
						if (i == arg0) {
							imageViews[i]
									.setImageResource(R.drawable.located_on);
						} else {
							imageViews[i]
									.setImageResource(R.drawable.located_off);
						}
					}
				}
				showDate(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		viewPager.setCurrentItem(adapter.getCount() - 1);
		initHistoryChart();
	}
	private void showDate(int index){
		int maxIndex = viewPager.getAdapter().getCount() - 1;
		int denta = maxIndex - index;
		if(denta<0) return;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -denta);
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		String string = String.valueOf(date)+'-' + String.valueOf(month) + '-' + String.valueOf(year);
		textView.setText(string);
	}
	public void initHistoryChart() {

		infoGraph1.clearData();
		infoGraph2.clearData();
		infoGraph3.clearData();
		int numberPoint = HistoryPref.NUMBER_POINT_IN_PER_4_HOUR * 6;
		Calendar calendar = Calendar.getInstance();
		int dateCur = calendar.get(Calendar.DAY_OF_MONTH);
		int hourCur = calendar.get(Calendar.HOUR_OF_DAY);
		int temp = 0;
		while (temp <= hourCur) {
			PointGraph pointGraph = new PointGraph();
			int level = HistoryPref.getLevel(getContext(),
					HistoryPref.getKeyFromTime(dateCur, temp));
			if (level == HistoryPref.DEFAULT_LEVEL) {
				pointGraph.isExis = false;
				pointGraph.index = POINT_NOT_EXIT_LEVEL;
			} else {
				pointGraph.isExis = true;
				pointGraph.index = level;
			}
			if (temp % HistoryPref.NUMBER_POINT_IN_PER_4_HOUR == 0) {
				pointGraph.isNote = true;
			} else {
				pointGraph.isNote = false;
			}
			temp++;
			infoGraph3.addPoint(pointGraph);
		}
		// điểm bắt đầu của ngày sau.
		PointGraph pointStart0 = new PointGraph();
		int level0 = HistoryPref.getLevel(getContext(),
				HistoryPref.getKeyFromTime(dateCur, 0));
		pointStart0.isNote = true;
		if (level0 == HistoryPref.DEFAULT_LEVEL) {
			pointStart0.isExis = false;
			pointStart0.index = POINT_NOT_EXIT_LEVEL;
		} else {
			pointStart0.isExis = true;
			pointStart0.index = level0;
		}
		// 1 ngay truoc
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		int dateOld1 = calendar.get(Calendar.DAY_OF_MONTH);
		int temp2 = 0;
		while (temp2 < numberPoint) {
			PointGraph pointGraph = new PointGraph();
			int level = HistoryPref.getLevel(getContext(),
					HistoryPref.getKeyFromTime(dateOld1, temp2));
			if (level == HistoryPref.DEFAULT_LEVEL) {
				pointGraph.isExis = false;
				pointGraph.index = POINT_NOT_EXIT_LEVEL;
			} else {
				pointGraph.isExis = true;
				pointGraph.index = level;
			}
			if (temp2 % HistoryPref.NUMBER_POINT_IN_PER_4_HOUR == 0) {
				pointGraph.isNote = true;
			} else {
				pointGraph.isNote = false;
			}
			temp2++;
			infoGraph2.addPoint(pointGraph);
		}
		infoGraph2.addPoint(pointStart0);
		// điểm bắt đầu của ngày sau.
		PointGraph pointStart1 = new PointGraph();
		int level1 = HistoryPref.getLevel(getContext(),
				HistoryPref.getKeyFromTime(dateOld1, 0));
		pointStart1.isNote = true;
		if (level1 == HistoryPref.DEFAULT_LEVEL) {
			pointStart1.isExis = false;
			pointStart1.index = POINT_NOT_EXIT_LEVEL;
		} else {
			pointStart1.isExis = true;
			pointStart1.index = level1;
		}
		// 2 ngay truoc
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		int dateOld2 = calendar.get(Calendar.DAY_OF_MONTH);
		int temp3 = 0;
		while (temp3 < numberPoint) {
			PointGraph pointGraph = new PointGraph();
			int level = HistoryPref.getLevel(getContext(),
					HistoryPref.getKeyFromTime(dateOld2, temp3));
			if (level == HistoryPref.DEFAULT_LEVEL) {
				pointGraph.isExis = false;
				pointGraph.index = POINT_NOT_EXIT_LEVEL;
			} else {
				pointGraph.isExis = true;
				pointGraph.index = level;
			}
			if (temp3 % HistoryPref.NUMBER_POINT_IN_PER_4_HOUR == 0) {
				pointGraph.isNote = true;
			} else {
				pointGraph.isNote = false;
			}
			temp3++;
			infoGraph1.addPoint(pointGraph);
		}
		infoGraph1.addPoint(pointStart1);
		// 3 ngày trước, lúc này sẽ xóa dữ liệu cũ.
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		int dateOld3 = calendar.get(Calendar.DAY_OF_MONTH);
		int temp4 = 0;
		while (temp4 < numberPoint) {
			HistoryPref.removeLevel(getContext(),
					HistoryPref.getKeyFromTime(dateOld3, temp4));
			temp4++;
		}
		infoGraph1.postInvalidate();
		infoGraph2.postInvalidate();
		infoGraph3.postInvalidate();
	}

	class ViewPagesAdapter extends PagerAdapter {
		ArrayList<BatteryHistoryGraph> infoGraphs;

		public ViewPagesAdapter(ArrayList<BatteryHistoryGraph> list) {
			// TODO Auto-generated constructor stub
			this.infoGraphs = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infoGraphs.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(this.infoGraphs.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			BatteryHistoryGraph graph = infoGraphs.get(position);
			container.addView(graph);
			return graph;
		}
	}
}
