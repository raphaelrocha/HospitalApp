package com.ufam.hospitalapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.ufam.hospitalapp.R;
import com.ufam.hospitalapp.conn.ServerInfo;
import com.ufam.hospitalapp.conn.VolleyConnection;
import com.ufam.hospitalapp.fragments.PlaceSearchListFragment;
import com.ufam.hospitalapp.interfaces.CustomVolleyCallbackInterface;
import com.ufam.hospitalapp.provider.SearchableProvider;
import com.ufam.hospitalapp.tools.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SearchableActivity extends BaseActivity implements CustomVolleyCallbackInterface {

    private Toolbar mToolbar;
    private PlaceSearchListFragment mFrag;
    private SearchView searchView;
    private Menu menuSearch;
    private String queryToShowInSearchView;
    private VolleyConnection mVolleyConnection;
    private String query;
    private int mCONTA_SNACK_ALERT; //garante q seja exibido apenas um snackalert no erro.
    private final String TAG = SearchableActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        mVolleyConnection = new VolleyConnection(this);

        mCONTA_SNACK_ALERT=0;
        /*mToolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        mToolbar.setTitle("Buscar");//texto temporário com o nome do local
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });*/

        mToolbar = setUpToolbar("Procurar",true,false);;

        // FRAGMENT
        mFrag = (PlaceSearchListFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        if(mFrag == null) {
            mFrag = new PlaceSearchListFragment();
            //mFrag.setCategory(category);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.place_search_list_frag_containert, mFrag, "mainFrag");
            ft.commit();
        }

        hendleSearch(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        hendleSearch(intent);
    }

    public void hendleSearch( Intent intent ){

        if(Intent.ACTION_SEARCH.equalsIgnoreCase( intent.getAction() )){
            String q = intent.getStringExtra( SearchManager.QUERY );

            queryToShowInSearchView = q;
            filter(q);

            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                    SearchableProvider.AUTHORITY,
                    SearchableProvider.MODE);
            searchRecentSuggestions.saveRecentQuery(q, null);
        }
    }

    public void filter(String q){
        //mListAux.clear();
        //buscar no servidor
        Log.w(TAG,"String to query: "+q);
        showQueryOnSearchView();
        callServer(q);
    }

    @Override
    public void onStop(){
        super.onStop();
        mVolleyConnection.cancelRequest();
    }

    public void showQueryOnSearchView(){
        //EXIBE O TEXTO BUSCADO NA SEARCHVIEW
        if(searchView!=null && menuSearch!=null){
            MenuItem item = menuSearch.findItem(R.id.action_searchable_activity);
            MenuItemCompat.expandActionView(item);
            Log.i(TAG, "showQueryOnSearchView(" + queryToShowInSearchView + ")");
            //searchView.setFocusable(false);
            //searchView.setFocusableInTouchMode(false);
            searchView.setQuery(queryToShowInSearchView, false);
            searchView.clearFocus();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_searchable_activity, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        menuSearch = menu;
        MenuItem item = menu.findItem(R.id.action_searchable_activity);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //Do whatever you want
                Log.i(TAG, "expand click");

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //Do whatever you want
                Log.i(TAG, "collapse click");

                return true;
            }
        });

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){
            searchView = (SearchView) item.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView( item );
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setQueryHint(Html.fromHtml("<font color = #DCDCDC>" + getResources().getString(R.string.search_hint) + "</font>"));
        SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        theTextArea.setTextColor(Color.WHITE);//or any color that you want

        return true;
    }

    public void callServer(String query){

        showDialog("Estamos procurando pra você. Aguarde.",true);

        GPSTracker gpsTracker = getGpsTracker();
        Double lat = gpsTracker.getLatitude();
        Double lng = gpsTracker.getLongitude();

        if(lat==0 ||  lng==0){
            lat = -3.088281;//temp ufam
            lng = -59.964379;//temp ufam
        }

        Integer radius = getDistanceRadius();

        Log.i(TAG,"callServer()");
        HashMap<String, String> params = new  HashMap<String, String> ();
        params.put("user_latitude", lat.toString());
        params.put("user_longitude", lng.toString());
        params.put("radius", radius.toString());
        params.put("query", query);

        mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.BUSCA_HOSPITAIS, Request.Method.POST,false,params,"BUSCAR_HOSPUITAIS");
    }

    @Override
    public void deliveryResponse(JSONArray response, String flag) {

    }

    @Override
    public void deliveryResponse(JSONObject response, String flag) {
        hideDialog();
        showQueryOnSearchView();

        Log.i(TAG, response.toString());
        mFrag.setCardView(response);
    }

    @Override
    public void deliveryError(VolleyError error, String flag) {

        hideDialog();
        Log.i(TAG, "error conn: " + error);
        Alert("Algo deu errado.");
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume()");
        hendleSearch(getIntent());
        mCONTA_SNACK_ALERT=0;
    }
}
