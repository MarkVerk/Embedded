package com.example.embedded

import com.example.embedded.device.DeviceTypes
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class AppSettings(private val viewModel: AppViewModel) {
    var version: Float = 0.0f
    fun loadFromFile(file: File) {
        if (!file.exists()) {
            file.createNewFile()
            return
        }
        val content = file.readText()
        if (content.isEmpty()) return
        val dataJson = JSONObject(content)
        version = dataJson.getDouble("version").toFloat()
        val devicesJson = dataJson.getJSONArray("devices")
        for (i in 0 until devicesJson.length()) {
            val deviceJson = devicesJson.getJSONObject(i)
            viewModel.deviceManager.value!!.addDevice(
                deviceJson.getString("name"),
                deviceJson.getString("rootGroup"),
                DeviceTypes.LAMP
            )
        }
    }
    fun saveToFile(file: File) {
        val dataJson = JSONObject()
        dataJson.put("version", version)
        val devicesJson = JSONArray()
        for (item in viewModel.deviceManager.value!!.devices) {
            val deviceJson = JSONObject()
            deviceJson.put("name", item.key)
            deviceJson.put("rootGroup", item.value.rootGroup)
            deviceJson.put("type", item.value.type.name)
            devicesJson.put(deviceJson)
        }
        dataJson.put("devices", devicesJson)
        file.writeText(dataJson.toString())
    }
}