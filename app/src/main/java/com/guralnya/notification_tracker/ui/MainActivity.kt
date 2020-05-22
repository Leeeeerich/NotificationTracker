package com.guralnya.notification_tracker.ui

import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.text.TextUtils
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.model.constants.Filtration
import com.guralnya.notification_tracker.model.services.NotificationTrackerService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_with_sorting_button.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModel()
    var filterUpdateListener: ((Long) -> Unit)? = null
    val TAG = "NotificationTracker"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this, NotificationTrackerService::class.java))

        if (!isNotificationServiceEnabled()) {
            buildNotificationServiceAlertDialog().show()
        }

        vFilter.setOnClickListener {
            showPopupMenu(it)
        }
    }

    private fun showPopupMenu(v: View) {
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

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":")
            for ((i, e) in names.withIndex()) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun buildNotificationServiceAlertDialog(): AlertDialog {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.notification_listener_service)
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation)
        alertDialogBuilder.setPositiveButton(R.string.yes_button,
            DialogInterface.OnClickListener { dialog, id ->
                startActivity(
                    Intent(
                        ACTION_NOTIFICATION_LISTENER_SETTINGS
                    )
                )
            })
        alertDialogBuilder.setNegativeButton(R.string.no_button,
            DialogInterface.OnClickListener { dialog, id ->
                // If you choose to not enable the notification listener
                // the app. will not work as expected
            })
        return alertDialogBuilder.create()
    }
}
