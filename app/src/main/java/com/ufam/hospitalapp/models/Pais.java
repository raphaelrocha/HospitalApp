package com.ufam.hospitalapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rli on 11/12/2015.
 */
public class Pais implements Parcelable {
    private String id,nome,dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public Pais(){

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString( getId() );
        dest.writeString( getNome() );
        dest.writeString( getDateTime() );
    }

    public Pais(Parcel parcel){
        setId(parcel.readString());
        setNome(parcel.readString());
        setDateTime(parcel.readString());
    }

    public static final Parcelable.Creator<Pais> CREATOR = new Parcelable.Creator<Pais>(){
        @Override
        public Pais createFromParcel(Parcel source) {
            return new Pais(source);
        }
        @Override
        public Pais[] newArray(int size) {
            return new Pais[size];
        }
    };
}
