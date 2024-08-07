package com.my.audio_video_fm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private final IBinder mBinder = new MyBinder();
    private MediaPlayer mediaPlayer;
    private ActionPlaying actionPlaying;

    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREVIOUS = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PAUSE = "PAUSE";
    public static final String ACTION_SPEED = "SPEED";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind", "Method");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        // Initialize MediaPlayer with default settings if needed
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionName = intent.getStringExtra("myActionName");
        if (actionName != null) {
            switch (actionName) {
                case ACTION_PLAY:
                    if (actionPlaying != null) {
                        actionPlaying.playClicked();
                    }
                    break;

                case ACTION_NEXT:
                    if (actionPlaying != null) {
                        actionPlaying.nextClicked();
                    }
                    break;
                case ACTION_PREVIOUS:
                    if (actionPlaying != null) {
                        actionPlaying.prevClicked();
                    }
                    break;
                case ACTION_SPEED:
                    float speed = intent.getFloatExtra("speed", 1.0f);
                    setPlaybackSpeed(speed);
                    break;
            }
        }
        return START_STICKY;
    }

    public void playTrack(int audioResId) {
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release any existing MediaPlayer
        }
        mediaPlayer = MediaPlayer.create(this, audioResId);
        if (mediaPlayer != null) {
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
            Log.d("MusicService", "Playback started");
        } else {
            Log.e("MusicService", "Failed to create MediaPlayer");
        }
    }



    public void pauseTrack() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stopTrack() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public void setPlaybackSpeed(float speed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                PlaybackParams params = mediaPlayer.getPlaybackParams();
                params.setSpeed(speed);
                mediaPlayer.setPlaybackParams(params);
                Log.d("MusicService", "Playback speed set to: " + speed);
            } else {
                Log.w("MusicService", "MediaPlayer is null or not playing");
            }
        } else {
            Log.w("MusicService", "Playback speed control is not supported on this device.");
        }
    }

    public void setCallBack(ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
