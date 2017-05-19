package com.example.kaumadi.fireplantapp.mFragment;

/**
 * Created by Niroshana on 5/7/2017.
 */

public class Flag {
    private boolean tempFlag;
    private boolean lightFlag;
    private boolean humFlag;
    private boolean soilFlag;

    public Flag(){
        tempFlag = false;
        lightFlag = false;
        humFlag = false;
        soilFlag = false;
    }

    public boolean isTempFlag() {
        return tempFlag;
    }

    public void setTempFlag(boolean tempFlag) {
        this.tempFlag = tempFlag;
    }

    public boolean isLightFlag() {
        return lightFlag;
    }

    public void setLightFlag(boolean lightFlag) {
        this.lightFlag = lightFlag;
    }

    public boolean isHumFlag() {
        return humFlag;
    }

    public void setHumFlag(boolean humFlag) {
        this.humFlag = humFlag;
    }

    public boolean isSoilFlag() {
        return soilFlag;
    }

    public void setSoilFlag(boolean soilFlag) {
        this.soilFlag = soilFlag;
    }
}
