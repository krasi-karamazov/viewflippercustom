package com.example.ViewFlipperTest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

/**
 * Created with IntelliJ IDEA.
 * User: Krasimir
 * Date: 8/26/13
 * Time: 8:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class HorizontalFlipView extends ViewFlipper {
    private static final int FLIP_NOMOVE = 0;
    private static final int FLIP_NEXT = 1;
    private static final int FLIP_PREVIOUS = 2;
    private int mFlipMode = FLIP_NOMOVE;
    private float mStartX;
    private int []mViews;
    private View mCurrentView;
    private static final int MOVE_THRESHOLD = 10;
    private int movePageThreshold;
    private int mXOffset;
    public HorizontalFlipView(Context context) {
        super(context);
        init();
    }

    public HorizontalFlipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnTouchListener(getOnTouchListener());
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViews = new int[]{R.layout.layout1, R.layout.layout2, R.layout.layout3};
        for(int i = 0; i < mViews.length; i++) {
            this.addView(inflater.inflate(mViews[i], null, false));
        }
    }

    private OnTouchListener getOnTouchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                int currentX = (int)e.getX();

                switch(e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartX = e.getX();
                        mCurrentView = getCurrentView();
                        mXOffset = currentX - mCurrentView.getLeft();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int currentXPos = (int)(currentX - mStartX);
                        if(mFlipMode == FLIP_NOMOVE){
                            if(currentXPos > MOVE_THRESHOLD){
                                mFlipMode = FLIP_PREVIOUS;
                            }else if(currentXPos < (MOVE_THRESHOLD * -1)) {
                                mFlipMode = FLIP_NEXT;
                            }
                        }
                        int left = (int)(e.getX() - mXOffset);
                        if(mFlipMode == FLIP_NEXT) {

                            mCurrentView.layout(left, mCurrentView.getTop(), left + mCurrentView.getWidth(), mCurrentView.getBottom());
                            Log.d("MOVING", "" + mCurrentView.getLeft());
                        }

                        if(mFlipMode == FLIP_PREVIOUS){
                            mCurrentView.layout(left, mCurrentView.getTop(), left + mCurrentView.getWidth(), mCurrentView.getBottom());
                            Log.d("MOVING", "previous");
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mFlipMode = FLIP_NOMOVE;
                        break;
                }
                return true;
            }
        };
    }
}

