package com.parking.helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemOnClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener myListener;

    GestureDetector myGestureDetector;

    public RecyclerItemOnClickListener(Context context, OnItemClickListener myListener){
        this.myListener = myListener;
        myGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View childrenView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if(childrenView != null && myListener != null && myGestureDetector.onTouchEvent(motionEvent)){
            myListener.onItemClick(childrenView, recyclerView.getChildPosition(childrenView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
}
