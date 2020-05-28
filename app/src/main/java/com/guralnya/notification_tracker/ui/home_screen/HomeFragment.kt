package com.guralnya.notification_tracker.ui.home_screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.databinding.FragmentHomeBinding
import com.guralnya.notification_tracker.model.services.NotificationTrackerService
import com.guralnya.notification_tracker.ui.MainActivity
import com.guralnya.notification_tracker.ui.utils.Utils.buildNotificationServiceAlertDialog
import com.guralnya.notification_tracker.ui.utils.Utils.isNotificationServiceEnabled
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: NotifyInfoAdapter
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

    private fun startSelectableModeListener(boolean: Boolean) {
        (activity as MainActivity).endSelectableModeListener = ::endSelectableModeListener
    }

    private fun endSelectableModeListener(isDelete: Boolean) {
        if (isDelete) {
            vm.deleteNotifies(adapter.list.filter { it.isChecked.get() })
        } else {
            adapter.list.forEach { it.isChecked.set(false) }
        }
        adapter.isSelectableMode = false
        (activity as MainActivity).endSelectableModeListener = null
    }

    private fun showEmptyScreen(isShow: Boolean) {
        binding.vEmptyScreen.visibility = if (isShow) View.VISIBLE else View.GONE
        binding.vNotificationList.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.filterUpdateListener = {
            observeNotifyTrackingLiveData(it)
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as? MainActivity)?.filterUpdateListener = null
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
}
