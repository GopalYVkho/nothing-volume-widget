package com.gopal.nothingvolume

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.RemoteViews

class VolumeWidget : AppWidgetProvider() {

    companion object {

        private const val ACTION_INCREASE =
            "com.gopal.nothingvolume.INCREASE"

        fun updateWidget(
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

            // Current volume percentage
            views.setTextViewText(
                R.id.volume_percentage,
                "$percentage%"
            )

            // Progress bar
            views.setProgressBar(
                R.id.volume_progress,
                100,
                percentage,
                false
            )

            // Widget click → increase volume
            val increaseIntent = Intent(
                context,
                VolumeWidget::class.java
            ).apply {
                action = ACTION_INCREASE
            }

            val increasePendingIntent = PendingIntent.getBroadcast(
                context,
                widgetId,
                increaseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

            views.setOnClickPendingIntent(
                R.id.volume_widget,
                increasePendingIntent
            )

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

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        super.onReceive(context, intent)

        if (intent.action == ACTION_INCREASE) {

            val audioManager =
                context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            // Increase media volume by one step
            audioManager.adjustVolume(
                AudioManager.ADJUST_RAISE,
                0
            )

            // Refresh widget
            val manager =
                AppWidgetManager.getInstance(context)

            val component =
                ComponentName(
                    context,
                    VolumeWidget::class.java
                )

            val widgetIds =
                manager.getAppWidgetIds(component)

            onUpdate(
                context,
                manager,
                widgetIds
            )
        }
    }
}

