package com.hanmajid.android.seed.ui.connectivity.wifi.p2p

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket

class WifiP2pViewModel @ViewModelInject constructor(
    @ActivityContext val context: Context
) : ViewModel() {
    var isInitialized = false

    fun sendFile(host: String, port: Int, uri: Uri, onComplete: (message: String?) -> Unit) {
        Log.wtf(TAG, "Sending file... $host $port")
        val socket = Socket()
        var len: Int?
        val buf = ByteArray(1024)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                socket.bind(null)
                socket.connect((InetSocketAddress(host, port)), 5000)

                /**
                 * Create a byte stream from a file and pipe it to the output stream
                 * of the socket. This data is retrieved by the server device.
                 */
                val outputStream = socket.getOutputStream()
                val cr = context.contentResolver
                val inputStream = cr.openInputStream(uri)
                while (inputStream?.read(buf).also { len = it } != -1) {
                    outputStream.write(buf, 0, len!!)
                }
                outputStream.close()
                inputStream?.close()
                onComplete("Success!")
            } catch (e: IOException) {
                Log.wtf(TAG, e.message)
                onComplete(e.message)
            } catch (e: Exception) {
                Log.wtf(TAG, e.message)
                onComplete(e.message)
            } finally {
                /**
                 * Clean up any open sockets when done
                 * transferring or if an exception occurred.
                 */
                socket.takeIf { it.isConnected }?.apply {
                    close()
                }
            }
        }
    }

    fun startServer(isGroupOwner: Boolean, onComplete: (error: String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val port = if (isGroupOwner) GROUP_OWNER_PORT else NON_GROUP_OWNER_PORT
            Log.wtf(TAG, "Starting server... $port")
            /**
             * Create a server socket.
             */
            try {
                val serverSocket = ServerSocket(port)
                serverSocket.use {
                    /**
                     * Wait for client connections. This call blocks until a connection
                     * is accepted from a client.
                     */
                    val client = serverSocket.accept()

                    /**
                     * If this code is reached, a client has connected and transferred data
                     * Save the input stream from the client.
                     */
                    val filename = "${System.currentTimeMillis()}.jpg"
                    val f = File(
                        context.getExternalFilesDir(null), filename
                    )
                    f.createNewFile()
                    val inputStream = client.getInputStream()
                    val out = FileOutputStream(f)

                    // Copy file
                    val buf = ByteArray(1024)
                    var len: Int
                    try {
                        while (inputStream.read(buf).also { len = it } != -1) {
                            out.write(buf, 0, len)
                        }
                        out.close()
                        inputStream.close()
                    } catch (e: IOException) {

                    }
                    serverSocket.close()
                    Log.wtf(TAG, "File received: $filename")
                    onComplete("File received: $filename")
                }
            } catch (e: IOException) {
                onComplete(e.message)
                Log.wtf(TAG, e.message)
            } catch (e: Exception) {
                onComplete(e.message)
                Log.wtf(TAG, e.message)
            }
        }
    }

    companion object {
        const val GROUP_OWNER_PORT = 8888
        const val NON_GROUP_OWNER_PORT = 8988
        private const val TAG = "WifP2pViewModel"
    }
}