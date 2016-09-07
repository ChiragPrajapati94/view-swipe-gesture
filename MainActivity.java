package com.aiminfotech.sipwgesturedemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    GestureDetectorCompat gestureDetectorCompat;
    RelativeLayout slider;
    private float xAdditive;
    private float mDownX;
    private float dX;
    private float frontPanelXSwipeThreshold = 100;
    private boolean isOpen;
    DropDownWarning dropDownWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slider = (RelativeLayout) findViewById(R.id.slider);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnTouchListener(this);
        slider.setOnTouchListener(this);
        gestureDetectorCompat = new GestureDetectorCompat(this, this);
        Animation animAccelerateDecelerate = AnimationUtils.loadAnimation(this, R.anim.bounce);

        dropDownWarning = new DropDownWarning.Builder(this, (ViewGroup) findViewById(R.id.relative))

                .interpolatorIn(new BounceInterpolator()) //Intepolator used for the "show" animation

                .interpolatorOut(new AnticipateOvershootInterpolator()) //Interpolator used for the "hide" animation

                .animationLength(1000) //Lenght of the animation in ms

                .textHeight(60) //Height of the text view

                .message("Message") //Message to display

                .foregroundColor(Color.WHITE) //Color of the text in argb

                .backgroundColor(Color.RED) //Color of the background in argb

                .autoHideMessage(true)

                .autoHideMS(2500)

                .build();
        dropDownWarning.show();


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            onSlide();
        }

        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        xAdditive = slider.getY() - e.getRawY();
        mDownX = slider.getTop();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        dX = xAdditive + e2.getRawY();
        if ((slider.getY() + dX) < frontPanelXSwipeThreshold) {
            // go ahead and animate the panel away
            dX = -slider.getHeight();
        }
        slider.animate().y(dX).setDuration(0).start();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getRawY() > e2.getRawY())
            isOpen = true;
        return true;
    }

    void onSlide() {
        if (isOpen) {
            slider.animate().y(slider.getHeight()).setDuration(200).start();
            isOpen = false;
        } else {
            slider.animate().y(mDownX).setDuration(200).start();
            isOpen = true;
        }

    }
}
