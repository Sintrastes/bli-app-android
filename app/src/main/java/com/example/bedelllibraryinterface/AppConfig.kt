package com.example.bedelllibraryinterface

import android.util.Log
import android.webkit.URLUtil
import java.io.File
import java.io.IOException

class AppConfig(dir: String) {
    private var serverAddress: String? = null
    private var serverPort: Int? = null
    private var username: String? = null
    private var password: String? = null
    private var fontSize: Int? = null
    private var configFile = File("$dir/config.txt")

    // Enum to represent the different fields that can be configured
    // in the server configuration page
    sealed class Field {
        object ServerPort: Field()
        object ServerURL: Field()
        object Username: Field()
        object Passsword: Field()
    }

    init {
        Log.i("dir:",dir)
        if (!configFile.exists()) {
            if(!configFile.createNewFile()) {
                // Todo: Throw a more specific exception here.
                throw(IOException())
            }
        } else {
            val configDataFromFile = ArrayList<String>()
            configFile.forEachLine { configDataFromFile.add(it) }
            serverAddress = configDataFromFile[0]
            serverPort = configDataFromFile[1].toIntOrNull()
            username = configDataFromFile[2]
            password = configDataFromFile[3]
        }
    }

    fun applicationConfigured(): Boolean {
        return serverAddress != null
                && serverPort != null
                && username != null
                && password != null
    }

    fun getServerAddress(): String? {
        return serverAddress
    }

    fun getPortNumber(): Int? {
        return serverPort
    }

    fun getUsername(): String? {
        return username
    }

    fun getPassword(): String? {
        return password
    }

    private fun updateConfig(): Boolean {
        configFile.writeText(serverAddress+"\n"+serverPort+"\n"+username+"\n"+password+"\n")
        Log.i("Updating config", serverAddress+"\n"+serverPort+"\n"+username+"\n"+password+"\n")
        return true
    }

    private fun setServerAddress(serverAddressField: String): Boolean {
        return if(URLUtil.isValidUrl(serverAddressField)) {
            serverAddress = serverAddressField
            true
        } else {
            false
        }
    }

    private fun setServerPort(serverPortField: String): Boolean {
        return if (serverPortField.toIntOrNull()?.let { it in 0..65535 } == true) {
            serverPort = serverPortField.toIntOrNull()
            true
        } else {
            false
        }
    }

    private fun setUsername(usernameField: String): Boolean {
        return if (usernameField == "") {
            username = null
            false
        } else {
            username = usernameField
            true
        }
    }

    private fun setPassword(passwordField: String): Boolean {
        return if (passwordField == "") {
            password = null
            return false
        } else {
            password = passwordField
            return true
        }
    }

    fun setConfig(serverAddressField: String, serverPortField: String, usernameField: String, passwordField: String): List<Field> {
        var list = mutableListOf<Field>()

        if (!setServerAddress(serverAddressField)) {
            list.add(Field.ServerURL)
        }
        if (!setServerPort(serverPortField)) {
            list.add(Field.ServerURL)
        }
        if (!setUsername(usernameField)) {
            list.add(Field.Username)
        }
        if (!setPassword(passwordField)) {
            list.add(Field.Passsword)
        }

        updateConfig()

        return list
    }

}