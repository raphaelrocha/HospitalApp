package com.ufam.hospitalapp.provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by rli on 06/11/2015.
 */
public class SearchableProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "com.ufam.hospitalapp.provider.SearchableProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SearchableProvider(){
        setupSuggestions( AUTHORITY, MODE );
    }
}
