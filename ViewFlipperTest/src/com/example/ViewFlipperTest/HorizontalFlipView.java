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
    private View mNextView;
    private int mNextIdx;
    private int mPrevIdx;
    private View mPrevView;
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
            View v = inflater.inflate(mViews[i], null, false);
            v.setId(i);
            this.addView(v);
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
                        mNextIdx = ((mCurrentView.getId() + 1) >= mViews.length)?0:mCurrentView.getId() + 1;
                        mPrevIdx = ((mCurrentView.getId() - 1) < 0)?mViews.length - 1:mCurrentView.getId() - 1;

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
                            mNextView = findViewById(mNextIdx);

                            mCurrentView.layout(left, mCurrentView.getTop(), left + mCurrentView.getWidth(), mCurrentView.getBottom());
                            if(mNextView != null) {
                                mNextView.layout(mCurrentView.getRight(), mCurrentView.getTop(), mCurrentView.getRight() + mNextView.getWidth(), mCurrentView.getBottom());
                                mNextView.setVisibility(View.VISIBLE);
                            }else{
                                Log.d("MOVING", "NULl");
                            }
                            Log.d("MOVING", "NEXT");
                        }

                        if(mFlipMode == FLIP_PREVIOUS){
                            mPrevView = findViewById(mPrevIdx);
                            mCurrentView.layout(left, mCurrentView.getTop(), left + mCurrentView.getWidth(), mCurrentView.getBottom());
                            if(mPrevView != null) {
                                mPrevView.layout(mCurrentView.getLeft() - mPrevView.getWidth(), mCurrentView.getTop(), mCurrentView.getLeft(), mCurrentView.getBottom());
                                mPrevView.setVisibility(View.VISIBLE);

                            }
                            Log.d("MOVING", "previous " + mPrevIdx);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(Math.abs(mCurrentView.getLeft()) > mCurrentView.getWidth() / 5 && mFlipMode == FLIP_NEXT) {
                            showNext();
                        }else{
                            showPrevious();
                        }
                        mFlipMode = FLIP_NOMOVE;

                        break;
                }
                return true;
            }
        };
    }
}

