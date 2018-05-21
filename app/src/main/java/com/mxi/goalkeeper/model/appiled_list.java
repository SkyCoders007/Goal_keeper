package com.mxi.goalkeeper.model;

/**
 * Created by sonali on 22/4/17.
 */
public class appiled_list {
    String id;
    String player_type;
    String p_id;
    String requested_id;
    String playing_amount;

    public String getPlaying_amount() {
        return playing_amount;
    }

    public void setPlaying_amount(String playing_amount) {
        this.playing_amount = playing_amount;
    }



    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getRequested_id() {
        return requested_id;
    }

    public void setRequested_id(String requested_id) {
        this.requested_id = requested_id;
    }

    String player_id;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    Boolean isChecked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayer_type() {
        return player_type;
    }

    public void setPlayer_type(String player_type) {
        this.player_type = player_type;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public String getSelf_rate() {
        return self_rate;
    }

    public void setSelf_rate(String self_rate) {
        this.self_rate = self_rate;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getPlaying_type() {
        return playing_type;
    }

    public void setPlaying_type(String playing_type) {
        this.playing_type = playing_type;
    }

    String calibre;
    String self_rate;
    String player_name;
    String manager_id;
    String playing_type;
}
