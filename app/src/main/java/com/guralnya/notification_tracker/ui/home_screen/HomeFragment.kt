package com.guralnya.notification_tracker.ui.home_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.databinding.FragmentHomeBinding
import com.guralnya.notification_tracker.ui.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: NotifyInfoAdapter
    private val vm: HomeViewModel by viewModel()

//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

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

        initUi()
        observeNotifyTrackingLiveData(0)
    }

    private fun initUi() {
        adapter = NotifyInfoAdapter()
        binding.vNotificationList.adapter = adapter
        binding.btStartTracking.setOnClickListener {
            //TODO need implementation start/stop tracking
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
