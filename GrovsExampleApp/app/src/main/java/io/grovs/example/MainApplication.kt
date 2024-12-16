package io.grovs.example

import android.app.Application
import io.grovs.Grovs
import io.grovs.model.LogLevel

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO: Replace with your own API Key
        val API_KEY = "grovst_06e36086dad3e934289560e3ca59527282030868f8c844629516c6e6c67bbf1f"
        Grovs.configure(this, API_KEY, useTestEnvironment = true)

        //Optionally, you can adjust the debug level for logging:
        Grovs.setDebug(LogLevel.INFO)

        Grovs.identifier = "1234"
        Grovs.attributes = mapOf("param1" to "value1", "param2" to 123, "param3" to true)
    }

}