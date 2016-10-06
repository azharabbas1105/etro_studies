package com.ingentive.leaderboard.model;

import android.graphics.drawable.Drawable;

/**
 * Created by PC on 03-10-2016.
 */
public class HickoryModel {
    int id;
    String investigator_first_name ;
    String investigator_last_name;
    int patients_randomised;
    String country;
    int rank ;
    String date_active;

    public int getDeltaDiff() {
        return deltaDiff;
    }

    public void setDeltaDiff(int deltaDiff) {
        this.deltaDiff = deltaDiff;
    }

    int deltaDiff;

    public String getSince_start() {
        return since_start;
    }

    public void setSince_start(String since_start) {
        this.since_start = since_start;
    }

    String since_start;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    int flag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvestigator_first_name() {
        return investigator_first_name;
    }

    public void setInvestigator_first_name(String investigator_first_name) {
        this.investigator_first_name = investigator_first_name;
    }

    public String getInvestigator_last_name() {
        return investigator_last_name;
    }

    public void setInvestigator_last_name(String investigator_last_name) {
        this.investigator_last_name = investigator_last_name;
    }

    public int getPatients_randomised() {
        return patients_randomised;
    }

    public void setPatients_randomised(int patients_randomised) {
        this.patients_randomised = patients_randomised;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getDate_active() {
        return date_active;
    }

    public void setDate_active(String date_active) {
        this.date_active = date_active;
    }

    public HickoryModel() {

    }

}
