package com.roodapps.ispy.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "ispy-mobilehub-1311707257-room")

public class RoomDO {
    private String _roomId;
    private Map<String, String> _leaderboard;
    private String _password;
    private List<String> _photoQueue;
    private List<String> _players;

    @DynamoDBHashKey(attributeName = "room_id")
    @DynamoDBAttribute(attributeName = "room_id")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(final String _roomId) {
        this._roomId = _roomId;
    }
    @DynamoDBAttribute(attributeName = "leaderboard")
    public Map<String, String> getLeaderboard() {
        return _leaderboard;
    }

    public void setLeaderboard(final Map<String, String> _leaderboard) {
        this._leaderboard = _leaderboard;
    }
    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return _password;
    }

    public void setPassword(final String _password) {
        this._password = _password;
    }
    @DynamoDBAttribute(attributeName = "photo_queue")
    public List<String> getPhotoQueue() {
        return _photoQueue;
    }

    public void setPhotoQueue(final List<String> _photoQueue) {
        this._photoQueue = _photoQueue;
    }
    @DynamoDBAttribute(attributeName = "players")
    public List<String> getPlayers() {
        return _players;
    }

    public void setPlayers(final List<String> _players) {
        this._players = _players;
    }

}
