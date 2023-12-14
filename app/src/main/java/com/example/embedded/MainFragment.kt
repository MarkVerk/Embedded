package com.example.embedded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.embedded.databinding.MainFragmentBinding
import com.example.embedded.device.DeviceTypes

class MainFragment : Fragment(), DevicesListAdapter.Interface {
    private val viewModel: AppViewModel by activityViewModels()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.addDeviceBtn.setOnClickListener {
            if (viewModel.mqtt.value!!.isConnected) {
                changeFragment(DeviceAddingFragment())
            }
        }
        binding.appSettings.setOnClickListener {
            changeFragment(SettingsFragment())
        }

        binding.devicesList.adapter = DevicesListAdapter(this, ArrayList(viewModel.deviceManager.value!!.devices.keys))

        binding.devicesList.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    override fun onItemSelected(name: String) {
        viewModel.device.value = name
        if (!viewModel.mqtt.value!!.isConnected || viewModel.deviceManager.value!!.get(name)?.type != DeviceTypes.LAMP) return
        changeFragment(DeviceFragment())
    }

    private fun changeFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    }
}