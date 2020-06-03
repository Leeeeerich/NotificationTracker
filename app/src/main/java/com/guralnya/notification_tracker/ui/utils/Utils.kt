package com.guralnya.notification_tracker.ui.utils

import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.guralnya.notification_tracker.R

object Utils {
    fun isNotificationServiceEnabled(ctx: Context): Boolean {
        val pkgName = ctx.packageName
        val flat = Settings.Secure.getString(
            ctx.contentResolver,
            "enabled_notification_listeners"
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":")
            for ((i, _) in names.withIndex()) {
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

    fun buildNotificationServiceAlertDialog(ctx: Context): AlertDialog {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        alertDialogBuilder.setTitle(R.string.notification_listener_service)
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation)
        alertDialogBuilder.setPositiveButton(
            R.string.yes_button,
            DialogInterface.OnClickListener { _, _ ->
                ctx.startActivity(
                    Intent(
                        Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
                    )
                )
            })
        alertDialogBuilder.setNegativeButton(
            R.string.no_button,
            DialogInterface.OnClickListener { _, _ -> })
        return alertDialogBuilder.create()
    }

    fun sendEmail(context: Context) {
        val emailIntent = Intent()
        emailIntent.type = "plain/text"

        emailIntent.putExtra(Intent.EXTRA_EMAIL, "sosov.valeriy@gmail.com")

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Notification Tracker")

        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hi! I have ask/offer/a complaint/wish - "
        )

        context.startActivity(
            Intent.createChooser(
                emailIntent, "Send mail..."
            )
        )
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun showToast(context: Context, msgResId: Int) {
        Toast.makeText(context, context.getString(msgResId), Toast.LENGTH_LONG).show()
    }
}