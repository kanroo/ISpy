package com.roodapps.ispy.utility;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.roodapps.ispy.activities.MainActivity;
import com.roodapps.ispy.gui.DrawCircles;

public class LetterManager implements View.OnTouchListener {
    private DrawCircles mDrawCircles;
    private MyHandler mHandler;
    private RelativeLayout mRelativeLayout;

    private Context mContext;
    private EmptySlotBuilder mEmptySlotBuilder;
    private TextView mClueText;
    private String mAnswer;

    private Map<ImageView, Letter> letters;
    private char[] guess;
    private boolean[] isOccupied;

    private float density;
    private int numOfCircles;
    private int topMargin;
    private int diameter;
    private int spacing;
    private float combinedLength;

    // Draws letters onto screen and manages their position and state etc.
    public LetterManager(Context context, MyHandler handler)
    {
        // Initialize all variables
        this.mContext = context;
        this.mHandler = handler;

        switch (mHandler.getActivityState())
        {
            case PLAY:
                mEmptySlotBuilder = mHandler.getPlayActivity().mEmptySlotBuilder;
                mClueText = mHandler.getPlayActivity().clueText;
                mRelativeLayout = mHandler.getPlayActivity().mRelativeLayout;
                mAnswer = mHandler.getPlayActivity().mAnswer;
                break;
            case ROOM:
                mEmptySlotBuilder = mHandler.getRoomActivity().mEmptySlotBuilder;
                mClueText = mHandler.getRoomActivity().clueText;
                mRelativeLayout = mHandler.getRoomActivity().mRelativeLayout;
                mAnswer = mHandler.getRoomActivity().mAnswer;
                break;
        }

        density = MainActivity.screenDensity;
        numOfCircles = 14;

        if (mAnswer.length() > 7)
        {
            diameter = (int) Math.ceil(40 * density);
            topMargin = (int) Math.ceil(480 * density);
            spacing = (int) Math.ceil(7 * density);
        }
        else
        {
            diameter = (int) Math.ceil(45 * density);
            topMargin = (int) Math.ceil(440 * density);
            spacing = (int) Math.ceil(5 * density);
        }


        combinedLength = ((diameter + spacing) * numOfCircles) - spacing;

        isOccupied = new boolean[mAnswer.length()];
        guess = new char[mAnswer.length()];

        mDrawCircles = new DrawCircles();
        letters = createImageViews();
    }

    // Creates Hashmap of ImageViews (key) and Letter.class (value)
    private Map<ImageView, Letter> createImageViews()
    {
        // Initialize Hashmap for ImageViews and Letter.class
        // Initialize ArrayList of positions where letters will be placed
        Map<ImageView, Letter> letters = new HashMap<>(14);
        List<Integer> positions = setPositions(14);

        // Pick a random place in position array and create either character of mAnswer at that position
        // or random character at the position (if all mAnswer characters created)
        for (int i = 0; i < numOfCircles; i++)
        {
            // Pick random position in
            int index = randomInt(0, positions.size()-1);
            int pos = positions.get(index);
            positions.remove(index);

            if (i < mAnswer.length())
            {
                createImage(pos, letters, mRelativeLayout, mAnswer.charAt(i));
            }
            else
            {
                createImage(pos, letters, mRelativeLayout, generateChar());
            }
        }

        return letters;
    }

    private void createImage(int pos, Map<ImageView, Letter> letters, RelativeLayout relativeLayout, char character)
    {
        // Create View and generate ID
        ImageView tempView = new ImageView(mContext);
        tempView.generateViewId();

        // Set View parameters, image resource, rotation, size
        int imageId = mContext.getResources().getIdentifier(character + "_btn", "drawable", mContext.getPackageName());
        Glide.with(mContext).load(imageId).into(tempView);

        tempView.setRotation(randomInt(-15, 15));

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.
                LayoutParams(diameter, diameter);

        // Draw letters with DrawCircles.class and setting layout params to view
        mDrawCircles.drawLetters(layoutParams, mAnswer.length(), pos, diameter, spacing, topMargin, combinedLength);

        tempView.setLayoutParams(layoutParams);

        // Add view to layout and listener
        relativeLayout.addView(tempView);
        tempView.setOnTouchListener(this);

        // Add Letter.class storing all required variables at the key of the View
        letters.put(tempView, new Letter(character, layoutParams.leftMargin, layoutParams.topMargin, diameter, -1, false));
    }

    // Add positions to array manually so they are not finalised and can be removed or added
    private List<Integer> setPositions(int size)
    {
        List<Integer> list = new ArrayList<>(size);
        for (int i=0; i<size; i++)
        {
            list.add(i);
        }
        return list;
    }

