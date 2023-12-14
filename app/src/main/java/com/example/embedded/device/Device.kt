package com.example.embedded.device

open class Device {
    var rootGroup: String = ""
    var type: DeviceTypes = DeviceTypes.UNKNOWN
    var version: Float = 0.0f
    var isConnected: Boolean = false
}