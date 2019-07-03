package com.example.bedelllibraryinterface

import android.content.Context
import android.content.ContextWrapper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.security.AccessController.getContext

class config : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        var dir  = applicationContext.filesDir.absolutePath
        if (!File(dir+"/config.txt").exists()) {
            val isNewFileCreated: Boolean = File(dir + "/config.txt").createNewFile()
        }
        var configDataFromFile = ArrayList<String>()
        try {
            File(dir + "/config.txt").forEachLine { configDataFromFile.add(it) }
            findViewById<EditText>(R.id.url_field).setText(configDataFromFile[0], TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.port_field).setText(configDataFromFile[1], TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.username_field).setText(configDataFromFile[2], TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.password_field).setText(configDataFromFile[3], TextView.BufferType.EDITABLE)
        } catch (e: Exception) {
            Toast.makeText(this@config, "Ayyyyyy, something bad happened. \n"+e.toString(), Toast.LENGTH_SHORT).show()
        }

        // get reference to button
        val btn_to_home = findViewById(R.id.button3) as Button
        // set on-click listener
        btn_to_home.setOnClickListener {
        // your code to perform when the user clicks on the button

            var serverUrlConfig = findViewById<EditText>(R.id.url_field).text.toString()
            var portNumberConfig = findViewById<EditText>(R.id.port_field).text.toString()
            var usernameConfig = findViewById<EditText>(R.id.username_field).text.toString()
            var passwordConfig = findViewById<EditText>(R.id.password_field).text.toString()

            File(dir+"/config.txt").writeText(serverUrlConfig+"\n"+portNumberConfig+"\n"+usernameConfig+"\n"+passwordConfig+"\n")
            File(dir+"/config.txt").forEachLine { Log.i("LOGGING",it) }

            startActivity(Intent(this@config, MainActivity::class.java))
        }

        // get reference to button
        val btn_to_connect = findViewById(R.id.button4) as Button
        // set on-click listener
        btn_to_connect.setOnClickListener {
            // your code to perform when the user clicks on the button

            var serverUrlConfig = findViewById<EditText>(R.id.url_field).text.toString()
            var portNumberConfig = findViewById<EditText>(R.id.port_field).text.toString()
            var usernameConfig = findViewById<EditText>(R.id.username_field).text.toString()
            var passwordConfig = findViewById<EditText>(R.id.password_field).text.toString()

            // Make
            try {
                GlobalScope.async {
                    val connection =
                        URL(
                            "http://" + serverUrlConfig + ":"
                                    + portNumberConfig + "/query-api?pl_server_start=true" + "&user=" + usernameConfig + "&password="
                                    + passwordConfig
                        ).openConnection() as HttpURLConnection
                    val data = connection.inputStream.bufferedReader().readText()
                }
                findViewById<TextView>(R.id.connectionStatus).text = "Connected"
                Toast.makeText(this@config, "You connected to "+findViewById<EditText>(R.id.url_field).text, Toast.LENGTH_SHORT).show()
            } catch (e : Exception){
                Toast.makeText(this@config, "Ayyyyyy, something bad happened. Error connecting to server. \n"+e.toString(), Toast.LENGTH_SHORT).show()
            }

        }

        // get reference to button
        val btn_to_disconnect = findViewById(R.id.button5) as Button
        // set on-click listener
        btn_to_disconnect.setOnClickListener {
            // your code to perform when the user clicks on the button
            findViewById<TextView>(R.id.connectionStatus).text = "Disconnected"
            Toast.makeText(this@config, "You clicked disconnect.", Toast.LENGTH_SHORT).show()
        }

    }
}
