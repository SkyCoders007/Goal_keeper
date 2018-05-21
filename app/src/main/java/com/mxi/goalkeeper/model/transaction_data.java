package com.mxi.goalkeeper.model;

/**
 * Created by android on 18/5/17.
 */

public class transaction_data {
/*"trancation_id": "ch_1AFNeRKrlXoKPHOAU1Dj51R3",
      "trancation_date": "2017-05-03 08:36:19",
      "total_credit": "5",*/
    String trancation_id;
    String trancation_date;
    String trancation_time;
    String trancation_plan;

    public String getTrancation_plan() {
        return trancation_plan;
    }

    public void setTrancation_plan(String trancation_plan) {
        this.trancation_plan = trancation_plan;
    }

    public String getTrancation_time() {
        return trancation_time;
    }

    public void setTrancation_time(String trancation_time) {
        this.trancation_time = trancation_time;
    }

    String total_credit;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal_credit() {
        return total_credit;
    }

    public void setTotal_credit(String total_credit) {
        this.total_credit = total_credit;
    }

    public String getTrancation_date() {
        return trancation_date;
    }

    public void setTrancation_date(String trancation_date) {
        this.trancation_date = trancation_date;
    }

    public String getTrancation_id() {
        return trancation_id;
    }

    public void setTrancation_id(String trancation_id) {
        this.trancation_id = trancation_id;
    }


}
