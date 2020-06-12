package com.guralnya.notification_tracker.ui.home_screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.databinding.FragmentHomeBinding
import com.guralnya.notification_tracker.model.constants.Filtration
import com.guralnya.notification_tracker.model.services.NotificationTrackerService
import com.guralnya.notification_tracker.ui.dialogs.UniversalDialog
import com.guralnya.notification_tracker.ui.utils.Utils
import com.guralnya.notification_tracker.ui.utils.Utils.buildNotificationServiceAlertDialog
import com.guralnya.notification_tracker.ui.utils.Utils.isNotificationServiceEnabled
import kotlinx.android.synthetic.main.toolbar_with_sorting_button.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: NotifyInfoAdapter
    private lateinit var navController: NavController
    private val vm: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.host_fragment)
        observeNotifyTrackingStatus()
        observeNotifyTrackingLiveData(0)
        initUi()
        vm.init()
    }

    private fun initUi() {
        adapter = NotifyInfoAdapter(::startSelectableModeListener)
        binding.vNotificationList.adapter = adapter
        binding.btStartTracking.setOnClickListener {
            (it as ToggleButton)
            vm.setTrackingStatus(it.isChecked)
        }
        setFilterButtonListener()
        setInfoButtonListener()
    }

    private fun observeNotifyTrackingLiveData(filtration: Long) {
        vm.getNotificationsLiveData(filtration).observe(requireActivity(), Observer {
            if (it != null) {
                adapter.list = it.toMutableList()
            }
            showEmptyScreen(it.isEmpty())
        })
    }

    private fun observeNotifyTrackingStatus() {
        vm.getTrackingStatus().observe(requireActivity(), Observer {
            Log.e(javaClass.name, "Notify tracker status worked. Status = $it")
            if (it != null) {
                if (it) {
                    startTracking()
                } else {
                    stopTracking()
                }
                binding.btStartTracking.isChecked = it
            }
        })
    }

    private fun startSelectableModeListener() {
        vFilter.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_done_all
            )
        )
        imInfo.visibility = View.GONE
    }

    private fun endSelectableModeListener() {
        vm.deleteNotifies(adapter.list.filter { it.isChecked.get() })
        adapter.isSelectableMode = false
        vFilter.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_sort
            )
        )
        imInfo.visibility = View.VISIBLE
    }

    private fun showEmptyScreen(isShow: Boolean) {
        binding.vEmptyScreen.visibility = if (isShow) View.VISIBLE else View.GONE
        binding.vNotificationList.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    private fun startTracking() {
        requireActivity().startService(
            Intent(
                requireContext(),
                NotificationTrackerService::class.java
            )
        )

        if (!isNotificationServiceEnabled(requireContext())) {
            buildNotificationServiceAlertDialog(requireActivity()).show()
        }
    }

    private fun stopTracking() {
        requireActivity().startService(
            Intent(
                requireContext(),
                NotificationTrackerService::class.java
            )
        )
    }

    private fun setFilterButtonListener() {
        vFilter.setOnClickListener {
            if (adapter.isSelectableMode) {
                endSelectableModeListener()
            } else {
                showFilterMenu(it)
            }
        }
    }

    private fun setInfoButtonListener() {
        imInfo.setOnClickListener {
            showInfoMenu(it)
        }
    }

    private fun showFilterMenu(v: View) {
        val popupMenu = PopupMenu(requireActivity(), v)
        popupMenu.inflate(R.menu.menu_filters)
        popupMenu.menu.getItem(vm.filtration.ordinal).isChecked = true
        popupMenu.menu.findItem(R.id.menuIsEnabledPackageFilter).isChecked =
            vm.getIsEnabledPackageFilter()
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuAllTime -> {
                    vm.filtration = Filtration.ALL_TIME
                }
                R.id.menuPerHour -> {
                    vm.filtration = Filtration.PER_HOUR
                }
                R.id.menuPerDay -> {
                    vm.filtration = Filtration.PER_DAY
                }
                R.id.menuPerMonth -> {
                    vm.filtration = Filtration.PER_MONTH
                }
                R.id.menuIsEnabledPackageFilter -> {
                    vm.setIsEnabledPackageFilter(!item.isChecked)
                }
            }
            observeNotifyTrackingLiveData(vm.filtration.filterValue)
            true
        }
        popupMenu.show()
    }

    private fun showInfoMenu(v: View) {
        val popupMenu = PopupMenu(requireActivity(), v)
        popupMenu.inflate(R.menu.menu_info)
        popupMenu.menu.getItem(vm.filtration.ordinal).isChecked = true
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settingsFragment -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                }
                R.id.menuChangePin -> {
                    Utils.showToast(requireActivity(), "Coming soon")
                }
                R.id.menuPrivacyPolicy -> {
                    UniversalDialog(getString(R.string.privacy_policy_text), null, null).show(this)
                }
                R.id.menuContactUs -> {
                    Utils.sendEmail(requireActivity())
                }
            }
            true
        }
        popupMenu.show()
    }
}
