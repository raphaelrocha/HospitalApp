package com.ufam.hospitalapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import com.ufam.hospitalapp.R;
import com.ufam.hospitalapp.fragments.PlaceListFragment;
import com.ufam.hospitalapp.models.Category;
import com.ufam.hospitalapp.models.Usuario;

public class PlaceListActivity extends BaseActivity {

    private Toolbar mToolbar;
    private PlaceListFragment mFrag;
    private SearchView searchView;
    private Menu menuSearch;
    //Category mCategory;
    private Drawer navigationDrawerLeft;
    private AccountHeader headerNavigationLeft;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_place_list);

        //mCategory = getIntent().getParcelableExtra("category");

        mToolbar = setUpToolbar("Hospitais", false, false);

        /*mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Hospitais");//texto temporário com o nome do local
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });*/

        //LIB PARA CARREGAR A FOTO DO DRAWER
        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).error(R.drawable.n_perfil).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                return null;
            }
        });

        setDrawerUser(savedInstanceState,mToolbar);

        // FRAGMENT
        mFrag = (PlaceListFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        if(mFrag == null) {
            mFrag = new PlaceListFragment();
            //mFrag.setCategory(category);
            //Bundle bundle = new Bundle();
           // bundle.putParcelable("category",mCategory);
            //mFrag.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.place_list_frag_container, mFrag, "mainFrag");
            ft.commit();
        }

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMap();
            }
        });

    }

    public void hideFab(){
        mFab.hide();
    }
    public void showFab(){
        mFab.show();
    }


    private void goToMap(){
        Intent myIntent = new Intent(PlaceListActivity.this, MapsActivity.class);
        myIntent.putExtra("mode", "many");
        startActivityForResult(myIntent, 0);
    }

    private void setDrawerUser(Bundle bundle, Toolbar toolbar){

        headerNavigationLeft = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withSavedInstance(bundle)
                .withThreeSmallProfileImages(true)
                .withHeaderBackground(R.drawable.header)
                //.withTextColor(R.color.colorPrimarytext)
                .addProfiles(
                        //new ProfileDrawerItem().withName(name).withEmail(email).withIcon(getResources().getDrawable(R.drawable.user))
                        //new ProfileDrawerItem().withName(name).withEmail(email).withIcon(uriPicProf)
                        new ProfileDrawerItem().withName(getUserLoggedObj().getNome()).withIcon(getUserLoggedObj().getFotoUsuario())
                        //new ProfileDrawerItem().withName("Person Two").withEmail("person2@gmail.com").withIcon(getResources().getDrawable(R.drawable.person_2))
                        //new ProfileDrawerItem().withName("Person Three").withEmail("person3@gmail.com").withIcon(getResources().getDrawable(R.drawable.person_3)),
                        //new ProfileDrawerItem().withName("Person Four").withEmail("person4@gmail.com").withIcon(getResources().getDrawable(R.drawable.person_4))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                        //Toast.makeText(CategoryActivity.this, "onProfileChanged: ", Toast.LENGTH_SHORT).show();
                        //headerNavigationLeft.setBackgroundRes(R.drawable.camaro);
                        //goToProfileUser();
                        return false;
                    }
                })

                .build();
        navigationDrawerLeft = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                //.withDisplayBelowToolbar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(bundle)
                .withSelectedItem(-1)
                //.withFullscreen(true)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerNavigationLeft)
                    /*.withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                        @Override
                        public boolean onNavigationClickListener(View view) {
                            return false;
                        }
                    })*/
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {


                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //Alert("teste"+position);
                        //return false;

                        //for (int count = 0, tam = navigationDrawerLeft.getDrawerItems().size(); count < tam; count++) {
                        //if (count == mPositionClicked && mPositionClicked <= 6) {
                        if (position == 1) {
                            //MAPA
                            goToMap();
                        }
                        else  if (position == 2) {
                            //AJSUTES
                            //Toast.makeText(MainActivity.this, "click: " + i, Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(PlaceListActivity.this, AdjustActivity.class);
                            startActivityForResult(myIntent, 0);
                        }
                        else if (position == 3) {
                            //SAIR
                            //Toast.makeText(MainActivity.this, "click: " + i, Toast.LENGTH_SHORT).show();
                            logoutUser();
                        } else if (position == 4) {
                            //SAIR
                            //Toast.makeText(MainActivity.this, "click: " + i, Toast.LENGTH_SHORT).show();
                            clearSearchHistory();
                            Alert("Suas buscas recentes foram apagadas");
                        }
                        //PrimaryDrawerItem aux = (PrimaryDrawerItem) navigationDrawerLeft.getDrawerItems().get(count);
                        //aux.setIcon(getResources().getDrawable( getCorretcDrawerIcon( count, false ) ));

                        // break;
                        //}
                        //}

                        /*if(i <= 3){
                            ((PrimaryDrawerItem) iDrawerItem).setIcon(getResources().getDrawable( getCorretcDrawerIcon( i, true ) ));
                        }*/

                        //mPositionClicked = position;
                        navigationDrawerLeft.getAdapter().notifyDataSetChanged();
                        return true;
                    }


                })
                /*.withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        //Toast.makeText(CategoryActivity.this, "onItemLongClick: " + i, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })*/
                /*.addStickyDrawerItems(
                        new SecondaryDrawerItem().withName("Configurações").withIcon(FontAwesome.Icon.faw_cog).withIdentifier(0),
                        //new SecondaryDrawerItem().withName("teste").withIcon(FontAwesome.Icon.faw_github)
                )*/
                .build();

        /*if(getSession().isLoggedIn()){
            navigationDrawerLeft.addItem(new SectionDrawerItem().withName(name));
        }*/

        //navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(R.string.drawer_find).withIcon(getResources().getDrawable(R.drawable.magnify)));
        //navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(R.string.drawer_favorites).withIcon(getResources().getDrawable(R.drawable.star)));
        //navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(R.string.drawer_find_in_map).withIcon(getResources().getDrawable(R.drawable.map)));
        //navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(R.string.drawer_popular).withIcon(getResources().getDrawable(R.drawable.magnify_plus)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName(R.string.drawer_find_in_map).withIcon(getResources().getDrawable(R.drawable.map)));
        //navigationDrawerLeft.addItem(new DividerDrawerItem());
        //navigationDrawerLeft.addItem(new SecondaryDrawerItem().withName(R.string.drawer_configuration).withIcon(getResources().getDrawable(R.drawable.settings)));
        //navigationDrawerLeft.addItem(new SecondaryDrawerItem().withName(R.string.drawer_configuration).withIcon(getResources().getDrawable(R.drawable.settings)));
        navigationDrawerLeft.addItem(new SecondaryDrawerItem().withName(R.string.drawer_adjust).withIcon(getResources().getDrawable(R.drawable.settings)));
        navigationDrawerLeft.addItem(new SecondaryDrawerItem().withName(R.string.drawer_logout).withIcon(getResources().getDrawable(R.drawable.logout)));
        navigationDrawerLeft.addItem(new SecondaryDrawerItem().withName(R.string.drawer_clear_search_history).withIcon(getResources().getDrawable(R.drawable.ic_delete_variant)));
        //navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Configurações"));
        //navigationDrawerLeft.addItem(new SwitchDrawerItem().withName("Notificação").withChecked(true).withOnCheckedChangeListener(mOnCheckedChangeListener));
        //navigationDrawerLeft.addItem(new ToggleDrawerItem().withName("News").withChecked(true).withOnCheckedChangeListener(mOnCheckedChangeListener));


        //rq = Volley.newRequestQueue(MainActivity.this);
        //callByJsonArrayRequest(url,"get-all-pro","");

        hideDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_list, menu);

        menuSearch = menu;

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_searchable_activity);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //Do whatever you want
                Log.i("Expand", "click");

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //Do whatever you want
                Log.i("Collapse", "click");

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_add_new_place) {
            //showLongSnack("Adicionar um comentário");
            newPlace();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void newPlace(){
        Intent intent = new Intent(this,FormPlaceActivity.class);
        //intent.putExtra("category",mCategory);
        startActivityForResult(intent,0);
    }
}
