package com.gopal.nothingvolume

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.media.AudioManager
import android.widget.RemoteViews

class VolumeWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { widgetId ->
            updateWidget(
                context,
                appWidgetManager,
                widgetId
            )
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        widgetId: Int
    ) {
        val audioManager =
            context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val currentVolume =
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        val maxVolume =
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        val percentage =
            if (maxVolume > 0) {
                (currentVolume * 100) / maxVolume
            } else {
                0
            }

        val views = RemoteViews(
            context.packageName,
            R.layout.widget_volume
        )

        views.setTextViewText(
            R.id.volume_percentage,
            "$percentage%"
        )

        views.setProgressBar(
            R.id.volume_progress,
            100,
            percentage,
            false
        )

        appWidgetManager.updateAppWidget(
            widgetId,
            views
        )
    }
}