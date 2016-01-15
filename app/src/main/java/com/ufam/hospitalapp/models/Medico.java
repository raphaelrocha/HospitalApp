package com.ufam.hospitalapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rli on 10/12/2015.
 */
public class Medico implements Parcelable {

    String crm, dataNacto,dataRegistro, email,id,nome,passwd,sexo, fotoMedico;

    public String getFotoMedico() {
        return fotoMedico;
    }

    public void setFotoMedico(String fotoMedico) {
        this.fotoMedico = fotoMedico;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Medico(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString( getId() );
        dest.writeString( getNome() );
        dest.writeString( getDataNacto() );
        dest.writeString( getDataRegistro() );
        dest.writeString( getEmail());
        dest.writeString( getCrm() );
        dest.writeString( getPasswd());
        dest.writeString( getSexo() );
        dest.writeString( getFotoMedico() );
    }

    public Medico(Parcel parcel){
        setId(parcel.readString());
        setNome(parcel.readString());
        setEmail(parcel.readString());
        setSexo(parcel.readString());
        setPasswd(parcel.readString());
        setCrm(parcel.readString());
        setDataNacto(parcel.readString());
        setDataRegistro(parcel.readString());
        setFotoMedico(parcel.readString());
    }

    public static final Parcelable.Creator<Medico> CREATOR = new Parcelable.Creator<Medico>(){
        @Override
        public Medico createFromParcel(Parcel source) {
            return new Medico(source);
        }
        @Override
        public Medico[] newArray(int size) {
            return new Medico[size];
        }
    };
}
