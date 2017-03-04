package com.roodapps.ispy.activities;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.roodapps.ispy.MainApplication;
import com.roodapps.ispy.R;
import com.roodapps.ispy.utility.MyHandler;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener
{
    public static int softButtonsBarHeight, screenWidth, screenHeight;
    public static float screenDensity;

    private MyHandler handler;
    private ImageView playButton, multiplayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = MainApplication.getHandler();
        handler.setMainActivity(this);

        handler.setActivityState(MyHandler.ActivityState.MAIN);

        // Set Activity to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set static variables
        getScreenDimensions();
        softButtonsBarHeight = getSoftButtonsBarHeight();
        screenDensity = getDensity();

        // Load images with Glide
        ImageView menuBG = (ImageView) findViewById(R.id.menu_bg);
        Glide.with(this).load(R.drawable.menu_bg).into(menuBG);

        ImageView logo = (ImageView) findViewById(R.id.logo);
        Glide.with(this).load(R.drawable.logo).into(logo);

        playButton = (ImageView) findViewById(R.id.play_button);
        Glide.with(this).load(R.drawable.play_button).into(playButton);

        multiplayerButton = (ImageView) findViewById(R.id.multiplayer_button);
        Glide.with(this).load(R.drawable.multiplayer_button).into(multiplayerButton);

        if (handler.getLastPlayActivity() != null)
            handler.getLastPlayActivity().destroyActivity();

        // Set onTouchListeners for buttons
        playButton.setOnTouchListener(this);
        multiplayerButton.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId())
        {
            // on Play button touch
            case R.id.play_button:
                // Bounds of Play button
                Rect playRect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                // On touch down
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    playButton.setImageResource(R.drawable.play_pressed);
                // On touch up
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    playButton.setImageResource(R.drawable.play_button);
                    if (playRect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
                    {
                        Intent playIntent = new Intent(this, PlayActivity.class);
                        startActivity(playIntent);
                    }
                }
                break;

            // on Multiplayer button touch
            case R.id.multiplayer_button:
                Rect multiRect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    multiplayerButton.setImageResource(R.drawable.multiplayer_pressed);
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    multiplayerButton.setImageResource(R.drawable.multiplayer_button);
                    if (multiRect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
                    {
                        Intent roomIntent = new Intent(this, RoomActivity.class);
                        roomIntent.putExtra("roomId", "12345");
                        startActivity(roomIntent);
                    }
                }
                break;
        }
        return true;
    }

    // Get dimensions for real screen size
    private void getScreenDimensions()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        screenWidth = size.x;
        screenHeight = size.y;
    }

    // Get dimensions of soft buttons
    private int getSoftButtonsBarHeight()
    {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    // Get screen density
    private float getDensity()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.density;
    }
}
