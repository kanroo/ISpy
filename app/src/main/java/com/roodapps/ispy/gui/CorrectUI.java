package com.roodapps.ispy.gui;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

import com.roodapps.ispy.R;

public class CorrectUI
{
    RelativeLayout mRelativeLayout;

    public CorrectUI(Context context)
    {
        mRelativeLayout = (RelativeLayout)((Activity)context).getWindow().getDecorView().findViewById(R.id.activity_play);
    }




}
