package com.compucar.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Diagnose extends DataEntity {
    private String eventName;
    private String result;

    @Override
    public boolean equals(Object obj) {
        Diagnose diagnose = (Diagnose) obj;
        return diagnose.getEventName().equalsIgnoreCase(this.eventName);
    }
}
