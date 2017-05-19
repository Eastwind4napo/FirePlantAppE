package com.example.kaumadi.fireplantapp.Greenhouse;

/**
 * Created by Eastwind on 5/7/2017.
 */

public class Greenhouses {
    private String greenId,greenName,greenLocation,greenDate,greenImage;

    public Greenhouses(){

    }

    public Greenhouses(String greenId, String greenName, String greenLocation, String greenImage, String greenDate) {
        this.greenDate = greenDate;
        this.greenId = greenId;
        this.greenImage = greenImage;
        this.greenLocation = greenLocation;
        this.greenName = greenName;
    }

    public Greenhouses(String greenId, String greenName, String greenLocation,String greenDate) {
        this.greenDate = greenDate;
        this.greenId = greenId;
        this.greenLocation = greenLocation;
        this.greenName = greenName;
    }

    public String getGreenDate() {
        return greenDate;
    }

    public void setGreenDate(String greenDate) {
        this.greenDate = greenDate;
    }

    public String getGreenId() {
        return greenId;
    }

    public void setGreenId(String greenId) {
        this.greenId = greenId;
    }

    public String getGreenImage() {
        return greenImage;
    }

    public void setGreenImage(String greenImage) {
        this.greenImage = greenImage;
    }

    public String getGreenLocation() {
        return greenLocation;
    }

    public void setGreenLocation(String greenLocation) {
        this.greenLocation = greenLocation;
    }

    public String getGreenName() {
        return greenName;
    }

    public void setGreenName(String greenName) {
        this.greenName = greenName;
    }
}

