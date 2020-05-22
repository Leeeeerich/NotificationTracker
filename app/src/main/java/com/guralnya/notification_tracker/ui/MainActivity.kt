package com.guralnya.notification_tracker.ui

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.model.constants.Filtration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_with_sorting_button.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModel()
    var filterUpdateListener: ((Long) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
