package com.roodapps.ispy.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.roodapps.ispy.MainApplication;
import com.roodapps.ispy.R;
import com.roodapps.ispy.amazonaws.ManagerClass;
import com.roodapps.ispy.litemodels.Photo;
import com.roodapps.ispy.utility.DatabaseHelper;
import com.roodapps.ispy.utility.MyHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SplashActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MainApplication.getHandler().setActivityState(MyHandler.ActivityState.SPLASH);

        // Set Activity to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Load Splash image with Glide
        ImageView splashView = (ImageView) findViewById(R.id.splash);
        Glide
                .with(this)
                .load(R.drawable.splash)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(splashView);

        initializeApp(this);

        // Run Activity for 4 seconds then open Menu Activity
        Thread splashTimer = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(4000);
                    Intent mainIntent = new Intent("android.intent.action.MENU");
                    startActivity(mainIntent);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    finish();
                }
            }
        };
        splashTimer.start();
    }

    private void initializeApp(final Context context)
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                MainApplication.getHandler().setDbHelper(dbHelper);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();

        ManagerClass managerClass = new ManagerClass();
        CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(context);
        managerClass.setDdbClient(credentialsProvider);
        managerClass.setMapper(managerClass.getDdbClient());

        managerClass.setS3Bucket(credentialsProvider);
        managerClass.setTransferUtility(this, managerClass.getS3());

        MainApplication.getHandler().setManagerClass(managerClass);

        List<Photo> photos = null;
        try {
            InputStream is = this.getAssets().open("files/add.json");
            photos = MainApplication.getHandler().getDbHelper().readJsonStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i<photos.size(); i++)
        {
            MainApplication.getHandler().getDbHelper().insertPhoto(photos.get(i).name, photos.get(i).clue, photos.get(i).answer);
            Log.i("PlayActivity", "testy");
        }
    }
}
