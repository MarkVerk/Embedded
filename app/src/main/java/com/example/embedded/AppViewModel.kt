package com.example.embedded

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.embedded.device.DeviceManager
import info.mqtt.android.service.MqttAndroidClient

class AppViewModel : ViewModel() {
    var mqtt = MutableLiveData<MqttAndroidClient>()
    var device = MutableLiveData<String>()
    var deviceManager = MutableLiveData<DeviceManager>()
    var settings = MutableLiveData<AppSettings>()
}