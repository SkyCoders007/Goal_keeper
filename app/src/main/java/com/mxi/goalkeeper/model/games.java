package com.mxi.goalkeeper.model;

/**
 * Created by sonali on 10/3/17.
 */
public class games {

    String title;
    String date;
    String time;
    String surface;
    String team_size;
    String gender;
    String team_category;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTeam_size() {
        return team_size;
    }

    public void setTeam_size(String team_size) {
        this.team_size = team_size;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTeam_category() {
        return team_category;
    }

    public void setTeam_category(String team_category) {
        this.team_category = team_category;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String location;
}
