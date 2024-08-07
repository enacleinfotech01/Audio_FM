package com.my.audio_video_fm.activity;

import static androidx.core.app.ServiceCompat.startForeground;
import static com.my.audio_video_fm.ApplicationClass.ACTION_NEXT;
import static com.my.audio_video_fm.ApplicationClass.ACTION_PLAY;
import static com.my.audio_video_fm.ApplicationClass.ACTION_PREVIOUS;
import static com.my.audio_video_fm.ApplicationClass.CHANNEL_ID_2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.my.audio_video_fm.ActionPlaying;
import com.my.audio_video_fm.MusicService;
import com.my.audio_video_fm.NotificationReceiver;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.TrackFiles;
import com.my.audio_video_fm.bottomsheet.TimerBottomSheetFragment;
import com.my.audio_video_fm.model.EpisodeItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EpisodeActivity extends AppCompatActivity implements TimerBottomSheetFragment.TimerSelectionListener, ActionPlaying, ServiceConnection {

    private ImageView targetImageView, playMusic, forward10Sec, replay10Sec, nextMusic, beforeMusic, bluetooth;
    private MediaPlayer mediaPlayer;
    private SeekBar musicSeekBar;

    private MusicService musicService;
    private MediaSessionCompat mediaSessionCompat;
    private CountDownTimer countDownTimer;
    private TextView go, startTimeline, endTimeline, musictext, bottomtime;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 100;
    private BluetoothAdapter bluetoothAdapter;

    private Handler handler = new Handler();
    private Runnable updateRunnable;
    private boolean isPlaying = false;
    private List<EpisodeItem> musicList = new ArrayList<>();
    private int currentIndex = 0;
    private String audioPath;
    private static final String TAG = "Episode";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);

        // Initialize UI components
        bottomtime = findViewById(R.id.show_dialog_button);
        targetImageView = findViewById(R.id.imageView);
        playMusic = findViewById(R.id.playmusic);
        musictext = findViewById(R.id.musicTitle);
        forward10Sec = findViewById(R.id.forward10sec);
        replay10Sec = findViewById(R.id.replay10sec);
        nextMusic = findViewById(R.id.nextmusic);
        beforeMusic = findViewById(R.id.beforemusic);
        musicSeekBar = findViewById(R.id.musicSeekBar);
        startTimeline = findViewById(R.id.starttimeline);
        endTimeline = findViewById(R.id.endtimeline);
        go = findViewById(R.id.go);
        bluetooth = findViewById(R.id.bluetooth);

        // Start and bind to MusicService
        Intent serviceIntent = new Intent(this, MusicService.class);
        serviceIntent.setAction(ACTION_PLAY); // Start action
        startService(serviceIntent);
        bindService(serviceIntent, this, BIND_AUTO_CREATE);

        bluetooth.setOnClickListener(v -> toggleBluetooth());
        populateFile();

        // Retrieve data from intent
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        String title = getIntent().getStringExtra("title");
        String audioUrl = getIntent().getStringExtra("AUDIO_URL");
        mediaSessionCompat = new MediaSessionCompat(this, "PlayerAudio");

        if (audioUrl != null) {
            audioPath = audioUrl;
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(audioPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        findViewById(R.id.show_dialog_button).setOnClickListener(v -> {
            TimerBottomSheetFragment bottomSheet = new TimerBottomSheetFragment();
            bottomSheet.setTimerSelectionListener(this);
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
        });

        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_video)
                    .error(R.drawable.ic_audiotrack)
                    .into(targetImageView);
        }

        if (title != null) {
            musictext.setText(title);
        } else {
            Log.e(TAG, "Title not received");
        }

        if (!musicList.isEmpty()) {
            initializeMediaPlayer(musicList.get(currentIndex));

            playMusic.setOnClickListener(v -> {
                if (isPlaying) {
                    pauseMusic();
                    showNotification(R.drawable.play_arrow_24dp_e8eaed_fill0_wght400_grad0_opsz24);
                } else {
                    playMusic();
                    showNotification(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24);
                }
            });

            forward10Sec.setOnClickListener(v -> forward10Seconds());
            replay10Sec.setOnClickListener(v -> replay10Seconds());
            nextMusic.setOnClickListener(v -> playNextMusic());
            beforeMusic.setOnClickListener(v -> playPreviousMusic());

            updateSeekBar();
            musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress * 1000);
                        startTimeline.setText(formatTime(progress));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    handler.removeCallbacks(updateRunnable);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    handler.post(updateRunnable);
                }
            });
        }

        go.setOnClickListener(v -> finish());
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_out_down);
        });
    }

    private void updateSeekBar() {
        // Ensure there's no previous runnable running
        if (updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }

        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    // Get the current position of the media player
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000; // convert to seconds
                    int duration = mediaPlayer.getDuration() / 1000; // convert to seconds

                    // Update the SeekBar and text views
                    musicSeekBar.setProgress(currentPosition);
                    musicSeekBar.setMax(duration);
                    startTimeline.setText(formatTime(currentPosition));
                    endTimeline.setText(formatTime(duration));

                    // Schedule the next update
                    handler.postDelayed(this, 1000); // update every second
                }
            }
        };

        // Start the first update
        handler.post(updateRunnable);
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder binder = (MusicService.MyBinder) service;
        musicService = binder.getService();
        // Use musicService to control playback
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }

    private void populateFile() {
        String title = getIntent().getStringExtra("title");
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        musicList.add(new EpisodeItem(1, imageUrl, title, "3:00", R.raw.music1, R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.daimond, "music"));
        musicList.add(new EpisodeItem(2, imageUrl, title, "4:00", R.raw.music2, R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.daimond, "music"));
        musicList.add(new EpisodeItem(3, imageUrl, title, "2:30", R.raw.music3, R.drawable.download_24dp_e8eaed_fill0_wght400_grad0_opsz24, R.drawable.daimond, "music"));
    }

    private void initializeMediaPlayer(EpisodeItem episodeItem) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, episodeItem.getIconResId());

        if (mediaPlayer != null) {
            int songDuration = mediaPlayer.getDuration() / 1000; // Duration in seconds
            musicSeekBar.setMax(songDuration);
            endTimeline.setText(formatTime(songDuration));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        unbindService(this);
    }

   

    private void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
            startUpdateSeekBar();
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
            handler.removeCallbacks(updateRunnable);
        }
    }

    private void startUpdateSeekBar() {
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    musicSeekBar.setProgress(currentPosition);
                    startTimeline.setText(formatTime(currentPosition));
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(updateRunnable);
    }

    private void forward10Seconds() {
        if (mediaPlayer != null) {
            int newPosition = mediaPlayer.getCurrentPosition() + 10000;
            mediaPlayer.seekTo(newPosition);
            musicSeekBar.setProgress(newPosition / 1000);
        }
    }

    private void replay10Seconds() {
        if (mediaPlayer != null) {
            int newPosition = mediaPlayer.getCurrentPosition() - 10000;
            if (newPosition < 0) newPosition = 0;
            mediaPlayer.seekTo(newPosition);
            musicSeekBar.setProgress(newPosition / 1000);
        }
    }

    private void playNextMusic() {
        if (currentIndex < musicList.size() - 1) {
            currentIndex++;
            initializeMediaPlayer(musicList.get(currentIndex));
            playMusic();
        }
    }

    private void playPreviousMusic() {
        if (currentIndex > 0) {
            currentIndex--;
            initializeMediaPlayer(musicList.get(currentIndex));
            playMusic();
        }
    }

    private void showNotification(int playPauseIcon) {
        // Create notification channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_2,
                    "Music Playback",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notification channel for music playback controls");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Intents and PendingIntents
        Intent playIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent previousIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent previousPendingIntent = PendingIntent.getBroadcast(this, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setContentTitle("Playing Music")
                .setContentText("Your current track")
                .setSmallIcon(playPauseIcon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_audiotrack))
                .addAction(R.drawable.forward_10_24dp_e8eaed_fill0_wght400_grad0_opsz24, "Previous", previousPendingIntent)
                .addAction(playPauseIcon, "Play/Pause", playPendingIntent)
                .addAction(R.drawable.next, "Next", nextPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Notification Manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(1, notificationBuilder.build());
        }
    }


    @SuppressLint("MissingPermission")
    private void toggleBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            bluetoothAdapter.disable();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toggleBluetooth();
            } else {
                Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void nextclicked() {
        
    }

    @Override
    public void prevclicked() {

    }

    @Override
    public void playclicked() {

    }

    @Override
    public void onTimerSelected(String timerOption) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(Integer.parseInt((String)timerOption));
        }
    }

    @Override
    public void onCustomTimerSelected(int hours, int minutes) {

    }
}
