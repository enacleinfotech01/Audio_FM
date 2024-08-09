package com.my.audio_video_fm.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.my.audio_video_fm.R;

import de.hdodenhof.circleimageview.BuildConfig;

public class activirty_setting extends AppCompatActivity {
    private LinearLayout legalLayout;
    private ImageView arrowImageView;
    CardView form;
    private boolean isMenuVisible = false;
    private ImageView copyImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activirty_setting);
form=findViewById(R.id.form);
        legalLayout = findViewById(R.id.legal);
        arrowImageView = findViewById(R.id.arrow);

        copyImageView = findViewById(R.id.copy);

        copyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyDeviceInfoToClipboard();
            }
        });
        legalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activirty_setting.this, Fragment_container.class);
                intent.putExtra("dailycheck", 1);
                startActivity(intent);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.legal_policies_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_terms_conditions) {
                    // Handle Terms and Conditions click
                    return true;
                } else if (itemId == R.id.menu_privacy_policy) {
                    // Handle Privacy Policy click
                    return true;
                } else if (itemId == R.id.menu_cancellation_refund) {
                    // Handle Cancellation and Refund Policy click
                    return true;
                } else if (itemId == R.id.menu_report_abuse) {
                    // Handle Report Abuse (DMCA) click
                    return true;
                }
                return false;
            }
        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                arrowImageView.setImageResource(R.drawable.arrow_drop_down_24dp_e8eaed_fill0_wght400_grad0_opsz24);
                isMenuVisible = false;
            }
        });

        popupMenu.show();

        arrowImageView.setImageResource(R.drawable.arrow_drop_up_24dp_e8eaed_fill0_wght400_grad0_opsz24);
        isMenuVisible = true;
    }
    private void copyDeviceInfoToClipboard() {
        String appVersion = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        String osVersion = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        String deviceModel = Build.MODEL;
        String deviceBrand = Build.BRAND;
        String userId = "80953213"; // Replace this with actual user id fetching logic if available

        String deviceInfo = "App Version - " + appVersion + "\n" +
                "Version Code - " + versionCode + "\n" +
                "Device OS - " + osVersion + "\n" +
                "SDK Version - " + sdkVersion + "\n" +
                "Device - " + deviceBrand + " " + deviceModel + "\n" +
                "User Id - " + userId;

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Device Info", deviceInfo);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Device information copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}
