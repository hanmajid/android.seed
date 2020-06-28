package com.hanmajid.android.seed.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.widget.RemoteViews
import androidx.navigation.NavDeepLinkBuilder
import com.hanmajid.android.seed.R

class ChatWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val remoteViews = RemoteViews(
            context.packageName,
            R.layout.chat_widget
        )

        val args = Bundle()
        args.putString("chatId", "420")
        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.navui_nav_graph)
            .setDestination(R.id.chat_fragment)
            .setArguments(args)
            .createPendingIntent()

        remoteViews.setOnClickPendingIntent(R.id.btn_chat_widget, pendingIntent)
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)
    }
}