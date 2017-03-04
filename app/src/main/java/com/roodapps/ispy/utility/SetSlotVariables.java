package com.roodapps.ispy.utility;

import static com.roodapps.ispy.activities.MainActivity.screenDensity;

public class SetSlotVariables
{
    public int answerLength;
    public int marginTop;
    public int size;
    public int spacing;
    public float combinedLength;

    // Initialize  all variables to do with slots according to length of answer
    public SetSlotVariables(int answerLength)
    {
        this.answerLength = answerLength;
        spacing = (int) Math.ceil(5 * screenDensity);

        if (answerLength == 7)
        {
            marginTop = (int) Math.ceil(378 * screenDensity);
            size = (int) Math.ceil(44 * screenDensity);
            combinedLength = ((size + spacing) * 7) - spacing;
        }
        else if (answerLength > 7)
        {
            marginTop = (int) Math.ceil(378 * screenDensity);
            size = (int) Math.ceil(40 * screenDensity);
            combinedLength = ((size + spacing) * 7) - spacing;
        }
        else
        {
            marginTop = (int) Math.ceil(375 * screenDensity);
            size = (int) Math.ceil(50 * screenDensity);
            combinedLength = ((size + spacing) * answerLength) - spacing;
        }
    }
}
