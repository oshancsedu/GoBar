package com.example.sifat.Custom;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.FrameLayout;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

import static com.example.sifat.Utilities.CommonUtilities.LOG_TAG_TAXIPOSITIONSERVICE;

/**
 * Created by Sifat on 10/25/2015.
 */
public class CustomMapFragmment extends MapFragment {

    public View mapLayout;
    public TouchableWrapper frameLayout;
    private OnTouchListener listenTouch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mapLayout = super.onCreateView(inflater, container, savedInstanceState);
        frameLayout = new TouchableWrapper(getActivity());

        frameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        frameLayout.addView(mapLayout);
        /*((ViewGroup) mapLayout).addView(frameLayout,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));*/

        return frameLayout;
    }

    @Nullable
    @Override
    public View getView() {
        return mapLayout;
    }

    public void setListener(OnTouchListener listener) {
        listenTouch = listener;
    }

    public interface OnTouchListener {
        public abstract void onCusTouchUp();

        public abstract void onCusTouchDown();
    }

    public class TouchableWrapper extends FrameLayout {

        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            //Log.i(LOG_TAG_TAXIPOSITIONSERVICE,"Touched event");
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    Log.i(LOG_TAG_TAXIPOSITIONSERVICE, "Touched up");
                    listenTouch.onCusTouchUp();
                    break;

                case MotionEvent.ACTION_DOWN:
                    Log.i(LOG_TAG_TAXIPOSITIONSERVICE, "Touched down");
                    listenTouch.onCusTouchDown();
                    break;
            }
            return super.dispatchTouchEvent(event);
        }
    }
}
