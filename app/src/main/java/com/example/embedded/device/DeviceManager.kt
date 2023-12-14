package com.example.embedded.device

import info.mqtt.android.service.MqttAndroidClient
import org.json.JSONObject

class DeviceManager(private val mqtt: MqttAndroidClient) {
    val devices = HashMap<String, EmbeddedLamp>()
    var events: Events? = null

    fun addDevice(name: String, rootGroup: String, type: DeviceTypes) {
        when (type) {
            DeviceTypes.UNKNOWN -> {
            }
            DeviceTypes.LAMP -> {
                val newDevice = EmbeddedLamp()
                newDevice.rootGroup = rootGroup
                newDevice.type = DeviceTypes.LAMP
                devices[name] = newDevice
                if (mqtt.isConnected) {
                    onBrokerConnected()
                }
            }
            DeviceTypes.METEO -> {

            }
        }
    }

    fun onBrokerConnected() {
        for (item in devices) {
            when (item.value.type) {
                DeviceTypes.UNKNOWN -> {
                    mqtt.subscribe(item.value.rootGroup, 1)
                    mqtt.subscribe("${item.value.rootGroup}/connection", 1)
                }
                DeviceTypes.LAMP -> {
                    mqtt.subscribe(item.value.rootGroup, 1)
                    mqtt.subscribe("${item.value.rootGroup}/connection", 1)
                }
                DeviceTypes.METEO -> {
                    mqtt.subscribe(item.value.rootGroup, 1)
                    mqtt.subscribe("${item.value.rootGroup}/connection", 1)
                }
            }
        }
    }

    fun remove(name: String) {
        mqtt.unsubscribe(devices[name]!!.rootGroup)
        mqtt.unsubscribe("${devices[name]!!.rootGroup}/connection")
        devices.remove(name)
    }

    fun get(name: String): EmbeddedLamp? {
        return devices[name]
    }

    fun rename(prev_name: String, name: String) {
        if (devices.contains(name)) {
            return
        }
        val device = devices[prev_name]!!
        devices.remove(prev_name)
        devices[name] = device
    }

    fun updateLamp(lamp: EmbeddedLamp) {
        val dataJson = JSONObject()
        dataJson.put("isOn", lamp.isOn)
        dataJson.put("brightness", lamp.brightness)
        dataJson.put("effect", lamp.effect)
        dataJson.put("effect_speed", lamp.effectSpeed)
        dataJson.put("palette", lamp.palette)
        mqtt.publish("${lamp.rootGroup}/control", dataJson.toString().toByteArray(), 1, false)
    }

    fun parseMsg(topic: String, msg: String) {
        for (item in devices) {
            when (item.value.type) {
                DeviceTypes.UNKNOWN -> {
                    if (topic == item.value.rootGroup) {

                    }
                }
                DeviceTypes.LAMP -> {
                    if (topic == item.value.rootGroup) {
                        val dataJson = JSONObject(msg)
                        item.value.isOn = dataJson.getBoolean("isOn")
                        item.value.brightness = dataJson.getInt("brightness")
                        item.value.effect = dataJson.getInt("effect")
                        item.value.effectSpeed = dataJson.getDouble("effect_speed").toFloat()
                        item.value.palette = dataJson.getInt("palette")
                    }
                    if (topic == "${item.value.rootGroup}/connection") {
                        item.value.isConnected = msg == "CONNECTED"
                        if (item.value.isConnected) {
                            events?.onDeviceConnected(item.key)
                        }
                        else {
                            events?.onDeviceDisconnected(item.key)
                        }
                    }
                }
                DeviceTypes.METEO -> {

                }
            }
        }
    }

    interface Events {
        fun onDeviceDisconnected(name: String)
        fun onDeviceConnected(name: String)
    }
}