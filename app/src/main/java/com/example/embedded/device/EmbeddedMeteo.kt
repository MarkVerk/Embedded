package com.example.embedded.device

class EmbeddedMeteo : Device() {
    interface Events {
        fun onMeteoConnected()
        fun onMeteoDisconnected()
    }
}