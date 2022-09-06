package com.example.agrocrop;

import java.io.Serializable;

public class Crop_Detail_Model implements Serializable {
    private String prop_id, image,planting_operation,site_preparation,soil_ph,soil_type,
            weeding;

    public Crop_Detail_Model() {
    }

    public Crop_Detail_Model(String prop_id, String image) {
        this.prop_id = prop_id;
        this.image = image;
    }

    public String getProp_id() {
        return prop_id;
    }

    public String getImage() {
        return image;
    }
}
