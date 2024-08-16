package com.importpark.office

import android.content.ContentResolver
import android.database.Cursor
import android.provider.CallLog

fun getCallLogs(contentResolver: ContentResolver): List<String> {
    val callLogList = mutableListOf<String>()

    val cursor: Cursor? = contentResolver.query(
        CallLog.Calls.CONTENT_URI,
        null,
        null,
        null,
        CallLog.Calls.DATE + " DESC"
    )

    cursor?.use {
        val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
        val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
        val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
        val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

        while (it.moveToNext()) {
            val number = it.getString(numberIndex)
            val type = it.getString(typeIndex)
            val date = it.getString(dateIndex)
            val duration = it.getString(durationIndex)

            val callLog = "Number: $number, Type: $type, Date: $date, Duration: $duration"
            callLogList.add(callLog)
        }
    }

    return callLogList
}
