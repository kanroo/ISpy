package com.roodapps.ispy.utility;

import android.util.Log;

import com.roodapps.ispy.amazonaws.models.nosql.RoomDO;

import java.util.List;

public class RoomManager
{
    private static final String TAG = "RoomManager";
    private static RoomDO room;

    public static void createRoom(MyHandler handler, String roomId, String password,
                                  List<String> photoQueue, List<String> players)
    {
        final MyHandler finalHandler = handler;
        final String insertRoomId = roomId;
        final String insertPassword = password;
        final List<String> insertPhotoQueue = photoQueue;
        final List<String> insertPlayers = players;

        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                room = new RoomDO();
                room.setRoomId(insertRoomId);
                room.setPassword(insertPassword);
                room.setPhotoQueue(insertPhotoQueue);
                room.setPlayers(insertPlayers);
                room.setLeaderboard(null);

                finalHandler.getManagerClass().getMapper().save(room);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static RoomDO getRoom(MyHandler handler, String roomId)
    {
        final MyHandler finalHandler = handler;
        final String finalRoomId = roomId;

        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                room = finalHandler.getManagerClass().getMapper().load(RoomDO.class, finalRoomId);
                Log.i(TAG, room.getRoomId() + ", " + room.getPassword() + ", " + room.getPlayers());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        return room;
    }
}
