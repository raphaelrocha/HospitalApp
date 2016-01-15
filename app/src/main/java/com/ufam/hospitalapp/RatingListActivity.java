package com.ufam.hospitalapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.ufam.hospitalapp.conn.ServerInfo;
import com.ufam.hospitalapp.conn.VolleyConnection;
import com.ufam.hospitalapp.fragments.RatingListFragment;
import com.ufam.hospitalapp.interfaces.CustomVolleyCallbackInterface;
import com.ufam.hospitalapp.models.AvaliaHospital;
import com.ufam.hospitalapp.models.Hospital;
import com.ufam.hospitalapp.models.Usuario;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class RatingListActivity extends BaseActivity implements CustomVolleyCallbackInterface{

    private RatingListFragment mFrag;
    private AvaliaHospital mRating;
    private Hospital mPlace;

    protected VolleyConnection mVolleyConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_list);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Avaliações e comentáraios");
        //setSupportActionBar(toolbar);

        Toolbar toolbar = setUpToolbar("Avaliações e comentáraios",true,false);

        mVolleyConnection = new VolleyConnection(this);

        mPlace = getIntent().getParcelableExtra("place");
        mRating = getIntent().getParcelableExtra("rating");


        mFrag = (RatingListFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        if(mFrag == null) {
            mFrag = new RatingListFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("place",mPlace);
            mFrag.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rating_list_frag_container, mFrag, "mainFrag");
            ft.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rating_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_rating) {
            //showLongSnack("Adicionar um comentário");
            rat_btn();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        /*if(TAG.equals("set-rat")){
            Log.i("PROFESSIONAL_PROFILE","set commentary: "+params.toString());
            //mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.SET_RATING, Request.Method.POST, params,TAG);

        }else if(TAG.equals("update-rat")){
            Log.i("PROFESSIONAL_PROFILE", "update commentary: " + params.toString());
            //Log.i("PROFESSIONAL_PROFILE","update commentary: "+getPrefs().getString("ul-id", null) + ";~;" + getProfessional().getIdProfessional()+";~;"+Math.round(value)+";~;"+comments);
           // mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.UPDATE_RATING, Request.Method.POST, params, TAG);
        }*/
    }

    @Override
    public void deliveryResponse(JSONArray response, String TAG) {

    }

    @Override
    public void deliveryResponse(JSONObject response, String TAG) {
        mFrag.callServer();
    }

    @Override
    public void deliveryError(VolleyError error, String TAG) {

    }
}
