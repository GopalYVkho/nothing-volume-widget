package com.gopal.nothingvolume

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.media.AudioManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.roundToInt

class VolumeSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF333333.toInt()
        style = Paint.Style.FILL
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        style = Paint.Style.FILL
    }

    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        style = Paint.Style.FILL
    }

    private val currentVolume: Int
        get() = audioManager.getStreamVolume(
            AudioManager.STREAM_MUSIC
        )

    private val maxVolume: Int
        get() = audioManager.getStreamMaxVolume(
            AudioManager.STREAM_MUSIC
        )

    private var volume = currentVolume

    var onVolumeChanged: ((Int) -> Unit)? = null

    init {
        isClickable = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerY = height / 2f

        val horizontalPadding = 20f

        val startX = horizontalPadding
        val endX = width - horizontalPadding

        val trackHeight = 8f

        val trackTop = centerY - trackHeight / 2
        val trackBottom = centerY + trackHeight / 2

        // Background track
        canvas.drawRoundRect(
            RectF(
                startX,
                trackTop,
                endX,
                trackBottom
            ),
            4f,
            4f,
            trackPaint
        )

        // Current progress
        val percentage =
            if (maxVolume > 0) {
                volume.toFloat() / maxVolume
            } else {
                0f
            }

        val progressX =
            startX + (endX - startX) * percentage

        canvas.drawRoundRect(
            RectF(
                startX,
                trackTop,
                progressX,
                trackBottom
            ),
            4f,
            4f,
            progressPaint
        )

        // Thumb
        canvas.drawCircle(
            progressX,
            centerY,
            14f,
            thumbPaint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {

            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_UP -> {

                updateVolume(event.x)

                return true
            }
        }

        return true
    }

    private fun updateVolume(x: Float) {

        val horizontalPadding = 20f

        val startX = horizontalPadding
        val endX = width - horizontalPadding

        val clampedX =
            x.coerceIn(startX, endX)

        val percentage =
            (clampedX - startX) /
                    (endX - startX)

        val newVolume =
            (percentage * maxVolume)
                .roundToInt()
                .coerceIn(0, maxVolume)

        if (newVolume != volume) {

            volume = newVolume

            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                volume,
                0
            )

            onVolumeChanged?.invoke(volume)

            invalidate()
        }
    }

    fun refreshVolume() {

        volume = currentVolume

        invalidate()
    }
}