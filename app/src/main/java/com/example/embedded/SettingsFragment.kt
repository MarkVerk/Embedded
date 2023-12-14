package com.example.embedded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.embedded.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {
    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        binding.backBtn6.setOnClickListener {
            changeFragment(MainFragment())
        }
        binding.saveChanges.setOnClickListener {

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