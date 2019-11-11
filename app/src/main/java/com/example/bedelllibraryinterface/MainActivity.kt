package com.example.bedelllibraryinterface

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.AsyncTask
import android.widget.Button
import android.widget.Toast
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.support.v7.widget.Toolbar
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import kotlinx.coroutines.Deferred
import java.io.File
import android.view.Menu
import android.view.MenuItem
import io.github.rybalkinsd.kohttp.dsl.async.httpGetAsync
import okhttp3.Response
import kotlin.text.*
import com.example.bedelllibraryinterface.MyApplication.*
import android.content.DialogInterface

class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}

open class MyActivity : AppCompatActivity() {
}

class MainActivity : MyActivity() {

    private fun makeQuery(dir: String, mainTextField: EditText) {
        val context = getApplicationContext();
        val highlightColor = ContextCompat.getColor(context, R.color.highlightColor)
        val prompt = SpannableString("?- ")
        prompt.setSpan(ForegroundColorSpan(highlightColor), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Some constants common to my application

        val queryString = mainTextField.getText().substring(3)

        val appConfig: AppConfig = (application as MyApplication).appConfig!!

        // Just messing around with alert dialogs.
        var dialog = AlertDialog.Builder(this@MainActivity).create()
        dialog.setTitle(Requests.typeOfRequest(queryString).show())
        dialog.setMessage(queryString)

        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog, which -> dialog.dismiss() }
        dialog.show()

        Toast.makeText(this@MainActivity, mainTextField.getText().substring(3), Toast.LENGTH_SHORT).show()

        // Get the configuration data from a file.
        // Todo: Need to check for exceptions here.
        val configDataFromFile = ArrayList<String>()
        val serverUrl = appConfig.getServerAddress()
        val portNumber = appConfig.getPortNumber()
        val username = appConfig.getUsername()
        val password = appConfig.getPassword()
        val configFile = File("$dir/ConfigActivity.txt")

        if (appConfig.applicationConfigured()) {
                val response: Deferred<Response> = httpGetAsync {
                    host = "https://$serverUrl:$portNumber"
                    path = "/"
                    param {
                        "query" to String
                    }
                }
        } else {
            Toast.makeText(this@MainActivity, "Cannot connect to server. Application has not been configured.", Toast.LENGTH_SHORT).show()
        }

        /*
        try {
            val deferredResult: Deferred<String> = GlobalScope1.async {
                // val connection = URL("http://google.com").openConnection() as HttpURLConnection
                val textBox = mainTextField.text.toString()
                val string = URLEncoder.encode(textBox,"UTF-8")
                // TODO: Change this to use the new API
                val connection =
                    URL("http://"+serverUrlConfig+":"
                            + portNumberConfig + "/query-api?pl_server_query="
                            + string + "&user=" + usernameConfig + "&password="
                            + passwordConfig).openConnection() as HttpURLConnection
                val data = connection.inputStream.bufferedReader().readText()
                data
            }
            runBlocking {
                Toast.makeText(this@MainActivity, deferredResult.await(), Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Error: \n\n$e", Toast.LENGTH_SHORT).show()
        }
        */

        // Reset the textbox after querying
        mainTextField.setText(prompt)
        mainTextField.setSelection(mainTextField.getText().length)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()

        if (id == R.id.about_action) startActivity(Intent(this@MainActivity, RefactorActivity::class.java))
        if (id == R.id.server_config_action) startActivity(Intent(this@MainActivity, ConfigActivity::class.java))
        if (id == R.id.settings_action) startActivity(Intent(this@MainActivity, SettingsActivity::class.java))

        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val context = getApplicationContext();
        val dir = applicationContext.filesDir.absolutePath

        // Format text used in EditText so we can do syntax highlighting
        val prompt = SpannableString("?- ")
        val highlightColor = ContextCompat.getColor(context, R.color.highlightColor)
        prompt.setSpan(ForegroundColorSpan(highlightColor), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        setContentView(R.layout.activity_main)
        val mainTextField: EditText = findViewById(R.id.main_text_field)
        val my_toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(my_toolbar)

        // mainTextField.setText(prompt)
        mainTextField.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
        mainTextField.setText(prompt)
        // Disable autocorrect by default
        mainTextField.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)

        // Request type of the current input
        var requestType: Requests.Request? = null

        // Handle text change events for our main text field.
        mainTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                // Update the request type when the text is changed.
                requestType = Requests.typeOfRequest(s.toString())
            }
        })

        // get reference to button
        val btn_click_me = findViewById(R.id.button) as Button

        // Set the listener for pressing "enter" in the main text field.
        mainTextField.setOnKeyListener { v, keyCode, event ->
            // If the event is a key-down event on the "enter" button
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                if(mainTextField.getText().last() != '.') {
                    // Perform action on key press
                    makeQuery(dir, mainTextField)
                    true
                } else {
                    mainTextField.text.append("\n")
                }
            }
            false
        }

        btn_click_me.setOnClickListener {
            // query button press

            makeQuery(dir, mainTextField)
        }

        val btn_to_config = findViewById(R.id.button2) as Button
        btn_to_config.setOnClickListener {
            startActivity(Intent(this@MainActivity, ConfigActivity::class.java))
        }

        // URL("https://google.com").readText() -- get data from HTTP request
    }
}
