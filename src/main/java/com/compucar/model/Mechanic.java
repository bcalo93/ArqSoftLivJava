package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Mechanic extends Contact {
    private LocalDateTime startDate;

    public void update(Mechanic mechanic) {
        this.setName(mechanic.getName());
        this.setStartDate(mechanic.getStartDate());
        this.setPhone(mechanic.getPhone());
    }
}
