package com.roodapps.ispy.gui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import static com.roodapps.ispy.activities.MainActivity.screenDensity;
import static com.roodapps.ispy.activities.MainActivity.screenWidth;

public class PinchZoomImageView extends android.support.v7.widget.AppCompatImageView
{
    public Bitmap mBitmap;
    private Context mContext;
    private ScaleGestureDetector mScaleGestureDetector;
    private State mState;

    private int mImageWidth;
    private int mImageHeight;
    private float mScaleFactor = 1.0f;
    private float mStartX = 0;
    private float mStartY = 0;
    private float mTranslateX = 0;
    private float mTranslateY = 0;
    private float mPrevTranslateX = 0;
    private float mPrevTranslateY = 0;
    private float mMinZoom = 1.0f;
    private float mMaxZoom = 2.5f;
    private float mEmptyY;
    private boolean mDragged = false;
    private boolean isFullScreen = false;

    public enum State { NONE, PAN, ZOOM }

    // Detects touch and scale gesture activity on view
    private class EventListener
    implements View.OnTouchListener,
            ScaleGestureDetector.OnScaleGestureListener
    {
        public EventListener(Context c)
        {
            mScaleGestureDetector = new ScaleGestureDetector(c, this);
            mContext = c;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            // Set translation variables when panning image
            // Detect if second pointer down on screen to initiate zoom
            switch (event.getAction() & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:
                    mState = State.PAN;
                    mStartX = event.getX() - mPrevTranslateX;
                    mStartY = event.getY() - mPrevTranslateY;
                    break;
                case MotionEvent.ACTION_MOVE:
                    mTranslateX = (event.getX() - mStartX);
                    mTranslateY = (event.getY() - mStartY);
                    double distance = Math.sqrt(Math.pow(
                            event.getX() - (mStartX - mPrevTranslateX), 2) +
                            Math.pow(event.getY() - (mStartY - mPrevTranslateY), 2)
                    );

                    if (distance > 0)
                        mDragged = true;

                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mState = State.ZOOM;
                    break;
                case MotionEvent.ACTION_UP:
                    mState = State.NONE;
                    mDragged = false;
                    mPrevTranslateX = mTranslateX;
                    mPrevTranslateY = mTranslateY;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mState = State.PAN;
                    mPrevTranslateX = mTranslateX;
                    mPrevTranslateY = mTranslateY;
                    break;
            }

            // call scale detector
            mScaleGestureDetector.onTouchEvent(event);


            // Refresh activity
            if ((mState == State.PAN && mScaleFactor != mMinZoom && mDragged) || mState == State.ZOOM)
            {
                invalidate();
            }

            return true;
        }

        // Update scale factor with scale factor of finger gestures
        @Override
        public boolean onScale(ScaleGestureDetector detector)
        {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(mMinZoom + 0.1f, Math.min(mMaxZoom, mScaleFactor));
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) { return true; }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {}

    }

    public PinchZoomImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOnTouchListener(new EventListener(getContext()));
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.save();

        // Detect if translation is outside of canvas range
        if(((mTranslateX-1) * -1) < 0)
        {
            mTranslateX = 0;
        }
        else if (((mTranslateX-1) * -1) > (mScaleFactor - 1) * mImageWidth)
        {
            mTranslateX = (1 - mScaleFactor) * mImageWidth;
        }
        if(((mTranslateY-1) * -1) < 0)
        {
            mTranslateY = 0;
        }
        else if (((mTranslateY-1) * -1) > (mScaleFactor -1) * mImageHeight - mEmptyY)
        {
            mTranslateY = (1 - mScaleFactor) * mImageHeight + mEmptyY;
        }

        // Translate and scale canvas, draw bitmap on canvas
        canvas.drawRGB(0, 0, 0);
        canvas.translate((mTranslateX), (mTranslateY));
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        invalidate();

        canvas.restore();
    }

    // Canvas drawing restrictions
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int imageWidth = MeasureSpec.getSize(widthMeasureSpec);
        int imageHeight = MeasureSpec.getSize(heightMeasureSpec);
        int scaledWidth = Math.round(mImageWidth * mScaleFactor);
        int scaledHeight = Math.round(mImageHeight * mScaleFactor);

        setMeasuredDimension(
                Math.min(imageWidth, scaledWidth),
                Math.max(imageHeight, scaledHeight)
        );

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    // Set parameters of bitmap
    public void setBitmap(String photoName)
    {
        // Decode with inJustDecodeBounds = true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        InputStream istr = getAssetInputStream(mContext, "photos/" + photoName + ".jpg");
        Bitmap bitmap = getAssetInputStream(mContext, "photos/" + photoName + ".jpg");
//        BitmapFactory.decodeStream(istr, null, options);

//        float height = options.outHeight;
//        float width = options.outWidth;
        float height = bitmap.getHeight();
        float width = bitmap.getWidth();
        int reqHeight = (int) Math.ceil(310 * screenDensity);
        float aspectRatio = height / width;
        int reqWidth = Math.round(reqHeight * aspectRatio);

        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        Bitmap tempBitmap = BitmapFactory.decodeStream(istr, null, options);

        mImageWidth = screenWidth;
        mImageHeight = Math.round(mImageWidth * aspectRatio);
        mBitmap = Bitmap.createScaledBitmap(bitmap, mImageWidth, mImageHeight, false);

        // Scale image up to proper size, get empty area of image so it can be restricted and cannot be panned to
        mEmptyY = (float) Math.ceil(365 * screenDensity) - mImageHeight;
        mMinZoom = (float) Math.ceil(365 * screenDensity) / mImageHeight;
        mMaxZoom = mMinZoom + 1.5f;
        mScaleFactor = mMinZoom;

        invalidate();
        requestLayout();
    }

    public static Bitmap getAssetInputStream(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch  (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}

