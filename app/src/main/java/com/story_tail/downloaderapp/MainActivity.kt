package com.story_tail.downloaderapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifyPermissions()
        findViewById<Button>(R.id.serviceButton).setOnClickListener {
            val intent = Intent(this, DownloadIntentService::class.java)
            intent.putExtra(EXTRA_URL, mainURl)
            intent.putExtra(EXTRA_RECEIVER, object : ResultReceiver(Handler()){
                @SuppressLint("SetTextI18n")
                override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                    super.onReceiveResult(resultCode, resultData)

                    if (resultCode == UPDATE_PROGRESS) {
                        val progress = resultData?.getInt(EXTRA_PROGRESS)
                        val textView = findViewById<TextView>(R.id.textView)
                        if (progress == 100) {
                            textView.text = "Your download is complete"
                            val videoView = findViewById<VideoView>(R.id.showServiceVideo)
                            videoView.visibility = View.VISIBLE
                            val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "serviceDownload.mp4")
                            videoView.setVideoURI(Uri.parse(file.absolutePath))
                            videoView.start()
                            return
                        }
                        textView.text = "Your download is $progress % "
                    }
                }
            })
            startService(intent)
        }

        findViewById<Button>(R.id.managerButton).setOnClickListener {
            startDownloadManager(mainURl)
        }
    }

    private fun verifyPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 7894)
        }
    }
}

const val mainURl = "https://storage.googleapis.com/coverr-main/mp4%2FWords.mp4"