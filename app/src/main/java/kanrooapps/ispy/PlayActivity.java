package kanrooapps.ispy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static kanrooapps.ispy.MainActivity.screenWidth;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.activity_play);
        relativeLayout.addView(new EmptySlotBuilder(this));

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float density = metrics.density;

        ImageView photoView = new ImageView(this);
        RelativeLayout.LayoutParams photoParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        photoParams.setMargins(0, (int) Math.ceil(55 * density), 0, 0);
        photoView.setLayoutParams(photoParams);
        photoView.getLayoutParams().height = (int) Math.ceil(310 * density);

        Bitmap photoBp = BitmapFactory.decodeResource(getResources(), R.drawable.photo_landscape);
        photoBp = scaleBitmap(photoBp, (int) Math.ceil(310 * density));
        photoView.setImageBitmap(photoBp);

        relativeLayout.addView(photoView);

    }

    private Bitmap scaleBitmap(Bitmap bmp, int maxHeight)
    {
        final int maxSize = maxHeight;
        int outWidth;
        int outHeight;
        int inWidth = bmp.getWidth();
        int inHeight = bmp.getHeight();
        int croppedWidth = 0;

        outHeight = maxSize;
        outWidth = (inWidth * maxSize) / inHeight;

        Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, outWidth, outHeight, false);

        if (scaledBmp.getWidth() > screenWidth)
            croppedWidth = scaledBmp.getWidth() - screenWidth;

        return Bitmap.createBitmap(scaledBmp, 0, 0, scaledBmp.getWidth() - croppedWidth, scaledBmp.getHeight());
    }

        // Possibly do this outside of this View and return an array of the circles and their positions so its easier to do placeLetters()
        // Draw letters along bottom of screen (maybe 12 of them) with random pos and angle
        private void drawLetters()
        {}

        // Place letters into circles
        private void placeLetters()
        {}



}
