package com.guralnya.notification_tracker.ui.pin_code

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.guralnya.notification_tracker.R
import com.guralnya.notification_tracker.databinding.ActivityPinCodeBinding
import com.guralnya.notification_tracker.ui.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

class PinCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinCodeBinding
    private val vm: PinCodeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pin_code)

        setDeleteCharListener()

        if (vm.pinCode.isNullOrBlank()) {
            createPinCode()
        }
    }

    fun onClickPin(view: View) {
        vm.inputPin.append(view.tag)
        upgradeDots()
        if (vm.inputPin.length == 4) {
            checkPin()
        }
    }

    private fun enter() {
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    private fun createPinCode() {
        binding.tvActionText.text = getString(R.string.pick_a_pin)
        vm.isCreatingPin = true
    }

    private fun repeatingInputPin() {
        binding.tvActionText.text = getString(R.string.repeat_input)
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
                    enter()
                } else {
                    badInput()
                }
            }
            !vm.isCreatingPin && vm.inputPin.toString() == vm.pinCode -> {
                enter()
            }
            !vm.isCreatingPin && vm.inputPin.toString() != vm.pinCode -> {
                badInput()
                repeatingInputPin()
            }
        }
        upgradeDots()
    }

    private fun badInput() {
        badInputAnimation()
        vm.inputPin.clear()
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
}