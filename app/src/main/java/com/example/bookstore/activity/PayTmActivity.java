package com.example.bookstore.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore.api.PaytmProcess;
import com.example.bookstore.api.RetrofitAdapter;
import com.example.bookstore.api.model.Checksum;
import com.example.bookstore.api.model.Paytm;
import com.example.bookstore.api.service.PayTmService;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;

public class PayTmActivity extends AppCompatActivity {

    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        price = i.getStringExtra("total");

        processPaytm();

        /*PayTmService payTmService = new PayTmService();

       Paytm paytm =  payTmService.processPaytm(price);
       String checksumhash = paytm.getCheckSum();
       processToPay(checksumhash,paytm);
        System.out.println("checksum======"+checksumhash);*/
    }

   private void processPaytm() {

        String custID = generateString();
        String orderID = generateString();
        String callBackurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderID;

        final Paytm paytm = new Paytm("CbArUD06457943921127","WAP",price,"WEBSTAGING",callBackurl,"Retail",orderID,custID);

        PaytmProcess api = new RetrofitAdapter().retroFit();
        Call<Checksum> call = api.getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId()
                , paytm.getChannelId(), paytm.getTxnAmount(), paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId());

        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(@NotNull Call<Checksum> call, @NotNull retrofit2.Response<Checksum> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        processToPay(response.body().getChecksumHash(), paytm);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Checksum> call, Throwable t) {

            }
        });

    }


    public void processToPay(String checksumHash, Paytm paytm) {

        PaytmPGService Service = PaytmPGService.getStagingService(null);

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", paytm.getmId());
// Key in your staging and production MID available in your dashboard
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
// This is the staging value. Production value is available in your dashboard
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());
// This is the staging value. Production value is available in your dashboard
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);

        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {
                Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();
            }

            public void onTransactionResponse(Bundle inResponse) {
                Toast.makeText(PayTmActivity.this, inResponse.toString(), Toast.LENGTH_SHORT).show();

            }

            public void networkNotAvailable() {
                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
            }

            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage, Toast.LENGTH_LONG).show();
            }

            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage, Toast.LENGTH_LONG).show();
            }

            public void onBackPressedCancelTransaction() {
                Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
            }

            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
            }
        });

        finish();

    }

    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }


}
