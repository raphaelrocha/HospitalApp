package com.ufam.hospitalapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rli on 11/12/2015.
 */
public class Cidade implements Parcelable{
    private String id, nome;
    private Estado estado;

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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Cidade(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString( getId() );
        dest.writeString( getNome() );
        dest.writeParcelable(getEstado(),flags);
    }

    public Cidade(Parcel parcel){
        setId(parcel.readString());
        setNome(parcel.readString());
        setEstado((Estado) parcel.readParcelable(Estado.class.getClassLoader()));
    }

    public static final Creator<Cidade> CREATOR = new Creator<Cidade>(){
        @Override
        public Cidade createFromParcel(Parcel source) {
            return new Cidade(source);
        }
        @Override
        public Cidade[] newArray(int size) {
            return new Cidade[size];
        }
    };


}
