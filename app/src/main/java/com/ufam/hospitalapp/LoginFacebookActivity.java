package com.ufam.hospitalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.facebook.Profile;
import com.google.gson.Gson;
import com.ufam.hospitalapp.conn.ServerInfo;
import com.ufam.hospitalapp.conn.VolleyConnection;
import com.ufam.hospitalapp.interfaces.CustomVolleyCallbackInterface;
import com.ufam.hospitalapp.models.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginFacebookActivity extends BaseActivity implements CustomVolleyCallbackInterface {

    private Profile mOldProfile, mNewProfile;
    private VolleyConnection mVolleyConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login_facebook);

        //setCustomVolleyCallbackInterface(this);
        mVolleyConnection = new VolleyConnection(this);

        showDialog("Configurando tudo para você, Aguarde..",false);
        /*mOldProfile = AppController.getINSTANCE().getOldProfile();
        mNewProfile = AppController.getINSTANCE().getNewProfile();*/

        mNewProfile = getIntent().getParcelableExtra("newProfile");

        if(mNewProfile!=null){
            String name, email, birth, sex, picture_profile,socialnet,passwd1;
            Usuario newUser = new Usuario();

            name = mNewProfile.getName();
            email = mNewProfile.getId()+"@userfacebook.com";
            birth = "2015/01/01";
            sex = "0";
            picture_profile = mNewProfile.getProfilePictureUri(300,300).toString();
            socialnet = mNewProfile.getLinkUri().toString();
            passwd1 = "tweghcewuy98378@hisgad87";

            newUser.setNome(name);
            newUser.setEmail(email);
            newUser.setDataNacto(birth);
            newUser.setSexo(sex);
            newUser.setFotoUsuario(picture_profile);
            newUser.setSenha(passwd1);
            newUser.setCpf("00000000000");

            Log.i("LOGIN fb", picture_profile);
            callServer(newUser);
        }else{
            finish();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_facebook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void setLogin(JSONObject jo){

        Usuario userLogged = null;
        userLogged = popUsuario(jo);
        getSession().setLogin(true);
        getEditorPref().putBoolean("ul", true); // Storing boolean - true/false

        Gson gson = new Gson();
        String jsonUserLogged = gson.toJson(userLogged);

        getEditorPref().putString("ul-obj", jsonUserLogged); // Storing string

        getEditorPref().commit(); // commit changes
        hideDialog();

        Intent intent = new Intent(LoginFacebookActivity.this, PlaceListActivity.class);
        intent.putExtra("user",userLogged);
        startActivity(intent);
        hideDialog();
        finish();
    }

    public void callServer(Usuario newUser) {
        showDialog("Estamos preparando tudo para você. Aguarde...",false);
        //JSONObject jou = creteJSONObjectUser(name, birth, email, sex, picture_profile, socialnet, passwd1, "0", ServerInfo.fileImageExt);

        HashMap<String,String> params = new HashMap<String,String>();
        params.put("nome",newUser.getNome());
        params.put("email",newUser.getEmail());
        params.put("senha",newUser.getSenha());
        params.put("foto",newUser.getFotoUsuario());
        params.put("cpf",newUser.getCpf());
        params.put("dtNasc",newUser.getDataRegistro());
        params.put("sexo",newUser.getSexo());

        mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.CRIA_USUARIO, Request.Method.POST, false, params, "CRIA_USUARIO");
    }

    @Override
    public void onStop(){
        super.onStop();
        mVolleyConnection.cancelRequest();
        mNewProfile=null;
    }

    //TRATA RESPOSTAS DE SUCESSO
    public void deliveryResponse(JSONArray response, String flag){

    }

    @Override
    public void deliveryResponse(JSONObject response, String flag) {
        hideDialog();

        try {
            setLogin(response.getJSONObject("usuario"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //TRATA RESPOSTAS DE ERRO
    public void deliveryError(VolleyError error, String flag){
        Alert("Problemas com a internet.");
    }
}
