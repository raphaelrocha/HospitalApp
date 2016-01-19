package com.ufam.hospitalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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

        mToolbar = setUpToolbar("Criar conta",true,false);
        /*mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
        });*/

        mVolleyConnection = new VolleyConnection(this);

        Button btnSave = (Button) findViewById(R.id.btn_save);
        edtName = (EditText) findViewById(R.id.edt_user_name);
        edtLogin = (EditText) findViewById(R.id.edt_login_user);
        edtPasswd = (EditText) findViewById(R.id.edt_login_passwd);
        edtPasswd2 = (EditText) findViewById(R.id.edt_login_passwd_2);
        edtName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edtName.requestFocus();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validade();
            }
        });

        hideKeyboard();
    }

    private void validade(){
        String name = edtName.getText().toString().trim();
        String email = edtLogin.getText().toString().trim();
        String passwd = edtPasswd.getText().toString().trim();
        String passwd2 = edtPasswd2.getText().toString().trim();

        boolean valid = true;
        String msg = "";

        if(name.equals("")){
            valid = false;
            if(msg.equals("")){
                msg = msg+"\"Informe seu nome.";
            }else{
                msg = msg+"\n\"Informe seu nome.";
            }
        }

        if(email.equals("")){
            valid = false;
            if(msg.equals("")){
                msg = msg+"\"Informe seu e-mail.";
            }else{
                msg = msg+"\n\"Informe seu e-mail.";
            }
        }

        if(passwd.equals("")){
            valid = false;
            if(msg.equals("")){
                msg = msg+"\"Informe sua senha.";
            }else{
                msg = msg+"\n\"Informe sua senha.";
            }
        }

        if(passwd2.equals("")){
            valid = false;
            if(msg.equals("")){
                msg = msg+"\"Repita sua senha.";
            }else{
                msg = msg+"\n\"Repita sua senha.";
            }
        }

        if(!passwd.equals(passwd2)){
            valid = false;
            if(msg.equals("")){
                msg = msg+"\"As senhas devem ser iguais.";
            }else{
                msg = msg+"\n\"As senhas devem ser iguais.";
            }
        }

        if(valid){
            Usuario u = new Usuario();
            u.setNome(name);
            u.setEmail(email);
            u.setSenha(passwd);
            u.setFotoUsuario("user.jpg");
            u.setCpf("00000000000");
            u.setDataNacto("2001-01-01");
            u.setSexo("0");
            save(u);
        }else{
            longAlert(msg);
        }
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

    private void save(Usuario u){

        HashMap<String,String> params = new HashMap<String,String>();

        params.put("nome",u.getNome());
        params.put("email",u.getEmail());
        params.put("senha",u.getSenha());
        params.put("foto",u.getFotoUsuario());
        params.put("cpf",u.getCpf());
        params.put("dtNasc",u.getDataNacto());
        params.put("sexo",u.getSexo());

        showLongSnack("Salvando novo usuário...");
        mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.CRIA_USUARIO, Request.Method.POST, false, params, "CRIA_USUARIO");
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
