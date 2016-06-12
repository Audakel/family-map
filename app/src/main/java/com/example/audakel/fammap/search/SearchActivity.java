package com.example.audakel.fammap.search;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.SearchView;

import com.example.audakel.fammap.MySingleton;
import com.example.audakel.fammap.R;
import com.example.audakel.fammap.model.Searchable;

import java.util.List;

public class SearchActivity extends Activity implements SearchView.OnQueryTextListener {
    private static String GMS_SEARCH_ACTION = "com.google.android.gms.actions.SEARCH_ACTION";


    private RecyclerView mRecyclerView;
    private SearchResultAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SearchView mSearchView;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_search);


        final SearchView searchView = (SearchView)findViewById(R.id.homeSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearch(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                doSearch(s);
                return false;
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a grid layout manager
//        int numColumns = getResources().getInteger(R.integer.search_results_columns);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new SearchResultAdapter();
        mRecyclerView.setAdapter(mAdapter);

//        onNewIntent(getIntent());
    }

    protected void onNewIntent(Intent intent) {
        doSearch("bob");
    }

    private void doSearch(String query) {
        /*Uri searchUri = RecipeContentProvider.CONTENT_URI.buildUpon()
                .appendPath("search").appendEncodedPath(query).build();*/

       /* String[] projection = { RecipeTable.ID, RecipeTable.TITLE,
                RecipeTable.DESCRIPTION, RecipeTable.PHOTO,
                RecipeTable.PREP_TIME};
        Cursor cursor = getContentResolver().query(searchUri, projection, null, null, null);
        cursor.moveToFirst();*/

        mAdapter.clearResults();

        mAdapter = new SearchResultAdapter();

        for (int i = 0; i < 5; i++) {
            mAdapter.addResult(MySingleton.getFamilyMap().getEvents().get(i));
            mAdapter.addResult(MySingleton.getFamilyMap().getPeople().get(i));
        }

        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchview_in_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);

        if (mQuery != null) {
            mSearchView.setQuery(mQuery, false);
        }

        return true;
    }

    private void setupSearchView(MenuItem searchItem) {

        mSearchView.setIconifiedByDefault(false);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            mSearchView.setSearchableInfo(info);
        }

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setFocusable(false);
        mSearchView.setFocusableInTouchMode(false);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
