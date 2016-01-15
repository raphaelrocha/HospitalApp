package com.ufam.hospitalapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.ufam.hospitalapp.conn.ServerInfo;
import com.ufam.hospitalapp.conn.VolleyConnectionQueue;
import com.ufam.hospitalapp.models.AvaliaHospital;
import com.ufam.hospitalapp.models.Bairro;
import com.ufam.hospitalapp.models.Category;
import com.ufam.hospitalapp.models.Cidade;
import com.ufam.hospitalapp.models.Comentario;
import com.ufam.hospitalapp.models.Estado;
import com.ufam.hospitalapp.models.Hospital;
import com.ufam.hospitalapp.models.Pais;
import com.ufam.hospitalapp.models.Usuario;
import com.ufam.hospitalapp.provider.SearchableProvider;
import com.ufam.hospitalapp.tools.GPSTracker;
import com.ufam.hospitalapp.tools.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    protected ProgressDialog dialog;
    private SessionManager session;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private final String TAG = BaseActivity.this.getClass().getSimpleName();
    private int DEFALT_RADIUS = 10;

    @Override
    protected void attachBaseContext(Context base) {
        Log.i(TAG, "install");
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        Fresco.initialize(this);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        VolleyConnectionQueue.getINSTANCE().startQueue(this);
        session = new SessionManager(this);

        pref = getApplicationContext().getSharedPreferences("hospitalapp.ufam.com", 0); // 0 - for private mode
        editor = pref.edit();

        //MUDA COR DA BARRA DE NAVEHAÇÃO DO SISTEMA
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    protected Context getContext() {
        return this;
    }

    public void forceStartVolleyQueue(){
        VolleyConnectionQueue.getINSTANCE().startQueue(this);
    }

    protected Activity getActivity() {
        return this;
    }



    public Usuario popUsuario(JSONObject jo) {
        Usuario user = new Usuario();

        try {
            user.setId(jo.getString("id"));
            user.setEmail(jo.getString("email"));
            user.setNome(jo.getString("nome"));

            if(jo.getString("foto").contains("graph.facebook.com")){
                user.setFotoUsuario(jo.getString("foto"));
            }else{
                user.setFotoUsuario(ServerInfo.IMAGE_FOLDER + jo.getString("foto"));
            }

            user.setSexo(jo.getString("sexo"));
            user.setSenha(jo.getString("senha"));
            user.setCpf(jo.getString("cpf"));
            user.setDataNacto(jo.getString("dt_Nascimento"));
            user.setDataRegistro(jo.getString("dt_Registro"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public Pais popPais(JSONObject jo){
        Pais p = new Pais();
        try {
            p.setId(jo.getString("id"));
            p.setNome(jo.getString("nome"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return p;
    }

    public Estado popEstado(JSONObject jo){
        Estado e = new Estado();
        try {
            e.setId(jo.getString("id"));
            e.setNome(jo.getString("nome"));
            e.setPais(popPais(jo.getJSONObject("pais")));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return e;
    }

    public Cidade popCidade(JSONObject jo){
        Cidade c = new Cidade();
        try {
            c.setId(jo.getString("id"));
            c.setNome(jo.getString("nome"));
            c.setEstado(popEstado(jo.getJSONObject("estado")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }

    public Bairro popBairro(JSONObject jo){
        Bairro b = new Bairro();
        try {
            b.setId(jo.getString("id"));
            b.setNome(jo.getString("nome"));
            b.setCidade(popCidade(jo.getJSONObject("cidade")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return b;
    }

    public Hospital popHospital(JSONObject jo){
        Hospital h = new Hospital();
        try {
            h.setId(jo.getString("id"));
            h.setNome(jo.getString("nome"));
            h.setEndereco(jo.getString("endereco"));
            h.setFotoHospital(ServerInfo.IMAGE_FOLDER+jo.getString("foto"));
            h.setLocalizacao(jo.getString("localizacao"));
            h.setLatitude(jo.getString("latitude"));
            h.setLongitude(jo.getString("longitude"));
            h.setBairro(popBairro(jo.getJSONObject("bairro")));

            if(jo.has("distancia")){
                h.setDistancia(jo.getString("distancia"));
            }

            if(jo.has("avaliacoes")){
                JSONArray ja = jo.getJSONArray("avaliacoes");
                ArrayList<AvaliaHospital> avaliaHospitals = new ArrayList<AvaliaHospital>();
                for(int i=0;i<ja.length();i++){
                    avaliaHospitals.add(popAvaliaHospital(ja.getJSONObject(i)));
                }
                h.setAvaliaHospitals(avaliaHospitals);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return h;
    }

    public AvaliaHospital popAvaliaHospital(JSONObject jo){
        Log.w(TAG,"popAvaliaHospital()");
        AvaliaHospital ah = null;
        try {
            ah = new AvaliaHospital();
            ah.setId(jo.getString("id"));
            ah.setIdHospital(jo.getString("id_hospital"));
            ah.setNota(jo.getString("nota"));
            ah.setTexto(jo.getString("comentario"));
            ah.setDataRegistro(jo.getString("dt_registro"));
            ah.setUsuario(popUsuario(jo.getJSONObject("usuario")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ah;
    }

    public Comentario popComentario(JSONObject jo){
        Comentario c = null;

        try {
            c = new Comentario();
            c.setId(jo.getString("id"));
            c.setIdHospital(jo.getString("id_Hospital"));
            c.setTexto(jo.getString("texto"));
            c.setDataRegistro(jo.getString("dt_Registro"));
            c.setUsuario(popUsuario(jo.getJSONObject("usuario")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return c;
    }


    public void showSnack(String msg){
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), null, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setTextColor(Color.WHITE);
        tv.setText(msg);
        snack.show();
    }

    public void showLongSnack(String msg){
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), null, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setTextColor(Color.WHITE);
        tv.setText(msg);
        snack.show();
    }

    public void Alert(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void longAlert(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public Toolbar setUpToolbar(String titleActivity, boolean setDisplayHomeAsUpEnabled, boolean transparent){
        //criando a toolbar
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //HABILITA MENU DE CONFIGURAÇÕES E SETINHA VOLTAR.
        if(toolbar != null){
            //setando o titulo da toolbar
            if(titleActivity.equals("")){
                toolbar.setTitle(R.string.app_name);
            }else if(titleActivity.equals("none")){
                toolbar.setTitle("");
            }else{
                toolbar.setTitle(titleActivity);
            }
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(setDisplayHomeAsUpEnabled);
            //FAZ A SETINHA VOLTAR
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
            //MUDA COR DO TITULO DO TOOLBAR
            toolbar.setTitleTextColor(0xFFFFFFFF);

            /*if(transparent == true) {
                toolbar.getBackground().setAlpha(0);
            }*/
        }

        return toolbar;
    }

    public void showDialog(String msg, boolean cancelable) {
        dialog.setCancelable(cancelable);
        if (!dialog.isShowing()){
            dialog.setMessage(msg);
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        finish();
                        dialog.dismiss();
                    }
                    return true;
                }
            });

            dialog.show();
        }
    }

    public void hideDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    public SharedPreferences.Editor getEditorPref(){
        return this.editor;
    }

    public SharedPreferences getPrefs(){
        return this.pref;
    }

    public SessionManager getSession(){
        return this.session;
    }

    public void hideKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void logoutUser(){
        session.setLogin(false);
        clearSearchHistory();
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        LoginManager.getInstance().logOut();
        //AppController.getINSTANCE().setFacebookLogin(false);

        Intent intent  = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 0);
        finish();

    }

    protected void clearSearchHistory(){
        SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                SearchableProvider.AUTHORITY,
                SearchableProvider.MODE);

        searchRecentSuggestions.clearHistory();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    public GPSTracker getGpsTracker (){
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            //Double lat = gpsTracker.getLatitude();
            //Double lng = gpsTracker.getLongitude();
            //showLongSnack(lat.toString()+lng.toString());
            /*String stringLatitude = String.valueOf(gpsTracker.latitude);
            textview = (TextView)findViewById(R.id.fieldLatitude);
            textview.setText(stringLatitude);

            String stringLongitude = String.valueOf(gpsTracker.longitude);
            textview = (TextView)findViewById(R.id.fieldLongitude);
            textview.setText(stringLongitude);

            String country = gpsTracker.getCountryName(this);
            textview = (TextView)findViewById(R.id.fieldCountry);
            textview.setText(country);

            String city = gpsTracker.getLocality(this);
            textview = (TextView)findViewById(R.id.fieldCity);
            textview.setText(city);

            String postalCode = gpsTracker.getPostalCode(this);
            textview = (TextView)findViewById(R.id.fieldPostalCode);
            textview.setText(postalCode);

            String addressLine = gpsTracker.getAddressLine(this);
            textview = (TextView)findViewById(R.id.fieldAddressLine);
            textview.setText(addressLine);*/

            return gpsTracker;
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            //gpsTracker.showSettingsAlert();
            //showLongSnack("Erro ao consultar GPS");
            return null;
        }
    }

    //raio para buscas
    public void saveDistanceRadius(int value){
        Log.w(TAG,"saveDistanceRadius(int value)");
        getEditorPref().putInt("ul-distance_radius", value); // Storing boolean - true/false
        getEditorPref().commit(); // commit changes
    }

    //raio para buscas
    public int getDistanceRadius(){
        int radius;
        try {
            radius = getPrefs().getInt("ul-distance_radius",DEFALT_RADIUS);
        }catch (Exception e){
            e.printStackTrace();
            radius = DEFALT_RADIUS;
        }
        return radius;
    }

    public Usuario getUserLoggedObj(){
        Gson gson = new Gson();
        return gson.fromJson(getPrefs().getString("ul-obj",null), Usuario.class);
    }

    public void updatePrefs(Usuario user){

        Log.w(TAG,"updatePrefs(User user)");

        getSession().setLogin(true);
        getEditorPref().putBoolean("ul", true); // Storing boolean - true/false

        Gson gson = new Gson();
        String jsonUserLogged = gson.toJson(user);

        getEditorPref().putString("ul-obj", jsonUserLogged); // Storing string
        getEditorPref().commit(); // commit changes
    }

    protected void goToHome(){
        Intent intent  = new Intent(this, PlaceListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
