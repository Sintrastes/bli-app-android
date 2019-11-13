package org.bedelibry.mobile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


class ScrollView : AppCompatActivity() {

    private var myArray: ArrayList<String> = arrayListOf()

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

        if (id == R.id.about_action) startActivity(Intent(this@ScrollView, AboutActivity::class.java))
        if (id == R.id.server_config_action) startActivity(Intent(this@ScrollView, ConfigActivity::class.java))
        if (id == R.id.settings_action) startActivity(Intent(this@ScrollView, SettingsActivity::class.java))

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_view)


        val my_toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(my_toolbar)

        val listView: ListView = findViewById(R.id.list_view)

        myArray = getIntent().getStringArrayListExtra("list")

        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, myArray)

        listView.setOnItemLongClickListener(object : OnItemLongClickListener {
            override fun onItemLongClick(
                arg0: AdapterView<*>, arg1: View,
                pos: Int, id: Long
            ): Boolean {

                Toast.makeText(this@ScrollView, "Copied query result to clipboard.", Toast.LENGTH_SHORT).show()

                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("", myArray.get(pos))
                clipboard.setPrimaryClip(clip)
                return true
            }
        })
    }
}
