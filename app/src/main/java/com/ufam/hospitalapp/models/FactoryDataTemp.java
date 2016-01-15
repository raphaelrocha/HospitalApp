package com.ufam.hospitalapp.models;

import com.ufam.hospitalapp.conn.ServerInfo;

import java.util.ArrayList;

/**
 * Created by rli on 10/12/2015.
 */
public class FactoryDataTemp {

    public ArrayList<Usuario> listUsuarios(){

        ArrayList<Usuario> lu = new ArrayList<Usuario>();

        for (int i=1; i<300; i++){
            Usuario u = new Usuario();
            u.setId(i+"");
            u.setNome("Usuario"+i);
            u.setDataNacto("12/12/2000");
            if(i%2==0){
                u.setSexo("m");
            }else{
                u.setSexo("f");
            }
            u.setSenha("123");
            u.setCpf("123.456.789-00");
            u.setDataRegistro("12/12/2000");
            u.setEmail("teste"+i+"@hospital.com");
            u.setFotoUsuario("usuario.jpg");

            lu.add(u);
        }

        return lu;
    }


    public ArrayList<Hospital> listHospitals(){

        ArrayList<Hospital> lh = new ArrayList<Hospital>();

        for (int i=1; i<20; i++){
            Hospital h = new Hospital();
            h.setId(i+"");
            h.setNome("Hospital"+i);
            h.setIdBairro("1");
            h.setEndereco("Manaus/Centro");
            h.setLocalizacao("-2.998012;-60.031104");
            h.setDataRegistro("12/12/2000");
            h.setFotoHospital("foto.jpg");
            lh.add(h);
        }

        return lh;
    }


    public ArrayList<Medico> listMedicos(){

        ArrayList<Medico> lm = new ArrayList<Medico>();

        for (int i=1; i<300; i++){
            Medico m = new Medico();
            m.setId(i+"");
            m.setNome("Medico"+i);
            m.setDataNacto("12/12/2000");
            if(i%2==0){
                m.setSexo("m");
            }else{
                m.setSexo("f");
            }
            m.setPasswd("123");
            m.setCrm("123.456.789-00");
            m.setDataRegistro("12/12/2000");
            m.setEmail("teste"+i+"@hospital.com");
            m.setFotoMedico("usuario.jpg");

            lm.add(m);
        }

        return lm;
    }

    public Usuario getUsuarioLogin(){
        Usuario u = new Usuario();
        u.setId("19823");
        u.setNome("Usuario Logado");
        u.setDataNacto("12/03/2010");
        u.setSexo("m");
        u.setSenha("123");
        u.setDataRegistro("00/00/0000");
        u.setEmail("usuariologado@teste.com");
        u.setCpf("123.456.789-00");
        u.setFotoUsuario(ServerInfo.IMAGE_FOLDER+"teste.jpg");

        return  u;
    }
}
