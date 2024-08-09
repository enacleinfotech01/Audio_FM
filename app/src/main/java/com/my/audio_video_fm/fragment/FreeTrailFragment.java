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
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.my.audio_video_fm.R;

import java.util.ArrayList;
import java.util.List;

public class FreeTrailFragment extends Fragment {

    private static final int UPI_PAYMENT = 0;
    private static final int PHONE_PE_PAYMENT = 1;
    private static final int GOOGLE_PAY_PAYMENT = 2;
    private static final int PLAY_STORE_PAYMENT = 3;
    private BillingClient billingClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_free_trail, container, false);

        // UPI Payment Method
        view.findViewById(R.id.upiCard).setOnClickListener(v -> startUPIPayment());

        // PhonePe Payment Method
        view.findViewById(R.id.phonePeLayout).setOnClickListener(v -> startPhonePePayment());

        // Google Pay Payment Method
        view.findViewById(R.id.googlePayLayout).setOnClickListener(v -> startGooglePayPayment());

        // Google Play Store Payment Method
        view.findViewById(R.id.playStoreCard).setOnClickListener(v -> startPlayStorePayment());

        return view;
    }

    private void startUPIPayment() {
        Uri uri = Uri.parse("upi://pay")
                .buildUpon()
                .appendQueryParameter("pa", "savaliyahitesha4@okicici")
                .appendQueryParameter("pn", "Merchant Name")
                .appendQueryParameter("tn", "Payment for FMPremium")
                .appendQueryParameter("am", "1.00")
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

    private void startPhonePePayment() {
        Uri uri = Uri.parse("upi://pay")
                .buildUpon()
                .appendQueryParameter("pa", "merchant_vpa@phonepe")
                .appendQueryParameter("pn", "Merchant Name")
                .appendQueryParameter("tn", "Payment for FMPremium")
                .appendQueryParameter("am", "1.00")
                .appendQueryParameter("cu", "INR")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Intent chooser = Intent.createChooser(intent, "Pay with PhonePe");

        if (null != chooser.resolveActivity(getActivity().getPackageManager())) {
            startActivityForResult(chooser, PHONE_PE_PAYMENT);
        } else {
            Toast.makeText(getContext(), "No PhonePe app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    private void startGooglePayPayment() {
        Uri uri = Uri.parse("GPay://pay")
                .buildUpon()
                .appendQueryParameter("pa", "merchant_vpa@googlepay")
                .appendQueryParameter("pn", "Merchant Name")
                .appendQueryParameter("tn", "Payment for FMPremium")
                .appendQueryParameter("am", "1.00")
                .appendQueryParameter("cu", "INR")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Intent chooser = Intent.createChooser(intent, "Pay with Google Pay");

        if (null != chooser.resolveActivity(getActivity().getPackageManager())) {
            startActivityForResult(chooser, GOOGLE_PAY_PAYMENT);
        } else {
            Toast.makeText(getContext(), "No Google Pay app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    private void startPlayStorePayment() {
       /* billingClient = BillingClient.newBuilder(getContext())
                .setListener((billingResult, purchases) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                        for (Purchase purchase : purchases) {
                            handlePurchase(purchase);
                        }
                    }
                })
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // BillingClient setup finished
                    querySkuDetailsAndLaunchPurchaseFlow();
                } else {
                    // Handle error during setup
                    Toast.makeText(getContext(), "Billing setup failed: " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Handle service disconnect
                Toast.makeText(getContext(), "Billing service disconnected", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
    /*private void querySkuDetailsAndLaunchPurchaseFlow() {
        List<String> skuList = new ArrayList<>();
        skuList.add("your_product_sku"); // Replace with your actual product SKU
        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP) // Use SkuType.SUB for subscriptions
                .build();

        billingClient.querySkuDetailsAsync(params, (billingResult, skuDetailsList) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                for (SkuDetails skuDetails : skuDetailsList) {
                    if ("your_product_sku".equals(skuDetails.getSku())) { // Replace with your actual product SKU
                        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                .setSkuDetails(skuDetails)
                                .build();
                        billingClient.launchBillingFlow(getActivity(), billingFlowParams);
                        break;
                    }
                }
            } else {
                // Handle query error
                Toast.makeText(getContext(), "Failed to query SKU details: " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void handlePurchase(Purchase purchase) {
        // Handle the purchase result
        // Update UI or notify the user
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case UPI_PAYMENT:
                case PHONE_PE_PAYMENT:
                case GOOGLE_PAY_PAYMENT:
                    // Handle payment result
                    Toast.makeText(getContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(getContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
