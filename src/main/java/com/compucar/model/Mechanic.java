package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.util.Calendar;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Mechanic extends Contact {
    private Calendar startDate;

    public void update(Mechanic mechanic) {
        this.setName(mechanic.getName());
        this.setStartDate(mechanic.getStartDate());
        this.setPhone(mechanic.getPhone());
    }
}
