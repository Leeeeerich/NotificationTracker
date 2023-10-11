package com.guralnya.notification_tracker.ui.pin_code

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.databinding.ActivityPinCodeBinding
import com.guralnya.notification_tracker.ui.MainActivity
import com.guralnya.notification_tracker.ui.dialogs.ResetPassDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinCodeBinding
    private val vm: PinCodeViewModel by viewModel()

    companion object {
        private const val RESET_PIN_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pin_code)

        observePinCodeLiveData()
        setDeleteCharListener()
        setForgotButtonListener()
        upgradeDots()
    }

    fun onClickPin(view: View) {
        vm.inputPin.append(view.tag)
        upgradeDots()
        if (vm.inputPin.length == 4) {
            checkPin()
        }
    }

    private fun observePinCodeLiveData() {
        vm.getPinCodeLiveData().observe(this, Observer {
            if (it == null) {
                createPinCode()
            }
        })
    }

    private fun enterInApp() {
        startActivity(MainActivity.getIntent(this))
    }

    private fun createPinCode() {
        binding.tvActionText.text = getString(R.string.pick_a_pin)
        vm.isCreatingPin = true
    }

    private fun repeatingInputPin() {
        binding.tvActionText.text = getString(R.string.repeat_input)
        vm.inputPin.clear()
    }

    private fun badInputRepeatingInputPin() {
        binding.tvActionText.text = getString(R.string.bad_input_repeat_input)
        vm.inputPin.clear()
    }

    private fun setDeleteCharListener() {
        binding.tvRightButton.setOnClickListener {
            if (vm.inputPin.isNotEmpty()) {
                vm.inputPin.delete(vm.inputPin.length - 1, vm.inputPin.length)
                upgradeDots()
            }
        }
    }

    private fun setForgotButtonListener() {
        binding.tvLeftButton.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                ResetPassDialog(getString(R.string.manual_reset_pin_text), ::manualResetPin).show(
                    this
                )
                //TODO need control: was reset PIN
            } else {
                ResetPassDialog(getString(R.string.reset_pin_text), ::resetPinCode).show(this)
            }
        }
    }

    private fun upgradeDots() {
        when (vm.inputPin.length) { //TODO need optimization code
            0 -> {
                binding.vFirstPin.background = ContextCompat.getDrawable(this, R.drawable.dot_empty)
                binding.vSecondPin.background =
                    ContextCompat.getDrawable(this, R.drawable.dot_empty)
                binding.vThirdPin.background = ContextCompat.getDrawable(this, R.drawable.dot_empty)
                binding.vFourthPin.background =
                    ContextCompat.getDrawable(this, R.drawable.dot_empty)
            }
            1 -> {
                binding.vFirstPin.background = ContextCompat.getDrawable(this, R.drawable.dot_full)
                binding.vSecondPin.background =
                    ContextCompat.getDrawable(this, R.drawable.dot_empty)
                binding.vThirdPin.background = ContextCompat.getDrawable(this, R.drawable.dot_empty)
                binding.vFourthPin.background =
                    ContextCompat.getDrawable(this, R.drawable.dot_empty)
            }
            2 -> {
                binding.vFirstPin.background = ContextCompat.getDrawable(this, R.drawable.dot_full)
                binding.vSecondPin.background = ContextCompat.getDrawable(this, R.drawable.dot_full)
                binding.vThirdPin.background = ContextCompat.getDrawable(this, R.drawable.dot_empty)
                binding.vFourthPin.background =
                    ContextCompat.getDrawable(this, R.drawable.dot_empty)
            }
            3 -> {
                binding.vFirstPin.background = ContextCompat.getDrawable(this, R.drawable.dot_full)
                binding.vSecondPin.background = ContextCompat.getDrawable(this, R.drawable.dot_full)
                binding.vThirdPin.background = ContextCompat.getDrawable(this, R.drawable.dot_full)
                binding.vFourthPin.background =
                    ContextCompat.getDrawable(this, R.drawable.dot_empty)
            }
            4 -> {
                binding.vFirstPin.background = ContextCompat.getDrawable(this, R.drawable.dot_full)
                binding.vSecondPin.background = ContextCompat.getDrawable(this, R.drawable.dot_full)
                binding.vThirdPin.background = ContextCompat.getDrawable(this, R.drawable.dot_full)
                binding.vFourthPin.background = ContextCompat.getDrawable(this, R.drawable.dot_full)
            }
        }
    }

    private fun checkPin() {
        when {
            vm.isCreatingPin && vm.newPinCode.isEmpty() -> {
                vm.newPinCode = vm.inputPin.toString()
                repeatingInputPin()
            }
            vm.isCreatingPin && vm.newPinCode.isNotEmpty() -> {
                if (vm.newPinCode == vm.inputPin.toString()) {
                    vm.createPinCode()
                    enterInApp()
                } else {
                    badInput()
                }
            }
            !vm.isCreatingPin && vm.inputPin.toString() == vm.getPinCodeLiveData().value -> {
                enterInApp()
            }
            !vm.isCreatingPin && vm.inputPin.toString() != vm.getPinCodeLiveData().value -> {
                badInput()
            }
        }
        upgradeDots()
    }

    private fun badInput() {
        badInputAnimation()
        vm.inputPin.clear()
        badInputRepeatingInputPin()
    }

    private fun badInputAnimation() {
        val animSet = AnimatorSet()
        animSet.playTogether(
            getAnimator(binding.vFirstPin),
            getAnimator(binding.vSecondPin),
            getAnimator(binding.vThirdPin),
            getAnimator(binding.vFourthPin)
        )
        animSet.start()
    }

    private fun getAnimator(view: View): ObjectAnimator {
        val anim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1F)
        anim.repeatCount = 2
        anim.repeatMode = ObjectAnimator.REVERSE
        anim.duration = 150
        return anim
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun resetPinCode() {
        baseContext.dataDir.deleteRecursively()
        restartActivity()
    }

    private fun restartActivity() {
        finishAffinity()
        val intent = Intent(applicationContext, this::class.java)
        startActivity(intent)
    }

    private fun manualResetPin() {
        startActivityForResult(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .addCategory(Intent.CATEGORY_DEFAULT)
                .setData(Uri.parse("package:$packageName")), RESET_PIN_REQUEST_CODE
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESET_PIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                restartActivity()
            }
        }
    }
}