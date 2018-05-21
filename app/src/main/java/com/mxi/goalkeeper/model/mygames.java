package com.mxi.goalkeeper.model;

/**
 * Created by sonali on 10/3/17.
 */
public class mygames {

    String title;
    String skill_rating;
    String attitude_rating;

    public String getGame_position() {
        return game_position;
    }

    public void setGame_position(String game_position) {
        this.game_position = game_position;
    }

    public String getAttitude_rating() {
        return attitude_rating;
    }

    public void setAttitude_rating(String attitude_rating) {
        this.attitude_rating = attitude_rating;
    }

    public String getSkill_rating() {
        return skill_rating;
    }

    public void setSkill_rating(String skill_rating) {
        this.skill_rating = skill_rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String game_position;
}
