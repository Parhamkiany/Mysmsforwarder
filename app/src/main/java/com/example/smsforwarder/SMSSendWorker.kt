package com.example.smsforwarder

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * Worker مسئول ارسال پیامک به سرور با استفاده از Retrofit در فضای پس‌زمینه.
 */
class SMSSendWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val sender = inputData.getString("sender") ?: return Result.failure()
        val message = inputData.getString("message") ?: return Result.failure()

        return try {
            val service = RetrofitClient.instance.create(SMSService::class.java)
            val payload = SmsPayload(sender, message)
            val response = service.sendSMS(payload)
            if (response.isSuccessful) {
                Log.d("SMSSendWorker", "پیام با موفقیت ارسال شد (کد: ${response.code()})")
                Result.success()
            } else {
                Log.e("SMSSendWorker", "ارسال پیام با خطا مواجه شد (کد: ${response.code()})")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("SMSSendWorker", "خطا در ارسال پیام: ${e.message}")
            Result.retry()
        }
    }
}
