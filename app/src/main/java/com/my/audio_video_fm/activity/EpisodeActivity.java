package com.my.audio_video_fm.activity;

import static com.my.audio_video_fm.ApplicationClass.ACTION_NEXT;
import static com.my.audio_video_fm.ApplicationClass.ACTION_PLAY;
import static com.my.audio_video_fm.ApplicationClass.ACTION_PREVIOUS;
import static com.my.audio_video_fm.ApplicationClass.CHANNEL_ID_2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import androidx.core.graphics.drawable.IconCompat;

import com.bumptech.glide.Glide;
import com.my.audio_video_fm.ActionPlaying;
import com.my.audio_video_fm.MusicService;
import com.my.audio_video_fm.NotificationReceiver;
import com.my.audio_video_fm.R;
import com.my.audio_video_fm.TrackFiles;
import com.my.audio_video_fm.bottomsheet.TimerBottomSheetFragment;
import com.my.audio_video_fm.model.EpisodeItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EpisodeActivity extends AppCompatActivity implements TimerBottomSheetFragment.TimerSelectionListener, ActionPlaying, ServiceConnection {
    private ImageView targetImageView, playMusic, forward10Sec, replay10Sec, nextMusic, beforeMusic;
    private MediaPlayer mediaPlayer;
    private SeekBar musicSeekBar;

    MusicService musicService;
    MediaSessionCompat mediaSessionCompat;
    private CountDownTimer countDownTimer;
    TextView go;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 100;
    private BluetoothAdapter bluetoothAdapter;

    private TextView startTimeline, endTimeline;
    private Handler handler = new Handler();
    private Runnable updateRunnable;
    private boolean isPlaying = false;
    private TextView musictext;
    private ImageView bluetooth;
    private List<EpisodeItem> musicList = new ArrayList<>();
    private int currentIndex = 0;
    TextView bottomtime;
    private String audioPath;
    private static final String TAG = "Episode";
    private int postion;

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
        go = findViewById(R.id.go);
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        bluetooth = findViewById(R.id.bluetooth);
        poppulatefile();
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

        // Initialize music list with EpisodeItem

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

    private void poppulatefile() {
        String title = getIntent().getStringExtra("title");
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        // Assuming EpisodeItem has been modified to accept a String for the image URL
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
        unbindService(this);
    }

    private void playMusic() {
        if (mediaPlayer != null) {

            mediaPlayer.start();
            isPlaying = true;
            playMusic.setImageResource(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Replace with your pause icon
            handler.post(updateRunnable);
        }
    }

    @Override
    protected void onResume() {

        playMusic();
        playNextMusic();
        playPreviousMusic();
        super.onResume();

    }


    private void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
            playMusic.setImageResource(R.drawable.play_arrow_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Replace with your play icon
            handler.removeCallbacks(updateRunnable);
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
        if (isPlaying) {
            showNotification(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24);
        } else {

            showNotification(R.drawable.play_arrow_24dp_e8eaed_fill0_wght400_grad0_opsz24);
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
        if (isPlaying) {
            showNotification(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24);
        } else {

            showNotification(R.drawable.play_arrow_24dp_e8eaed_fill0_wght400_grad0_opsz24);
        }
    }

    private void playNextMusic() {
        if (!musicList.isEmpty() && currentIndex < musicList.size() - 1) {
            currentIndex++;
            initializeMediaPlayer(musicList.get(currentIndex));
            musictext.setText(musicList.get(currentIndex).getIconResId());// Initializes media player with the new track
            playMusic(); // Starts playing the new track
            updateTitle(musicList.get(currentIndex)); // Update title when changing track
        }
    }

    private void playPreviousMusic() {
        if (!musicList.isEmpty() && currentIndex > 0) {
            currentIndex--;
            initializeMediaPlayer(musicList.get(currentIndex));
            musictext.setText(musicList.get(currentIndex).getIconResId());// Initializes media player with the new track
            playMusic(); // Starts playing the new track
            updateTitle(musicList.get(currentIndex)); // Update title when changing track
        }
    }


    private void updateTitle(EpisodeItem episodeItem) {
        if (episodeItem != null) {
            musictext.setText(episodeItem.getTitle()); // Update the title TextView
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
            updateRunnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            handler.postDelayed(updateRunnable, 1000);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateRunnable);
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder binder = (MusicService.MyBinder) service;
        musicService = binder.getService();
        Log.e("Connected", "musicservices");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.e("Discountect", musicService + "");
        musicService = null;
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

    @SuppressLint("UnspecifiedImmutableFlag")
    public void showNotification(int PlayPausebtn) {
        String title = getIntent().getStringExtra("title");
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        Intent intent = new Intent(this, EpisodeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevpendingIntent = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent playIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent playpendingIntent = PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextpendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Decode image from URL string
        Bitmap picture = null;
        try {
            URL url = new URL(imageUrl);
            picture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                    .setSmallIcon(IconCompat.createWithContentUri(musicList.get(postion).getImageUrl())) // Replace with your own small icon
                    .setContentTitle(musicList.get(postion).getTitle())
                    .setLargeIcon(picture)
                    .addAction(R.drawable.replay_10_24dp_e8eaed_fill0_wght400_grad0_opsz24, "Previous", prevpendingIntent) // Replace with your own icons
                    .addAction(PlayPausebtn, "Play", playpendingIntent)
                    .addAction(R.drawable.forward_10_24dp_e8eaed_fill0_wght400_grad0_opsz24, "Next", nextpendingIntent) // Replace with your own icons
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(contentIntent)
                    .setOnlyAlertOnce(true)
                    .build();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }


}