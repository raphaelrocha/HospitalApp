package com.ufam.hospitalapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rli on 07/01/2016.
 */
public class Comentario implements Parcelable{
    private String id;
    private Usuario usuario;
    private String idHospital;
    private String texto;
    private String dataRegistro;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getIdHospital() {
        return idHospital;
    }

    public void setIdHospital(String idHospital) {
        this.idHospital = idHospital;
    }

    public Comentario(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString( getId() );
        dest.writeString( getDataRegistro() );
        dest.writeString( getTexto());
        dest.writeString( getIdHospital());
        dest.writeParcelable(getUsuario(),flags);
    }

    public Comentario(Parcel parcel){
        setId(parcel.readString());
        setDataRegistro(parcel.readString());
        setTexto(parcel.readString());
        setIdHospital(parcel.readString());
        setUsuario((Usuario) parcel.readParcelable(Usuario.class.getClassLoader()));
    }

    public static final Parcelable.Creator<Comentario> CREATOR = new Parcelable.Creator<Comentario>(){
        @Override
        public Comentario createFromParcel(Parcel source) {
            return new Comentario(source);
        }
        @Override
        public Comentario[] newArray(int size) {
            return new Comentario[size];
        }
    };
}
