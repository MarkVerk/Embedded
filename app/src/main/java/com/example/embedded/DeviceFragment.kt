package com.example.embedded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.embedded.databinding.DeviceFragmentBinding
import com.example.embedded.device.DeviceManager

class DeviceFragment : Fragment(), DeviceManager.Events {
    private var _binding: DeviceFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DeviceFragmentBinding.inflate(inflater, container, false)
        val viewModel: AppViewModel by activityViewModels()
        val name = viewModel.device.value!!
        val lamp = viewModel.deviceManager.value!!.get(name)!!
        binding.isConnectedText.setText(if (lamp.isConnected) "ONLINE" else "OFFLINE")
        binding.isOnSwitch.isChecked = lamp.isOn
        binding.lampEffectCfg.setSelection(lamp.effect, false)
        binding.lampBrightnessCfg.setProgress(lamp.brightness)
        binding.lampPaletteCfg.setSelection(lamp.palette, false)
        binding.deviceName3.text = name
        viewModel.deviceManager.value!!.events = this
        binding.isOnSwitch.setOnCheckedChangeListener { compoundButton, b ->
            lamp.isOn = b
            viewModel.deviceManager.value!!.updateLamp(lamp)
        }
        binding.lampBrightnessCfg.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {}
            override fun onStartTrackingTouch(p0: SeekBar) {}
            override fun onStopTrackingTouch(p0: SeekBar) {
                lamp.brightness = p0.progress
                viewModel.deviceManager.value!!.updateLamp(lamp)
            }
        })
        binding.lampEffectCfg.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                lamp.effect = p2
                viewModel.deviceManager.value!!.updateLamp(lamp)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.lampPaletteCfg.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                lamp.palette = p2
                viewModel.deviceManager.value!!.updateLamp(lamp)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.backBtn2.setOnClickListener {
            viewModel.deviceManager.value!!.events = null
            changeFragment(MainFragment())
        }
        binding.editDeviceButton.setOnClickListener {
            changeFragment(DeviceEditFragment())
        }
        return binding.root
    }

    private fun changeFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }

    override fun onDeviceDisconnected(name: String) {
        binding.isConnectedText.setText("OFFLINE")
    }

    override fun onDeviceConnected(name: String) {
        binding.isConnectedText.setText("ONLINE")
    }
}