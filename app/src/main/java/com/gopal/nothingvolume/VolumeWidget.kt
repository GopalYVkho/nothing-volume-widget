package com.gopal.nothingvolume

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.RemoteViews

class VolumeWidget : AppWidgetProvider() {

    companion object {

        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            widgetId: Int
        ) {
            // Get AudioManager
            val audioManager =
                context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            // Current media volume
            val currentVolume =
                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

            // Maximum media volume
            val maxVolume =
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

            // Convert volume to percentage
            val percentage =
                if (maxVolume > 0) {
                    (currentVolume * 100) / maxVolume
                } else {
                    0
                }

            // Create widget views
            val views = RemoteViews(
                context.packageName,
                R.layout.widget_volume
            )

            // Show current percentage
            views.setTextViewText(
                R.id.volume_percentage,
                "$percentage%"
            )

            // Update progress bar
            views.setProgressBar(
                R.id.volume_progress,
                100,
                percentage,
                false
            )

            // --------------------------------
            // Open Volume Control Activity
            // --------------------------------

            val activityIntent = Intent(
                context,
                VolumeControlActivity::class.java
            )

            val activityPendingIntent = PendingIntent.getActivity(
                context,
                widgetId,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

            // Make the entire widget clickable
            views.setOnClickPendingIntent(
                R.id.volume_widget,
                activityPendingIntent
            )

            // Update widget
            appWidgetManager.updateAppWidget(
                widgetId,
                views
            )
        }
    }

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
}

