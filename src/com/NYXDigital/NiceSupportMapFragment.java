package com.NYXDigital;

import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

public class NiceSupportMapFragment extends SupportMapFragment {
	
	private boolean preventParentScrolling = true;
    
    private SurfaceView searchAndFindSurfaceView(ViewGroup group){
    	int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = group.getChildAt(i);
            if (child instanceof ViewGroup) {
            	SurfaceView surfaceView = searchAndFindSurfaceView((ViewGroup) child);
            	
            	if (surfaceView != null) {
            		return surfaceView;
            	}
            } else if (child instanceof SurfaceView) {
                return (SurfaceView) child;
            }
        }
		return null;
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup)super.onCreateView(inflater, container, savedInstanceState);
		view.setBackgroundColor(0x00000000); //Set Root View to be transparent to prevent black screen on load
		
		SurfaceView surfaceView = searchAndFindSurfaceView(view); //Find the surface view
		
		// Janky "fix" to prevent artefacts when embedding GoogleMaps in a sliding view.
	    // https://github.com/jfeinstein10/SlidingMenu/issues/168
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ) {
			
			if (surfaceView != null) { //We should get a surface view but check just in case we don't
				surfaceView.setBackgroundColor(0x00000000);  // set background to transparent
				
				//Fix for reducing black view flash issue
				surfaceView.setZOrderMediaOverlay(true); 
				SurfaceHolder holder = surfaceView.getHolder();
				holder.setFormat(PixelFormat.RGBA_8888);
			}
			
		}
		
		//Stop Containing Views from moving when a user is interacting with Map View Directly
		surfaceView.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View view, MotionEvent event) {
				view.getParent().requestDisallowInterceptTouchEvent(preventParentScrolling);
	            return false;
			}
		});
		
		return view;
	}
	
	public boolean getPreventParentScrolling(){
		return preventParentScrolling;
	}
	
	public void setPreventParentScrolling(boolean value){
		preventParentScrolling = value;
	}

}
