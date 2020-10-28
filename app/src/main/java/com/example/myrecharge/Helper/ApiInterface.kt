package com.example.myrecharge.Helper

import com.example.myrecharge.Models.ModelRequestdetails
import com.google.gson.JsonArray
import com.google.gson.JsonObject
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

    @POST("loginprocess")
    fun getbalance(
        @Header("Membermsrno") Membermsrno: String?): Call<JsonObject>

    @POST("getoperatordetails")
    fun getoperatordetails(
        @Header("operatortype") operatortype: String?
    ): Call<JsonObject>

    @GET("apimanager/APIUSE.aspx")
    fun getUrl(
        @Query("UrlName") UrlName: String?): Call<JsonArray>


    @FormUrlEncoded
    @POST("getrechargereport")
    fun getrechargereport(
        @Header("Membermsrno") Membermsrno: String?,
        @Header("recordhistoryID") recordhistoryID: String?,
        @Field("Rechargetype") Rechargetype:String
    ): Call<JsonObject>
//http://recharge.codunite.com/Mobileapi.asmx/getrechargereport
    @FormUrlEncoded
    @POST("getrechargereport")
    fun getRechargeprcess(
        @Header("username") username: String?,
        @Header("password") password: String?,
        @Field("RechargeAmount") RechargeAmount:String,
        @Field("Opreator") Opreator:String,
        @Field("Number") Number:String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("getWalletreport")
    fun getHistory(
        @Header("Membermsrno") Membermsrno: String?,
        @Header("recordWalletID") recordhistoryID: String?,
        @Field(".") Rechargetype:String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("getbalance")
    fun getUserBalance(
        @Header("Membermsrno") Membermsrno: String?,
        @Field(".") Rechargetype:String
    ): Call<JsonObject>





    @FormUrlEncoded
    @POST("GetPlandetails")
    fun GetPlandetails(
        @Field("Operator") Rechargetype:String
    ): Call<ModelRequestdetails>

    @FormUrlEncoded
    @POST("billGetFatchbilleroperatordetails")
    fun get_billGetFatchbilleroperatordetails(
        @Header("username") username:String,
        @Header("password") password:String,
        @Field("Operator") Operator:String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("billGetFatchdetails")
    fun billGetFatchdetails(
        @Field("Operator") Operator:String,
        @Field("servicenum") servicenum:String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("PayFatchMemberDeatils")
    fun PayFatchMemberDeatils(
        @Header("MemberID") MemberID:String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("PayFundtransfertomember")
    fun PayFundtransfertomember(
        @Header("FromMember") FromMember:String,
        @Header("Tomember") Tomember:String,
        @Field("Amount") Amount:String
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("PayReportDeatils")
    fun PayReportDeatils(
        @Header("Membermsrno") Membermsrno:String,
        @Header("recordPaymentID") recordPaymentID:String,
        @Field(".") Amount:String
    ): Call<JsonObject>

    @POST("getAllbanknamedetails")
    fun getAllbanknamedetails(): Call<JsonObject>
}