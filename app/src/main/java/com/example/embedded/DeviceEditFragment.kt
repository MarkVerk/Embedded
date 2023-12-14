package com.example.embedded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.embedded.databinding.DeviceEditFragmentBinding
import java.io.File

class DeviceEditFragment : Fragment() {
    private var _binding: DeviceEditFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DeviceEditFragmentBinding.inflate(inflater, container, false)
        val viewModel: AppViewModel by activityViewModels()
        binding.deviceEditName.setText(viewModel.device.value!!)
        binding.backBtn4.setOnClickListener {
            changeFragment(DeviceFragment())
        }
        binding.saveDeviceChangesBtn.setOnClickListener {
            val newName = binding.deviceEditName.text.toString()
            viewModel.deviceManager.value!!.rename(viewModel.device.value!!, newName)
            viewModel.device.value = newName
            viewModel.settings.value!!.saveToFile(File(requireActivity().filesDir, getString(R.string.config_file)))
        }
        binding.deleteDeviceBtn.setOnClickListener {
            viewModel.deviceManager.value!!.remove(viewModel.device.value!!)
            viewModel.settings.value!!.saveToFile(File(requireActivity().filesDir, getString(R.string.config_file)))
            viewModel.device.value = ""
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