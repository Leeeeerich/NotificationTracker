package com.guralnya.notification_tracker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.databinding.FragmentSettingsBinding
import com.guralnya.notification_tracker.ui.utils.Utils
import kotlinx.android.synthetic.main.toolbar_save_button.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val vm: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsVo = vm.settingsVo
        setSaveChangedListener()
    }

    private fun setSaveChangedListener() {
        binding.vToolbar.vSave.setOnClickListener {
            vm.saveChanged()
            Utils.showToast(requireActivity(), R.string.changes_was_saved)
        }
    }
}