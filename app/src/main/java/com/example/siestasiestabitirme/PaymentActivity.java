package com.example.siestasiestabitirme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebMessage;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.siestasiestabitirme.databinding.ActivityPaymentBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPaymentBinding binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //WebView myWebView = (WebView) findViewById(R.id.webview);

        WebView myWebView = binding.webview;
        WebSettings webSettings = myWebView.getSettings();
        //The following line must be added for the javascript to work.
        webSettings.setJavaScriptEnabled(true);
        String sdkUrl = "https://sandbox-mobil-sdk.iyzipay.com/";
        String callbackUrl = "https://www.google.com/";
        myWebView.loadUrl(sdkUrl);
        //5890040000000016

        // The event listener that listens to the loading process of the page opened on the webview.
        myWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {

                // An additional condition has been added to the if query to understand that the page opened here is the sdk page.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && url.equals(sdkUrl)) {
                    try {
                        // Generate of the JSON Object required to initialize Sdk and PWI payment;
                        // Descriptions of these parameters can be found in the Integration Document.
                        // Main JSON Object is sdkObject and other objects are collected in it.
                        JSONObject sdkObject = new JSONObject();
                        JSONObject paymentBody = new JSONObject();

                        paymentBody.put("locale", "tr");
                        paymentBody.put("conversationId", "123456789");
                        paymentBody.put("price", "50.19");
                        paymentBody.put("paidPrice", "50.19");
                        paymentBody.put("currency", "TRY");
                        JSONArray enabledInstallments = new JSONArray();
                        enabledInstallments.put(2);
                        enabledInstallments.put(3);
                        enabledInstallments.put(6);
                        enabledInstallments.put(9);
                        paymentBody.put("enabledInstallments", enabledInstallments);
                        paymentBody.put("basketId", "B67832");
                        paymentBody.put("paymentGroup", "PRODUCT");
                        paymentBody.put("paymentSource", "MOBILE_SDK");
                        paymentBody.put("callbackUrl", callbackUrl);

                        JSONObject buyer = new JSONObject();

                        buyer.put("id", "BY789");
                        buyer.put("name", "John");
                        buyer.put("surname", "Buyer");
                        buyer.put("identityNumber", "74300864791");
                        buyer.put("email", "john.buyer@mail.com");
                        buyer.put("gsmNumber", "+905555555555");
                        buyer.put("registrationAddress", "Adres");
                        buyer.put("city", "Istanbul");
                        buyer.put("country", "Turkey");
                        buyer.put("ip", "192.168.1.1");
                        paymentBody.put("buyer", buyer);

                        JSONObject shippingAddress = new JSONObject();

                        shippingAddress.put("address", "Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
                        shippingAddress.put("contactName", "John Buyer");
                        shippingAddress.put("city", "Istanbul");
                        shippingAddress.put("country", "Turkey");
                        paymentBody.put("shippingAddress", shippingAddress);

                        JSONObject billingAddress = new JSONObject();

                        billingAddress.put("address", "Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
                        billingAddress.put("contactName", "John Buyer");
                        billingAddress.put("city", "Istanbul");
                        billingAddress.put("country", "Turkey");
                        paymentBody.put("billingAddress", billingAddress);

                        JSONArray basketItems = new JSONArray();
                        JSONObject basketItem1 = new JSONObject();

                        basketItem1.put("id", "BI101");
                        basketItem1.put("price", "50.19");
                        basketItem1.put("name", "Binocular");
                        basketItem1.put("category1", "Collectibles");
                        basketItem1.put("itemType", "PHYSICAL");
                        basketItems.put(basketItem1);
                        paymentBody.put("basketItems", basketItems);

                        JSONObject mobileDeviceInfoDto = new JSONObject();

                        mobileDeviceInfoDto.put("operatingSystemVersion", "Android 6");
                        mobileDeviceInfoDto.put("model", "Samsung Note 5");
                        mobileDeviceInfoDto.put("brand", "Samsung");
                        paymentBody.put("mobileDeviceInfoDto", mobileDeviceInfoDto);
                        sdkObject.put("paymentBody", paymentBody);

                        sdkObject.put("thirdPartyClientId", "3395912_Sandbox Merchant Name - 3395912");
                        sdkObject.put("thirdPartyClientSecret", "72150036103395912");
                        sdkObject.put("merchantApiKey", "sandbox-hylhEPAc1hIInkhBPLgLqxPDfOzQ6xml");
                        sdkObject.put("merchantSecretKey", "sandbox-PM1mbfI0znqPhzcr46sNgNWQopBykOPj");
                        sdkObject.put("sdkType", "pwi");

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            // Send the created JSON Object to the sdk side to initialize the Sdk and the PWI payment;
                            myWebView.postWebMessage(new WebMessage(sdkObject.toString()), Uri.EMPTY);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                if(url.equals(callbackUrl)){
                    Intent intentToMain = new Intent(PaymentActivity.this,PaymentSuccessfulActivity.class);
                    startActivity(intentToMain);
                    finish();
                }

            }

        });
    }
}