package com.guralnya.notification_tracker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.databinding.FragmentSettingsBinding
import com.guralnya.notification_tracker.model.models.IgnorePackage
import com.guralnya.notification_tracker.ui.utils.Utils
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.toolbar_save_button.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var packagesIgnoreAdapter: ApplicationAdapter
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

        packagesIgnoreAdapter = ApplicationAdapter()
        rvIgnorePackages.adapter = packagesIgnoreAdapter

        observeListPackagesWithIgnoreVo()
        setSaveChangedListener()
        vm.loadListPackagesWithIgnore()
    }

    private fun observeListPackagesWithIgnoreVo() {
        vm.getIgnorePackagesLiveData().observe(requireActivity(), Observer {
            if (it != null) {
                packagesIgnoreAdapter.list = it.toMutableList()
            }
        })
    }

    private fun setSaveChangedListener() {
        binding.vToolbar.vSave.setOnClickListener {
            val ignorePackages = mutableListOf<IgnorePackage>()
            packagesIgnoreAdapter.getIgnorePackages()
                .forEach { ignorePackages.add(IgnorePackage(it.appPackageName)) }
            vm.saveChanged(ignorePackages)
            Utils.showToast(requireActivity(), R.string.changes_was_saved)
        }
    }
}