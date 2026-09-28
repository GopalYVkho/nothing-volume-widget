package com.gopal.nothingvolume

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.media.AudioManager
import android.os.Bundle
import android.widget.TextView

class VolumeControlActivity : Activity() {

    private lateinit var volumeSlider: VolumeSlider
    private lateinit var volumePercentage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_volume_control)

        volumeSlider = findViewById(
            R.id.volume_slider
        )

        volumePercentage = findViewById(
            R.id.volume_percentage
        )

        updatePercentage()

        volumeSlider.onVolumeChanged = {
            updatePercentage()
        }
    }

    private fun updatePercentage() {

        val audioManager =
            getSystemService(AUDIO_SERVICE) as AudioManager

        val currentVolume =
            audioManager.getStreamVolume(
                AudioManager.STREAM_MUSIC
            )

        val maxVolume =
            audioManager.getStreamMaxVolume(
                AudioManager.STREAM_MUSIC
            )

        val percentage =
            if (maxVolume > 0) {
                (currentVolume * 100) / maxVolume
            } else {
                0
            }

        volumePercentage.text = "$percentage%"
    }

    override fun onPause() {
        super.onPause()

        updateWidget()
    }

    private fun updateWidget() {

        val appWidgetManager =
            AppWidgetManager.getInstance(this)

        val componentName =
            ComponentName(
                this,
                VolumeWidget::class.java
            )

        val widgetIds =
            appWidgetManager.getAppWidgetIds(
                componentName
            )

        VolumeWidget().onUpdate(
            this,
            appWidgetManager,
            widgetIds
        )
    }

    override fun onResume() {
        super.onResume()

        if (::volumeSlider.isInitialized) {
            volumeSlider.refreshVolume()
            updatePercentage()
        }
    }
}