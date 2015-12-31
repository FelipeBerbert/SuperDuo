package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

/**
 * Created by Felipe Berbert on 10/12/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NextGamesWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for(int appWidgetid : appWidgetIds){
            RemoteViews rViews = new RemoteViews(context.getPackageName(), R.layout.widget_nextgames);

            setRemoteAdapter(context, rViews);


            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent headerPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            PendingIntent itemPendingIntent = TaskStackBuilder.create(context).addNextIntentWithParentStack(intent).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            rViews.setOnClickPendingIntent(R.id.widget_header, headerPendingIntent);
            rViews.setPendingIntentTemplate(R.id.widget_match_list,itemPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetid, rViews);

        }


    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    private void setRemoteAdapter(Context context, RemoteViews views){
        views.setRemoteAdapter(0, R.id.widget_match_list, new Intent(context, NextGamesWidgetService.class));
    }
}
