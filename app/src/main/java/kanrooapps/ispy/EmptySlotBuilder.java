package kanrooapps.ispy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;

import static kanrooapps.ispy.MainActivity.screenWidth;

/**
 * Created by Ethan on 1/02/2017.
 */

public class EmptySlotBuilder extends View {

    Paint paint;
    Context context;

    float density;

    int circleMarginTop;
    int rectMarginTop;
    int rectMarginBottom;

    int numOfCircles;
    int circleRadius;
    int spacing;
    float combinedLength;

    public EmptySlotBuilder(Context context)
    {
        super(context);
        this.context = context;
        paint = new Paint();

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        density = metrics.density;
        numOfCircles = 10;
        circleMarginTop = (int) Math.ceil(400 * density);
        rectMarginTop = (int) Math.ceil(370 * density);
        rectMarginBottom = rectMarginTop + (int) Math.ceil((60) * density);
        spacing = (int) Math.ceil(5 * density);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (numOfCircles == 7)
        {
            circleMarginTop = (int) Math.ceil(400 * density);
            circleRadius = (int) Math.ceil(22 * density);
            combinedLength = ((circleRadius*2 + spacing) * 7) - spacing;

            paint.setColor(ContextCompat.getColor(context, R.color.light_orange));
            canvas.drawRect(0, rectMarginTop, screenWidth, rectMarginBottom, paint);

            paint.setColor(ContextCompat.getColor(context, R.color.shadow_red));
            canvas.drawRect(0, rectMarginBottom, screenWidth,
                    rectMarginBottom + (int) Math.ceil((5) * density), paint);
        }
        else if (numOfCircles >= 7)
        {
            circleMarginTop = (int) Math.ceil(400 * density);
            circleRadius = (int) Math.ceil(20 * density);
            combinedLength = ((circleRadius*2 + spacing) * 7) - spacing;

            paint.setColor(ContextCompat.getColor(context, R.color.light_orange));
            canvas.drawRect(0, rectMarginTop, screenWidth,
                    rectMarginBottom + circleRadius*2 + spacing, paint);

            paint.setColor(ContextCompat.getColor(context, R.color.shadow_red));
            canvas.drawRect(0, rectMarginBottom + circleRadius*2 + spacing, screenWidth,
                    rectMarginBottom + circleRadius*2 + spacing + (int) Math.ceil((5) * density), paint);
        }
        else
        {
            circleRadius = (int) Math.ceil(25 * density);
            combinedLength = ((circleRadius*2 + spacing) * numOfCircles) - spacing;

            paint.setColor(ContextCompat.getColor(context, R.color.light_orange));
            canvas.drawRect(0, rectMarginTop, screenWidth,
                    rectMarginBottom, paint);

            paint.setColor(ContextCompat.getColor(context, R.color.shadow_red));
            canvas.drawRect(0, rectMarginBottom, screenWidth,
                    rectMarginBottom + (int) Math.ceil((5) * density), paint);
        }

        drawCircles(canvas);
    }

    private void drawCircles(Canvas canvas)
    {
        paint.setColor(ContextCompat.getColor(context, R.color.shadow_red));

        for (int i=0; i<numOfCircles; i++)
        {
            if (i<7)
                canvas.drawCircle(screenWidth/2 - combinedLength/2 + circleRadius + (i * (circleRadius*2 + spacing)),
                        circleMarginTop, circleRadius, paint);
            else {
                //combinedLength = ((circleRadius*2 + spacing) * (numOfCircles - 7)) - spacing; // Centre bottom circles

                canvas.drawCircle(screenWidth / 2 - combinedLength / 2 + circleRadius + ((i-7) * (circleRadius*2 + spacing)),
                        circleMarginTop + circleRadius*2 + spacing, circleRadius, paint);
            }
        }
    }
}
