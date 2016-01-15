package com.ufam.hospitalapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ufam.hospitalapp.conn.ServerInfo;
import com.ufam.hospitalapp.conn.VolleyConnection;
import com.ufam.hospitalapp.interfaces.CustomVolleyCallbackInterface;
import com.ufam.hospitalapp.models.AvaliaHospital;
import com.ufam.hospitalapp.models.Hospital;
import com.ufam.hospitalapp.models.Usuario;
import com.ufam.hospitalapp.tools.DataUrl;
import com.ufam.hospitalapp.tools.RatingAvgCalculator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PlaceActivity extends BaseActivity implements CustomVolleyCallbackInterface{

    private TextView tvDescription;
    //private ViewGroup mRoot;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Hospital mPlace;
    private SimpleDraweeView ivPlace;
    private float scale;
    private int width, height, roundPixels;
    private VolleyConnection mVolleyConnection;
    private ImageButton btFavorite;
    private TextView btMap, btRat, btAlbum, btComments;
    private AvaliaHospital mRating;
    private TextView mTvAverage;
    private TextView mTotalRating;
    private RatingBar mRatingBarAverage;
    private final String TAG = PlaceActivity.this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("APP","SDK: "+Build.VERSION.SDK_INT);
        // TRANSITIONS
        /*if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            Log.i("APP","Entrou na TRANSITIONS");
                /*Explode trans1 = new Explode();
                trans1.setDuration(3000);
                Fade trans2 = new Fade();
                trans2.setDuration(3000);

                getWindow().setEnterTransition( trans1 );
                getWindow().setReturnTransition( trans2 );*

            TransitionInflater inflater = TransitionInflater.from( this );
            Transition transition = inflater.inflateTransition( R.transition.transitions );

            getWindow().setSharedElementEnterTransition(transition);

            Transition transition1 = getWindow().getSharedElementEnterTransition();
            transition1.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    Log.i("APP", "onTransitionStart()");
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    Log.i("APP", "onTransitionEnd()");
                    TransitionManager.beginDelayedTransition(mRoot, new Slide());
                    tvDescription.setVisibility( View.VISIBLE );
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    Log.i("APP", "onTransitionCancel()");
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    Log.i("APP", "onTransitionPause()");
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    Log.i("APP", "onTransitionResume()");
                }
            });
        }*/

        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_place);

        mVolleyConnection = new VolleyConnection(this);

        mPlace = getIntent().getExtras().getParcelable("place");

        /*if(savedInstanceState != null){
            mPlace = savedInstanceState.getParcelable("place");
        }
        else {
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getParcelable("place") != null) {
                mPlace = getIntent().getExtras().getParcelable("place");
            } else {
                Toast.makeText(this, "Fail!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }*/

        btFavorite = (ImageButton) findViewById(R.id.bt_favorite);
        btMap = (TextView) findViewById(R.id.button_location);
        btRat = (TextView) findViewById(R.id.button_rat);
        //btAlbum = (TextView) findViewById(R.id.button_album);
        btComments = (TextView) findViewById(R.id.button_comment);


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //mCollapsingToolbarLayout.setTitle("1Teatro Amazonas");

        //mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
        mCollapsingToolbarLayout.setTitle(mPlace.getNome());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TransparentText);
        //mCollapsingToolbarLayout.setExpandedTitleTextAppearance(getResources().getColor(R.color.transparent));

        Toolbar localToolbar = setUpToolbar("", true, false);

        /*mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //mToolbar.setTitle(mPlace.getName());//texto temporário com o nome do local
        setSupportActionBar(mToolbar);
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

        tvDescription = (TextView) findViewById(R.id.place_tv_description);
        ivPlace = (SimpleDraweeView) findViewById(R.id.iv_place);
        mTotalRating = (TextView) findViewById(R.id.tv_total_rating);
        mTvAverage = (TextView) findViewById(R.id.reputation_average_pro);
        mRatingBarAverage = (RatingBar) findViewById(R.id.ratbar_pro_reputation);


        //////drawee

        scale = this.getResources().getDisplayMetrics().density;
        width = this.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;

        roundPixels = (int)(2 * scale + 0.5f);

        int w = 0;
        if( ivPlace.getLayoutParams().width == FrameLayout.LayoutParams.MATCH_PARENT
                || ivPlace.getLayoutParams().width == FrameLayout.LayoutParams.WRAP_CONTENT){

            Display display = this.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize( size );

            try{
                w = size.x;
            }
            catch( Exception e ){
                w = display.getWidth();
            }
        }

        Uri uri = Uri.parse(DataUrl.getUrlCustom(mPlace.getFotoHospital(), w));
        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri( uri )
                .setOldController( ivPlace.getController() )
                .build();

        RoundingParams rp = RoundingParams.fromCornersRadii(roundPixels, roundPixels, 0, 0);
        ivPlace.setController(dc);
        ivPlace.getHierarchy().setRoundingParams(rp);


        ////fim drawee

        TextView tvPlaceName = (TextView) findViewById(R.id.place_tv_name);
        TextView tvPlaceCity = (TextView) findViewById(R.id.place_tv_city);

        //tvPlaceName.setText(getResources().getString(R.string.tmp_place_name));//texto temporario com o nome do local
        //tvPlaceCity.setText(getResources().getString(R.string.tmp_place_city));//texto temporario com o nome da cidade

        tvPlaceName.setText(mPlace.getNome());//texto temporario com o nome do local
        String cityDistrict = mPlace.getBairro().getCidade().getNome() + "/" + mPlace.getBairro().getCidade().getEstado().getNome();
        tvPlaceCity.setText(cityDistrict);//texto temporario com o nome da cidade

        tvPlaceName.setTypeface(null, Typeface.BOLD);

        //tvDescription.setText(getResources().getString(R.string.tmp_place_description));//texto temporário de descrição
        String fullAddr = mPlace.getEndereco()+ " - "+mPlace.getBairro().getNome();
        tvDescription.setText(fullAddr);
        //tvDescription.setVisibility(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || savedInstanceState != null ? View.VISIBLE : View.INVISIBLE);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Exibir no mapa", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                showMap();

            }
        });*/


        //setIconOptions();

        setIconOptions();
        getRating();
        //checkFavorite(mPlace);
        setViewRating();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_home:

                // Complete with your code
                goToHome();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setViewRating(){
        ArrayList<AvaliaHospital> ratings = mPlace.getAvaliaHospitals();
        if(ratings!=null){
            Float avg = new RatingAvgCalculator().calc(ratings);
            String value = new RatingAvgCalculator().formatValue(avg);
            mTvAverage.setText(value);
            mTotalRating.setText(ratings.size()+" "+" "+this.getResources().getString(R.string.total_vote_rating));
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                Log.w("Profile","Rating < LOLLIPOP");
                LayerDrawable stars = (LayerDrawable) mRatingBarAverage.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.md_blue_grey_100), PorterDuff.Mode.SRC_IN);
                int aux = avg.intValue();
                mRatingBarAverage.setRating(aux);
            }else{
                Log.w("Profile","Rating >= LOLLIPOP");
                mRatingBarAverage.setRating(avg);
            }
        }else{
            mTvAverage.setText("0,0");
        };
    }

    private void changeFavIcon(boolean b){
        if(b){
            btFavorite.setImageResource(R.drawable.favorite_on);
            btFavorite.setTag(R.drawable.favorite_on);
        }else{
            btFavorite.setImageResource(R.drawable.favorite_off);
            btFavorite.setTag(R.drawable.favorite_off);
        }

        setIconOptions();
    }


    public void setIconOptions(){

        //changeFavIcon(false);

        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int resource = (int) btFavorite.getTag();
                if (resource == R.drawable.favorite_off) {
                    Log.i("PROFESSIONAL_PROFILE", "clicou favorito: favOff para favOn");
                    btFavorite.setImageResource(R.drawable.favorite_on);
                    btFavorite.setTag(R.drawable.favorite_on);
                    //setFavorite(mPlace);
                } else if (resource == R.drawable.favorite_on) {
                    Log.i("PROFESSIONAL_PROFILE", "clicou favorito: favOn para favOff");
                    btFavorite.setImageResource(R.drawable.favorite_off);
                    btFavorite.setTag(R.drawable.favorite_off);
                    //unSetFavorite(mPlace);
                }
            }
        });

        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();
            }
        });
        btRat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rat_btn();
            }
        });
        /*btAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                album_btn();
            }
        });*/
        btComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_btn();
            }
        });

    }

    private void album_btn(){
        showLongSnack("Aqui vai abrir a tela de fotos.");
    }

    private void comment_btn(){
        Intent intent = new Intent(this, CommentaryListActivity.class);
        intent.putExtra("place",mPlace);
        startActivity(intent);
    }

    public void getRating(){
        Usuario userLogged = getUserLoggedObj();
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("id_usuario", userLogged.getId());
        params.put("id_hospital",mPlace.getId());
        mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.VERIFICA_AVALIACAO, Request.Method.POST,false, params, "VERIFICA_AVALIACAO");
        Log.i("APP", "Pegoou rating: " + params.toString());
    }

    public void checkFavorite(Hospital p){
        Usuario userLogged = getUserLoggedObj();
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("id_user", userLogged.getId());
        params.put("id_place",p.getId());
        //mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.CHECK_FAVORITE, Request.Method.POST, params, "chk-fav");
        Log.i("APP", "Verificou favorito: " + params.toString());
    }

    public void setFavorite(Hospital p){
        Usuario userLogged = getUserLoggedObj();
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("id_user", userLogged.getId());
        params.put("id_place",p.getId());
        //mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.SET_FAVORITE, Request.Method.POST, params, "set-fav");
        Log.i("APP", "Marcou favorito: " + params.toString());
    }

    public void unSetFavorite(Hospital p){
        Usuario userLogged = getUserLoggedObj();
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("id_user", userLogged.getId());
        params.put("id_place",p.getId());
        //mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.UNSET_FAVORITE, Request.Method.POST, params, "unset-fav");
        Log.i("APP", "Desmarcou favorito: " + params.toString());
    }

    private void showMap(){
        //Intent intent = new Intent(this, MapActivity.class);
        //intent.putExtra("place", mPlace);
        //this.startActivity(intent);
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("mode", "one");
        intent.putExtra("place",mPlace);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        mPlace = getIntent().getExtras().getParcelable("place");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlace = getIntent().getExtras().getParcelable("place");
    }

    public void rat_btn(){
        final Dialog rankDialog;
        final RatingBar ratingBar;
        Log.i("PROFESSIONAL_PROFILE","rat_bnt()");
        String TAG = "set-rat";
        rankDialog = new Dialog(this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.dialog_rating);
        rankDialog.setCancelable(true);
        ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(0);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        text.setText(mPlace.getNome());

        final EditText comments = (EditText) rankDialog.findViewById(R.id.edt_comment_rat);
        final TextView feedback = (TextView) rankDialog.findViewById(R.id.rating_feedback);
        comments.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        if(mRating==null){
            mRating = new AvaliaHospital();
        }
        if(mRating.getNota()!=null){
            ratingBar.setRating(Float.parseFloat(mRating.getNota()));
            TAG = "update-rat";
            if(mRating.getTexto()!=null){
                comments.setText(mRating.getTexto());
            }

            float rating = Float.parseFloat(mRating.getNota());
            ratingBar.setRating(rating);
            if(rating<=1){
                feedback.setText("Odiei");
            }
            else if(rating>1 && rating<=2){
                feedback.setText("Não gostei");
            }
            else if(rating>2 && rating<=3){
                feedback.setText("Razoável");
            }
            else if(rating>3 && rating<=4){
                feedback.setText("Bom");
            }
            else if(rating>4 && rating<=5){
                feedback.setText("Excelente");
            }
        }else{
            mRating = new AvaliaHospital();
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating<=1){
                    feedback.setText("Odiei");
                }
                else if(rating>1 && rating<=2){
                    feedback.setText("Não gostei");
                }
                else if(rating>2 && rating<=3){
                    feedback.setText("Razoável");
                }
                else if(rating>3 && rating<=4){
                    feedback.setText("Bom");
                }
                else if(rating>4 && rating<=5){
                    feedback.setText("Excelente");
                }
            }
        });
        if(mRating.getNota()!=null){
            ratingBar.setRating(Float.parseFloat(mRating.getNota()));
            TAG = "update-rat";
            if(mRating.getTexto()!=null){
                comments.setText(mRating.getTexto());
            }
        }else{
            mRating = new AvaliaHospital();
        }

        final String SEND_TAG = TAG;
        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ratingBar.getRating();
                Log.i("RANK DIALOG","CLICK");
                Usuario user = getUserLoggedObj();
                mRating.setUsuario(user);
                mRating.setIdHospital(mPlace.getId());
                mRating.setNota(Integer.toString(Math.round(ratingBar.getRating())));
                mRating.setTexto(comments.getText().toString().trim());
                //EditText comments = (EditText) rankDialog.findViewById(R.id.edt_comment_rat);
                setRating(mRating);
                rankDialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }

    public void setRating(AvaliaHospital rating){

        HashMap<String,String> params = new HashMap<String,String>();
        params.put("id_usuario",getUserLoggedObj().getId());
        params.put("id_hospital",mPlace.getId());
        params.put("nota",rating.getNota());
        params.put("texto",rating.getTexto());

        mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.AVALIA, Request.Method.POST, false, params,"AVALIA");

        /*if(TAG.equals("set-rat")){
            Log.i("PROFESSIONAL_PROFILE","set commentary: "+params.toString());
            //mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.SET_RATING, Request.Method.POST, params,TAG);

        }else if(TAG.equals("update-rat")){
            Log.i("PROFESSIONAL_PROFILE", "update commentary: " + params.toString());
            //Log.i("PROFESSIONAL_PROFILE","update commentary: "+getPrefs().getString("ul-id", null) + ";~;" + getProfessional().getIdProfessional()+";~;"+Math.round(value)+";~;"+comments);
            //mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.UPDATE_RATING, Request.Method.POST, params, TAG);
        }*/
    }

    private void parseAvaliacao(JSONObject jo){
        Log.i(TAG,"parseAvaliacao()");
        try {
            boolean b = jo.getBoolean("success");

            if(b){
                Log.i(TAG,"true");
                if(mRating==null){
                    mRating = new AvaliaHospital();
                }
                mRating = popAvaliaHospital(jo.getJSONObject("avaliacao"));
                setViewRating();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseAvaliacoes(JSONObject jo){
        try {
            boolean b = jo.getBoolean("success");
            if(b){
                JSONArray ja = jo.getJSONArray("avaliacoes");
                ArrayList<AvaliaHospital> avaliaHospitals = new ArrayList<AvaliaHospital>();
                for(int i=0;i<ja.length();i++){
                    AvaliaHospital ah = popAvaliaHospital(ja.getJSONObject(i));
                    avaliaHospitals.add(ah);
                }
                mPlace.setAvaliaHospitals(avaliaHospitals);
                setViewRating();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void listarAvaliacoes(){
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("id_hospital",mPlace.getId());

        mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.LISTA_AVALIACOES, Request.Method.POST,false,params,"LISTA_AVALIACOES");
    }


    @Override
    public void deliveryResponse(JSONArray response, String TAG) {

    }

    @Override
    public void deliveryResponse(JSONObject response, String flag) {
        if(flag.equals("chk-fav")){
            //changeFavIcon(Boolean.parseBoolean(id));
        }
        else if(flag.equals("AVALIA")){
            showLongSnack("Você avaliou este local. Obrigado.");
            getRating();
            listarAvaliacoes();
        }
        else if(flag.equals("update-rat")){
            showLongSnack("Você avaliou este local. Obrigado.");
        }
        else if(flag.equals("set-fav")){
            showLongSnack(mPlace.getNome()+" foi adicionado a sua lista de locais favoritos.");
        }
        else if(flag.equals("unset-fav")){
            showLongSnack(mPlace.getNome()+" foi removido da sua lista de locais favoritos.");
        }
        else if(flag.equals("VERIFICA_AVALIACAO")){
            Log.i(TAG,"sucess verifica avaliacao");
            parseAvaliacao(response);
        }else if(flag.equals("LISTA_AVALIACOES")){
            parseAvaliacoes(response);
        }
        Log.i("RESPONSE",response.toString());
    }

    @Override
    public void deliveryError(VolleyError error, String TAG) {

    }
}
