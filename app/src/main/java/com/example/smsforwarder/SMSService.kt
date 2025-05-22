package com.example.smsforwarder

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * واسط Retrofit جهت مدیریت درخواست ارسال پیامک به سرور.
 */
interface SMSService {
    @POST("api/receive_sms")
    suspend fun sendSMS(@Body payload: SmsPayload): Response<Unit>
}
