package com.roodapps.ispy.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.Random;

import com.roodapps.ispy.activities.MainActivity;
import com.roodapps.ispy.R;
import com.roodapps.ispy.gui.DrawCircles;

public class EmptySlotBuilder
{
    private DrawCircles mDrawCircles;
    private SetSlotVariables mSetSlotVariables;

    private Paint paint;
    private Context context;
    private int layout;

    public ImageView[] slots;

    private float density;

    private int marginTop;

    private int answerLength;
    private int size;
    private int spacing;
    private float combinedLength;

    public EmptySlotBuilder(Context context, SetSlotVariables mSetSlotVariables, int layout)
    {
        // Initialize classes and variables
        this.context = context;
        this.layout = layout;

        mDrawCircles = new DrawCircles();
        this.mSetSlotVariables = mSetSlotVariables;

        density = MainActivity.screenDensity;
        answerLength = mSetSlotVariables.answerLength;
        marginTop = mSetSlotVariables.marginTop;
        size = mSetSlotVariables.size;
        spacing = mSetSlotVariables.spacing;
        combinedLength = mSetSlotVariables.combinedLength;

        slots = new ImageView[answerLength];

        createSlotViews();
    }

    // Initialize and set properties for ImageViews for each slot
    private void createSlotViews()
    {
        RelativeLayout relativeLayout = (RelativeLayout)((Activity)context).getWindow().getDecorView().findViewById(layout);
        // Loop through length of slots array and initialize ImageViews and draw slots
        for(int i = 0; i < slots.length; i++)
        {
            slots[i] = new ImageView(context);
            Glide.with(context).load(R.drawable.slot).into(slots[i]);
            slots[i].setRotation(randomInt(0, 359));

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(size, size);

            mDrawCircles.drawSlots(layoutParams, i, size, spacing, marginTop, combinedLength);

            slots[i].setLayoutParams(layoutParams);
            relativeLayout.addView(slots[i]);
        }
    }

    // Generate random integer
    private int randomInt(int min, int max)
    {
        Random rand = new Random();

        return rand.nextInt(max - min + 1) + min;
    }
}
