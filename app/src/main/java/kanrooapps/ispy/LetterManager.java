package kanrooapps.ispy;

import android.content.Context;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by Ethan on 1/02/2017.
 */

public class LetterManager
{
    private Context context;
    private String answer;

    public LetterManager(Context context, String answer)
    {
        this.context = context;
        this.answer = answer;
    }

    private ImageView[] createImageViews()
    {
        ImageView[] letters = new ImageView[answer.length()-1];

        for (int i = 0; i < answer.length(); i++)
        {
            letters[i] = new ImageView(context);
            // Generate random IDs for each image view and return an array associating each ID
            // with a randomly generated character.
            // The characters of the answer must also be randomly generated.
            //letters[i].setId
        }
        return letters;
    }

    private char generateChar()
    {
        Random r = new Random();
        return (char)(r.nextInt(26) + 'a');
    }

}
