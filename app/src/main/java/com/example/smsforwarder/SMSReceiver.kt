package com.example.smsforwarder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

/**
 * دریافت پیامک‌های دریافتی و ثبت وظیفه ارسال آن‌ها با استفاده از WorkManager.
 */
class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED" && context != null) {
            val bundle = intent.extras
            try {
                if (bundle != null) {
                    val pdus = bundle["pdus"] as Array<*>
                    val messages = pdus.map { pdu ->
                        SmsMessage.createFromPdu(pdu as ByteArray)
                    }

                    messages.forEach { sms ->
                        val sender = sms.displayOriginatingAddress
                        val messageBody = sms.displayMessageBody
                        Log.d("SMSReceiver", "دریافت پیامک از: $sender, متن: $messageBody")

                        // آماده‌سازی داده‌های مورد نیاز برای Worker
                        val inputData = Data.Builder()
                            .putString("sender", sender)
                            .putString("message", messageBody)
                            .build()

                        // ساخت Job با WorkManager برای ارسال پیام در پس‌زمینه
                        val workRequest = OneTimeWorkRequestBuilder<SMSSendWorker>()
                            .setInputData(inputData)
                            .build()

                        WorkManager.getInstance(context).enqueue(workRequest)
                    }
                }
            } catch (e: Exception) {
                Log.e("SMSReceiver", "خطا در پردازش پیامک: ${e.message}")
            }
        }
    }
}
