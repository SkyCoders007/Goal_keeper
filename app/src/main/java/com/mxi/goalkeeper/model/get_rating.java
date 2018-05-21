package com.mxi.goalkeeper.model;

/**
 * Created by sonali on 29/3/17.
 */
public class get_rating {
    String attitude_rate;
    String skill_rating;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getSkill_rating() {
        return skill_rating;
    }

    public void setSkill_rating(String skill_rating) {
        this.skill_rating = skill_rating;
    }

    public String getAttitude_rate() {
        return attitude_rate;
    }

    public void setAttitude_rate(String attitude_rate) {
        this.attitude_rate = attitude_rate;
    }

    String request_id;
    String team_name;
    String player_type;

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getPlayer_type() {
        return player_type;
    }

    public void setPlayer_type(String player_type) {
        this.player_type = player_type;
    }

    public String getManagername() {
        return managername;
    }

    public void setManagername(String managername) {
        this.managername = managername;
    }

    String managername;
}
