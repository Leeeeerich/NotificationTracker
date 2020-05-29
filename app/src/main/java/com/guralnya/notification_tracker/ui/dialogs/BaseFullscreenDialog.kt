package com.eshmun.mobile.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class BaseFullscreenDialog : DialogFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setDimAmount(0.2f)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun show(activity: AppCompatActivity?) {
        if (activity != null && !activity.isFinishing) {
            this.show(activity.supportFragmentManager, tag())
        }
    }

    fun show(fragment: Fragment?) {
        if (fragment != null) {
            val fragmentManager = fragment.parentFragmentManager
            if (!fragmentManager.isDestroyed && !isShowing() || !isAdded) {
                this.show(fragmentManager, tag())
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    fun isShowing(): Boolean {
        val dialog = dialog
        return dialog != null && dialog.isShowing
    }

    override fun dismiss() {
        if (isShowing()) {
            try {
                super.dismissAllowingStateLoss()
            } catch (ignored: IllegalStateException) {
            }
        }
    }

    abstract fun tag(): String
}