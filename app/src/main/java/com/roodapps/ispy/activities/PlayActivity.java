package com.roodapps.ispy.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.bumptech.glide.Glide;
import com.roodapps.ispy.amazonaws.models.nosql.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.roodapps.ispy.MainApplication;
import com.roodapps.ispy.R;
import com.roodapps.ispy.gui.BottomUI;
import com.roodapps.ispy.utility.EmptySlotBuilder;
import com.roodapps.ispy.utility.LetterManager;
import com.roodapps.ispy.utility.MyHandler;
import com.roodapps.ispy.utility.RoomManager;
import com.roodapps.ispy.utility.SetSlotVariables;
import com.roodapps.ispy.gui.PinchZoomImageView;
import com.roodapps.ispy.gui.PlayBackground;

public class PlayActivity extends AppCompatActivity
{
    public EmptySlotBuilder mEmptySlotBuilder;
    public RelativeLayout mRelativeLayout;
    public TextView clueText;
    public Typeface mBebasFont;
    public String mAnswer;

    private MyHandler handler;
    private PinchZoomImageView mPinchZoomImageView;

    private String mPhotoName;
    private String mClue;
    private String mPhotoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        handler = MainApplication.getHandler();
        handler.setPlayActivity(this);

        handler.setActivityState(MyHandler.ActivityState.PLAY);

        List<String> list = new ArrayList<>();
        list.add("G'day");
        list.add("Mate");

//        RoomManager.createRoom(handler, "asde12", "password", list, list);
        RoomManager.getRoom(handler, "123");

        //
        getSave();
        getPhoto();

        // Set Activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set Background Image View
        ImageView playBG = (ImageView) findViewById(R.id.play_bg);
        Glide.with(this).load(R.drawable.play_bg).into(playBG);

        // Add bottom user interface
        BottomUI bottomUI = new BottomUI(this, handler, R.id.activity_play);

        // Set variables for placing slots
        SetSlotVariables setSlotVariables = new SetSlotVariables(mAnswer.length());

        // Add slot background to relative layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_play);
        mRelativeLayout.addView(new PlayBackground(this, setSlotVariables));

        // Build empty slots
        mEmptySlotBuilder = new EmptySlotBuilder(this, setSlotVariables, R.id.activity_play);
        // Draw, place, manage letters
        new LetterManager(this, handler);


        // Add image with zoom and pan abilities
        mPinchZoomImageView = (PinchZoomImageView) findViewById(R.id.photoImage);
        mPinchZoomImageView.setBitmap(mPhotoName);

        // Destroy last activity after views loaded
        if (handler.getLastPlayActivity() != null)
            handler.getLastPlayActivity().destroyActivity();

        // Add clue text
        mBebasFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Regular.ttf");

        clueText = (TextView) findViewById(R.id.clueText);
        clueText.setText(mClue);
        clueText.setTypeface(mBebasFont);
        clueText.setTextColor(Color.argb(195, 255, 255, 255));
        clueText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    clueText.setTextColor(Color.argb(255, 255, 255, 255));
                    v.setScaleX(1.05f);
                    v.setScaleY(1.05f);
                }
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    clueText.setTextColor(Color.argb(195, 255, 255, 255));
                    v.setScaleX(1f);
                    v.setScaleY(1f);
                }
                return true;
            }
        });

        introSequence();
    }

    private void getPhoto()
    {
        Cursor res = handler.getDbHelper().findPhoto(mPhotoSave);
        res.moveToNext();
        mPhotoName = res.getString(1);
        mClue = res.getString(2);
        mAnswer = res.getString(3);
    }

    private void getSave()
    {
        Cursor save = handler.getDbHelper().getSave();
        if (save.getCount() == 0)
        {
            handler.getDbHelper().newSave();
            save = handler.getDbHelper().getSave();
        }
        save.moveToNext();
        mPhotoSave = save.getString(1);
    }

    private void introSequence()
    {
        final ImageView introBack = (ImageView) findViewById(R.id.introBack);
        introBack.bringToFront();

        final TextView introISpyText = (TextView) findViewById(R.id.introISpyText);
        introISpyText.setText("I spy with my little eye...");
        introISpyText.setTextColor(Color.WHITE);
        introISpyText.setTypeface(mBebasFont);
        introISpyText.bringToFront();

        final TextView introClueText = (TextView) findViewById(R.id.introClueText);
        introClueText.setText("Something beginning with P");
        introClueText.setTextColor(Color.argb(0, 255, 255, 255));
        introClueText.setTypeface(mBebasFont);
        introClueText.bringToFront();

        final AlphaAnimation animFadeout = new AlphaAnimation(1.0f, 0.0f);
        animFadeout.setDuration(500);
        animFadeout.setStartOffset(1500);

        introISpyText.startAnimation(animFadeout);

        animFadeout.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                introISpyText.setVisibility(View.GONE);
                introBack.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    public void saveGame()
    {
        handler.getDbHelper().updateSave("1", Integer.toString(randomInt(1, handler.getDbHelper().getTableLength())));
        handler.setLastPlayActivity(this);
    }

    public void correctScreen()
    {
        final Intent mainIntent = new Intent(this, PlayActivity.class);

        final TextView correctView = new TextView(this);
        correctView.setText("correct");
        correctView.setTextColor(Color.GREEN);
        correctView.setAlpha(0.0f);
        correctView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70f);
        correctView.setTextAppearance(this, R.style.TextShadowTest);
        correctView.setTypeface(mBebasFont);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        correctView.setLayoutParams(lp);

        mRelativeLayout.addView(correctView);

        correctView.setAlpha(0f);
        correctView.setVisibility(View.VISIBLE);

        correctView.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        correctView.animate()
                                .alpha(0f)
                                .setStartDelay(1500)
                                .setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        changeScreen(mainIntent);
                                    }
                                });
                    }
                });
    }

    public void changeScreen(Intent intent)
    {
        this.startActivity(intent);
    }

    public void destroyActivity()
    {
        handler.setLastPlayActivity(null);
        unbindDrawables(findViewById(R.id.activity_play));
        System.gc();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent menuIntent = new Intent(this, MainActivity.class);
        changeScreen(menuIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }

    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            //SCREEN IS NOT SHOWING
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindDrawables(findViewById(R.id.activity_play));
        System.gc();
    }

    private void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    // Generate random integer
    private int randomInt(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
}
