package com.hanmajid.android.seed.util

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.storage.StorageManager
import androidx.core.content.getSystemService
import java.io.File
import java.io.FileNotFoundException

object InternalStorageUtil {

    fun isStorageAvailable(context: Context, neededBytes: Long): Boolean {
        val storageManager = context.getSystemService<StorageManager>()!!
        val appSpecificInternalDirUuid = storageManager.getUuidForPath(context.filesDir)
        val availableBytes = storageManager.getAllocatableBytes(appSpecificInternalDirUuid)

        return availableBytes >= neededBytes
    }

    fun allocateStorage(context: Context, neededBytes: Long) {
        val storageManager = context.getSystemService<StorageManager>()!!
        val appSpecificInternalDirUuid = storageManager.getUuidForPath(context.filesDir)
        storageManager.allocateBytes(
            appSpecificInternalDirUuid,
            neededBytes
        )
    }

    fun getStorageInformation(context: Context): StorageInformation {
        val storageManager = context.getSystemService<StorageManager>()!!
        val storageStatsManager = context.getSystemService<StorageStatsManager>()!!
        val appSpecificInternalDirUuid = storageManager.getUuidForPath(context.filesDir)

        val free = storageStatsManager.getFreeBytes(appSpecificInternalDirUuid)
        val total = storageStatsManager.getTotalBytes(appSpecificInternalDirUuid)
        val allocatable = storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
        val cacheQuota = storageManager.getCacheQuotaBytes(appSpecificInternalDirUuid)
        return StorageInformation(
            freeBytes = free,
            totalBytes = total,
            allocatableBytes = allocatable,
            cacheQuotaBytes = cacheQuota
        )
    }

    fun storeFile(context: Context, filename: String, content: String) {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
        }
    }

    fun readFile(context: Context, filename: String): String? {
        return try {
            context.openFileInput(filename).bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    "$some\n$text"
                }
            }
        } catch (e: FileNotFoundException) {
            null
        }
    }

    fun deleteFile(context: Context) {}

    fun listFiles(context: Context): Array<String> = context.fileList()

    fun storeCache(context: Context, filename: String, content: String) {
        val cache = File.createTempFile(filename, null, context.cacheDir)
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
        }
    }

    fun readCache(context: Context, filename: String): String {
        return context.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }
    }

    data class StorageInformation(
        val freeBytes: Long,
        val totalBytes: Long,
        val allocatableBytes: Long,
        val cacheQuotaBytes: Long
    ) {
        private fun toMb(bytes: Long) = bytes / 1024 / 1024

        override fun toString(): String {
            return "Free: ${toMb(freeBytes)} MB\n" +
                    "Allocatable: ${toMb(allocatableBytes)} MB\n" +
                    "Total: ${toMb(totalBytes)} MB\n" +
                    "\n" +
                    "Cache Quota: ${toMb(cacheQuotaBytes)} MB"
        }
    }
}