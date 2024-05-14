package com.rekoj134.moodandhabittracker.util

import android.os.Environment
import com.rekoj134.moodandhabittracker.db.MyDatabase.Companion.BACKUP_DIRECTORY
import java.io.File

fun getPublicSharedPath(): File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)

fun getBackUpDirectory() : File {
    val rootFolder = File(getPublicSharedPath(), BACKUP_DIRECTORY)
    if (!rootFolder.exists()) {
        rootFolder.mkdir()
    }
    return rootFolder
}
