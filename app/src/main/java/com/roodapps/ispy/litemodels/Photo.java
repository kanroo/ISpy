package com.roodapps.ispy.litemodels;

/**
 * Created by Ethan on 25/02/2017.
 */

public class Photo
{
    public int id;
    public String name;
    public String clue;
    public String answer;

    public Photo(int id, String name, String clue, String answer)
    {
        this.id = id;
        this.name = name;
        this.clue = clue;
        this.answer = answer;
    }
}
