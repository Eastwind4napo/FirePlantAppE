package com.example.kaumadi.fireplantapp;

/**
 * Created by kaumadi on 4/26/2017.
 */

public class GreenHouse {
    private String greenId,greenName,greenLocation,greenDate,greenImage;
    public GreenHouse(){

    }

    public GreenHouse(String greenId, String greenName, String greenLocation, String greenImage,String greenDate) {
        this.greenDate = greenDate;
        this.greenId = greenId;
        this.greenImage = greenImage;
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
