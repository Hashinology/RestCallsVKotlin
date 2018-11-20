package com.example.bob.restcalls2

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.bob.restcalls2.KEY.CALLURL
import com.example.bob.restcalls2.KEY.CODE
import com.example.bob.restcalls2.KEY.MESSAGE
import com.example.bob.restcalls2.KEY.RESPONSE
import java.io.BufferedInputStream
import java.io.IOException
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class MyIntentService: IntentService("IntentService"){


    private val TAG = "MyIntentService"

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent")

        val url = intent?.getStringExtra(CALLURL)
        val newIntent = Intent()
        var scanner : Scanner? = null

        try {
            val connectionURL = URL(CALLURL)
            val httpURLConnection = connectionURL.openConnection() as HttpURLConnection
            val inputStream = BufferedInputStream(httpURLConnection.inputStream)
            scanner = Scanner(inputStream)
            val sb = StringBuilder()
            while (scanner.hasNext()){
                sb.append(scanner.next())
            }
            val statusCode = httpURLConnection.responseCode
            val statusMessage = httpURLConnection.responseMessage
            newIntent.action = NATIVE_RECIEVER_ACTION
            newIntent.putExtra(CODE, statusCode)
            newIntent.putExtra(MESSAGE, statusMessage)
            newIntent.putExtra(RESPONSE, sb.toString())
            sendBroadcast(newIntent)

        } catch (e: MalformedURLException){
            e.printStackTrace()

        } catch (e: IOException){
            e.printStackTrace()
        } finally {
            scanner?.close()
        }
    }
}