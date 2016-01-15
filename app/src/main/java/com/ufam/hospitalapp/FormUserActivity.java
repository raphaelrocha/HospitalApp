package com.ufam.hospitalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ufam.hospitalapp.R;
import com.ufam.hospitalapp.conn.ServerInfo;
import com.ufam.hospitalapp.conn.VolleyConnection;
import com.ufam.hospitalapp.interfaces.CustomVolleyCallbackInterface;
import com.ufam.hospitalapp.models.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FormUserActivity extends BaseActivity implements CustomVolleyCallbackInterface{


    private Toolbar mToolbar;
    private EditText edtName, edtLogin, edtPasswd, edtPasswd2;
    private VolleyConnection mVolleyConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_user);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Novo usuário");//texto temporário com o nome do local
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
        });

        mVolleyConnection = new VolleyConnection(this);

        Button btnSave = (Button) findViewById(R.id.btn_save);
        edtName = (EditText) findViewById(R.id.edt_user_name);
        edtLogin = (EditText) findViewById(R.id.edt_login_user);
        edtPasswd = (EditText) findViewById(R.id.edt_login_passwd);
        edtPasswd2 = (EditText) findViewById(R.id.edt_login_passwd_2);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

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

        Intent intent = new Intent(FormUserActivity.this, PlaceListActivity.class);
        intent.putExtra("user",userLogged);
        startActivity(intent);
        hideDialog();
        finish();
    }

    private void save(){
        String senha = edtPasswd.getText().toString();
        String senha_ck = edtPasswd2.getText().toString();
        if (senha.equals(senha_ck)){
            String name = edtName.getText().toString().trim();
            String login = edtLogin.getText().toString().trim();
            String passwd = edtPasswd.getText().toString().trim();
            String passwd2 = edtPasswd2.getText().toString().trim();

            HashMap<String,String> params = new HashMap<String,String>();
            //params.put("name",name);
            //params.put("login",login);
            //params.put("passwd",passwd);
            //params.put("passwd2", passwd2);

            params.put("nome",name);
            params.put("email",login);
            params.put("senha",passwd);
            params.put("foto","1.jpg");
            params.put("cpf","0000000000");
            params.put("dtNasc","2001-01-01");
            params.put("sexo","0");

            showLongSnack("Salvando novo usuário...");
            mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.CRIA_USUARIO, Request.Method.POST, false, params, "CRIA_USUARIO");
        }else{
            showLongSnack("Desculpe, mas a senha deve ser a mesma nos dois campos de preenchimento!");
            edtPasswd2.getOnFocusChangeListener();
        }


    }

    @Override
    public void deliveryResponse(JSONArray response, String TAG) {

    }

    @Override
    public void deliveryResponse(JSONObject response, String TAG) {
        hideDialog();

        try {
            setLogin(response.getJSONObject("usuario"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deliveryError(VolleyError error, String TAG) {
        Alert("Problemas com a internet.");
    }
}
