package com.example.embedded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.embedded.databinding.DeviceAddingFragmentBinding
import com.example.embedded.device.DeviceTypes
import java.io.File

class DeviceAddingFragment : Fragment() {
    private var _binding: DeviceAddingFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DeviceAddingFragmentBinding.inflate(inflater, container, false)
        val viewModel: AppViewModel by activityViewModels()
        binding.finishNewDeviceBtn.setOnClickListener {
            binding.finishNewDeviceBtn.isEnabled = false
            viewModel.deviceManager.value!!.addDevice(
                binding.deviceName.text.toString(),
                binding.deviceMqttName.text.toString(),
                DeviceTypes.LAMP
            )
            viewModel.settings.value!!.saveToFile(File(requireActivity().filesDir, getString(R.string.config_file)))
            changeFragment(MainFragment())
        }
        binding.backBtn.setOnClickListener {
            changeFragment(MainFragment())
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
}