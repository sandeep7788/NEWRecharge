package com.example.myrecharge.Helper

import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
//https://paymyrecharge.in/APIMANAGER/oparetor.aspx?Servertype=2

//https://therippco.com/api/wallet.ashx?f=t1&loginid=5002081557&
// FromDate=11/11/1111&ToDate=&ttype=&start=10&length=20

    @POST("loginprocess")
    fun getLogin(
        @Header("username") username: String?,
        @Header("Password") Password: String?
    ): Call<JsonObject>

    fun getbalance(
        @Header("Membermsrno") Membermsrno: String?): Call<JsonObject>

    fun getoperatordetails(
        @Header("operatortype") username: String?
    ): Call<JsonObject>

    @POST("common.ashx?")
        @FormUrlEncoded
        fun modelTransaction(
            @Field("f") f: String?,
            @Field("start") start: String?,
            @Field("length") length: String?,
            @Field("sponsor") sponsor: String?,
            @Field("fdate") fdate: String?,
            @Field("tdate") tdate: String?,
            @Field("itemid") itemid: String?,
            @Field("status") status: String?
        ): Call<JSONObject?>?

    @POST("wallet.ashx?")
    @FormUrlEncoded
    fun passbook_getdaat(
        @Field("f") f: String?,
        @Field("loginid") loginid: String?,
        @Field("FromDate") FromDate: String?,
        @Field("ToDate") ToDate: String?,
        @Field("ttype") ttype: String?,
        @Field("start") start: String?,
        @Field("length") length: String?
    ): Call<JSONObject?>?

    @POST("common.ashx?")
    @FormUrlEncoded
    fun get_List(
        @Field("f") f: String?,
        @Field("loginid") loginid: String?,
        @Field("password") password: String?,
        @Field("ip") ip: String?
    ): Call<JSONObject?>?
//https://paymyrecharge.in/APIMANAGER/oparetor.aspx?Servertype=1





    @GET("common.ashx?")
    fun getUser(
        @Query("f") f: String?,
        @Query("loginid") loginid: String?
    ): Call<JSONObject?>?


    @GET("a")
    fun pay_amount(
        @Query("f") f: String?,
        @Query("fromloginid") fromloginid: String?,
        @Query("tologinid") tologinid: String?,
        @Query("amount") amount: String?,
        @Query("pass") pass: String?,
        @Query("ip") ip: String?
    ): Call<JSONObject?>?

    @GET("recharge.ashx?")
    fun get_Recharge_services(
        @Query("f") f: String?,
        @Query("loginid") loginid: String?,
        @Query("service_type") service_type: String?,
        @Query("operator_code") operator_code: String?,
        @Query("operator_name") operator_name: String?,
        @Query("account_number") account_number: String?,
        @Query("amount") amount: String?
    ): Call<JSONObject?>?
}