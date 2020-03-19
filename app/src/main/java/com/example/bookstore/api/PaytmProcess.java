package com.example.bookstore.api;

import com.example.bookstore.api.model.Checksum;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PaytmProcess {

    @FormUrlEncoded
    @POST("Paytm_App_Checksum_Kit_PHP-master/generateChecksum.php")
    Call<Checksum> getChecksum(@Field(value = "MID", encoded = true) String mId,
                               @Field(value = "ORDER_ID", encoded = true) String orderId,
                               @Field(value = "CUST_ID", encoded = true) String custId,
                               @Field(value = "CHANNEL_ID", encoded = true) String channelId,
                               @Field(value = "TXN_AMOUNT", encoded = true) String txnAmount,
                               @Field(value = "WEBSITE", encoded = true) String website,
                               @Field(value = "CALLBACK_URL", encoded = true) String callbackUrl,
                               @Field(value = "INDUSTRY_TYPE_ID", encoded = true) String industryTypeId);
}
