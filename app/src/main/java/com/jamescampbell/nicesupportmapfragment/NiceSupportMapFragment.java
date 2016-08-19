package com.jamescampbell.nicesupportmapfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.gms.maps.SupportMapFragment;

public class NiceSupportMapFragment extends SupportMapFragment {

    //Many thanks to Pepsi1x1 for his contribution to this Texture View detection flag
    private static final boolean HAS_TEXTURE_VIEW_SUPPORT = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    private static final boolean IS_RGBA_8888_BY_DEFAULT = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    private static final boolean SUPPORTS_TEXTURE_VIEW_BACKGROUND = android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N;

    private boolean preventParentScrolling = true;
    private int detectedBestPixelFormat = -1;

    private View searchAndFindDrawingView(ViewGroup group) {
        int childCount = group.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = group.getChildAt(i);

            if (child instanceof ViewGroup) {
                View view = searchAndFindDrawingView((ViewGroup) child);

                if (view != null) {
                    return view;
                }
            }

            if (child instanceof SurfaceView) {
                return child;
            }

            if (HAS_TEXTURE_VIEW_SUPPORT) { // if we have support for texture view
                if (child instanceof TextureView) {
                    return child;
                }
            }
        }
        return null;
    }

    private int detectBestPixelFormat() {

        //Skip check if this is a new device as it will be RGBA_8888 by default.
        if (IS_RGBA_8888_BY_DEFAULT) {
            return PixelFormat.RGBA_8888;
        }

        Context context = this.getActivity();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        //Get display pixel format
        @SuppressWarnings("deprecation")
        int displayFormat = display.getPixelFormat();

        if (PixelFormat.formatHasAlpha(displayFormat)) {
            return displayFormat;
        } else {
            return PixelFormat.RGB_565;//Fallback for those who don't support Alpha
        }
    }

    @SuppressLint("NewApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);

        // Find the view the map is using for Open GL
        View drawingView = searchAndFindDrawingView(view);

        if (SUPPORTS_TEXTURE_VIEW_BACKGROUND) {
            //Transparent Color For Views, android.R.color.transparent doesn't work on all devices
            int transparent = 0x00000000;

            // Set Root View to be transparent to prevent black screen on load
            view.setBackgroundColor(transparent);

            if (drawingView == null) { // If we didn't get anything then abort
                return view;
            }

            // Stop black artifact from being left behind on scroll
            drawingView.setBackgroundColor(transparent);
        }

        // Create On Touch Listener for MapView Parent Scrolling Fix - Many
        // thanks to Gemerson Ribas (gmribas) for help with this fix.
        OnTouchListener touchListener = new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {

                int action = event.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow Parent to intercept touch events.
                        view.getParent().requestDisallowInterceptTouchEvent(preventParentScrolling);
                        break;
                    case MotionEvent.ACTION_UP:
                        // Allow Parent to intercept touch events.
                        view.getParent().requestDisallowInterceptTouchEvent(!preventParentScrolling);
                        break;
                }

                // Handle View touch events.
                view.onTouchEvent(event);
                return false;
            }
        };

        // texture view
        if (HAS_TEXTURE_VIEW_SUPPORT) { // If we support texture view and the
            // drawing view is a TextureView then
            // tweak it and return the fragment view

            if (drawingView instanceof TextureView) {

                TextureView textureView = (TextureView) drawingView;

                // Stop Containing Views from moving when a user is interacting
                // with Map View Directly
                textureView.setOnTouchListener(touchListener);

                return view;
            }
        }

        // Otherwise continue onto legacy surface view hack
        final SurfaceView surfaceView = (SurfaceView) drawingView;

        // Fix for reducing black view flash issues
        SurfaceHolder holder = surfaceView.getHolder();

        //Detect Display Format if we haven't already
        if (detectedBestPixelFormat == -1) {
            detectedBestPixelFormat = detectBestPixelFormat();
        }

        //Use detected best pixel format
        holder.setFormat(detectedBestPixelFormat);

        // Stop Containing Views from moving when a user is interacting with
        // Map View Directly
        surfaceView.setOnTouchListener(touchListener);

        return view;
    }

    public boolean getPreventParentScrolling() {
        return preventParentScrolling;
    }

    public void setPreventParentScrolling(boolean value) {
        preventParentScrolling = value;
    }
}