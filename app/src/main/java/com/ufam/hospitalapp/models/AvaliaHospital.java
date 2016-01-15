package com.ufam.hospitalapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rli on 07/01/2016.
 */
public class AvaliaHospital implements Parcelable{
    private String id;
    private Usuario usuario;
    private String idHospital;
    private String texto;
    private String nota;
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

    public String getIdHospital() {
        return idHospital;
    }

    public void setIdHospital(String idHospital) {
        this.idHospital = idHospital;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public AvaliaHospital(){

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
        dest.writeString( getNota());
        dest.writeParcelable(getUsuario(),flags);
    }

    public AvaliaHospital(Parcel parcel){
        setId(parcel.readString());
        setDataRegistro(parcel.readString());
        setTexto(parcel.readString());
        setIdHospital(parcel.readString());
        setNota(parcel.readString());
        setUsuario((Usuario) parcel.readParcelable(Usuario.class.getClassLoader()));
    }

    public static final Creator<AvaliaHospital> CREATOR = new Creator<AvaliaHospital>(){
        @Override
        public AvaliaHospital createFromParcel(Parcel source) {
            return new AvaliaHospital(source);
        }
        @Override
        public AvaliaHospital[] newArray(int size) {
            return new AvaliaHospital[size];
        }
    };
}
