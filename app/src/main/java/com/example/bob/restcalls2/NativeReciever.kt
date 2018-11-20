package com.example.bob.restcalls2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject

class NativeReciever: BroadcastReceiver() {

    private val TAG = "NativeReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive:  ${intent?.getIntExtra(KEY.CODE, 0)}\n ${intent?.getStringExtra(KEY.MESSAGE)}")
        val response = intent?.getStringExtra(KEY.RESPONSE)
        try {
            val reader = JSONObject(response)
            val widget = reader.getJSONObject("widget")
            val text = widget.getJSONObject("text")
            val size = text.getString("style")
            Toast.makeText(context, size, Toast.LENGTH_LONG).show()
        } catch (e: JSONException){
            e.printStackTrace()
        }
    }
}