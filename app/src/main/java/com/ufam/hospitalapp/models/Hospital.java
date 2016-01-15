package com.ufam.hospitalapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by rli on 12/11/2015.
 */
public class Hospital implements Parcelable {

    private String id;
    private String nome;
    private String idBairro;
    private String endereco;
    private String DataRegistro;
    private String fotoHospital;
    private String localizacao;
    private String latitude;
    private String longitude;
    private String distancia;
    private Bairro bairro;
    private ArrayList<AvaliaHospital> avaliaHospitals;

    public ArrayList<AvaliaHospital> getAvaliaHospitals() {
        return avaliaHospitals;
    }

    public void setAvaliaHospitals(ArrayList<AvaliaHospital> avaliaHospitals) {
        this.avaliaHospitals = avaliaHospitals;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
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

    public String getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(String idBairro) {
        this.idBairro = idBairro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDataRegistro() {
        return DataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        DataRegistro = dataRegistro;
    }

    public String getFotoHospital() {
        return fotoHospital;
    }

    public void setFotoHospital(String fotoHospital) {
        this.fotoHospital = fotoHospital;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public Hospital(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString( getId() );
        dest.writeString( getNome() );
        dest.writeString( getIdBairro() );
        dest.writeString( getEndereco() );
        dest.writeString( getLocalizacao());
        dest.writeString( getDataRegistro() );
        dest.writeString( getFotoHospital());
        dest.writeString( getDistancia());
        dest.writeString( getLatitude());
        dest.writeString( getLongitude());
        dest.writeParcelable(getBairro(),flags);
        dest.writeList(getAvaliaHospitals());
    }

    public Hospital(Parcel parcel){
        setId(parcel.readString());
        setNome(parcel.readString());
        setIdBairro(parcel.readString());
        setEndereco(parcel.readString());
        setLocalizacao(parcel.readString());
        setDataRegistro(parcel.readString());
        setFotoHospital(parcel.readString());
        setDistancia(parcel.readString());
        setLatitude(parcel.readString());
        setLongitude(parcel.readString());
        setBairro((Bairro) parcel.readParcelable(Bairro.class.getClassLoader()));
        setAvaliaHospitals(parcel.readArrayList(AvaliaHospital.class.getClassLoader()));
    }

    public static final Parcelable.Creator<Hospital> CREATOR = new Parcelable.Creator<Hospital>(){
        @Override
        public Hospital createFromParcel(Parcel source) {
            return new Hospital(source);
        }
        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };
}
