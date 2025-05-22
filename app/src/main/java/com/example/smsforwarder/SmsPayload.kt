package com.example.smsforwarder

/**
 * مدل داده‌ای برای ارسال محتویات پیامک به سرور.
 */
data class SmsPayload(
    val sender: String,
    val message: String
)
