package com.roodapps.ispy.gui;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Ethan on 23/02/2017.
 */

public class CorrectView extends ViewGroup
{
    Context context;

    public CorrectView(Context context)
    {
        super(context);
        this.context = context;

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.setLayoutParams(lp);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
