package com.roodapps.ispy.gui;

import android.widget.RelativeLayout;

import java.util.Random;

import static com.roodapps.ispy.activities.MainActivity.screenDensity;
import static com.roodapps.ispy.activities.MainActivity.screenWidth;

public class DrawCircles
{
    // Draw empty slots centred on screen
    public RelativeLayout.LayoutParams drawSlots(RelativeLayout.LayoutParams layoutParams, int pos,
                                                       int size, int spacing, int marginTop, float combinedLength)
    {
        if (pos<7)
        {
            // Set left margin where the start position is the place where once all slots placed, they will be centred
            layoutParams.leftMargin = (int)(screenWidth/2 - combinedLength/2 + pos * (size + spacing));
            layoutParams.topMargin = marginTop;
        }
        else
        {
            // Move down 1 line
            layoutParams.leftMargin = (int)(screenWidth/2 - combinedLength/2 + (pos-7) * (size + spacing));
            layoutParams.topMargin = marginTop + size + spacing;
        }

        return layoutParams;
    }

    // Draw letters centred onto screen
    public RelativeLayout.LayoutParams drawLetters(RelativeLayout.LayoutParams layoutParams, int answerLength, int pos,
                                                   int size, int spacing, int marginTop, float combinedLength)
    {
        if (pos<7)
        {
            // Set left margin where the start position is the place where once all letters placed, they will be centred
            // Random factor to add position diversity
            layoutParams.leftMargin = (int)(screenWidth/2 - combinedLength/2 + size/2 + (pos+3)
                    * (size + spacing) + (int) Math.ceil(randomInt(-3, 3) * screenDensity));
            layoutParams.topMargin = marginTop;
        }
        else
        {
            // Move down 1 line
            layoutParams.leftMargin = (int)(screenWidth/2 - combinedLength/2 + size/2 + (pos-4)
                    * (size + spacing) + (int) Math.ceil(randomInt(-3, 3) * screenDensity));
            if (answerLength >7)
                layoutParams.topMargin = marginTop + size + spacing;
            else
                layoutParams.topMargin = marginTop + (int)(size*1.2) + spacing;
        }

        return layoutParams;
    }

    // Generate random integer
    private int randomInt(int min, int max)
    {
        Random rand = new Random();

        return rand.nextInt(max - min + 1) + min;
    }
}
