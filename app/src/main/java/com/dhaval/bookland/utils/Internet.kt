package com.dhaval.bookland.utils

import java.net.Socket
import java.net.InetSocketAddress

object Internet {
    fun isAvailable() : Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8",53))
            socket.close()
            true
        } catch ( e: Exception){
            e.printStackTrace()
            false
        }
    }
}