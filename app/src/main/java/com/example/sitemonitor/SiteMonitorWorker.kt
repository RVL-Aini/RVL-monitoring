package com.example.sitemonitor

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.net.HttpURLConnection
import java.net.URL

class SiteMonitorWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val sites = listOf("https://google.com", "https://example.com")
        
        sites.forEach { site ->
            try {
                val url = URL(site)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.connect()

                if (connection.responseCode != 200) {
                    (applicationContext as? MainActivity)?.sendNotification(
                        applicationContext.getString(R.string.site_unavailable),
                        site
                    )
                }
            } catch (e: Exception) {
                (applicationContext as? MainActivity)?.sendNotification(
                    applicationContext.getString(R.string.connection_error),
                    site
                )
            }
        }
        Result.success()
    }
}
