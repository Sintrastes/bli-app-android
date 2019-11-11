package com.example.bedelllibraryinterface

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

class SettingsActivity : AppCompatActivity() {

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

        if (id == R.id.about_action) startActivity(Intent(this@SettingsActivity, RefactorActivity::class.java))
        if (id == R.id.server_config_action) startActivity(Intent(this@SettingsActivity, ConfigActivity::class.java))
        if (id == R.id.settings_action) startActivity(Intent(this@SettingsActivity, SettingsActivity::class.java))

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val my_toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(my_toolbar)
    }
}
