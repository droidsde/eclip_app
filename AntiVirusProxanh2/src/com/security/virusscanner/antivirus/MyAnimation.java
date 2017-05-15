package com.security.virusscanner.antivirus;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MyAnimation extends Animation{

	private View view;
    private float centerX, centerY;           // center x,y position of circular path
    private float prevX, prevY;     // previous x,y position of image during animation
    private float r;                // radius of circle
    private float prevDx, prevDy;
    private boolean firstMove = true;

    /**
     * @param view - View that will be animated
     * @param r - radius of circular path
     */
    public MyAnimation(View view, float r){
        this.view = view;
        this.r = r;
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        // calculate position of image center
        int cxImage = width / 2;
        int cyImage = height / 2;
        centerX = view.getLeft() + cxImage;
        centerY = view.getTop() + cyImage;

        // set previous position to center
        prevX = centerX;
        prevY = centerY;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if(interpolatedTime == 0){
            t.getMatrix().setTranslate(prevDx, prevDy);
            return;
        }

        float angleDeg = (interpolatedTime * 360f + 90) % 360;
        float angleRad = (float) -Math.toRadians(angleDeg);

        // r = radius, cx and cy = center point, a = angle (radians)
        float x = (float) (centerX - r * Math.cos(angleRad));
        float y = (float) (centerY - r * Math.sin(angleRad));


        float dx = prevX - x;
        float dy = prevY - y;

        prevX = x;
        prevY = y;

        prevDx = dx;
        prevDy = dy;


        t.getMatrix().setTranslate(dx, dy);
        if(firstMove){
        	this.view.setVisibility(View.VISIBLE);
        	firstMove = false;
        }
    }
}
