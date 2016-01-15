package com.ufam.hospitalapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rli on 11/12/2015.
 */
public class Bairro implements Parcelable{

    private String id, nome;
    private Cidade cidade;

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

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Bairro() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString( getId() );
        dest.writeString( getNome() );
        dest.writeParcelable(getCidade(),flags);
    }

    public Bairro(Parcel parcel){
        setId(parcel.readString());
        setNome(parcel.readString());
        setCidade((Cidade) parcel.readParcelable(Cidade.class.getClassLoader()));
    }

    public static final Parcelable.Creator<Bairro> CREATOR = new Parcelable.Creator<Bairro>(){
        @Override
        public Bairro createFromParcel(Parcel source) {
            return new Bairro(source);
        }
        @Override
        public Bairro[] newArray(int size) {
            return new Bairro[size];
        }
    };
}
