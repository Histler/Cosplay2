package com.badr.cosplay2.model.schedule;

import java.io.Serializable;

/**
 * Created by Badr on 15.11.2015.
 */
public class Plan implements Serializable {
    private String message;
    private String plan;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
