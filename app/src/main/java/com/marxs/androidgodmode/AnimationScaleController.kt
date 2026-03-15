package com.marxs.androidgodmode

import android.content.Context

data class AnimationScaleState(
    val supported: Boolean,
    val enabled: Boolean,
    val windowScale: Float,
    val transitionScale: Float,
    val animatorScale: Float,
    val reason: String
)

class AnimationScaleController(private val context: Context) {
    private val values = listOf(0f, 0.5f, 1f, 1.5f, 2f)

    fun readState(): AnimationScaleState {
        return AnimationScaleState(
            supported = false,
            enabled = false,
            windowScale = 1f,
            transitionScale = 1f,
            animatorScale = 1f,
            reason = "需要 ADB / Shizuku / Root 才能直接修改动画倍率"
        )
    }

    fun allowedValues(): List<Float> = values

    fun setEnabled(enabled: Boolean): Result<Unit> {
        return Result.failure(IllegalStateException("Direct animation scale control requires elevated privileges"))
    }

    fun setScales(window: Float, transition: Float, animator: Float): Result<Unit> {
        return Result.failure(IllegalStateException("Direct animation scale control requires elevated privileges"))
    }
}
