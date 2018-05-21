package com.mxi.goalkeeper.model;

import java.util.ArrayList;

/**
 * Created by sonali on 28/3/17.
 */
public class game_type {

    boolean isSet = false;
    String id;
    String team_name;
    String game_date;
    String game_caliber;
    String game_start_time;
    String duration;
    String game_type;
    String ground_size;
    String level;
    String p_id;
    String request_id;
    String applicant_status;
    String address_lat;
    String player_first_name;
    String manager_name;
    ArrayList<String> game_player_list;


    public String getGame_caliber() {
        return game_caliber;
    }

    public void setGame_caliber(String game_caliber) {
        this.game_caliber = game_caliber;
    }


    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }


    public ArrayList<String> getGame_player_list() {
        return game_player_list;
    }

    public void setGame_player_list(ArrayList<String> game_player_list) {
        this.game_player_list = game_player_list;
    }


    public Boolean isChecked = false;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    String Status;

    public String getPlayer_last_name() {
        return player_last_name;
    }

    public void setPlayer_last_name(String player_last_name) {
        this.player_last_name = player_last_name;
    }

    public String getPlayer_first_name() {
        return player_first_name;
    }

    public void setPlayer_first_name(String player_first_name) {
        this.player_first_name = player_first_name;
    }

    String player_last_name;

    public String getAvailable_time() {
        return available_time;
    }

    public void setAvailable_time(String available_time) {
        this.available_time = available_time;
    }

    String available_time;

    public String getPlayer_type() {
        return player_type;
    }

    public void setPlayer_type(String player_type) {
        this.player_type = player_type;
    }

    String player_type;

    public String getAddress_long() {
        return address_long;
    }

    public void setAddress_long(String address_long) {
        this.address_long = address_long;
    }

    public String getAddress_lat() {
        return address_lat;
    }

    public void setAddress_lat(String address_lat) {
        this.address_lat = address_lat;
    }

    String address_long;

    public String getIs_status() {
        return is_status;
    }

    public void setIs_status(String is_status) {
        this.is_status = is_status;
    }

    String is_status;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String address;

    public String getGame_gender() {
        return game_gender;
    }

    public void setGame_gender(String game_gender) {
        this.game_gender = game_gender;
    }

    String game_gender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getGame_date() {
        return game_date;
    }

    public void setGame_date(String game_date) {
        this.game_date = game_date;
    }

    public String getGame_start_time() {
        return game_start_time;
    }

    public void setGame_start_time(String game_start_time) {
        this.game_start_time = game_start_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getGame_type() {
        return game_type;
    }

    public void setGame_type(String game_type) {
        this.game_type = game_type;
    }

    public String getGround_size() {
        return ground_size;
    }

    public void setGround_size(String ground_size) {
        this.ground_size = ground_size;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getApplicant_status() {
        return applicant_status;
    }

    public void setApplicant_status(String applicant_status) {
        this.applicant_status = applicant_status;
    }

    public String getRequestor_status() {
        return requestor_status;
    }

    public void setRequestor_status(String requestor_status) {
        this.requestor_status = requestor_status;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }


    String requestor_status;
    String full_name;
    String user_id;
}
