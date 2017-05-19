package com.example.kaumadi.fireplantapp;

/**
 * Created by kaumadi on 4/20/2017.
 */

public class Plant {
    private String id,name,date,image,sci,geuns;

    public Plant()
    {

    }

    public Plant(String image, String id,String name,String date) {
        this.date = date;
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeuns() {
        return geuns;
    }

    public void setGeuns(String geuns) {
        this.geuns = geuns;
    }

    public String getSci() {
        return sci;
    }

    public void setSci(String sci) {
        this.sci = sci;
    }
}
