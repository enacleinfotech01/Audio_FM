package com.my.audio_video_fm;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.my.audio_video_fm.model.EpisodeItem;

import java.util.ArrayList;
import java.util.List;

public class Episode extends AppCompatActivity {
    private ImageView targetImageView, playMusic, forward10Sec, replay10Sec, nextMusic, beforeMusic;
    private MediaPlayer mediaPlayer;
    private SeekBar musicSeekBar;
    TextView go;
    private static final int REQUEST_ENABLE_BT = 1;


    private BluetoothAdapter bluetoothAdapter;
    private TextView startTimeline, endTimeline;
    private Handler handler = new Handler();
    private Runnable updateRunnable;
    private boolean isPlaying = false;
    private TextView musictext;
    private ImageView bluetooth;
    private List<EpisodeItem> musicList = new ArrayList<>();
    private int currentIndex = 0;
    private static final String TAG = "Episode";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);

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
        go=findViewById(R.id.go);

        bluetooth = findViewById(R.id.bluetooth);
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBluetooth();
            }
        });
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        String imageUrl1 = getIntent().getStringExtra("image_url");
        String title = getIntent().getStringExtra("title");

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
        musicList.add(new EpisodeItem(imageUrl, title, "3:00", R.raw.music1));
        musicList.add(new EpisodeItem(imageUrl, title, "4:00", R.raw.music2));
        musicList.add(new EpisodeItem(imageUrl, title, "2:30", R.raw.music3));

        if (!musicList.isEmpty()) {
            initializeMediaPlayer(musicList.get(currentIndex));

            playMusic.setOnClickListener(v -> {
                if (isPlaying) {
                    pauseMusic();
                } else {
                    playMusic();
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
                overridePendingTransition(0,R.anim.slide_out_down);
            }
        });
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

    private void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
            playMusic.setImageResource(R.drawable.pause_24dp_e8eaed_fill0_wght400_grad0_opsz24); // Replace with your pause icon
            handler.post(updateRunnable);
        }
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

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth is already enabled", Toast.LENGTH_SHORT).show();
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
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_down);
    }
}