package com.guralnya.notification_tracker.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.eshmun.mobile.ui.dialogs.BaseFullscreenDialog
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.databinding.DialogResetPassBinding

class UniversalDialog(
    private val message: String,
    private val positiveButtonText: String?,
    private val positiveCallBack: (() -> Unit)?
) : BaseFullscreenDialog() {
    private lateinit var binding: DialogResetPassBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_reset_pass, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = true

        binding.tvTextDialog.text = message

        view.findViewById<FrameLayout>(R.id.flBackgroundDialog).setOnClickListener {
            dismiss()
        }

        positiveCallBack?.let {
            binding.btOkay.visibility = View.VISIBLE
            binding.btOkay.text = positiveButtonText
            view.findViewById<Button>(R.id.btOkay).setOnClickListener {
                positiveCallBack.invoke()
                dismiss()
            }
        }

        view.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun tag(): String = UniversalDialog::class.java.name
}