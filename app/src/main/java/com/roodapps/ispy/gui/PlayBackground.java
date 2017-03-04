package com.roodapps.ispy.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import com.roodapps.ispy.R;
import com.roodapps.ispy.utility.SetSlotVariables;

import static com.roodapps.ispy.activities.MainActivity.screenWidth;
import static com.roodapps.ispy.activities.MainActivity.screenDensity;

/**
 * Created by Ethan on 5/02/2017.
 */

public class PlayBackground extends View
{
    private Context context;
    private SetSlotVariables mSetSlotVariables;
    private Bitmap wood, scaledWood;

    public int rectMarginTop;
    public int rectMarginBottom;
    private int answerLength;
    private int spacing;
    private int size;

    // Initialize all variables
    public PlayBackground(Context context, SetSlotVariables mSetSlotVariables)
    {
        super(context);
        this.context = context;
        this.mSetSlotVariables = mSetSlotVariables;
        answerLength = mSetSlotVariables.answerLength;

        spacing = mSetSlotVariables.spacing;
        size = mSetSlotVariables.size;
        int woodSize = (int) Math.ceil((70) * screenDensity);
        rectMarginTop = (int) Math.ceil(365 * screenDensity);
        rectMarginBottom = rectMarginTop + (int) Math.ceil((60) * screenDensity);

        // Add size to height of woodSize if slots go onto second lie
        if (answerLength > 7)
            woodSize += size;

        wood = BitmapFactory.decodeResource(context.getResources(), R.drawable.wood);
        scaledWood = Bitmap.createScaledBitmap(wood, screenWidth, woodSize, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(scaledWood, 0, rectMarginTop, null);

    }
}
