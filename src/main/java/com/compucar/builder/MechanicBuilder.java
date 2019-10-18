package com.compucar.builder;

import com.compucar.model.Client;
import com.compucar.model.ClientType;
import com.compucar.model.Mechanic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

public class MechanicBuilder {
    private Mechanic mechanic;

    public MechanicBuilder() {
        this.mechanic = new Mechanic();
    }

    public MechanicBuilder id(Long id) {
        this.mechanic.setId(id);
        return this;
    }

    public MechanicBuilder number(int number) {
        this.mechanic.setCode(number);
        return this;
    }

    public MechanicBuilder name(String name) {
        this.mechanic.setName(name);
        return this;
    }

    public MechanicBuilder phone(String phone) {
        this.mechanic.setPhone(phone);
        return this;
    }

    public MechanicBuilder startDate(LocalDateTime date) {
        this.mechanic.setStartDate(date);
        return this;
    }

    public Mechanic build() {
        return this.mechanic;
    }

}
