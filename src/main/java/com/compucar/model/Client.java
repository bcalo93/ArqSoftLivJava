package com.compucar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Client extends Contact {
    private String email;

    @Enumerated(EnumType.STRING)
    private ClientType type;

    public Client() {
    }

    public Client(Integer number) {
        this.setNumber(number);
    }

    public void update(Client client) {
        this.setType(client.getType());
        this.setEmail(client.getEmail());
        this.setPhone(client.getPhone());
        this.setName(client.getName());
    }
}