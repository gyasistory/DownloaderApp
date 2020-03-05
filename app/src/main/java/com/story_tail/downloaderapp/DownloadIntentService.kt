package com.story_tail.downloaderapp

import android.app.IntentService
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.ResultReceiver
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL


const val EXTRA_URL = "com.gyasistory.downloaderapp.url"
const val EXTRA_RECEIVER = "com.gyasistory.downloaderapp.receiver"
const val EXTRA_PROGRESS = "com.gyasistory.downloaderapp.progress"
const val UPDATE_PROGRESS: Int = 123456
class DownloadIntentService : IntentService("DownloadIntentService") {



    override fun onHandleIntent(intent: Intent?) {
        val urlString = intent?.getStringExtra(EXTRA_URL)
        val receiver = intent?.getParcelableExtra(EXTRA_RECEIVER) as ResultReceiver
        val url = URL(urlString)
        val connection = url.openConnection()
        connection.connect()

        val fileLength = connection.contentLength
        val inputStream = BufferedInputStream(connection.getInputStream())
        val externalFiles =this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(externalFiles, "serviceDownload.mp4")
        file.createNewFile()
        val outputStream = file.outputStream()
        val data = ByteArray(1024)
        var total = 0
        var count = 0
        while (inputStream.read(data).also { count = it } != -1) {
            total += count

            val bundle = Bundle()
            bundle.putInt(EXTRA_PROGRESS, (total * 100 / fileLength))
            receiver.send(UPDATE_PROGRESS, bundle)
            outputStream.write(data, 0, count)
        }
        outputStream.flush()
        outputStream.close()
        inputStream.close()
        val finalBundle = Bundle()
        finalBundle.putInt(EXTRA_PROGRESS, 100)
        receiver.send(UPDATE_PROGRESS, finalBundle)

        val notification = NotificationCompat.Builder(this, "download_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Your DownloadIntent is Complete")
            .setContentText("This used an Intent Service to Download the MP4 file")
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(this).apply {
            notify(321, notification)
        }
    }



}
