package com.example.storygram.utils

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

class MotionVisibility {
    companion object {
        fun View.setMotionVisibilities(visibility: Int) {
            val motionLayout = parent as MotionLayout
            motionLayout.constraintSetIds.forEach {
                val constraintSet = motionLayout.getConstraintSet(it) ?: return@forEach
                constraintSet.setVisibility(this.id, visibility)
                constraintSet.applyTo(motionLayout)
            }
        }
    }
}