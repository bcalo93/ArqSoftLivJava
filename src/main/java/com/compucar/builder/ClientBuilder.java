package com.compucar.builder;

import com.compucar.model.Client;
import com.compucar.model.ClientType;

public class ClientBuilder {
    private Client client;

    public ClientBuilder() {
        this.client = new Client();
    }

    public ClientBuilder id(Long id) {
        this.client.setId(id);
        return this;
    }

    public ClientBuilder number(int number) {
        this.client.setCode(number);
        return this;
    }

    public ClientBuilder name(String name) {
        this.client.setName(name);
        return this;
    }

    public ClientBuilder phone(String phone) {
        this.client.setPhone(phone);
        return this;
    }

    public ClientBuilder email(String email) {
        this.client.setEmail(email);
        return this;
    }

    public ClientBuilder type(ClientType type) {
        this.client.setType(type);
        return this;
    }

    public Client build() {
        return this.client;
    }

}
