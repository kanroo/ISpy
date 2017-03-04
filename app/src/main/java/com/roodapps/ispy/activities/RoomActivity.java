package com.roodapps.ispy.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
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

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.internal.Constants;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.bumptech.glide.Glide;
import com.roodapps.ispy.MainApplication;
import com.roodapps.ispy.R;
import com.roodapps.ispy.amazonaws.mobile.AWSMobileClient;
import com.roodapps.ispy.amazonaws.models.nosql.RoomDO;
import com.roodapps.ispy.gui.BottomUI;
import com.roodapps.ispy.gui.PinchZoomImageView;
import com.roodapps.ispy.gui.PlayBackground;
import com.roodapps.ispy.utility.EmptySlotBuilder;
import com.roodapps.ispy.utility.LetterManager;
import com.roodapps.ispy.utility.MyHandler;
import com.roodapps.ispy.utility.RoomManager;
import com.roodapps.ispy.utility.SetSlotVariables;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class RoomActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = "RoomActivity";

    public EmptySlotBuilder mEmptySlotBuilder;
    public TextView clueText;
    public Typeface mBebasFont;
    public RelativeLayout mRelativeLayout;
    public String mAnswer;

    private MyHandler handler;
    private PinchZoomImageView mPinchZoomImageView;

    private String mRoomId;
    private String mPhotoName;
    private String mClue;
    private String mPhotoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent i = getIntent();
        mRoomId = i.getExtras().getString("roomId");

        handler = MainApplication.getHandler();
        handler.setRoomActivity(this);

        handler.setActivityState(MyHandler.ActivityState.ROOM);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        BottomUI bottomUI = new BottomUI(this, handler, R.id.activity_room);

        // Set variables for placing slots
        SetSlotVariables setSlotVariables = new SetSlotVariables(mAnswer.length());

        // Add slot background to relative layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_room);
        mRelativeLayout.addView(new PlayBackground(this, setSlotVariables));

        // Create clue text
        initClue();

        // Build empty slots
        mEmptySlotBuilder = new EmptySlotBuilder(this, setSlotVariables, R.id.activity_room);
        // Draw, place, manage letters
        new LetterManager(this, handler);


        // Add image with zoom and pan abilities
        mPinchZoomImageView = (PinchZoomImageView) findViewById(R.id.photoImage);
        mPinchZoomImageView.setBitmap(mPhotoName);

        // Destroy last activity after views loaded
        if (handler.getLastPlayActivity() != null)
            handler.getLastPlayActivity().destroyActivity();

        Picasso.with(this).load("http://roodapps.ispy.s3.amazonaws.com/IMG_0933.JPG").into(getTarget("photos"));

        testS3();
        introSequence();

    }

    //target to save
    private static Target getTarget(final String url){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File("c:\\Users\\ethan\\img.jpg");
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    private void testS3()
    {
        ImageView imageView = new ImageView(this);

        Glide.with(this).load("http://roodapps.ispy.s3.amazonaws.com/IMG_0933.JPG").into(imageView);
        mRelativeLayout.addView(imageView);

    }

    private void initClue()
    {
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
//        handler.setLastPlayActivity(this);
    }

    public void correctScreen()
    {
        final Intent mainIntent = new Intent(this, RoomActivity.class);
        mainIntent.putExtra("roomId", mRoomId);

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
        unbindDrawables(findViewById(R.id.activity_room));
        System.gc();
        this.finish();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();

            Intent menuIntent = new Intent(this, MainActivity.class);
            changeScreen(menuIntent);
        }
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

        unbindDrawables(findViewById(R.id.activity_room));
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
