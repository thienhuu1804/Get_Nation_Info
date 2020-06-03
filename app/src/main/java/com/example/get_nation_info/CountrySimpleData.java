package com.example.get_nation_info;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CountrySimpleData implements Serializable {
    String countryCode, name, population, areaInSqKm = "";

//    public CountrySimpleData(JSONObject jsonObject) throws JSONException {
//            countryCode = jsonObject.getString("countryCode");
//            name = jsonObject.getString("countryName");
//            population = jsonObject.getString("population");
//            areaInSqKm =  jsonObject.getString("areaInSqKm");
//    }

    public CountrySimpleData(String code, String name, String population, String areaInSqKm){
        this.countryCode = code;
        this.name = name;
        this.population = population;
        this.areaInSqKm = areaInSqKm;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getAreaInSqKm() {
        return areaInSqKm;
    }

    public void setAreaInSqKm(String areaInSqKm) {
        this.areaInSqKm = areaInSqKm;
    }
}
