package org.bedelibry.mobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import android.widget.TextView

class ConfigActivity : AppCompatActivity() {

    private fun getServerUrlField(): String { return findViewById<EditText>(R.id.url_field).text.toString() }
    private fun getPortNumberField(): String { return findViewById<EditText>(R.id.port_field).text.toString() }
    private fun getUsernameField(): String { return findViewById<EditText>(R.id.username_field).text.toString() }
    private fun getPasswordField(): String { return findViewById<EditText>(R.id.password_field).text.toString() }

    private fun popup(text: String) { Toast.makeText(this, text, Toast.LENGTH_SHORT).show() }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.about_action) startActivity(Intent(this@ConfigActivity, AboutActivity::class.java))
        if (id == R.id.server_config_action) startActivity(Intent(this@ConfigActivity, ConfigActivity::class.java))
        if (id == R.id.settings_action) startActivity(Intent(this@ConfigActivity, SettingsActivity::class.java))

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val my_toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(my_toolbar)

        var dir  = applicationContext.filesDir.absolutePath

        val appConfig: AppConfig = (application as MyApplication).appConfig!!

        var serverField = findViewById<EditText>(R.id.url_field).text.toString()
        var portField = findViewById<EditText>(R.id.port_field).text.toString()
        var usernameField = findViewById<EditText>(R.id.username_field).text.toString()
        var passwordField = findViewById<EditText>(R.id.password_field).text.toString()

        if (appConfig.applicationConfigured()) {
            findViewById<EditText>(R.id.url_field).setText(appConfig.getServerAddress(), TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.port_field).setText(appConfig.getPortNumber()?.toString(), TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.username_field).setText(appConfig.getUsername(), TextView.BufferType.EDITABLE)
            findViewById<EditText>(R.id.password_field).setText(appConfig.getPassword(), TextView.BufferType.EDITABLE)
        }

        // get reference to button
        val btn_to_home = findViewById(R.id.doneButton) as Button
        // set on-click listener
        btn_to_home.setOnClickListener {
        // your code to perform when the user clicks on the button

            serverField = findViewById<EditText>(R.id.url_field).text.toString()
            portField = findViewById<EditText>(R.id.port_field).text.toString()
            usernameField = findViewById<EditText>(R.id.username_field).text.toString()
            passwordField = findViewById<EditText>(R.id.password_field).text.toString()

            val fields = appConfig.setConfig(serverField, portField, usernameField, passwordField)

            fields.forEach {
                popup(it.toString())
            }

            startActivity(Intent(this@ConfigActivity, MainActivity::class.java))
        }

        // get reference to button
        val btn_to_connect = findViewById(R.id.connectButton) as Button
        // set on-click listener
        btn_to_connect.setOnClickListener {
            // Make a simple request to determine if the server configuration
            // is valid or not.
            /*
            try {
                GlobalScope.async {
                    val connection =
                        URL(
                            "http://${getServerUrlField()}:${getPortNumberField()}/query-api?pl_server_start=true&user=${getUsernameField()}&password=${getPasswordField()}"
                        ).openConnection() as HttpURLConnection
                    val data = connection.inputStream.bufferedReader().readText()
                }
                findViewById<TextView>(R.id.connectionStatus).text = "Connected"
                popup("You connected to ${findViewById<EditText>(R.id.url_field).text}")
            } catch (e : Exception) {
                popup("Error: \n$e")
            }
            */
        }

        val applyButton = findViewById(R.id.applyButton) as Button
        applyButton.setOnClickListener {

            serverField = findViewById<EditText>(R.id.url_field).text.toString()
            portField = findViewById<EditText>(R.id.port_field).text.toString()
            usernameField = findViewById<EditText>(R.id.username_field).text.toString()
            passwordField = findViewById<EditText>(R.id.password_field).text.toString()

            val fields = appConfig.setConfig(serverField, portField, usernameField, passwordField)

            fields.forEach {
                // Note: This is not very clean. We need a better "show" method here.
                popup(it.toString())
            }
        }

        // get reference to button
        val disconnectButton = findViewById(R.id.disconnectButton) as Button
        // set on-click listener
        disconnectButton.setOnClickListener {
            // your code to perform when the user clicks on the button
            findViewById<TextView>(R.id.connectionStatus).text = "Disconnected"
            Toast.makeText(this@ConfigActivity, "You clicked disconnect.", Toast.LENGTH_SHORT).show()
        }

    }
}
