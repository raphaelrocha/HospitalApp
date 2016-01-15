package com.ufam.hospitalapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rli on 11/12/2015.
 */
public class Estado implements Parcelable {
    private String id,nome;
    private Pais pais;

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
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

    //parcel

    public Estado(){

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString( getId() );
        dest.writeString( getNome() );
        dest.writeParcelable(getPais(),flags);
    }

    public Estado(Parcel parcel){
        setId(parcel.readString());
        setNome(parcel.readString());
        setPais((Pais)parcel.readParcelable(Pais.class.getClassLoader()));
    }

    public static final Creator<Estado> CREATOR = new Creator<Estado>(){
        @Override
        public Estado createFromParcel(Parcel source) {
            return new Estado(source);
        }
        @Override
        public Estado[] newArray(int size) {
            return new Estado[size];
        }
    };
}
