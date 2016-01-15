package com.ufam.hospitalapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rli on 25/11/2015.
 */
public class Usuario implements  Parcelable{
    private String id;
    private String nome;
    private String email;
    private String fotoUsuario;
    private String sexo,dataNacto,dataRegistro,cpf,senha;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDataNacto() {
        return dataNacto;
    }

    public void setDataNacto(String dataNacto) {
        this.dataNacto = dataNacto;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Usuario(){}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString( getId() );
        dest.writeString( getNome() );
        dest.writeString( getEmail() );
        dest.writeString( getFotoUsuario() );
        dest.writeString( getCpf() );
        dest.writeString( getSenha() );
        dest.writeString( getDataRegistro() );
        dest.writeString( getDataNacto() );
        dest.writeString( getSexo() );
    }

    public Usuario(Parcel parcel){
        setId(parcel.readString());
        setNome(parcel.readString());
        setEmail(parcel.readString());
        setFotoUsuario(parcel.readString());
        setCpf(parcel.readString());
        setSenha(parcel.readString());
        setDataRegistro(parcel.readString());
        setDataNacto(parcel.readString());
        setSexo(parcel.readString());
    }

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>(){
        @Override
        public Usuario createFromParcel(Parcel source) {
            return new Usuario(source);
        }
        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}

