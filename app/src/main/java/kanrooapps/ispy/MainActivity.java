package kanrooapps.ispy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener
{
    public static int softButtonsBarHeight, screenWidth, screenHeight;
    private ImageView playButton, multiplayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getScreenDimensions();
        softButtonsBarHeight = getSoftButtonsBarHeight();

        playButton = (ImageView) findViewById(R.id.play_button);
        multiplayerButton = (ImageView) findViewById(R.id.multiplayer_button);

        playButton.setOnTouchListener(this);
        multiplayerButton.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId())
        {
            case R.id.play_button:
                Rect playRect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    playButton.setImageResource(R.drawable.play_pressed);
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

            case R.id.multiplayer_button:
                Rect multiRect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    multiplayerButton.setImageResource(R.drawable.multiplayer_pressed);
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    multiplayerButton.setImageResource(R.drawable.multiplayer_button);
                    if (multiRect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
                    {
//                        Intent multiIntent = new Intent(this, MultiplayerActivity.class);
//                        startActivity(multiIntent);
                    }
                }
                break;
        }
        return true;
    }

    private void getScreenDimensions()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        screenWidth = size.x;
        screenHeight = size.y;
    }

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
}
