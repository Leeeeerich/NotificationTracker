package com.guralnya.notification_tracker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.model.constants.Filtration
import com.guralnya.notification_tracker.ui.dialogs.UniversalDialog
import com.guralnya.notification_tracker.ui.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_with_sorting_button.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val vm: MainViewModel by viewModel()
    var filterUpdateListener: ((Long) -> Unit)? = null
    var endSelectableModeListener: ((Boolean) -> Unit)? = null
        set(value) {
            field = value
            if (field != null) {
                vFilter.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_done_all
                    )
                )
                imInfo.visibility = View.GONE
            } else if (field == null) {
                vFilter.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_sort
                    )
                )
                imInfo.visibility = View.VISIBLE
            }
        }

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.host_fragment)

        setInfoButtonListener()
        vFilter.setOnClickListener {
            if (endSelectableModeListener != null) {
                endSelectableModeListener?.invoke(true)
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
        val popupMenu = PopupMenu(this, v)
        popupMenu.inflate(R.menu.menu_filters)
        popupMenu.menu.getItem(vm.filtration.ordinal).isChecked = true
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
            }
            llMainActivity.foreground = null
            filterUpdateListener?.invoke(vm.filtration.filterValue)
            true
        }
        popupMenu.setOnDismissListener {
            llMainActivity.foreground = null
        }
        llMainActivity.foreground = ContextCompat.getDrawable(this, R.color.colorTranslucency)
        popupMenu.show()
    }

    private fun showInfoMenu(v: View) {
        val popupMenu = PopupMenu(this, v)
        popupMenu.inflate(R.menu.menu_info)
        popupMenu.menu.getItem(vm.filtration.ordinal).isChecked = true
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuChangePin -> {
                    Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
                }
                R.id.menuPrivacyPolicy -> {
                    UniversalDialog(getString(R.string.privacy_policy_text), null, null).show(this)
                }
                R.id.menuContactUs -> {
                    Utils.sendEmail(this)
                }
            }
            llMainActivity.foreground = null
            filterUpdateListener?.invoke(vm.filtration.filterValue)
            true
        }
        popupMenu.setOnDismissListener {
            llMainActivity.foreground = null
        }
        llMainActivity.foreground = ContextCompat.getDrawable(this, R.color.colorTranslucency)
        popupMenu.show()
    }

    override fun onBackPressed() {
        if (endSelectableModeListener != null) {
            endSelectableModeListener?.invoke(false)
            endSelectableModeListener = null
        } else {
            super.onBackPressed()
        }
    }
}
