package com.roodapps.ispy.utility;

public class Letter
{
    public char character;
    public float origX;
    public float origY;
    public int size;
    public int position;
    public boolean isMounted;

    // Letter Class holds all necessary  variables of a letter
    public Letter(char character, float origX, float origY, int size, int position, boolean isMounted)
    {
        this.character = character;
        this.origX = origX;
        this.origY = origY;
        this.size = size;
        this.position = position;
        this.isMounted = isMounted;
    }
}
