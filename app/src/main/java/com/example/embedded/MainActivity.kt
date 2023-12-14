package com.example.embedded

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.embedded.device.DeviceManager
import info.mqtt.android.service.Ack
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.mqtt.value = MqttAndroidClient(this, "tcp://broker.hivemq.com:1883", MqttClient.generateClientId(), Ack.AUTO_ACK)
        viewModel.deviceManager.value = DeviceManager(viewModel.mqtt.value!!)
        viewModel.settings.value = AppSettings(viewModel)
        val connectOptions = MqttConnectOptions()
        connectOptions.isCleanSession = true
        connectOptions.isAutomaticReconnect = true
        viewModel.mqtt.value!!.connect(connectOptions).actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                viewModel.deviceManager.value!!.onBrokerConnected()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Toast.makeText(this@MainActivity, "Cannot connect to broker", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.mqtt.value!!.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Toast.makeText(this@MainActivity, "Connection lost", Toast.LENGTH_SHORT).show()
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                if (message == null || topic == null) return
                viewModel.deviceManager.value!!.parseMsg(topic, String(message.payload))
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })
        viewModel.settings.value!!.loadFromFile(File(filesDir, getString(R.string.config_file)))
        changeFragment(MainFragment())
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }
}