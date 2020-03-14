package com.example.bookstore.api.service;

import com.example.bookstore.activity.PayTmActivity;
import com.example.bookstore.api.PaytmProcess;
import com.example.bookstore.api.RetrofitAdapter;
import com.example.bookstore.api.model.Checksum;
import com.example.bookstore.api.model.Paytm;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;

public class PayTmService {

    public void processPaytm(String price) {

        String custID = generateString();
        String orderID = generateString();
        String callBackurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderID;
        final Paytm paytm = new Paytm("CbArUD06457943921127","WAP",price,"WEBSTAGING",callBackurl,"Retail",orderID,custID);

        PaytmProcess.JsonPlaceHolderApi api = new RetrofitAdapter().retroFit();
        Call<Checksum> call = api.getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId()
                , paytm.getChannelId(), paytm.getTxnAmount(), paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId());

        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(@NotNull Call<Checksum> call, @NotNull retrofit2.Response<Checksum> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        PayTmActivity payTmActivity = new PayTmActivity();
                        payTmActivity.processToPay(response.body().getChecksumHash(), paytm);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Checksum> call, Throwable t) {

            }
        });

    }

    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

}
