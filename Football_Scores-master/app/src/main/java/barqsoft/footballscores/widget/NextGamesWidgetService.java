package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;


/**
 * Created by Felipe Berbert on 11/12/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NextGamesWidgetService extends RemoteViewsService {
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor cursor = null;

            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (cursor != null)
                    cursor.close();
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                String[] date = {mformat.format(new Date(System.currentTimeMillis()))};
                final long idToken = Binder.clearCallingIdentity();
                cursor = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null, null, date, null);
                Binder.restoreCallingIdentity(idToken);

            }

            @Override
            public void onDestroy() {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }

            @Override
            public int getCount() {
                if (cursor != null)
                    return cursor.getCount();
                else
                    return 0;
            }

            @Override
            public RemoteViews getViewAt(int i) {
                if (cursor == null || i == AdapterView.INVALID_POSITION || !cursor.moveToPosition(i)) {
                    return null;
                }
                RemoteViews rViews = new RemoteViews(getPackageName(), R.layout.widget_scores_list_item);

                //todo remotecontentdescription
                rViews.setTextViewText(R.id.home_name, cursor.getString(COL_HOME));
                rViews.setTextViewText(R.id.away_name, cursor.getString(COL_AWAY));
                rViews.setTextViewText(R.id.data_textview, cursor.getString(COL_MATCHTIME));
                rViews.setTextViewText(R.id.score_textview, Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
                rViews.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(cursor.getString(COL_HOME)));
                rViews.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(cursor.getString(COL_AWAY)));

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra("matchId", cursor.getDouble(COL_ID));
                fillInIntent.putExtra("position", i);
                rViews.setOnClickFillInIntent(R.id.list_item, fillInIntent);
                return rViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.scores_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                if (cursor.moveToPosition(i))
                    return cursor.getLong(COL_ID);
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