    // Find empty slot
    private int getEmptySlot()
    {
        for (int i = 0; i < isOccupied.length; i++)
        {
            if (!isOccupied[i])
                return i;
        }
        return -1;
    }

    // Place Letter in slot
    private void mountLetter(View v, int position)
    {
        if (mAnswer.length() > 7)
        {
            v.getLayoutParams().width = (int) Math.ceil(36 * density);
            v.getLayoutParams().height = (int) Math.ceil(36 * density);
        }
        else if (mAnswer.length() == 7)
        {
            v.getLayoutParams().width = (int) Math.ceil(40 * density);
            v.getLayoutParams().height = (int) Math.ceil(40 * density);
        }

        View slotView = mEmptySlotBuilder.slots[position];
        int slotX = (int) slotView.getX();
        int slotY = (int) slotView.getY();
        int slotSize = slotView.getWidth();
        int viewSize = v.getLayoutParams().width;

        char vChar = letters.get(v).character;

        int imageId = mContext.getResources().getIdentifier(vChar + "_btn_placed", "drawable", mContext.getPackageName());
        ((ImageView) v).setImageResource(imageId);
        v.setX(slotX + (slotSize - viewSize)/2);
        v.setY(slotY + (slotSize - viewSize)/2);

        letters.get(v).position = position;
        letters.get(v).isMounted = true;
        guess[position] = vChar;
        isOccupied[position] = true;
    }

    // Replace letter back to original position from slot
    private void unmountLetter(View v)
    {
        v.getLayoutParams().width = letters.get(v).size;
        v.getLayoutParams().height = letters.get(v).size;

        char vChar = letters.get(v).character;
        int imageId = mContext.getResources().getIdentifier(vChar + "_btn", "drawable", mContext.getPackageName());

        ((ImageView) v).setImageResource(imageId);
        v.setX(letters.get(v).origX);
        v.setY(letters.get(v).origY);

        guess[letters.get(v).position] = '\u0000';
        isOccupied[letters.get(v).position] = false;
        letters.get(v).isMounted = false;
        letters.get(v).position = -1;
    }

    // Check guess against mAnswer when all slots are filled
    private void checkAnswer()
    {
        String guessString = "";

        for (int i = 0; i < guess.length; i++)
        {
            guessString += guess[i];
        }
        Log.i("LM", guessString + "-" + mAnswer);
        if (guessString.equals(mAnswer))
        {
            mClueText.setTextColor(Color.argb(255, 0, 255, 0));
            mClueText.setScaleX(1.05f);
            mClueText.setScaleY(1.05f);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    mClueText.setTextColor(Color.argb(195, 255, 255, 255));
                    mClueText.setScaleX(1.0f);
                    mClueText.setScaleY(1.0f);
                }
            }, 2000);

            switch (mHandler.getActivityState())
            {
                case PLAY:
                    mHandler.getPlayActivity().saveGame();
                    mHandler.getPlayActivity().correctScreen();
                    break;
                case ROOM:
                    mHandler.getRoomActivity().saveGame();
                    mHandler.getRoomActivity().correctScreen();
                    break;
            }
        }
        else
        {
            mClueText.setTextColor(Color.argb(255, 255, 0, 0));
            mClueText.setScaleX(1.05f);
            mClueText.setScaleY(1.05f);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    mClueText.setTextColor(Color.argb(195, 255, 255, 255));
                    mClueText.setScaleX(1.0f);
                    mClueText.setScaleY(1.0f);
                }
            }, 2000);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        Rect letterBounds = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // On touch down
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            v.bringToFront();
            if (!letters.get(v).isMounted)
            {
                v.setScaleX(1.25f);
                v.setScaleY(1.25f);
            }
            else
            {
                v.setScaleX(0.9f);
                v.setScaleY(0.9f);
            }
        }
        // On touch up
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            v.setScaleX(1.0f);
            v.setScaleY(1.0f);
            if (letterBounds.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
            {
                // If view is not mounted
                if (!letters.get(v).isMounted)
                {
                    int position = getEmptySlot();

                    if (position != -1)
                    {
                        mountLetter(v, position);

                        // Check if all slots are filled
                        int count = 0;
                        for (int i = 0; i < isOccupied.length; i++)
                            if (isOccupied[i]) count++;
                        if (count >= isOccupied.length) checkAnswer();
                    }
                }
                // If view is mounted
                else
                    unmountLetter(v);
            }
        }
        return true;
    }

    // Generate random lower case alphabetical character
    private char generateChar()
    {
        Random r = new Random();
        return (char)(r.nextInt(26) + 'a');
    }

    // Generate random integer
    private int randomInt(int min, int max)
    {
        Random rand = new Random();

        return rand.nextInt(max - min + 1) + min;
    }
}
