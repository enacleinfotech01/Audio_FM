package com.my.audio_video_fm.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.my.audio_video_fm.R;

import java.util.List;

public class FreeTrailFragment extends Fragment {

    private static final int UPI_PAYMENT = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_free_trail, container, false);

        // UPI Payment Method
        view.findViewById(R.id.upiCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUPIPayment();
            }
        });

        // PhonePe Payment Method
        view.findViewById(R.id.phonePeLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhonePePayment();
            }
        });

        // Google Pay Payment Method
        view.findViewById(R.id.googlePayLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGooglePayPayment();
            }
        });

        // Google Play Store Payment Method
        view.findViewById(R.id.playStoreCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlayStorePayment();
            }
        });

        return view;
    }


    private void startUPIPayment() {
        Uri uri = Uri.parse("upi://pay")
                .buildUpon()
                .appendQueryParameter("pa", "merchant_vpa@bank")
                .appendQueryParameter("pn", "Merchant Name")
                .appendQueryParameter("tn", "Payment for FMPremium")
                .appendQueryParameter("am", "10.00")
                .appendQueryParameter("cu", "INR")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Intent chooser = Intent.createChooser(intent, "Pay with");

        if (null != chooser.resolveActivity(getActivity().getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(getContext(), "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPI_PAYMENT) {
            // Handle UPI payment result
        }
    }

    private void startPhonePePayment() {
        // Implementation for PhonePe Payment
    }

    private void startGooglePayPayment() {
        // Implementation for Google Pay Payment
    }

    private void startPlayStorePayment() {
        BillingClient billingClient = BillingClient.newBuilder(getContext())
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
                        // Handle purchase result
                    }
                })
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // BillingClient setup finished
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // BillingClient service disconnected
            }
        });
    }

}
