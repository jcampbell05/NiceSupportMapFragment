package com.NyxDigital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

public class NiceSupportMapFragment extends SupportMapFragment {
    
    private SurfaceView searchAndFindSurfaceView(ViewGroup group){
    	int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = group.getChildAt(i);
            if (child instanceof ViewGroup) {
                return searchAndFindSurfaceView((ViewGroup) child);
            } else if (child instanceof SurfaceView) {
                return (SurfaceView) child;
            }
        }
		return null;
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup)super.onCreateView(inflater, container, savedInstanceState);
		
		// Janky "fix" to prevent artefacts when embedding GoogleMaps in a sliding view.
	    // https://github.com/jfeinstein10/SlidingMenu/issues/168
		
		SurfaceView surfaceView = searchAndFindSurfaceView(view); //Find the surface view
		
		if (surfaceView != null) { //We should get a surface view but check just in case we don't
			surfaceView.setBackgroundColor(0x00000000);  // set background to transparent
		}
		
		return view;
	}

}
