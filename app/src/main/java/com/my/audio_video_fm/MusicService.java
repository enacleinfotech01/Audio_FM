package com.my.audio_video_fm;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.my.audio_video_fm.activity.EpisodeActivity;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends MediaSessionService {

    private MediaSession mediaSession;
    private ExoPlayer exoPlayer;
    private final IBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        exoPlayer = new ExoPlayer.Builder(this).build();
        mediaSession = new MediaSession.Builder(this, exoPlayer)
                .setCallback(new MediaSessionCallback())
                .build();
    }

    @Override
    public MediaSession onGetSession(@NonNull MediaSession.ControllerInfo controllerInfo) {
        return mediaSession;
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return mBinder;
    }

    @Override
    public void onDestroy() {
        if (mediaSession != null) {
            exoPlayer.release();
            mediaSession.release();
            mediaSession = null;
        }
        super.onDestroy();
    }

    private class MediaSessionCallback implements MediaSession.Callback {
        @NonNull
        @Override
        public ListenableFuture<List<MediaItem>> onAddMediaItems(
                @NonNull MediaSession mediaSession,
                @NonNull MediaSession.ControllerInfo controller,
                @NonNull List<MediaItem> mediaItems) {

            List<MediaItem> updatedMediaItems = new ArrayList<>();
            for (MediaItem item : mediaItems) {
                MediaItem updatedItem = item.buildUpon()
                        .setUri(item.mediaId)  // Assuming mediaId is a valid URI
                        .build();
                updatedMediaItems.add(updatedItem);
            }
            return Futures.immediateFuture(updatedMediaItems);
        }
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void setCallBack(EpisodeActivity callback) {
        // Implement callback handling here
    }
}
