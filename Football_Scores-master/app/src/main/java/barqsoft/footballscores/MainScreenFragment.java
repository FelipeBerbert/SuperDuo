package barqsoft.footballscores;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public ScoresAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private String[] fragmentdate = new String[1];
    private int last_selected_item = -1;
    ListView scoreList;

    public MainScreenFragment() {
    }


    public void setFragmentDate(String date) {
        fragmentdate[0] = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        scoreList = (ListView) rootView.findViewById(R.id.scores_list);
        mAdapter = new ScoresAdapter(getActivity(), null, 0);
        scoreList.setAdapter(mAdapter);
        getLoaderManager().initLoader(SCORES_LOADER, null, this);
        mAdapter.detail_match_id = MainActivity.selected_match_id;
        scoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder selected = (ViewHolder) view.getTag();
                setSelectedMatch(selected.match_id);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity().getIntent().hasExtra("matchId")) {
            setSelectedMatch(getActivity().getIntent().getDoubleExtra("matchId", 0));
            scoreList.smoothScrollToPosition(getActivity().getIntent().getIntExtra("position", 0));
            getActivity().getIntent().removeExtra("matchId");
            scoreList.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            scoreList.setSelection(getActivity().getIntent().getIntExtra("position", 0));
                            getActivity().getIntent().removeExtra("position");
                            scoreList.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    });
        }
    }

    private void setSelectedMatch(Double matchId) {
        mAdapter.detail_match_id = matchId;
        MainActivity.selected_match_id = matchId;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(getActivity(), DatabaseContract.scores_table.buildScoreWithDate(), null, null, fragmentdate, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //Log.v(FetchScoreTask.LOG_TAG,"loader finished");
        //cursor.moveToFirst();
        /*
        while (!cursor.isAfterLast())
        {
            Log.v(FetchScoreTask.LOG_TAG,cursor.getString(1));
            cursor.moveToNext();
        }
        */

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }
        //Log.v(FetchScoreTask.LOG_TAG,"Loader query: " + String.valueOf(i));
        mAdapter.swapCursor(cursor);
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }


}
