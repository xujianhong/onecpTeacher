package com.jmolsmobile.landscapevideocapture;

import android.content.Context;
import android.view.OrientationEventListener;

/**
 * Created by Administrator on 2017/4/14.
 */

public class MyOrientationDetector extends OrientationEventListener {
    int lastOrientation = 0;
    OnOrientationChangeListener onOrientationChangeListener;
    public MyOrientationDetector(Context context, OnOrientationChangeListener onOrientationChangeListener) {
        super(context);
        this.onOrientationChangeListener = onOrientationChangeListener;
    }

    @Override
    public void onOrientationChanged(int orientation) {
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            return;  //手机平放时，检测不到有效的角度
        }
//只检测是否有四个角度的改变
        if (orientation > 350 || orientation < 10) { //0度
            orientation = 90;
        } else if (orientation > 80 && orientation < 100) { //90度
            orientation = 180;
        } else if (orientation > 170 && orientation < 190) { //180度
            orientation = 270;
        } else if (orientation > 260 && orientation < 280) { //270度
            orientation = 0;
        } else {
            return;
        }
       if (lastOrientation != orientation){
           onOrientationChangeListener.changed(orientation);
           lastOrientation = orientation;
       }
    }
    public interface OnOrientationChangeListener{
        void changed(int degree);
    }
}
