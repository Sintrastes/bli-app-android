package com.example.bedelllibraryinterface

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.AsyncTask
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.EditText
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlinx.coroutines.GlobalScope as GlobalScope1

class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dir  = applicationContext.filesDir.absolutePath
        // get reference to button
        val btn_click_me = findViewById(R.id.button) as Button
        // set on-click listener
        btn_click_me.setOnClickListener {
            // query button press
            // startActivity(Intent(this@MainActivity, ScrollingActivity::class.java))
            //Toast.makeText(this@MainActivity, "You made the query: \""
            //        + findViewById<EditText>(R.id.main_text_field).text + "\"",
            //    Toast.LENGTH_SHORT).show()

            // Note: This doesn't work. The data doesn't actually persist when I exit the config window.
            // I need to make it so when I exit the config window, it saves the data to a file.
            var configDataFromFile = ArrayList<String>()
            File(dir + "/config.txt").forEachLine { configDataFromFile.add(it) }
            val serverUrlConfig  = configDataFromFile[0]
            val portNumberConfig = configDataFromFile[1]
            val usernameConfig   = configDataFromFile[2]
            val passwordConfig   = configDataFromFile[3]

            try {
                val deferredResult: Deferred<String> = GlobalScope1.async {
                        Log.i(" ","test1")
                        // val connection = URL("http://google.com").openConnection() as HttpURLConnection
                        val textBox = findViewById<EditText>(R.id.main_text_field).text.toString()
                        val string = URLEncoder.encode(textBox,"UTF-8")
                        val connection =
                            URL("http://"+serverUrlConfig+":"
                                    + portNumberConfig + "/query-api?pl_server_query="
                                    + string + "&user=" + usernameConfig + "&password="
                                    + passwordConfig).openConnection() as HttpURLConnection
                         Log.i(" ","test2")
                        val data = connection.inputStream.bufferedReader().readText()
                        data
                }
                runBlocking {
                    Toast.makeText(this@MainActivity, deferredResult.await(), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "There was an error for some reason: \n\n" + e.toString(), Toast.LENGTH_SHORT).show()
            }
            // Reset the textbox after querying
            var textboxData =findViewById<EditText>(R.id.main_text_field).text
            textboxData.delete(0,textboxData.toString().length)
        }

        val btn_to_config = findViewById(R.id.button2) as Button
        btn_to_config.setOnClickListener {
            startActivity(Intent(this@MainActivity, config::class.java))
        }

        // URL("https://google.com").readText() -- get data from HTTP request
    }
}
