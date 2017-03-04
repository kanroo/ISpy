package com.roodapps.ispy.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.roodapps.ispy.R;
import com.roodapps.ispy.activities.MainActivity;
import com.roodapps.ispy.utility.MyHandler;

import static com.roodapps.ispy.activities.MainActivity.screenDensity;

/**
 * Created by Ethan on 19/02/2017.
 */

public class BottomUI
{
    private Context mContext;
    private MyHandler handler;
    private RelativeLayout mRelativeLayout;

    public BottomUI(Context context, MyHandler handler, int layout)
    {
        this.handler = handler;
        mRelativeLayout = (RelativeLayout)((Activity)context).getWindow().getDecorView().findViewById(layout);

        mRelativeLayout.addView(new BackButton(context));
        mRelativeLayout.addView(new Background(context));
        mRelativeLayout.addView(new ActiveAbility(context));
        mRelativeLayout.addView(new PassiveAbility(context));
    }

    private class Background extends ImageView
    {
        public Background(Context context)
        {
            super(context);
            mContext = context;

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    (int) Math.ceil(110 * screenDensity)
            );
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            this.setLayoutParams(lp);

            this.setScaleType(ScaleType.FIT_END);
            Glide
                    .with(context)
                    .load(R.drawable.bottom)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(this);

            this.setClickable(false);
            this.setFocusableInTouchMode(false);
            this.setFocusable(false);
        }
    }

    private class BackButton extends ImageView
    {
        public BackButton(Context context)
        {
            super(context);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    (int) Math.ceil(55 * screenDensity)
            );
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            this.setLayoutParams(lp);

            this.setScaleType(ScaleType.FIT_END);
            Glide
                    .with(context)
                    .load(R.drawable.back)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(this);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Rect viewBounds = new Rect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                int color = ResourcesCompat.getColor(getResources(), R.color.dark_red, null);
                ColorFilter filter = new LightingColorFilter(color, color);
                this.setColorFilter(filter);
            }
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                int color = ResourcesCompat.getColor(getResources(), R.color.dark_brown, null);

                ColorFilter filter = new LightingColorFilter(color, color);
                this.setColorFilter(filter);
                if (viewBounds.contains(this.getLeft() + (int) event.getX(), this.getTop() + (int) event.getY()))
                {
                    Intent menuIntent = new Intent(mContext, MainActivity.class);
                    handler.getPlayActivity().changeScreen(menuIntent);
                }
            }
            return true;
        }
    }

    private class ActiveAbility extends ImageView
    {

        public ActiveAbility(Context context)
        {
            super(context);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    (int) Math.ceil(85 * screenDensity),
                    (int) Math.ceil(85 * screenDensity)
            );
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.leftMargin = (int) Math.ceil(140 * screenDensity);

            this.setLayoutParams(lp);

            this.setScaleType(ScaleType.FIT_END);
            Glide.with(context).load(R.drawable.active).into(this);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            Rect viewBounds = new Rect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                this.setScaleX(1.12f);
                this.setScaleY(1.12f);
            }
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                this.setScaleX(1.0f);
                this.setScaleY(1.0f);
                if (viewBounds.contains(this.getLeft() + (int) event.getX(), this.getTop() + (int) event.getY()))
                {

                }
            }

            return true;
        }
    }

    private class PassiveAbility extends ImageView
    {

        public PassiveAbility(Context context)
        {
            super(context);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    (int) Math.ceil(50 * screenDensity),
                    (int) Math.ceil(50 * screenDensity)
            );
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.leftMargin = (int) Math.ceil(230 * screenDensity);
            lp.bottomMargin = (int) Math.ceil(5 * screenDensity);

            this.setLayoutParams(lp);

            this.setScaleType(ScaleType.FIT_END);
            Glide.with(context).load(R.drawable.passive).into(this);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            Rect viewBounds = new Rect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                this.setScaleX(1.1f);
                this.setScaleY(1.1f);
            }
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                this.setScaleX(1.0f);
                this.setScaleY(1.0f);
                if (viewBounds.contains(this.getLeft() + (int) event.getX(), this.getTop() + (int) event.getY()))
                {

                }
            }

            return true;
        }
    }
}
