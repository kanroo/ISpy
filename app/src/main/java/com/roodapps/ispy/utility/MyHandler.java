package com.roodapps.ispy.utility;

import com.roodapps.ispy.activities.MainActivity;
import com.roodapps.ispy.activities.PlayActivity;
import com.roodapps.ispy.activities.RoomActivity;
import com.roodapps.ispy.amazonaws.ManagerClass;


public class MyHandler
{
    DatabaseHelper dbHelper;
    ManagerClass managerClass;
    MainActivity mainActivity;
    PlayActivity playActivity;
    PlayActivity lastPlayActivity;
    RoomActivity roomActivity;
    ActivityState activityState;

    public enum ActivityState
    {
        SPLASH, MAIN, PLAY, ROOM
    }

    // Setters
    public void setDbHelper(DatabaseHelper dbHelper) { this.dbHelper = dbHelper; }
    public void setManagerClass(ManagerClass managerClass) {this.managerClass = managerClass; }
    public void setMainActivity(MainActivity mainActivity) { this.mainActivity = mainActivity; }
    public void setPlayActivity(PlayActivity playActivity) { this.playActivity = playActivity; }
    public void setLastPlayActivity(PlayActivity lastPlayActivity) { this.lastPlayActivity = lastPlayActivity; }
    public void setRoomActivity(RoomActivity roomActivity) { this.roomActivity = roomActivity; }
    public void setActivityState(ActivityState activityState) { this.activityState = activityState; }

    // Getters
    public DatabaseHelper getDbHelper() { return this.dbHelper; }
    public ManagerClass getManagerClass() {return this.managerClass; }
    public MainActivity getMainActivity() { return this.mainActivity; }
    public PlayActivity getPlayActivity() { return this.playActivity; }
    public PlayActivity getLastPlayActivity() { return this.lastPlayActivity; }
    public RoomActivity getRoomActivity() { return this.roomActivity; }
    public ActivityState getActivityState() { return this.activityState; }
}
