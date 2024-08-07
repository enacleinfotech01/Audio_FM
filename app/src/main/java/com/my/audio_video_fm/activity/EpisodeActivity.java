package com.my.audio_video_fm.activity;

import static com.my.audio_video_fm.ApplicationClass.ACTION_NEXT;
import static com.my.audio_video_fm.ApplicationClass.ACTION_PLAY;
import static com.my.audio_video_fm.ApplicationClass.ACTION_PREVIOUS;
import static com.my.audio_video_fm.ApplicationClass.CHANNEL_ID_2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
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
import com.my.audio_video_fm.bottomsheet.SpeedControlBottomSheetFragment;
import com.my.audio_video_fm.bottomsheet.TimerBottomSheetFragment;

import java.io.IOException;
import java.util.ArrayList;

public class EpisodeActivity extends AppCompatActivity implements TimerBottomSheetFragment.TimerSelectionListener, ActionPlaying, ServiceConnection,SpeedControlBottomSheetFragment.SpeedChangeListener {
    private ImageView targetImageView, playMusic, forward10Sec, replay10Sec, nextMusic, beforeMusic;
    private MediaPlayer mediaPlayer;
    private SeekBar musicSeekBar;
    private boolean isBound = false;
    private boolean isPlaying = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinder binder = (MusicService.MyBinder) service;
            musicService = binder.getService();
            isBound = true;
            Log.d("EpisodeActivity", "Service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isBound = false;
            Log.d("EpisodeActivity", "Service disconnected");
        }
    };


    TextView speed;
    MusicService musicService;
    MediaSessionCompat mediaSession;
    private TextView speedTextView;
    private SeekBar speedSeekBar;
    int position = 0;

    ArrayList<TrackFiles> trackFilesArrayList = new ArrayList<>();
    MediaSessionCompat mediaSessionCompat;
    private CountDownTimer countDownTimer;
    TextView go;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 100;
    private BluetoothAdapter bluetoothAdapter;

    private TextView startTimeline, endTimeline;
    private Handler handler = new Handler();


    private TextView musictext;
    private ImageView bluetooth;

    private int currentIndex = 0;
    TextView bottomtime;
    private String audioPath;
    private static final String TAG = "Episode";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);
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
        speed = findViewById(R.id.speed);
        go = findViewById(R.id.go);
        mediaSession = new MediaSessionCompat(this, "PlayerAudio");
        bluetooth = findViewById(R.id.bluetooth);
        poppulateFiles();
        setupSeekBar();
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBluetooth();
            }
        });
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        String imageUrl1 = getIntent().getStringExtra("image_url");
        String title = getIntent().getStringExtra("title");
        String audioUrl = getIntent().getStringExtra("AUDIO_URL");
        mediaSessionCompat = new MediaSessionCompat(this, "PlyerAudio");
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);


        if (audioPath != null) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpeedControlBottomSheet();

            }
        });


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

        if (imageUrl1 != null) {
            Glide.with(this)
                    .load(imageUrl1)
                    .placeholder(R.drawable.ic_video)
                    .error(R.drawable.ic_audiotrack)
                    .into(targetImageView);
        }

        if (title != null) {
            musictext.setText(title);
        } else {
            Log.e("EpisodeActivity", "Title not received");
        }
        if (mediaPlayer == null) {
            Log.e("Episode", "MediaPlayer is null. Populating files.");
            poppulateFiles();
        } else if (!mediaPlayer.isPlaying()) {
            Log.e("Episode", "MediaPlayer is not playing");
        } else {
            Log.d("Episode", "Safe to change speed");
        }


        nextMusic.setOnClickListener(view -> {
            nextClicked();
            playNextMusic();
            Log.e("Playing", isPlaying + "");
            showNotification(isPlaying ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play);

        });

        beforeMusic.setOnClickListener(view -> {
            prevClicked();
            playPreviousMusic();
            Log.e("Playing", isPlaying + "");
            showNotification(isPlaying ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play);

        });

        playMusic.setOnClickListener(view -> {
            playClicked();

            Log.e("Playing", isPlaying + "");
        });


        forward10Sec.setOnClickListener(v -> forward10Seconds());
        replay10Sec.setOnClickListener(v -> replay10Seconds());
        onTrackSelected(0);

        updateSeekBar();
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                    startTimeline.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Stop updating SeekBar while user is interacting with it
                handler.removeCallbacks(updateRunnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Resume updating SeekBar after user stops interacting
                handler.post(updateRunnable);
            }
        });


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity
                finish();
                // Optional: add a transition animation
                overridePendingTransition(0, R.anim.slide_out_down);
            }
        });
    }

    private void onTrackSelected(int position) {
        if (position >= 0 && position < trackFilesArrayList.size()) {
            TrackFiles selectedTrack = trackFilesArrayList.get(position);
            initializeMediaPlayer(selectedTrack);
        } else {
            Log.e("SelectionError", "Invalid track position: " + position);
        }
    }

    private void poppulateFiles() {
        trackFilesArrayList.add(new TrackFiles(1, "Faded", "Alan Walker", R.drawable.t1, R.raw.musicplay));
        trackFilesArrayList.add(new TrackFiles(2, "Attention", "Charlie Puth", R.drawable.t2, R.raw.music2));
        trackFilesArrayList.add(new TrackFiles(3, "Baarish", "Darshan Raval", R.drawable.t3, R.raw.music3));
        trackFilesArrayList.add(new TrackFiles(4, "Believer", "Imagine Dragons", R.drawable.t4, R.raw.music2));
    }

    private void initializeMediaPlayer(TrackFiles trackFiles) {
        // Release any existing MediaPlayer instance
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Get the resource ID from TrackFiles
        int audioResId = trackFiles.getAudioResource(); // Ensure getAudioResource() returns the correct resource ID

        if (audioResId != 0) {
            try {
                // Initialize MediaPlayer with the provided audio resource ID
                mediaPlayer = MediaPlayer.create(this, audioResId);

                // Check if MediaPlayer was successfully initialized
                if (mediaPlayer != null) {
                    mediaPlayer.setOnPreparedListener(mp -> {
                        Log.d(TAG, "MediaPlayer is prepared.");
                        // Do not start playback here, let playClicked() handle it
                    });

                    mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                        Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
                        return true; // Indicate that the error was handled
                    });

                    int songDuration = mediaPlayer.getDuration() / 1000; // Duration in seconds

                    // Update UI components if they are not null
                    if (musicSeekBar != null) {
                        musicSeekBar.setMax(songDuration);
                    }
                    if (endTimeline != null) {
                        endTimeline.setText(formatTime(songDuration));
                    }
                    updateSeekBar(); // Start updating the seek bar
                } else {
                    Log.e("MediaPlayerError", "Failed to initialize MediaPlayer with resource ID: " + audioResId);
                }
            } catch (Resources.NotFoundException e) {
                Log.e("MediaPlayerError", "Resource not found for ID: " + audioResId, e);
            }
        } else {
            Log.e("MediaPlayerError", "Invalid audio resource ID: " + audioResId);
        }
    }




    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                int currentPosition = mediaPlayer.getCurrentPosition() / 1000; // Current position in seconds
                if (musicSeekBar != null) {
                    musicSeekBar.setProgress(currentPosition);
                }
                if (startTimeline != null) {
                    startTimeline.setText(formatTime(currentPosition));
                }
                handler.postDelayed(this, 1000); // Update every second
            }
        }
    };

    public void playClicked() {
        // Ensure you have a valid TrackFiles instance
        if (position < 0 || position >= trackFilesArrayList.size()) {
            Log.e("Episode", "Invalid position: " + position);
            return;
        }

        // Check if MediaPlayer is already initialized
        if (mediaPlayer == null) {
            // Initialize MediaPlayer with the selected TrackFiles
            TrackFiles trackFiles = trackFilesArrayList.get(position);
            initializeMediaPlayer(trackFiles);
        }

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                // Pause music
                mediaPlayer.pause();
                isPlaying = false;
                playMusic.setImageResource(R.drawable.play_arrow_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Replace with your play icon
                showNotification(R.drawable.play_arrow_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Update notification with play icon
            } else {
                // Start or resume music
                mediaPlayer.start();
                isPlaying = true;
                playMusic.setImageResource(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Replace with your pause icon
                showNotification(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Update notification with pause icon
                handler.post(updateRunnable); // Start updating the seek bar
            }
        } else {
            Log.e("Episode", "Cannot play music. MediaPlayer is null.");
        }
    }





    @Override

    public void nextClicked() {
        // Stop current track if playing
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Move to the next track
        if (position == trackFilesArrayList.size() - 1) {
            position = 0;
        } else {
            position++;
        }

        TrackFiles trackFiles = trackFilesArrayList.get(position);
        musictext.setText(trackFiles.getTitle());

        // Initialize and start the next track
        initializeMediaPlayer(trackFiles);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
            playMusic.setImageResource(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Update play/pause icon
            showNotification(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Update notification with pause icon
        }

        // Update UI and start updating the seek bar
        handler.post(updateRunnable);
    }

    @Override
    public void prevClicked() {
        // Stop current track if playing
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Move to the previous track
        if (position == 0) {
            position = trackFilesArrayList.size() - 1;
        } else {
            position--;
        }

        TrackFiles trackFiles = trackFilesArrayList.get(position);
        musictext.setText(trackFiles.getTitle());

        // Initialize and start the previous track
        initializeMediaPlayer(trackFiles);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
            playMusic.setImageResource(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Update play/pause icon
            showNotification(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Update notification with pause icon
        }

        // Update UI and start updating the seek bar
        handler.post(updateRunnable);
    }



    private void playMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isPlaying = false;
                playMusic.setImageResource(R.drawable.play_arrow_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Replace with your play icon
                showNotification(R.drawable.play_arrow_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Update notification with play icon
            } else {
                mediaPlayer.start();
                isPlaying = true;
                playMusic.setImageResource(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Replace with your pause icon
                showNotification(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Update notification with pause icon
                handler.post(updateRunnable);
            }
        }
    }


    private void toggleBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!hasBluetoothPermissions()) {
            requestBluetoothPermissions();
        } else {
            enableBluetooth();
        }
    }

    private void enableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth is already enabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    private boolean hasBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 (API 31) and above
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 (API 31) and above
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
            }, REQUEST_BLUETOOTH_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
            }, REQUEST_BLUETOOTH_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            boolean permissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = false;
                    break;
                }
            }

            if (permissionsGranted) {
                enableBluetooth();
            } else {
                Toast.makeText(this, "Bluetooth permissions are required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void forward10Seconds() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            if (currentPosition + 10000 <= mediaPlayer.getDuration()) {
                mediaPlayer.seekTo(currentPosition + 10000);
            } else {
                mediaPlayer.seekTo(mediaPlayer.getDuration());
            }
        }

    }

    private void replay10Seconds() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            if (currentPosition - 10000 >= 0) {
                mediaPlayer.seekTo(currentPosition - 10000);
            } else {
                mediaPlayer.seekTo(0);
            }
        }

    }

    private void playNextMusic() {
        if (!trackFilesArrayList.isEmpty() && currentIndex < trackFilesArrayList.size() - 1) {
            currentIndex++;
            initializeMediaPlayer(trackFilesArrayList.get(currentIndex)); // Initializes media player with the new track
            mediaPlayer.start(); // Starts playing the new track
            isPlaying = true;
            playMusic.setImageResource(R.drawable.play_arrow_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Update play button to pause icon
            nextClicked();
            // Instead of setting a resource ID
            musictext.setText(trackFilesArrayList.get(position).getTitle()); // Use the actual title string
            // Update text with the new track information
            updateTitle(trackFilesArrayList.get(currentIndex)); // Update title when changing track

            showNotification(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Update notification with pause icon
        }
    }

    private void playPreviousMusic() {
        if (!trackFilesArrayList.isEmpty() && currentIndex > 0) {
            currentIndex--;
            prevClicked();
            initializeMediaPlayer(trackFilesArrayList.get(currentIndex));
            musictext.setText(trackFilesArrayList.get(currentIndex).getTitle());// Initializes media player with the new track
       // Starts playing the new track
            updateTitle(trackFilesArrayList.get(currentIndex)); // Update title when changing track

        }
    }

    private void updateTitle(TrackFiles trackFiles) {
        if (trackFiles != null) {
            musictext.setText(trackFiles.getTitle()); // Update the title TextView
        } else {
            Log.e("EpisodeActivity", "EpisodeItem is null");
        }
    }


    private String formatTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }


    private void updateSeekBar() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition() / 1000; // Position in seconds
            musicSeekBar.setProgress(currentPosition);
            startTimeline.setText(formatTime(currentPosition));

        }
    }

    private void setupSeekBar() {
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mediaPlayer != null) {
                        mediaPlayer.seekTo(progress * 1000); // Convert seconds to milliseconds
                        startTimeline.setText(formatTime(progress));
                    } else {
                        // Handle the case where mediaPlayer is null
                        Log.e("MediaPlayerError", "MediaPlayer is not initialized.");
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Stop updating seek bar when user is manually adjusting it
                if (updateRunnable != null) {
                    handler.removeCallbacks(updateRunnable);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Resume updating seek bar after user stops adjusting it
                if (mediaPlayer != null) {
                    updateSeekBar();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
        musicService = binder.getService();
        musicService.setCallBack(EpisodeActivity.this);
        Log.e("Connected", musicService + "");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
        Log.e("Disconnected", musicService + "");
    }

    @Override
    public void onTimerSelected(String timerOption) {
        bottomtime.setText(timerOption);
        Log.d("Episode", "Timer set for: " + timerOption);

        if ("Time Off".equals(timerOption)) {
            // Immediately stop the music
            cancelCountDownTimer(); // Cancel the countdown timer if it is running
            return; // Exit early as no countdown timer is needed
        }

        long durationInMillis;
        switch (timerOption) {
            case "When current show ends":
                durationInMillis = getRemainingTimeForShow();
                break;
            case "When current episode ends":
                durationInMillis = getRemainingTimeForEpisode();
                break;
            case "30 mins":
                durationInMillis = 30 * 60 * 1000;
                break;
            case "1 hour":
                durationInMillis = 60 * 60 * 1000;
                break;
            default:
                durationInMillis = 0;
                break;
        }

        if (durationInMillis > 0) {
            startCountDownTimert(durationInMillis);
        }
    }


    private long getRemainingTimeForShow() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition(); // Current position in milliseconds
            int totalDuration = mediaPlayer.getDuration(); // Implement this method to get the total duration of the playlist
            return totalDuration - currentPosition;
        }
        return 0; // Placeholder value
    }

    private void startCountDownTimert(long durationInMillis) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = millisUntilFinished / 3600000;
                long minutes = (millisUntilFinished % 3600000) / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                String timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                bottomtime.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                bottomtime.setText("Time's up!");
                stopMusic();
            }
        }.start();
    }


    private void cancelCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null; // Clear the reference to avoid leaks
        }
    }

    private long getRemainingTimeForEpisode() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition(); // Current position in milliseconds
            int songDuration = mediaPlayer.getDuration(); // Duration of the current song in milliseconds
            return songDuration - currentPosition;
        }
        return 0; // Placeholder value
    }

    @Override
    public void onCustomTimerSelected(int hours, int minutes) {
        String customTimer = hours + " hours and " + minutes + " minutes";
        bottomtime.setText(customTimer);
        Log.d("Episode", "Custom Timer set for: " + customTimer);
        Toast.makeText(this, "Custom Timer set for: " + customTimer, Toast.LENGTH_SHORT).show();

        long durationInMillis = (hours * 3600 + minutes * 60) * 1000;
        startCountDownTimer(durationInMillis);
    }

    private void startCountDownTimer(long durationInMillis) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }


        countDownTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = millisUntilFinished / 3600000;
                long minutes = (millisUntilFinished % 3600000) / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                String timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                bottomtime.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                bottomtime.setText("Time's up!");
                stopMusic();
            }
        }.start();
    }


    private void stopMusic() {
        // Implement the logic to stop your music playback here
        Log.d("Episode", "Music stopped.");
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
            handler.removeCallbacks(updateRunnable);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_down);
    }


    public void showNotification(int playPauseBtn) {
        // Create an Intent for the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Intents for Previous, Play/Pause, and Next actions
        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(
                this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent playIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(
                this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(
                this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Load the large icon for the notification
        Bitmap picture = BitmapFactory.decodeResource(getResources(), trackFilesArrayList.get(position).getThumbnail());

        // Create NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(R.drawable.ic_skip_next) // Ensure this icon exists
                .setLargeIcon(picture)
                .setContentTitle(trackFilesArrayList.get(position).getTitle())
                .setContentText(trackFilesArrayList.get(position).getArtist())
                .addAction(R.drawable.ic_skip_previous, "Previous", prevPendingIntent)
                .addAction(playPauseBtn, "Play", playPendingIntent)
                .addAction(R.drawable.ic_skip_next, "Next", nextPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(contentIntent)
                .setOnlyAlertOnce(true)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(1)); // Shows only the Play/Pause action in compact view

        // Ensure notification channel is created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_2,
                    "Playback Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // Notify with the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify( 1, builder.build()); // Use a consistent ID
    }

    private void showSpeedControlBottomSheet() {
        SpeedControlBottomSheetFragment bottomSheetFragment = new SpeedControlBottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }


    @Override
    public void onSpeedChanged(float speed) {
        // Apply the speed to the MediaPlayer or other components
        if (mediaPlayer != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
            }
        }
    }



}