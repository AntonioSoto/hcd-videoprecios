package com.soto.videoprecios;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by José on 07/10/2017.
 */

public class Videogame{

    private Bitmap image;
    private String name;
    private String price;
    private String seller;
    private URL url;

    public Videogame(Bitmap image, String name, String price, String seller, URL url) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.seller = seller;
        this.url = url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
