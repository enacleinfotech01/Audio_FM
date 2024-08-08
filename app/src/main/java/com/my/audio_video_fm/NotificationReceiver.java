package com.my.audio_video_fm;

import static com.my.audio_video_fm.ApplicationClass.ACTION_NEXT;
import static com.my.audio_video_fm.ApplicationClass.ACTION_PLAY;
import static com.my.audio_video_fm.ApplicationClass.ACTION_PREVIOUS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case ACTION_PREVIOUS:
                    // Handle previous action
                    break;
                case ACTION_PLAY:
                    // Handle play/pause action
                    break;
                case ACTION_NEXT:
                    // Handle next action
                    break;
                default:
                    break;
            }
        }
    }
}
