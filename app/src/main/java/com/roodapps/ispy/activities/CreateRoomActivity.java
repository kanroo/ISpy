package com.roodapps.ispy.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.roodapps.ispy.MainApplication;
import com.roodapps.ispy.R;
import com.roodapps.ispy.utility.MyHandler;
import com.roodapps.ispy.utility.RoomManager;

public class CreateRoomActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        // Set Activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set Background Image View
        ImageView playBG = (ImageView) findViewById(R.id.play_bg);
        Glide.with(this).load(R.drawable.play_bg).into(playBG);

        EditText editRoomId = (EditText) findViewById(R.id.edit_roomid);
        final String roomId = editRoomId.getText().toString();

        EditText editPassword = (EditText) findViewById(R.id.edit_password);
        final String password = editPassword.getText().toString();

        Button btnCreate = (Button) findViewById(R.id.btn_create_room);

        final Intent roomIntent = new Intent(this, RoomActivity.class);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RoomManager.createRoom(MainApplication.getHandler(), roomId, password, null, null);
                roomIntent.putExtra("roomId", roomId);
                startActivity(roomIntent);
            }
        });
    }
}
