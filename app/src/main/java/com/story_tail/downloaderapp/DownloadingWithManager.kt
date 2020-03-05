package com.story_tail.downloaderapp

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

fun Context.startDownloadManager(url: String) {
    val request = DownloadManager.Request(Uri.parse(url))
    request.setDescription("Downloading with DownloadManager")
    request.setTitle("Gyasi's Download Manager")

    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "DownloadManager.mp4")

    val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    manager.enqueue(request)
}
