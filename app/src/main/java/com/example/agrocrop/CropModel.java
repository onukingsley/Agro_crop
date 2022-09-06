package com.example.agrocrop;

import java.io.Serializable;

public class CropModel implements Serializable {

    private String crop_id, cropTitle, crop_description, soil_ph, harvest, sitePreparation, weeding,
            image;

    public CropModel() {
    }



    public CropModel(String cropTitle, String image, String crop_id) {
        this.cropTitle = cropTitle;
        this.image = image;
        this.crop_id = crop_id;
    }

    public CropModel(String cropTitle, String crop_description, String soil_ph, String harvest, String sitePreparation, String weeding) {
        this.cropTitle = cropTitle;
        this.crop_description = crop_description;
        this.soil_ph = soil_ph;
        this.harvest = harvest;
        this.sitePreparation = sitePreparation;
        this.weeding = weeding;
    }
    public String getImage() {
        return image;
    }
    public String getCropTitle() {
        return cropTitle;
    }

    public String getCrop_description() {
        return crop_description;
    }

    public String getSoil_ph() {
        return soil_ph;
    }

    public String getHarvest() {
        return harvest;
    }

    public String getSitePreparation() {
        return sitePreparation;
    }

    public String getWeeding() {
        return weeding;
    }

    public String getCrop_id() {
        return crop_id;
    }
}
